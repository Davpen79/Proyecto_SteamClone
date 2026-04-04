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
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BibliotecaController {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final ICompraRepo compraRepo;

    public BibliotecaController(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo,
                                ICompraRepo compraRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.compraRepo = compraRepo;
    }

    //Ver biblioteca personal
    public List<BibliotecaDto> verBibliotecaPersonal(Long idUsuario, TipoOrden tipoOrden) throws ValidationException {
        //Validar usuario
        var errores = new ArrayList<ErrorDto>();
        if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()) {
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        List<BibliotecaDto> bibliotecaOrdenada = new ArrayList<>();

        //lista ordenada alfabeticamente
        if (tipoOrden == TipoOrden.ALFABETICO) {
            var listaDesordenada = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .map(Mapper::mapaSimple)
                    .toList();
            bibliotecaOrdenada = listaDesordenada.stream()
                    .sorted(Comparator.comparing(b -> b.getJuegoDto().orElseThrow().getTituloJuego()))
                    .toList();
        }

        //lista ordenada por tiempo de juego
        else if (tipoOrden == TipoOrden.TIEMPO_JUEGO) {
            bibliotecaOrdenada = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .sorted(Comparator.comparingDouble(BibliotecaEntity::getTiempoJuegoBiblio).reversed())
                    .map(Mapper::mapaSimple)
                    .toList();

        }
        //lista ordenada por
        else if (tipoOrden == TipoOrden.ULTIMA_SESION) {
            bibliotecaOrdenada = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .sorted(Comparator.comparing(BibliotecaEntity::getUltiFechaJuegoBiblio).reversed())
                    .map(Mapper::mapaSimple)
                    .toList();
        }
        //lista ordenada por fecha adquisicion
        else if (tipoOrden == TipoOrden.FECHA_ADQUISICION) {
            bibliotecaOrdenada = bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .sorted(Comparator.comparing(BibliotecaEntity::getFechaCompraJuegoBiblio).reversed())
                    .map(Mapper::mapaSimple)
                    .toList();
        }

        return bibliotecaOrdenada;
    }

    //Añadir juego a biblioteca - ¿Compra Verificada?? == Crear Biblioteca
    public BibliotecaDto anhadirJuegoABiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        //Validar modelo
        var errores = new ArrayList<ErrorDto>();
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
        var biblioteca = bibliotecaOpt.orElse(null);

        return Mapper.mapaSimple(biblioteca);
    }

    //Eliminar juego de biblioteca - ¿Como devuelve algo que elimina?
    public BibliotecaDto eliminarJuegoDeBiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
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

        return Mapper.mapaSimple(entradaBiblioteca.orElseThrow());
    }

    //Actualizar tiempo de juego
    public BibliotecaDto actualizarTiempoJuego(Long idUsuario, Long idJuego, double horasParaAnhadir) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
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
                entradaBibEntity.getIdJuegoBiblio(), entradaBibEntity.getFechaCompraJuegoBiblio(), horasActualizadas,
                entradaBibEntity.getUltiFechaJuegoBiblio(), entradaBibEntity.getEstadoInstJuegoBiblio());

        var bibliotecaActualizada =
                bibliotecaRepo.actualizar(idEntradaEncontrada, bibliotecaActualizadaForm).orElse(null);
        return Mapper.mapaSimple(bibliotecaActualizada);
    }

    //Consultar ultima sesion
    public BibliotecaDto consultarUltimaSesion(Long idUsuario, Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
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

        var bibliotecaUltimaSesion = Mapper.mapaSimple(entradaBibEntity);
        return bibliotecaUltimaSesion;
    }

    //Ver estadísticas de biblioteca
    public EstadisticasBibliotecaDto verEstadisticasBiblioteca(Long idUsuario) {

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
        //codigo antiguo
        //var juegoMasJugado = juegoRepo.obtenerPorId(bibliotecaJuegoMasJugado.get().getIdJuegoBiblio()).get();
        //nuevo codigo corregido
        var juegoMasJugado = bibliotecaJuegoMasJugado.flatMap(b -> juegoRepo.obtenerPorId(b.getIdJuegoBiblio()))
                .map(Mapper::mapaJuegoCompleto);

        var listComprasBiblioteca = bibliotecaUsuario.stream()
                .map(b -> compraRepo.obtenerPorIdUsuario(b.getIdUsuarioBiblio()))
                .toList();
        var valorTotalBiblioteca = listComprasBiblioteca.stream()
                .map(c -> c.get().getPrecioBaseCompra())
                .reduce((a, b) -> a + b)
                .orElse(0d);
        var listaJuegosNoJugados = bibliotecaUsuario.stream()
                .filter(b -> b.getTiempoJuegoBiblio() == 0d)
                .map(b -> juegoRepo.obtenerPorId(b.getIdJuegoBiblio()).get())
                .map(Mapper::mapaJuegoCompleto)
                .toList();

        return new EstadisticasBibliotecaDto(idUsuario, bibliotecaUsuario.size(), totalHoras, listaJuegosInstalados,
                juegoMasJugado, valorTotalBiblioteca, listaJuegosNoJugados);
    }

    //TODO: Filtrar biblioteca (Ficheros)

}





