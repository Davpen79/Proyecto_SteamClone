package org.davpen.controller;

import org.davpen.enums.TipoEstadoInstalacion;
import org.davpen.enums.TipoOrden;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.BibliotecaDto;
import org.davpen.modelo.dto.EstadisticasBibliotecaDto;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.repositorio.interfaces.IBibliotecaRepo;
import org.davpen.repositorio.interfaces.ICompraRepo;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.davpen.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BibliotecaController {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final ICompraRepo compraRepo;
    public ITransactionManager transMgr;

    public BibliotecaController(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo,
                                ICompraRepo compraRepo, ITransactionManager transaMgr) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.compraRepo = compraRepo;
        this.transMgr = transaMgr;
    }

    /**
     * Devuelve la biblioteca personal del usuario ordenada según el criterio indicado.
     * <p>
     * Validaciones:
     * - El usuario debe existir.
     * <p>
     * Orden disponible (TipoOrden):
     * <p>
     * - ALFABETICO: ordena por título del juego.
     * <p>
     * - TIEMPO_JUEGO: ordena por tiempo de juego (descendente).
     * <p>
     * - ULTIMA_SESION: ordena por fecha de última sesión (descendente).
     * <p>
     * - FECHA_ADQUISICION: ordena por fecha de compra (descendente).
     *
     * @param idUsuario Identificador del usuario cuya biblioteca se consulta.
     * @param tipoOrden Criterio de ordenación.
     * @return Lista de BibliotecaDto ordenada según el criterio; puede ser vacía si no hay entradas.
     * @throws ValidationException Si el usuario no existe.
     */
    public List<BibliotecaDto> verBibliotecaPersonal(Long idUsuario, TipoOrden tipoOrden) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var bibliotecaOrdenada = transMgr.inTransaction(() -> {
            //Validar usuario
            if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()) {
                errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            List<BibliotecaDto> bibliotecaOrd = new ArrayList<>();
            var usuario = usuarioRepo.obtenerPorId(idUsuario).get();


            //lista ordenada alfabeticamente
            if (tipoOrden == TipoOrden.ALFABETICO) {
                var listaDesordenada = bibliotecaRepo.obtenerTodos().stream()
                        .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                        .map(b -> Mapper.mapaCompleto(b,usuario,
                                juegoRepo.obtenerPorId(b.getIdJuegoBiblio()).get()))
                        .toList();
                bibliotecaOrd = listaDesordenada.stream()
                        .sorted(Comparator.comparing(b -> b.getJuegoDto().get().getTituloJuego()))
                        .toList();
            }

            //lista ordenada por tiempo de juego
            else if (tipoOrden == TipoOrden.TIEMPO_JUEGO) {
                bibliotecaOrd = bibliotecaRepo.obtenerTodos().stream()
                        .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                        .sorted(Comparator.comparingDouble(BibliotecaEntity::getTiempoJuegoBiblio).reversed())
                        .map(Mapper::mapaSimple)
                        .toList();

            }
            //lista ordenada por fecha de ultima sesion
            else if (tipoOrden == TipoOrden.ULTIMA_SESION) {
                bibliotecaOrd = bibliotecaRepo.obtenerTodos().stream()
                        .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                        .filter(b -> !Objects.isNull(b.getUltiFechaJuegoBiblio()))
                        .sorted(Comparator.comparing(BibliotecaEntity::getUltiFechaJuegoBiblio).reversed())
                        .map(Mapper::mapaSimple)
                        .toList();
            }
            //lista ordenada por fecha adquisicion
            else if (tipoOrden == TipoOrden.FECHA_ADQUISICION) {
                bibliotecaOrd = bibliotecaRepo.obtenerTodos().stream()
                        .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                        .sorted(Comparator.comparing(BibliotecaEntity::getFechaCompraJuegoBiblio).reversed())
                        .map(Mapper::mapaSimple)
                        .toList();
            }
            return bibliotecaOrd;

        });

        return bibliotecaOrdenada;
    }

    /**
     * Añade un juego a la biblioteca de un usuario.
     * <p>
     * Validaciones:
     * - El usuario debe existir.<p>
     * - El juego debe existir.<p>
     * - El juego no debe estar ya en la biblioteca del usuario.
     * <p>
     * Crea una entrada en la biblioteca con fecha de compra actual, tiempo jugado 0.00 y estado NO_INSTALADO.
     *
     * @param idUsuario Identificador del usuario.
     * @param idJuego   Identificador del juego a añadir.
     * @return BibliotecaDto con la entrada creada.
     * @throws ValidationException Si alguna validación falla; la excepción contiene la lista de errores.
     */
    public BibliotecaDto anhadirJuegoABiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        //Validar modelo
        var errores = new ArrayList<ErrorDto>();

        var biblioteca = transMgr.inTransaction(() -> {

            //Validar usuario
            if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()) {
                errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //Validar juego
            if (!juegoRepo.obtenerPorId(idJuego).isPresent()) {
                errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            if (bibliotecaRepo.obtenerTodos().stream().filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuego))) {
                errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var bibliotecaNuevaEntrada = new BibliotecaForm(idUsuario, idJuego, LocalDate.now(),
                    0.00, null, TipoEstadoInstalacion.NO_INSTALADO);
            var bibliotecaOpt = bibliotecaRepo.crear(bibliotecaNuevaEntrada);
            return bibliotecaOpt;

        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaSimple(biblioteca.orElse(null));
    }

    /**
     * Elimina un juego de la biblioteca de un usuario.
     * <p>
     * Validaciones:<p>
     * - El usuario y el juego deben existir.<p></p>
     * - La entrada en la biblioteca debe existir.<p></p>
     *
     * <p>
     * Elimina la entrada encontrada y devuelve el Dto de la entrada eliminada.
     *
     * @param idUsuario Identificador del usuario.
     * @param idJuego   Identificador del juego a eliminar.
     * @return BibliotecaDto con la entrada eliminada.
     * @throws ValidationException Si alguna validación falla; la excepción contiene la lista de errores.
     */
    public BibliotecaDto eliminarJuegoDeBiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var bibliotecaEliminada = transMgr.inTransaction(() -> {
            //Validar Usuario && Juego existen
            var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
            var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
            if (!usuarioExiste || !juegoExiste) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //Validar entrada en Biblioteca existe
            var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                    .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                    .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                    .findFirst();
            if (entradaBiblioteca.isEmpty()) {
                errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            var idEntradaEncontrada = entradaBiblioteca.get().getIdBiblio();
            bibliotecaRepo.eliminar(idEntradaEncontrada);

            return entradaBiblioteca;
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaSimple(bibliotecaEliminada.orElse(null));
    }

    /**
     * Actualiza el tiempo de juego registrado en la biblioteca para una entrada concreta.
     * <p>
     * Validaciones:<p>
     * - El usuario y el juego deben existir.<p>
     * - La entrada en la biblioteca debe existir.<p>
     * - Las horas a añadir deben ser >= 0.<p>
     * <p>
     * Suma las horas proporcionadas al tiempo existente y actualiza la entrada.
     *
     * @param idUsuario        Identificador del usuario.
     * @param idJuego          Identificador del juego.
     * @param horasParaAnhadir Horas a sumar al tiempo de juego (double).
     * @return BibliotecaDto con la entrada actualizada.
     * @throws ValidationException Si alguna validación falla; la excepción contiene la lista de errores.
     */
    public BibliotecaDto actualizarTiempoJuego(Long idUsuario, Long idJuego, double horasParaAnhadir) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var bibliotecaActualizadaEntity = transMgr.inTransaction(() -> {
            //Validar Usuario && Juego existen
            var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
            var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
            if (!usuarioExiste || !juegoExiste) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //Validar entrada en Biblioteca existe
            var entradaBiblioteca =
                    bibliotecaRepo.obtenerTodos().stream()
                            .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                            .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                            .findFirst();
            if (entradaBiblioteca.isEmpty()) {
                errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
            }
            //validar horas jugadas positivas - Deberia ser en Formulario??
            if (horasParaAnhadir < 0) {
                errores.add(new ErrorDto("horas", ErrorType.VALOR_NEGATIVO));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            var entradaBibEntity = entradaBiblioteca.get();
            var idEntradaEncontrada = entradaBibEntity.getIdBiblio();
            var horasActualizadas = entradaBibEntity.getTiempoJuegoBiblio() + horasParaAnhadir;

            var bibliotecaActualizadaForm = new BibliotecaForm(entradaBibEntity.getIdUsuarioBiblio(),
                    entradaBibEntity.getIdJuegoBiblio(), entradaBibEntity.getFechaCompraJuegoBiblio(),
                    horasActualizadas,
                    entradaBibEntity.getUltiFechaJuegoBiblio(), entradaBibEntity.getEstadoInstJuegoBiblio());

            var bibliotecaActualizada =
                    bibliotecaRepo.actualizar(idEntradaEncontrada, bibliotecaActualizadaForm).orElse(null);
            return bibliotecaActualizada;

        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaSimple(bibliotecaActualizadaEntity);
    }

    /**
     * Devuelve la última sesión jugada para una entrada concreta de la biblioteca.
     * <p>
     * Validaciones:<p>
     * - El usuario y el juego deben existir.<p>
     * - La entrada en la biblioteca debe existir.<p>
     * - Si la entrada nunca fue jugada.<p>
     *
     * @param idUsuario Identificador del usuario.
     * @param idJuego   Identificador del juego.
     * @return BibliotecaDto con la entrada (incluye la fecha de la última sesión).
     * @throws ValidationException Si alguna validación falla; la excepción contiene la lista de errores.
     */

    public BibliotecaDto consultarUltimaSesion(Long idUsuario, Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var ultimaSesion = transMgr.inTransaction(() -> {
            //Validar Usuario y Juego existen
            var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
            var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
            if (!usuarioExiste || !juegoExiste) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //Validar entrada en Biblioteca existe
            var entradaBiblioteca =
                    bibliotecaRepo.obtenerTodos().stream()
                            .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                            .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                            .findFirst();
            if (entradaBiblioteca.isEmpty()) {
                errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            var entradaBibEntity = entradaBiblioteca.get();
            var ultimaPartida = entradaBibEntity.getUltiFechaJuegoBiblio();

            if (ultimaPartida == null) {
                errores.add(new ErrorDto("Fecha_ultima_sesion", ErrorType.NUNCA_JUGADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            return entradaBibEntity;
        });

        return Mapper.mapaSimple(ultimaSesion);
    }

    /**
     * Calcula y devuelve estadísticas de la biblioteca del usuario.
     * <p>
     * Estadísticas calculadas:<p>
     * - Número total de juegos en la biblioteca.<p>
     * - Total de horas jugadas.<p>
     * - Lista de juegos instalados (mapeados a JuegoDto).<p>
     * - Juego más jugado (opcional JuegoDto).<p>
     * - Valor total de la biblioteca (suma de precios de compras asociadas).<p>
     * - Lista de juegos no jugados.<p>
     *
     * @param idUsuario Identificador del usuario.
     * @return EstadisticasBibliotecaDto con los datos agregados de la biblioteca.
     */
    public EstadisticasBibliotecaDto verEstadisticasBiblioteca(Long idUsuario) throws ValidationException {

        var estadisticas = transMgr.inTransaction(() -> {
            var bibliotecaUsuario = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .toList();

            var totalHoras = bibliotecaUsuario.stream()
                    .map(b -> b.getTiempoJuegoBiblio())
                    .reduce((a, b) -> a + b)
                    .orElse(0d);

            var listaJuegosEnBibliotecaInstalados = bibliotecaUsuario.stream()
                    .filter(b -> b.getEstadoInstJuegoBiblio().equals(TipoEstadoInstalacion.INSTALADO))
                    .toList();

            var listaJuegosInstalados = listaJuegosEnBibliotecaInstalados.stream()
                    .map(b -> juegoRepo.obtenerPorId(b.getIdJuegoBiblio()).get())
                    .map(Mapper::mapaJuegoCompleto)
                    .toList();

            var bibliotecaJuegoMasJugado = bibliotecaUsuario.stream()
                    .max(Comparator.comparing(BibliotecaEntity::getTiempoJuegoBiblio));

            var juegoMasJugado = bibliotecaJuegoMasJugado.flatMap(b -> juegoRepo.obtenerPorId(b.getIdJuegoBiblio()))
                    .map(Mapper::mapaJuegoCompleto);

            var listaComprasBiblioteca = compraRepo.obtenerTodos().stream()
                    .filter(c -> c.getIdUsuarioCompra().equals(idUsuario))
                    .toList();
            var valorTotalBiblioteca = listaComprasBiblioteca.stream()
                    .map(c -> c.getPrecioBaseCompra())
                    .reduce((a, b) -> a + b)
                    .orElse(0d);

            var listaJuegosNoJugados = bibliotecaUsuario.stream()
                    .filter(b -> b.getTiempoJuegoBiblio() == 0d)
                    .map(b -> juegoRepo.obtenerPorId(b.getIdJuegoBiblio()).get())
                    .map(Mapper::mapaJuegoCompleto)
                    .toList();

            return new EstadisticasBibliotecaDto(idUsuario, bibliotecaUsuario.size(), totalHoras, listaJuegosInstalados,
                    juegoMasJugado, valorTotalBiblioteca, listaJuegosNoJugados);

        });
        return estadisticas;
    }

}





