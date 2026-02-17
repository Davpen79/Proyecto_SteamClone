package org.davpen.modelo.form;

import org.davpen.enums.TipoEstadoResenha;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResenhaForm {

    //Atributos
    private Long idUsuarioResenha;
    private Long idJuegoResenha;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDate fechaPublicacionResenha;
    private LocalDate fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaForm(Long idUsuarioResenha, Long idJuegoResenha, boolean recomendacionResenha, String textoResenha, double tiempoJugadoResenha, LocalDate fechaPublicacionResenha, LocalDate fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
        this.idUsuarioResenha = idUsuarioResenha;
        this.idJuegoResenha = idJuegoResenha;
        this.recomendacionResenha = recomendacionResenha;
        this.textoResenha = textoResenha;
        this.tiempoJugadoResenha = tiempoJugadoResenha;
        this.fechaPublicacionResenha = fechaPublicacionResenha;
        this.fechaUltiEdicResenha = fechaUltiEdicResenha;
        this.estadoResenha = estadoResenha;
    }

    //Getters

    public Long getIdUsuarioResenha() {
        return idUsuarioResenha;
    }

    public Long getIdJuegoResenha() {
        return idJuegoResenha;
    }

    public boolean isRecomendacionResenha() {
        return recomendacionResenha;
    }

    public String getTextoResenha() {
        return textoResenha;
    }

    public double getTiempoJugadoResenha() {
        return tiempoJugadoResenha;
    }

    public LocalDate getFechaPublicacionResenha() {
        return fechaPublicacionResenha;
    }

    public LocalDate getFechaUltiEdicResenha() {
        return fechaUltiEdicResenha;
    }

    public TipoEstadoResenha getEstadoResenha() {
        return estadoResenha;
    }

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones usuario
        if (idUsuarioResenha == null){
            errores.add(new ErrorDto("Id_Usario", ErrorType.REQUERIDO));
        }
        //Validaciones juego
        if (idJuegoResenha == null){
            errores.add(new ErrorDto("Id_Juego", ErrorType.REQUERIDO));
        }
        //TODO Validaciones Recomendacion


        //Validaciones Texto Reseña
        if (textoResenha == null || textoResenha.isBlank()){
            errores.add(new ErrorDto("texto_reseña", ErrorType.REQUERIDO));
        }

        if (textoResenha != null && textoResenha.length() < 50){
            errores.add(new ErrorDto("texto_reseña", ErrorType.DEMASIADO_CORTO));
        }

        if (textoResenha != null && textoResenha.length() > 8000){
            errores.add(new ErrorDto("texto_reseña", ErrorType.DEMASIADO_LARGO));
        }

        return errores;
    }
}
