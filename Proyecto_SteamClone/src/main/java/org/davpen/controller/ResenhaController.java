package org.davpen.controller;

import org.davpen.enums.TipoEstadoResenha;
import org.davpen.enums.TipoRecomendacionJuego;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.ResenhaDto;
import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.hibernate.BibliotecaRepoHibernate;
import org.davpen.repositorio.hibernate.JuegoRepoHibernate;
import org.davpen.repositorio.hibernate.ResenhaRepoHibernate;
import org.davpen.repositorio.hibernate.UsuarioRepoHibernate;
import org.davpen.repositorio.interfaces.IBibliotecaRepo;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.repositorio.interfaces.IResenhaRepo;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.davpen.transaction.HibernateTransactionManager;
import org.davpen.transaction.ISessionManager;
import org.davpen.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ResenhaController {

    private final IResenhaRepo resenhaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final IBibliotecaRepo bibliotecaRepo;
    private Optional<TipoRecomendacionJuego> tipoRecomendacion;
    private ITransactionManager transMgr;

    public ResenhaController(IResenhaRepo resenhaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo,
                             IBibliotecaRepo bibliotecaRepo, ITransactionManager transMgr) {
        this.resenhaRepo = resenhaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
        this.transMgr = transMgr;
    }


    /**
     * Crea una nueva reseña para un juego tras validar un formulario. En caso de errores lanza ValidationException
     * con lista de errores. Si tiene éxito, crea la reseña y devuelve una ResenhaDto
     *
     * @param resenhaForm Formulario de datos de una reseña
     * @return ResenhaDto
     * @throws ValidationException
     */
    public ResenhaDto escribirResenha(ResenhaForm resenhaForm) throws ValidationException {
        //Validaciones
        var errores = resenhaForm.validar();

        var resenha = transMgr.inTransaction(() -> {
            //Validaciones modelo
            //usuario y juego existen
            var idUsuarioResenha = resenhaForm.getIdUsuarioResenha();
            if (!usuarioRepo.obtenerPorId(idUsuarioResenha).isPresent()) {
                errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
            }
            var idJuegoResenha = resenhaForm.getIdJuegoResenha();
            if (!juegoRepo.obtenerPorId(idJuegoResenha).isPresent()) {
                errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            //validar juego en biblioteca = usuario es propietario

            var juegoEnBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuarioResenha))
                    .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuegoResenha));
            if (!juegoEnBiblioteca) {
                errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
            }
            //validar reseña no duplicada
            var listaResenhas = resenhaRepo.obtenerTodos();
            var resenhaExistente = listaResenhas.stream()
                    .filter(r -> r.getIdUsuarioResenha().equals(idUsuarioResenha))
                    .anyMatch(r -> r.getIdJuegoResenha().equals(idJuegoResenha));
            if (resenhaExistente) {
                errores.add(new ErrorDto("resenha", ErrorType.DUPLICADO));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            //crear reseña
            var resenhaFormPublicada = new ResenhaForm(idUsuarioResenha, idJuegoResenha,
                    resenhaForm.isRecomendacionResenha(),
                    resenhaForm.getTextoResenha(), resenhaForm.getTiempoJugadoResenha(),
                    resenhaForm.getFechaPublicacionResenha(),
                    resenhaForm.getFechaUltiEdicResenha(), TipoEstadoResenha.PUBLICADA);
            var resenhaPublicada = resenhaRepo.crear(resenhaFormPublicada);
            return resenhaPublicada;

        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaSimple(resenha.orElse(null));
    }

    /**
     * Cambia estado a OCULTA una reseña existente tras validar condiciones. En caso de errores lanza
     * ValidationException con lista de errores. En caso de exito oculta la reseña y devuelve la ResenhaDto modificada.
     *
     * @param idResenha Identificador de Reseña
     * @param idUsuario Identificador de Usuario
     * @return ResenhaDto
     * @throws ValidationException
     */
    public ResenhaDto ocultarResenha(Long idResenha, Long idUsuario) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();

        var resenhaOculta = transMgr.inTransaction(() -> {

            var resenhaAOcultar = resenhaRepo.obtenerPorId(idResenha);
            //validar reseña existe
            if (!resenhaAOcultar.isPresent()) {
                errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //validar reseña es de usuario
            var listaResenhas = resenhaRepo.obtenerTodos();
            var resenhasAUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario, listaResenhas);
            var resenhaAEliminarEsDeUsuario = resenhasAUsuario.stream()
                    .anyMatch(r -> r.getIdResenha().equals(idResenha));
            if (!resenhaAEliminarEsDeUsuario) {
                errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
            }
            //validar reseña está publicada

            var estadoResenhaAOcultar = resenhaAOcultar.get().getEstadoResenha();
            if (estadoResenhaAOcultar != TipoEstadoResenha.PUBLICADA) {
                errores.add(new ErrorDto("estado_resenha", ErrorType.RESENHA_NO_PUBLICADA));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var resenhaForm = new ResenhaForm(idUsuario, resenhaAOcultar.get().getIdJuegoResenha(),
                    resenhaAOcultar.get().isRecomendacionResenha(),
                    resenhaAOcultar.get().getTextoResenha(), resenhaAOcultar.get().getTiempoJugadoResenha(),
                    resenhaAOcultar.get().getFechaPublicacionResenha(),
                    resenhaAOcultar.get().getFechaUltiEdicResenha(), TipoEstadoResenha.OCULTA);
            var resenhaActualizada = resenhaRepo.actualizar(idResenha, resenhaForm);

            return resenhaActualizada;

        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaSimple(resenhaOculta.orElse(null));
    }

    /**
     * Elimina una reseña si la reseña existe y pertenece al usuario indicado.
     * En caso de errores lanza ValidationException con lista de errores.
     *
     * @param idResenha Identificador de Resenha
     * @param idUsuario Identificador de Usuario
     * @return true si la eliminacion fue exitosa
     * @throws ValidationException
     */
    public boolean eliminarResenha(Long idResenha, Long idUsuario) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();

        var resenhaEliminada = transMgr.inTransaction(() -> {

            //validar modelo
            //validar reseña existe
            var resenhaOpt = resenhaRepo.obtenerPorId(idResenha);
            if (!resenhaOpt.isPresent()) {
                errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //validar reseña es de usuario
            var listaResenhas = resenhaRepo.obtenerTodos();
            var resenhasAUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario, listaResenhas);
            var resenhaAEliminarEsDeUsuario = resenhasAUsuario.stream().anyMatch(r -> r.getIdResenha().equals(idResenha));
            if (!resenhaAEliminarEsDeUsuario) {
                errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var resenhaAEliminar = resenhaRepo.eliminar(idResenha);
            return resenhaAEliminar;

        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resenhaEliminada;

    }

    /**
     * Devuelve la lista de reseñas de un juego (por idJuego).Valida que el juego exista; si no, lanza
     * ValidationException. Recupera todas las reseñas del juego, las filtra opcionalmente por su estado de
     * recomendacion y las ordena por fecha de publicación descendente (más recientes primero).
     *
     * @param idJuego           Identificador de Juego
     * @param tipoRecomendacion Optional de TipoRecomendacionJuego
     * @return Lista ordenada de reseñas de un Juego
     * @throws ValidationException
     */
    public List<ResenhaDto> verResenhasJuego(Long idJuego, Optional<TipoRecomendacionJuego> tipoRecomendacion) throws ValidationException {
        this.tipoRecomendacion = tipoRecomendacion;
        var errores = new ArrayList<ErrorDto>();
        //Validar juego existe
        var listaJuegos = juegoRepo.obtenerTodos();
        var juegoExiste = listaJuegos.stream().anyMatch(j -> j.getIdJuego().equals(idJuego));
        if (!juegoExiste) {
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        var listaResenhas = resenhaRepo.obtenerTodos();
        var listaResenhasJuego = resenhaRepo.obtenerTodasPorIdJuego(idJuego, listaResenhas);
        List<ResenhaDto> listaResenhasDtoJuego = List.of();

        if (!tipoRecomendacion.isPresent()) {

            listaResenhasDtoJuego = listaResenhasJuego.stream()
                    //filtramos para quedarnos solo con las reseñas que no esten ocultas
                    .filter(r -> !r.getEstadoResenha().equals(TipoEstadoResenha.OCULTA))
                    //Ordenamos priorizando las reseñas mas recientes como mas utiles
                    .sorted(Comparator.comparing(ResenhaEntity::getFechaPublicacionResenha).reversed())
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();

        } else if (tipoRecomendacion.get().equals(TipoRecomendacionJuego.RECOMENDADO)) {
            listaResenhasDtoJuego = listaResenhasJuego.stream()
                    //filtramos para quedarnos solo con las reseñas que no esten ocultas
                    .filter(r -> !r.getEstadoResenha().equals(TipoEstadoResenha.OCULTA))
                    .filter(r -> r.isRecomendacionResenha())
                    .sorted(Comparator.comparing(ResenhaEntity::getFechaPublicacionResenha).reversed())
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();

        } else if (tipoRecomendacion.get().equals(TipoRecomendacionJuego.NO_RECOMENDADO)) {
            listaResenhasDtoJuego = listaResenhasJuego.stream()
                    //filtramos para quedarnos solo con las reseñas que no esten ocultas
                    .filter(r -> !r.getEstadoResenha().equals(TipoEstadoResenha.OCULTA))
                    .filter(r -> !r.isRecomendacionResenha())
                    .sorted(Comparator.comparing(ResenhaEntity::getFechaPublicacionResenha).reversed())
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();
        }

        return listaResenhasDtoJuego;
    }

    /**
     * Devuelve la lista de reseñas de un usuario (por idUsuario).Valida que el usuario exista; si no, lanza
     * ValidationException. Recupera las reseñas del usuario y las muestra todas o las filtra por PUBLICADAS u OCULTAS.
     *
     * @param idUsuario         Identificador de Usuario
     * @param tipoEstadoResenha Optional de TipoEstadoResenha
     * @return Lista de reseñas publicadas por un Usuario
     * @throws ValidationException
     */
    public List<ResenhaDto> verResenhasUsuario(Long idUsuario, Optional<TipoEstadoResenha> tipoEstadoResenha) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar usuario existe
        var listaUsuarios = usuarioRepo.obtenerTodos();
        var usuarioExiste = listaUsuarios.stream().anyMatch(u -> u.getIdUsuario().equals(idUsuario));
        if (!usuarioExiste) {
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        var listaResenhas = resenhaRepo.obtenerTodos();
        var listaResenhasUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario, listaResenhas);
        List<ResenhaDto> listaResenhasDtoUsuario = List.of();

        if (!tipoEstadoResenha.isPresent()) {
            listaResenhasDtoUsuario = listaResenhasUsuario.stream()
                    //Mostramos todas las reseñas publicadas
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();

        } else if (tipoEstadoResenha.get().equals(TipoEstadoResenha.PUBLICADA)) {
            listaResenhasDtoUsuario = listaResenhasUsuario.stream()
                    //Filtramos para mostrar solo las reseñas publicadas
                    .filter(r -> r.getEstadoResenha().equals(TipoEstadoResenha.PUBLICADA))
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();

        } else if (tipoEstadoResenha.get().equals(TipoEstadoResenha.OCULTA)) {
            listaResenhasDtoUsuario = listaResenhasUsuario.stream()
                    //Filtramos para mostrar solo las reseñas ocultas
                    .filter(r -> r.getEstadoResenha().equals(TipoEstadoResenha.OCULTA))
                    .map(resenha -> Mapper.mapaSimple(resenha))
                    .toList();

        }
        return listaResenhasDtoUsuario;
    }

    static void main() throws ValidationException {

        ITransactionManager transMgr = new HibernateTransactionManager();
        var c = new ResenhaController(new ResenhaRepoHibernate((ISessionManager) transMgr),
                                    new UsuarioRepoHibernate((ISessionManager) transMgr),
                                    new JuegoRepoHibernate((ISessionManager) transMgr),
                                    new BibliotecaRepoHibernate((ISessionManager) transMgr), transMgr);

        var resenhaForm = new ResenhaForm(1L, 1L, true,
                "Excelente juego,Excelente juego,Excelente juego,Excelente juego,Excelente juego",
                20.5, LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);

        var resenhaForm2 = new ResenhaForm(1L, 2L, true,
                "Excelente juego,Excelente juego,Excelente juego,Excelente juego,Excelente juego",
                20.5, LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);


        //var resenha1 = c.escribirResenha(resenhaForm);
        //var resenha2 = c.escribirResenha(resenhaForm2);

        //System.out.println(resenha1);
        //System.out.println(resenha2);

        c.eliminarResenha(2L,1L);

        //c.ocultarResenha(2L, 1L);

    }

}
