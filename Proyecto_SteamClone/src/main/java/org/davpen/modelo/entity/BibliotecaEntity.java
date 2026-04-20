package org.davpen.modelo.entity;

import jakarta.persistence.*;
import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "bibliotecas")
@Entity
public class BibliotecaEntity {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBiblio;
    @Column(name = "id_usuario")
    private Long idUsuarioBiblio;
    @Column(name = "id_juego")
    private Long idJuegoBiblio;
    @Column(name = "fecha_compra")
    private LocalDate fechaCompraJuegoBiblio;
    @Column(name = "tiempo_jugado")
    private double tiempoJuegoBiblio;
    @Column(name = "ultima_sesion")
    private LocalDateTime ultiFechaJuegoBiblio;
    @Column(name = "estado")
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaEntity(Long idBiblio, Long idUsuarioBiblio, Long idJuegoBiblio, LocalDate fechaCompraJuegoBiblio
            , double tiempoJuegoBiblio, LocalDateTime ultiFechaJuegoBiblio,
                            TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idBiblio = idBiblio;
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.idJuegoBiblio = idJuegoBiblio;
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }

    //Getters

    public Long getIdBiblio() {
        return idBiblio;
    }

    public Long getIdUsuarioBiblio() {
        return idUsuarioBiblio;
    }

    public Long getIdJuegoBiblio() {
        return idJuegoBiblio;
    }

    public LocalDate getFechaCompraJuegoBiblio() {
        return fechaCompraJuegoBiblio;
    }

    public double getTiempoJuegoBiblio() {
        return tiempoJuegoBiblio;
    }

    public LocalDateTime getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BibliotecaEntity that = (BibliotecaEntity) o;
        return Double.compare(tiempoJuegoBiblio, that.tiempoJuegoBiblio) == 0
                && Objects.equals(idBiblio, that.idBiblio)
                && Objects.equals(idUsuarioBiblio, that.idUsuarioBiblio)
                && Objects.equals(idJuegoBiblio, that.idJuegoBiblio)
                && Objects.equals(fechaCompraJuegoBiblio, that.fechaCompraJuegoBiblio)
                && Objects.equals(ultiFechaJuegoBiblio, that.ultiFechaJuegoBiblio)
                && estadoInstJuegoBiblio == that.estadoInstJuegoBiblio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBiblio, idUsuarioBiblio, idJuegoBiblio, fechaCompraJuegoBiblio, tiempoJuegoBiblio,
                ultiFechaJuegoBiblio, estadoInstJuegoBiblio);
    }

    @Override
    public String toString() {
        return "BibliotecaEntity{" +
                "idBiblio=" + idBiblio +
                ", idUsuarioBiblio=" + idUsuarioBiblio +
                ", idJuegoBiblio=" + idJuegoBiblio +
                ", fechaCompraJuegoBiblio=" + fechaCompraJuegoBiblio +
                ", tiempoJuegoBiblio=" + tiempoJuegoBiblio +
                ", ultiFechaJuegoBiblio=" + ultiFechaJuegoBiblio +
                ", estadoInstJuegoBiblio=" + estadoInstJuegoBiblio +
                '}';
    }
}
