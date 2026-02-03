package modelo.form;

import enums.TipoEstadoInstalacion;

import java.time.LocalDate;

public class BibliotecaFormDTO {

    //Atributos
    private int idUsuarioBiblio;
    private int idJuegoBiblio;
    private LocalDate fechaCompraJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDate ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaFormDTO(int idUsuarioBiblio, int idJuegoBiblio, LocalDate fechaCompraJuegoBiblio, double tiempoJuegoBiblio, LocalDate ultiFechaJuegoBiblio, TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.idJuegoBiblio = idJuegoBiblio;
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }

    //Getters y Setters

    public int getIdUsuarioBiblio() {
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

    public LocalDate getFechaCompraJuegoBiblio() {
        return fechaCompraJuegoBiblio;
    }

    public void setFechaCompraJuegoBiblio(LocalDate fechaCompraJuegoBiblio) {
        this.fechaCompraJuegoBiblio = fechaCompraJuegoBiblio;
    }

    public double getTiempoJuegoBiblio() {
        return tiempoJuegoBiblio;
    }

    public void setTiempoJuegoBiblio(double tiempoJuegoBiblio) {
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
    }

    public LocalDate getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public void setUltiFechaJuegoBiblio(LocalDate ultiFechaJuegoBiblio) {
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }

    public void setEstadoInstJuegoBiblio(TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }
}
