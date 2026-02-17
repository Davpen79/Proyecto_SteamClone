package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;
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
    private LocalDate ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor

    public BibliotecaDto(Long idBiblio, Long idUsuarioBiblio, Optional<UsuarioDto> usuarioDto, Long idJuegoBiblio,
                         Optional<JuegoDto> juegoDto, LocalDate fechaCompraJuegoBiblio, double tiempoJuegoBiblio,
                         LocalDate ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
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

    public LocalDate getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }
}
