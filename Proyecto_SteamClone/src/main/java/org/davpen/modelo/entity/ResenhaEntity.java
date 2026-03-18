package org.davpen.modelo.entity;

import org.davpen.enums.TipoEstadoResenha;

import java.time.LocalDate;
import java.util.Objects;

public class ResenhaEntity {

    //Atributos
    private Long idResenha;
    private Long idUsuarioResenha;
    private Long idJuegoResenha;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDate fechaPublicacionResenha;
    private LocalDate fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaEntity(Long idResenha, Long idUsuarioResenha, Long idJuegoResenha, boolean recomendacionResenha,
                         String textoResenha, double tiempoJugadoResenha, LocalDate fechaPublicacionResenha,
                         LocalDate fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
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

    //Getters

    public Long getIdResenha() {
        return idResenha;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResenhaEntity that = (ResenhaEntity) o;
        return recomendacionResenha == that.recomendacionResenha
                && Double.compare(tiempoJugadoResenha, that.tiempoJugadoResenha) == 0
                && Objects.equals(idResenha, that.idResenha)
                && Objects.equals(idUsuarioResenha, that.idUsuarioResenha)
                && Objects.equals(idJuegoResenha, that.idJuegoResenha)
                && Objects.equals(textoResenha, that.textoResenha)
                && Objects.equals(fechaPublicacionResenha, that.fechaPublicacionResenha)
                && Objects.equals(fechaUltiEdicResenha, that.fechaUltiEdicResenha)
                && estadoResenha == that.estadoResenha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idResenha, idUsuarioResenha, idJuegoResenha, recomendacionResenha, textoResenha,
                tiempoJugadoResenha, fechaPublicacionResenha, fechaUltiEdicResenha, estadoResenha);
    }
}
