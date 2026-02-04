package modelo.dto;

import enums.TipoEstadoInstalacion;


import java.time.LocalDateTime;

public class BibliotecaDto {

    //Atributos
    private long idBiblio;
    private long idUsuarioBiblio;
    private long idJuegoBiblio;
    private LocalDateTime fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDateTime ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaDto(long idBiblio, long idUsuarioBiblio, long idJuegoBiblio, LocalDateTime fechaCompraJuegoBiblio, double tiempoJuegoBiblio, LocalDateTime ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idBiblio = idBiblio;
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.idJuegoBiblio = idJuegoBiblio;
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }

    //Getters y Setters

    public long getIdBiblio() {
        return idBiblio;
    }

    public void setIdBiblio(int idBiblio) {
        this.idBiblio = idBiblio;
    }

    public long getIdUsuarioBiblio() {
        return idUsuarioBiblio;
    }

    public void setIdUsuarioBiblio(int idUsuarioBiblio) {
        this.idUsuarioBiblio = idUsuarioBiblio;
    }

    public long getIdJuegoBiblio() {
        return idJuegoBiblio;
    }

    public void setIdJuegoBiblio(int idJuegoBiblio) {
        this.idJuegoBiblio = idJuegoBiblio;
    }

    public LocalDateTime getFechaCompraJuegoBiblio() {
        return fechaCompraJuegoBiblio;
    }

    public void setFechaCompraJuegoBiblio(LocalDateTime fechaCompraJuegoBiblio) {
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
    }

    public double getTiempoJuegoBiblio() {
        return tiempoJuegoBiblio;
    }

    public void setTiempoJuegoBiblio(double tiempoJuegoBiblio) {
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
    }

    public LocalDateTime getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public void setUltiFechaJuegoBiblio(LocalDateTime ultiFechaJuegoBiblio) {
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }

    public void setEstadoInstJuegoBiblio(TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }
}
