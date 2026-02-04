package modelo.entity;

import enums.TipoEstadoResenha;

import java.time.LocalDateTime;

public class ResenhaEntity {

    //Atributos
    private long idResenha;
    private int idUsuarioResenha;
    private int idJuegoResenha;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDateTime fechaPublicacionResenha;
    private LocalDateTime fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaEntity(long idResenha, int idUsuarioResenha, int idJuegoResenha, boolean recomendacionResenha, String textoResenha, double tiempoJugadoResenha, LocalDateTime fechaPublicacionResenha, LocalDateTime fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
        this.idResenha = idResenha;
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

    public long getIdResenha() {
        return idResenha;
    }

    public void setIdResenha(int idResenha) {
        this.idResenha = idResenha;
    }

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
