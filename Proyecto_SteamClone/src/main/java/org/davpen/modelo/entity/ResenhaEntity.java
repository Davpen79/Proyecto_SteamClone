package org.davpen.modelo.entity;

import jakarta.persistence.*;
import org.davpen.enums.TipoEstadoResenha;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "reseñas")
@Entity
public class ResenhaEntity {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResenha;
    @Column(name = "id_usuario")
    private Long idUsuarioResenha;
    @Column(name = "id_juego")
    private Long idJuegoResenha;
    @Column(name = "recomendacion")
    private boolean recomendacionResenha;
    @Column(name = "texto")
    private String textoResenha;
    @Column(name = "tiempo_jugado")
    private double tiempoJugadoResenha;
    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacionResenha;
    @Column(name = "fecha_edicion")
    private LocalDate fechaUltiEdicResenha;
    @Column(name = "estado")
    private TipoEstadoResenha estadoResenha;

    //Constructor
    public ResenhaEntity(){}

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

    //Constructor
    public ResenhaEntity(Long idUsuarioResenha, Long idJuegoResenha, boolean recomendacionResenha,
                         String textoResenha, double tiempoJugadoResenha, LocalDate fechaPublicacionResenha,
                         LocalDate fechaUltiEdicResenha, TipoEstadoResenha estadoResenha) {
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

    @Override
    public String toString() {
        return "ResenhaEntity{" +
                "idResenha=" + idResenha +
                ", idUsuarioResenha=" + idUsuarioResenha +
                ", idJuegoResenha=" + idJuegoResenha +
                ", recomendacionResenha=" + recomendacionResenha +
                ", textoResenha='" + textoResenha + '\'' +
                ", tiempoJugadoResenha=" + tiempoJugadoResenha +
                ", fechaPublicacionResenha=" + fechaPublicacionResenha +
                ", fechaUltiEdicResenha=" + fechaUltiEdicResenha +
                ", estadoResenha=" + estadoResenha +
                '}';
    }
}
