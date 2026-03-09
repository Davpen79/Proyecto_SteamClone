package org.davpen.controller;

import org.davpen.enums.TipoEstadoResenha;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.ResenhaDto;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IResenhaRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;

public class ResenhaController {

    private final IResenhaRepo resenhaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final IBibliotecaRepo bibliotecaRepo;

    public ResenhaController(IResenhaRepo resenhaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo, IBibliotecaRepo bibliotecaRepo) {
        this.resenhaRepo = resenhaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
    }

    //Escribir reseña
    public ResenhaDto escribirResenha(ResenhaForm resenhaForm) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        resenhaForm.validar();
        //Validaciones modelo
        //usuario y juego existen
        var idUsuarioResenha = resenhaForm.getIdUsuarioResenha();
        if (!usuarioRepo.obtenerPorId(idUsuarioResenha).isPresent()){
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        var idJuegoResenha = resenhaForm.getIdJuegoResenha();
        if (!juegoRepo.obtenerPorId(idJuegoResenha).isPresent()){
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        //validar juego en biblioteca = usuario es propietario

        var juegoEnBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuarioResenha))
                                .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuegoResenha));
        if (!juegoEnBiblioteca){
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        //validar reseña no duplicada
        var listaResenhas = resenhaRepo.obtenerTodos();
        var resenhaExistente = listaResenhas.stream()
                            .filter(r -> r.getIdUsuarioResenha().equals(idUsuarioResenha))
                            .anyMatch(r -> r.getIdJuegoResenha().equals(idJuegoResenha));
        if (resenhaExistente){
            errores.add(new ErrorDto("resenha", ErrorType.DUPLICADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        //crear reseña
        var resenhaCreada = resenhaRepo.crear(resenhaForm).orElse(null);

        return Mapper.mapaSimple(resenhaCreada);
    }


    //Ocultar reseña
    public ResenhaDto ocultarResenha(Long idResenha, Long idUsuario) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        //validar reseña existe
        if (!resenhaRepo.obtenerPorId(idResenha).isPresent()){
            errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
        }
        //validar reseña es de usuario
        var listaResenhas = resenhaRepo.obtenerTodos();
        var resenhasAUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario,listaResenhas);
        var resenhaAEliminarEsDeUsuario = resenhasAUsuario.stream().anyMatch(r -> r.getIdResenha().equals(idResenha));
        if (!resenhaAEliminarEsDeUsuario){
            errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
        }
        //validar reseña está publicada
        var resenhaAOcultar = resenhaRepo.obtenerPorId(idResenha);
        var estadoResenhaAOcultar = resenhaAOcultar.get().getEstadoResenha();
        if (estadoResenhaAOcultar != TipoEstadoResenha.PUBLICADA){
            errores.add(new ErrorDto("estado_resenha", ErrorType.RESENHA_NO_PUBLICADA));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var resenhaForm = new ResenhaForm(idUsuario,resenhaAOcultar.get().getIdJuegoResenha(), resenhaAOcultar.get().isRecomendacionResenha(),
                            resenhaAOcultar.get().getTextoResenha(), resenhaAOcultar.get().getTiempoJugadoResenha(),resenhaAOcultar.get().getFechaPublicacionResenha(),
                            resenhaAOcultar.get().getFechaUltiEdicResenha(), TipoEstadoResenha.OCULTA);
        var resenhaActualizada = resenhaRepo.actualizar(idResenha, resenhaForm).orElse(null);
        return Mapper.mapaSimple(resenhaActualizada);
    }


    //Eliminar reseña
    public boolean eliminarResenha(Long idResenha, Long idUsuario) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();

        //validar modelo
        //validar reseña existe
        if (!resenhaRepo.obtenerPorId(idResenha).isPresent()){
            errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
        }
        //validar reseña es de usuario
        var listaResenhas = resenhaRepo.obtenerTodos();
        var resenhasAUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario,listaResenhas);
        var resenhaAEliminarEsDeUsuario = resenhasAUsuario.stream().anyMatch(r -> r.getIdResenha().equals(idResenha));
        if (!resenhaAEliminarEsDeUsuario){
            errores.add(new ErrorDto("id_resenha", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var resenhaEliminada = resenhaRepo.eliminar(idResenha);
        return resenhaEliminada;

    }

    //Ver reseñas de un juego - Faltan Filtros/Orden como pasan por parametro + Estadisticas
    public List<ResenhaDto> verReseñasJuego(Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar juego existe
        var listaJuegos = juegoRepo.obtenerTodos();
        var juegoExiste = listaJuegos.stream().anyMatch(j -> j.getIdJuego().equals(idJuego));
        if (!juegoExiste){
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var listaResenhas = resenhaRepo.obtenerTodos();
        var listaResenhasJuego = resenhaRepo.obtenerTodasPorIdJuego(idJuego,listaResenhas);

        var listaResenhasDtoJuego = listaResenhasJuego.stream().map(resenha -> Mapper.mapaSimple(resenha)).toList();
        return listaResenhasDtoJuego;
    }


    //Ver reseñas de un usuario - Faltan Filtros + Estadisticas
    public List<ResenhaDto> verReseñasUsuario(Long idUsuario) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar usuario existe
        var listaUsuarios = usuarioRepo.obtenerTodos();
        var usuarioExiste = listaUsuarios.stream().anyMatch(u -> u.getIdUsuario().equals(idUsuario));
        if (!usuarioExiste){
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var listaResenhas = resenhaRepo.obtenerTodos();
        var listaResenhasUsuario = resenhaRepo.obtenerTodasPorIdUsuario(idUsuario,listaResenhas);

        var listaResenhasDtoUsuario = listaResenhasUsuario.stream().map(resenha -> Mapper.mapaSimple(resenha)).toList();
        return listaResenhasDtoUsuario;
    }


    //Consultar estadisticas de reseñas (Ficheros)
}
