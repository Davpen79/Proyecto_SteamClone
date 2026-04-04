package org.davpen.modelo.dto;

import java.util.List;
import java.util.Optional;

public class EstadisticasBibliotecaDto {
    //Atributos
    private Long idUsuario;
    private int totalJuegos;
    private double horasTotales;
    private List<JuegoDto> juegosInstalados;
    private Optional<JuegoDto> juegoMasJugado;
    private double valorTotalBiblioteca;
    private List<JuegoDto> juegosNoJugados;

    //Constructor
    public EstadisticasBibliotecaDto(Long idUsuario, int totalJuegos, double horasTotales,
                                     List<JuegoDto> juegosInstalados,
                                     Optional<JuegoDto> juegoMasJugado, double valorTotalBiblioteca,
                                     List<JuegoDto> juegosNoJugados) {
        this.idUsuario = idUsuario;
        this.totalJuegos = totalJuegos;
        this.horasTotales = horasTotales;
        this.juegosInstalados = juegosInstalados;
        this.juegoMasJugado = juegoMasJugado;
        this.valorTotalBiblioteca = valorTotalBiblioteca;
        this.juegosNoJugados = juegosNoJugados;
    }

    //Getters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public int getTotalJuegos() {
        return totalJuegos;
    }

    public double getHorasTotales() {
        return horasTotales;
    }

    public List<JuegoDto> getJuegosInstalados() {
        return juegosInstalados;
    }

    public Optional<JuegoDto> getJuegoMasJugado() {
        return juegoMasJugado;
    }

    public double getValorTotalBiblioteca() {
        return valorTotalBiblioteca;
    }

    public List<JuegoDto> getJuegosNoJugados() {
        return juegosNoJugados;
    }
}
