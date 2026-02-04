package modelo.form;

import enums.TipoEstadoInstalacion;

import java.time.LocalDateTime;

public class BibliotecaForm {

    //Atributos
    private long idUsuarioBiblio;
    private int idJuegoBiblio;
    private LocalDateTime fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDateTime ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaForm(long idUsuarioBiblio, int idJuegoBiblio, LocalDateTime fechaCompraJuegoBiblio, double tiempoJuegoBiblio, LocalDateTime ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.idJuegoBiblio = idJuegoBiblio;
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }

    //Getters y Setters

    public long getIdUsuarioBiblio() {
        return idUsuarioBiblio;
    }

    public void setIdUsuarioBiblio(int idUsuarioBiblio) {
        this.idUsuarioBiblio = idUsuarioBiblio;
    }

    public int getIdJuegoBiblio() {
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
