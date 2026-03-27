package org.davpen.modelo.dto;

import org.davpen.modelo.entity.JuegoEntity;

import java.util.List;
import java.util.Optional;

public class EstadisticasBibliotecaDto {
    //Atributos
    private Long idUsuario;
    private int totalJuegos;
    private double horasTotales;
    private List<JuegoEntity> juegosInstalados;
    private Optional<JuegoEntity> juegoMasJugado;
    private double valorTotalBiblioteca;
    private List<JuegoEntity> juegosNoJugados;

    //Constructor
    public EstadisticasBibliotecaDto(Long idUsuario, int totalJuegos, double horasTotales,
                                     List<JuegoEntity> juegosInstalados,
                                     Optional<JuegoEntity> juegoMasJugado, double valorTotalBiblioteca,
                                     List<JuegoEntity> juegosNoJugados) {
        this.idUsuario = idUsuario;
        this.totalJuegos = totalJuegos;
        this.horasTotales = horasTotales;
        this.juegosInstalados = juegosInstalados;
        this.juegoMasJugado = juegoMasJugado;
        this.valorTotalBiblioteca = valorTotalBiblioteca;
        this.juegosNoJugados = juegosNoJugados;
    }

    //Getters
    public int getTotalJuegos() {
        return totalJuegos;
    }

    public double getHorasTotales() {
        return horasTotales;
    }

    public List<JuegoEntity> getJuegosInstalados() {
        return juegosInstalados;
    }

    public Optional<JuegoEntity> getJuegoMasJugado() {
        return juegoMasJugado;
    }

    public double getValorTotalBiblioteca() {
        return valorTotalBiblioteca;
    }

    public List<JuegoEntity> getJuegosNoJugados() {
        return juegosNoJugados;
    }
}
