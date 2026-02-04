package modelo.form;

import enums.TipoEstadoResenha;

import java.time.LocalDate;

public class ResenhaForm {

    //Atributos
    private int idUsuarioResenha;
    private int idJuegoResenha;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDate fechaPublicacionResenha;
    private LocalDate fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaForm(int idUsuarioResenha, int idJuegoResenha, boolean recomendacionResenha, String textoResenha, double tiempoJugadoResenha, LocalDate fechaPublicacionResenha, LocalDate fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
        this.idUsuarioResenha = idUsuarioResenha;
        this.idJuegoResenha = idJuegoResenha;
        this.recomendacionResenha = recomendacionResenha;
        this.textoResenha = textoResenha;
        this.tiempoJugadoResenha = tiempoJugadoResenha;
        this.fechaPublicacionResenha = fechaPublicacionResenha;
        this.fechaUltiEdicResenha = fechaUltiEdicResenha;
        this.estadoResenha = estadoResenha;
    }

    //Getters y Setters

    public int getIdUsuarioResenha() {
        return idUsuarioResenha;
    }

    public void setIdUsuarioResenha(int idUsuarioResenha) {
        this.idUsuarioResenha = idUsuarioResenha;
    }

    public int getIdJuegoResenha() {
        return idJuegoResenha;
    }

    public void setIdJuegoResenha(int idJuegoResenha) {
        this.idJuegoResenha = idJuegoResenha;
    }

    public boolean isRecomendacionResenha() {
        return recomendacionResenha;
    }

    public void setRecomendacionResenha(boolean recomendacionResenha) {
        this.recomendacionResenha = recomendacionResenha;
    }

    public String getTextoResenha() {
        return textoResenha;
    }

    public void setTextoResenha(String textoResenha) {
        this.textoResenha = textoResenha;
    }

    public double getTiempoJugadoResenha() {
        return tiempoJugadoResenha;
    }

    public void setTiempoJugadoResenha(double tiempoJugadoResenha) {
        this.tiempoJugadoResenha = tiempoJugadoResenha;
    }

    public LocalDate getFechaPublicacionResenha() {
        return fechaPublicacionResenha;
    }

    public void setFechaPublicacionResenha(LocalDate fechaPublicacionResenha) {
        this.fechaPublicacionResenha = fechaPublicacionResenha;
    }

    public LocalDate getFechaUltiEdicResenha() {
        return fechaUltiEdicResenha;
    }

    public void setFechaUltiEdicResenha(LocalDate fechaUltiEdicResenha) {
        this.fechaUltiEdicResenha = fechaUltiEdicResenha;
    }

    public TipoEstadoResenha getEstadoResenha() {
        return estadoResenha;
    }

    public void setEstadoResenha(TipoEstadoResenha estadoResenha) {
        this.estadoResenha = estadoResenha;
    }
}
