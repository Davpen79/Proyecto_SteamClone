package modelo.form;

import enums.TipoEstadoCompra;
import enums.TipoMetodoPago;

import java.time.LocalDateTime;

public class CompraForm {

    //Atributos
    private long idUsuarioCompra;
    private int idJuegoCompra;
    private LocalDateTime fechaCompra;
    private TipoMetodoPago tipoPagoCompra;
    private double precioBaseCompra;
    private int descuentoEnCompra;
    private TipoEstadoCompra estadoCompra;

    //Constructor
    public CompraForm(long idUsuarioCompra, int idJuegoCompra, LocalDateTime fechaCompra, TipoMetodoPago tipoPagoCompra, double precioBaseCompra, int descuentoEnCompra, TipoEstadoCompra estadoCompra) {
        this.idUsuarioCompra = idUsuarioCompra;
        this.idJuegoCompra = idJuegoCompra;
        this.fechaCompra = fechaCompra;
        this.tipoPagoCompra = tipoPagoCompra;
        this.precioBaseCompra = precioBaseCompra;
        this.descuentoEnCompra = descuentoEnCompra;
        this.estadoCompra = estadoCompra;
    }

    //Getters y Setters

    public long getIdUsuarioCompra() {
        return idUsuarioCompra;
    }

    public void setIdUsuarioCompra(int idUsuarioCompra) {
        this.idUsuarioCompra = idUsuarioCompra;
    }

    public int getIdJuegoCompra() {
        return idJuegoCompra;
    }

    public void setIdJuegoCompra(int idJuegoCompra) {
        this.idJuegoCompra = idJuegoCompra;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public TipoMetodoPago getTipoPagoCompra() {
        return tipoPagoCompra;
    }

    public void setTipoPagoCompra(TipoMetodoPago tipoPagoCompra) {
        this.tipoPagoCompra = tipoPagoCompra;
    }

    public double getPrecioBaseCompra() {
        return precioBaseCompra;
    }

    public void setPrecioBaseCompra(double precioBaseCompra) {
        this.precioBaseCompra = precioBaseCompra;
    }

    public int getDescuentoEnCompra() {
        return descuentoEnCompra;
    }

    public void setDescuentoEnCompra(int descuentoEnCompra) {
        this.descuentoEnCompra = descuentoEnCompra;
    }

    public TipoEstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(TipoEstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }
}
