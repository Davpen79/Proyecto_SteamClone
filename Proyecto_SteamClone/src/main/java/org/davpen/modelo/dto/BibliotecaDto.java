package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class BibliotecaDto {

    //Atributos
    private Long idBiblio;
    private Long idUsuarioBiblio;
    private Optional<UsuarioDto> usuarioDto;
    private Long idJuegoBiblio;
    private Optional<JuegoDto> juegoDto;
    private LocalDate fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDateTime ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor

    public BibliotecaDto(Long idBiblio, Long idUsuarioBiblio, Optional<UsuarioDto> usuarioDto, Long idJuegoBiblio,
                         Optional<JuegoDto> juegoDto, LocalDate fechaCompraJuegoBiblio, double tiempoJuegoBiblio,
                         LocalDateTime ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idBiblio = idBiblio;
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.usuarioDto = usuarioDto;
        this.idJuegoBiblio = idJuegoBiblio;
        this.juegoDto = juegoDto;
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }


    //Getters

    public Optional<UsuarioDto> getUsuarioDto() {
        return usuarioDto;
    }

    public Optional<JuegoDto> getJuegoDto() {
        return juegoDto;
    }

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
        BibliotecaDto that = (BibliotecaDto) o;
        return Double.compare(tiempoJuegoBiblio, that.tiempoJuegoBiblio) == 0
                && Objects.equals(idBiblio, that.idBiblio)
                && Objects.equals(idUsuarioBiblio, that.idUsuarioBiblio)
                && Objects.equals(usuarioDto, that.usuarioDto)
                && Objects.equals(idJuegoBiblio, that.idJuegoBiblio)
                && Objects.equals(juegoDto, that.juegoDto)
                && Objects.equals(fechaCompraJuegoBiblio, that.fechaCompraJuegoBiblio)
                && Objects.equals(ultiFechaJuegoBiblio, that.ultiFechaJuegoBiblio)
                && estadoInstJuegoBiblio == that.estadoInstJuegoBiblio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBiblio, idUsuarioBiblio, usuarioDto, idJuegoBiblio, juegoDto, fechaCompraJuegoBiblio,
                tiempoJuegoBiblio, ultiFechaJuegoBiblio, estadoInstJuegoBiblio);
    }

    @Override
    public String toString() {
        return "BibliotecaDto{" +
                "idBiblio=" + idBiblio +
                ", idUsuarioBiblio=" + idUsuarioBiblio +
                ", usuarioDto=" + usuarioDto +
                ", idJuegoBiblio=" + idJuegoBiblio +
                ", juegoDto=" + juegoDto +
                ", fechaCompraJuegoBiblio=" + fechaCompraJuegoBiblio +
                ", tiempoJuegoBiblio=" + tiempoJuegoBiblio +
                ", ultiFechaJuegoBiblio=" + ultiFechaJuegoBiblio +
                ", estadoInstJuegoBiblio=" + estadoInstJuegoBiblio +
                '}';
    }
}
