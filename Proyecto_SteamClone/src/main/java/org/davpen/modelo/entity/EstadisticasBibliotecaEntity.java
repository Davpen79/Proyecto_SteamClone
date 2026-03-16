package org.davpen.modelo.entity;

import java.util.List;

public class EstadisticasBibliotecaEntity {

    //Atributos
    private int totalJuegos;
    private double horasJugadas;
    private List<JuegoEntity> juegosInstalados;
    private JuegoEntity juegoMasJugado;
    private double valorTotalBiblioteca;
    private List<JuegoEntity> juegosNuncaJugados;

    //Constructor
    public EstadisticasBibliotecaEntity(int totalJuegos, double horasJugadas, List<JuegoEntity> juegosInstalados,
                                        JuegoEntity juegoMasJugado, double valorTotalBiblioteca,
                                        List<JuegoEntity> juegosNuncaJugados) {
        this.totalJuegos = totalJuegos;
        this.horasJugadas = horasJugadas;
        this.juegosInstalados = juegosInstalados;
        this.juegoMasJugado = juegoMasJugado;
        this.valorTotalBiblioteca = valorTotalBiblioteca;
        this.juegosNuncaJugados = juegosNuncaJugados;
    }
    //Getters

    public int getTotalJuegos() {
        return totalJuegos;
    }

    public double getHorasJugadas() {
        return horasJugadas;
    }

    public List<JuegoEntity> getJuegosInstalados() {
        return juegosInstalados;
    }

    public JuegoEntity getJuegoMasJugado() {
        return juegoMasJugado;
    }

    public double getValorTotalBiblioteca() {
        return valorTotalBiblioteca;
    }

    public List<JuegoEntity> getJuegosNuncaJugados() {
        return juegosNuncaJugados;
    }


}
