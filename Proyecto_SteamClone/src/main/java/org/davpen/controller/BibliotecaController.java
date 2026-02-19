package org.davpen.controller;

import org.davpen.enums.TipoEstadoInstalacion;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.BibliotecaDto;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaController {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;

    public BibliotecaController(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
    }

    //CREAR BIBLIOTECA???? INNECESARIO
    public BibliotecaDto crearBiblioteca(BibliotecaForm bibliotecaForm) throws ValidationException {
        //Validar Formato
        var errores = bibliotecaForm.validar();
        //Validar modelo
        //Validar usuario
        if (!usuarioRepo.obtenerPorId(bibliotecaForm.getIdUsuarioBiblio()).isPresent()){
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var bibliotecaOpt = bibliotecaRepo.crear(bibliotecaForm);
        var biblioteca = bibliotecaOpt.orElse(null);

        return Mapper.mapaSimple(biblioteca);
    }

    //Ver biblioteca personal
    public List<BibliotecaDto> verBibliotecaPersonal(Long idUsuario){

        return bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                .map(Mapper::mapaSimple)
                .toList();
    }

    //Añadir juego a biblioteca - Compra Verificada?? == Crear Biblioteca
    public BibliotecaDto anhadirJuegoABiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        //Validar modelo
        var errores = new ArrayList<ErrorDto>();
        //Validar usuario
        if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()){
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        //Validar juego
        if (!juegoRepo.obtenerPorId(idJuego).isPresent()){
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        if (bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuego))){
            errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var bibliotecaNuevaEntrada = new BibliotecaForm(idUsuario,idJuego,LocalDate.now(),0.00,
                                     null, TipoEstadoInstalacion.NO_INSTALADO);
        var bibliotecaOpt = bibliotecaRepo.crear(bibliotecaNuevaEntrada);
        var biblioteca = bibliotecaOpt.orElse(null);

        return Mapper.mapaSimple(biblioteca);
    }

    //Eliminar juego de biblioteca
    public boolean eliminarJuegoDeBiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar Usuario && Juego existen
        var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
        var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
        if (!usuarioExiste || !juegoExiste){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        //Validar entrada en Biblioteca existe
        var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                .findFirst();
        if (entradaBiblioteca.isEmpty()){
            errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var idEntradaEncontrada = entradaBiblioteca.get().getIdBiblio();

        return bibliotecaRepo.eliminar(idEntradaEncontrada);
    }

    //Actualizar tiempo de juego
    public double actualizarTiempoJuego(Long idUsuario,Long idJuego,double horasParaAnhadir) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar Usuario && Juego existen
        var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
        var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
        if (!usuarioExiste || !juegoExiste){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        //Validar entrada en Biblioteca existe
        var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                .findFirst();
        if (entradaBiblioteca.isEmpty()){
            errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var entradaBibEntity = entradaBiblioteca.get();
        var idEntradaEncontrada = entradaBibEntity.getIdBiblio();
        var horasActualizadas = entradaBibEntity.getTiempoJuegoBiblio() + horasParaAnhadir;

        var bibliotecaActualizadaForm = new BibliotecaForm(entradaBibEntity.getIdUsuarioBiblio(), entradaBibEntity.getIdJuegoBiblio(),
                                        entradaBibEntity.getFechaCompraJuegoBiblio(), horasActualizadas, entradaBibEntity.getUltiFechaJuegoBiblio(),
                                        entradaBibEntity.getEstadoInstJuegoBiblio());

        bibliotecaRepo.actualizar(idEntradaEncontrada, bibliotecaActualizadaForm);
        return horasActualizadas;
    }

    //Consultar ultima sesion
    public LocalDateTime consultarUltimaSesion(Long idUsuario,Long idJuego) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        //Validar Usuario && Juego existen
        var usuarioExiste = usuarioRepo.obtenerPorId(idUsuario).isPresent();
        var juegoExiste = juegoRepo.obtenerPorId(idJuego).isPresent();
        if (!usuarioExiste || !juegoExiste){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        //Validar entrada en Biblioteca existe
        var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                .filter(u -> u.getIdUsuarioBiblio().equals(idUsuario))
                .filter(j -> j.getIdJuegoBiblio().equals(idJuego))
                .findFirst();
        if (entradaBiblioteca.isEmpty()){
            errores.add(new ErrorDto("entrada", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var entradaBibEntity = entradaBiblioteca.get();
        var ultimaPartida = entradaBibEntity.getUltiFechaJuegoBiblio();

        if (ultimaPartida == null){
            errores.add(new ErrorDto("Fecha_ultima_sesion", ErrorType.NUNCA_JUGADO));
        }
        return ultimaPartida;
    }

    //TODO: Ver estadísticas de biblioteca => NUEVO OBJETO?



    //TODO: Filtrar biblioteca (Ficheros)


}
