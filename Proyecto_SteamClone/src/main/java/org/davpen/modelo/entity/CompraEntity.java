package org.davpen.modelo.entity;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoMetodoPago;

import java.time.LocalDate;
import java.util.Objects;

public class CompraEntity {

    //Atributos
    private Long idCompra;
    private Long idUsuarioCompra;
    private Long idJuegoCompra;
    private LocalDate fechaCompra;
    private TipoMetodoPago tipoPagoCompra;
    private double precioBaseCompra;
    private int descuentoEnCompra;
    private TipoEstadoCompra estadoCompra;

    //Constructor
    public CompraEntity(Long idCompra, Long idUsuarioCompra, Long idJuegoCompra, LocalDate fechaCompra,
                        TipoMetodoPago tipoPagoCompra, double precioBaseCompra, int descuentoEnCompra,
                        TipoEstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.idUsuarioCompra = idUsuarioCompra;
        this.idJuegoCompra = idJuegoCompra;
        this.fechaCompra = fechaCompra;
        this.tipoPagoCompra = tipoPagoCompra;
        this.precioBaseCompra = precioBaseCompra;
        this.descuentoEnCompra = descuentoEnCompra;
        this.estadoCompra = estadoCompra;
    }

    //Getters

    public Long getIdCompra() {
        return idCompra;
    }

    public Long getIdUsuarioCompra() {
        return idUsuarioCompra;
    }

    public Long getIdJuegoCompra() {
        return idJuegoCompra;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public TipoMetodoPago getTipoPagoCompra() {
        return tipoPagoCompra;
    }

    public double getPrecioBaseCompra() {
        return precioBaseCompra;
    }

    public int getDescuentoEnCompra() {
        return descuentoEnCompra;
    }

    public TipoEstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraEntity that = (CompraEntity) o;
        return Double.compare(precioBaseCompra, that.precioBaseCompra) == 0
                && descuentoEnCompra == that.descuentoEnCompra
                && Objects.equals(idCompra, that.idCompra)
                && Objects.equals(idUsuarioCompra, that.idUsuarioCompra)
                && Objects.equals(idJuegoCompra, that.idJuegoCompra)
                && Objects.equals(fechaCompra, that.fechaCompra)
                && tipoPagoCompra == that.tipoPagoCompra
                && estadoCompra == that.estadoCompra;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idUsuarioCompra, idJuegoCompra, fechaCompra, tipoPagoCompra, precioBaseCompra,
                descuentoEnCompra, estadoCompra);
    }

    @Override
    public String toString() {
        return "CompraEntity{" +
                "idCompra=" + idCompra +
                ", idUsuarioCompra=" + idUsuarioCompra +
                ", idJuegoCompra=" + idJuegoCompra +
                ", fechaCompra=" + fechaCompra +
                ", tipoPagoCompra=" + tipoPagoCompra +
                ", precioBaseCompra=" + precioBaseCompra +
                ", descuentoEnCompra=" + descuentoEnCompra +
                ", estadoCompra=" + estadoCompra +
                '}';
    }
}
