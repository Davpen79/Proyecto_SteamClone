package org.davpen.modelo.entity;

import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BibliotecaEntity {

    //Atributos
    private Long idBiblio;
    private Long idUsuarioBiblio;
    private Long idJuegoBiblio;
    private LocalDate fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDateTime ultiFechaJuegoBiblio;
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
}
