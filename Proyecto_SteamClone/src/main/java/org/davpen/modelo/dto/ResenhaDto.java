package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoResenha;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class ResenhaDto {

    //Atributos
    private Long idResenha;
    private Long idUsuarioResenha;
    private Optional<UsuarioDto> usuarioDto;
    private Long idJuegoResenha;
    private Optional<JuegoDto> juegoDto;
    private boolean recomendacionResenha;
    private String textoResenha;
    private double tiempoJugadoResenha;
    private LocalDate fechaPublicacionResenha;
    private LocalDate fechaUltiEdicResenha;
    private TipoEstadoResenha estadoResenha;

    //Constructor

    public ResenhaDto(Long idResenha, Long idUsuarioResenha, Optional<UsuarioDto> usuarioDto, Long idJuegoResenha,
                      Optional<JuegoDto> juegoDto, boolean recomendacionResenha, String textoResenha,
                      double tiempoJugadoResenha, LocalDate fechaPublicacionResenha, LocalDate fechaUltiEdicResenha,
                      TipoEstadoResenha estadoResenha) {
        this.idResenha = idResenha;
        this.idUsuarioResenha = idUsuarioResenha;
        this.usuarioDto = usuarioDto;
        this.idJuegoResenha = idJuegoResenha;
        this.juegoDto = juegoDto;
        this.recomendacionResenha = recomendacionResenha;
        this.textoResenha = textoResenha;
        this.tiempoJugadoResenha = tiempoJugadoResenha;
        this.fechaPublicacionResenha = fechaPublicacionResenha;
        this.fechaUltiEdicResenha = fechaUltiEdicResenha;
        this.estadoResenha = estadoResenha;
    }

    //Getters

    public Optional<UsuarioDto> getUsuarioDto() {
        return usuarioDto;
    }

    public Optional<JuegoDto> getJuegoDto() {
        return juegoDto;
    }

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
        ResenhaDto that = (ResenhaDto) o;
        return recomendacionResenha == that.recomendacionResenha
                && Double.compare(tiempoJugadoResenha, that.tiempoJugadoResenha) == 0
                && Objects.equals(idResenha, that.idResenha)
                && Objects.equals(idUsuarioResenha, that.idUsuarioResenha)
                && Objects.equals(usuarioDto, that.usuarioDto)
                && Objects.equals(idJuegoResenha, that.idJuegoResenha)
                && Objects.equals(juegoDto, that.juegoDto)
                && Objects.equals(textoResenha, that.textoResenha)
                && Objects.equals(fechaPublicacionResenha, that.fechaPublicacionResenha)
                && Objects.equals(fechaUltiEdicResenha, that.fechaUltiEdicResenha)
                && estadoResenha == that.estadoResenha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idResenha, idUsuarioResenha, usuarioDto, idJuegoResenha, juegoDto, recomendacionResenha,
                textoResenha, tiempoJugadoResenha, fechaPublicacionResenha, fechaUltiEdicResenha, estadoResenha);
    }
}
