package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;

public class BibliotecaDto {

    //Atributos
    private Long idBiblio;
    private Long idUsuarioBiblio;
    private Long idJuegoBiblio;
    private LocalDate fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDate ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaDto(Long idBiblio, Long idUsuarioBiblio, Long idJuegoBiblio, LocalDate fechaCompraJuegoBiblio, double tiempoJuegoBiblio, LocalDate ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
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

    public LocalDate getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }
}
