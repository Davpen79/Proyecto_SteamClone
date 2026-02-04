package modelo.form;

import enums.TipoEstadoResenha;

import java.time.LocalDateTime;

public class ResenhaForm {

    //Atributos
    private long idUsuarioResenha;
    private long idJuegoResenha;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDateTime fechaPublicacionResenha;
    private LocalDateTime fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaForm(long idUsuarioResenha, long idJuegoResenha, boolean recomendacionResenha, String textoResenha, double tiempoJugadoResenha, LocalDateTime fechaPublicacionResenha, LocalDateTime fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
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

    public long getIdUsuarioResenha() {
        return idUsuarioResenha;
    }

    public void setIdUsuarioResenha(int idUsuarioResenha) {
        this.idUsuarioResenha = idUsuarioResenha;
    }

    public long getIdJuegoResenha() {
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

    public LocalDateTime getFechaPublicacionResenha() {
        return fechaPublicacionResenha;
    }

    public void setFechaPublicacionResenha(LocalDateTime fechaPublicacionResenha) {
        this.fechaPublicacionResenha = fechaPublicacionResenha;
    }

    public LocalDateTime getFechaUltiEdicResenha() {
        return fechaUltiEdicResenha;
    }

    public void setFechaUltiEdicResenha(LocalDateTime fechaUltiEdicResenha) {
        this.fechaUltiEdicResenha = fechaUltiEdicResenha;
    }

    public TipoEstadoResenha getEstadoResenha() {
        return estadoResenha;
    }

    public void setEstadoResenha(TipoEstadoResenha estadoResenha) {
        this.estadoResenha = estadoResenha;
    }
}
