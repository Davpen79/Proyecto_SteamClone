package org.davpen.controller;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.JuegoDto;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.intefaces.IJuegoRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JuegoController {

    private final IJuegoRepo juegoRepo;

    public JuegoController(IJuegoRepo juegoRepo) {
        this.juegoRepo = juegoRepo;
    }


    //Añadir juego al catálogo
    public JuegoDto anhadirJuego(JuegoForm juegoForm) throws ValidationException {
        //validar formato
        var errores = juegoForm.validar();
        //validar modelo
        //juego unico
        if(juegoRepo.obtenerTodos().stream()
                .anyMatch(j -> j.getTituloJuego().equals(juegoForm.getTituloJuego()))){
            errores.add(new ErrorDto("titulo", ErrorType.DUPLICADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var juegoOpt = juegoRepo.crear(juegoForm);
        var juego = juegoOpt.orElse(null);

        return Mapper.mapaJuegoCompleto(juego);
    }


    //TODO: Buscar juegos
    public List<JuegoDto> listaJuegosPorCategoria(TipoCategoriaJuego categoria){
        return juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getCategoriaJuego().equals(categoria))
                .map(Mapper::mapaJuegoCompleto)
                .toList();
    }


    //TODO: Consultar catalogo completo - Metadatos Paginacion??



    //TODO: Consultar detalles de juego + estadísticas y reseñas destacadas
    public JuegoDto detalleJuego(Long id) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!juegoRepo.obtenerPorId(id).isPresent()){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var juegoInfo = juegoRepo.obtenerPorId(id);
        var juego = juegoInfo.orElse(null);
        return Mapper.mapaJuegoCompleto(juego);

    }


    //Aplicar descuento

    public double aplicarDescuento(Long id, int descuento) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!juegoRepo.obtenerPorId(id).isPresent()){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (juegoRepo.obtenerPorId(id).get().getEstadoJuego() == TipoEstadoJuego.NO_DISPONIBLE){
            errores.add(new ErrorDto("estado", ErrorType.NO_DISPONIBLE));
        }
        if (descuento < 0){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_NEGATIVO));
        }
        if (descuento > 0){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var juegoActual = juegoRepo.obtenerPorId(id);
        var nuevoPrecio = juegoRepo.obtenerPorId(id).get().getPrecioBaseJuego()
                        - (juegoRepo.obtenerPorId(id).get().getPrecioBaseJuego() * descuento / 100);

        var juegoActualizadoForm = new JuegoForm(juegoActual.get().getTituloJuego(), juegoActual.get().getDescripcionJuego(),
                            juegoActual.get().getDesarrolladorJuego(), juegoActual.get().getFechaLanzaJuego(),
                            nuevoPrecio, descuento, juegoActual.get().getCategoriaJuego(), juegoActual.get().getClasEdadJuego(),
                            juegoActual.get().getIdiomasJuego(), juegoActual.get().getEstadoJuego());

        juegoRepo.actualizar(id, juegoActualizadoForm);

        return nuevoPrecio;
    }

    //Cambiar estado de juego

    public TipoEstadoJuego cambiarEstadoJuego(Long id, TipoEstadoJuego nuevoEstado) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!juegoRepo.obtenerPorId(id).isPresent()){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (!Arrays.stream(TipoEstadoJuego.values()).anyMatch(t -> t.equals(nuevoEstado))){
            errores.add(new ErrorDto("estado", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }
        var juegoActual = juegoRepo.obtenerPorId(id);
        var juegoActualizadoForm = new JuegoForm(juegoActual.get().getTituloJuego(), juegoActual.get().getDescripcionJuego(),
                juegoActual.get().getDesarrolladorJuego(), juegoActual.get().getFechaLanzaJuego(),
                juegoActual.get().getPrecioBaseJuego(), juegoActual.get().getDescuentoActualJuego(), juegoActual.get().getCategoriaJuego(),
                juegoActual.get().getClasEdadJuego(), juegoActual.get().getIdiomasJuego(), nuevoEstado);

        juegoRepo.actualizar(id, juegoActualizadoForm);

        return nuevoEstado;
    }


}
