package org.davpen.controller;

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

public class BibliotecaController {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;

    public BibliotecaController(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
    }

    //CREAR BIBLIOTECA????
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



    //Añadir juego a biblioteca - Compra Verificada??
    public BibliotecaDto anhadirJuegoABiblioteca(BibliotecaForm bibliotecaForm) throws ValidationException {
        //Validar formato
        var errores = bibliotecaForm.validar();
        //Validar modelo
        //Validar usuario
        if (!usuarioRepo.obtenerPorId(bibliotecaForm.getIdUsuarioBiblio()).isPresent()){
            errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
        }
        //Validar juego
        if (!juegoRepo.obtenerPorId(bibliotecaForm.getIdJuegoBiblio()).isPresent()){
            errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
        }
        if (bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(bibliotecaForm.getIdUsuarioBiblio()))
                .anyMatch(b -> b.getIdJuegoBiblio().equals(bibliotecaForm.getIdJuegoBiblio()))){
            errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var bibliotecaOpt = bibliotecaRepo.crear(bibliotecaForm);
        var biblioteca = bibliotecaOpt.orElse(null);

        return Mapper.mapaSimple(biblioteca);
    }


    //Eliminar juego de biblioteca



    //Actualizar tiempo de juego



    //Consultar ultima sesion



    //Ver estadisticas de biblioteca



    //Filtrar biblioteca (Ficheros)



}
