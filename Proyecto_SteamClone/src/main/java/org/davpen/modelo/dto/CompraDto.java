package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoMetodoPago;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class CompraDto {

    //Atributos
    private Long idCompra;
    private Long idUsuarioCompra;
    private Optional<UsuarioDto> usuarioDto;
    private Long idJuegoCompra;
    private Optional<JuegoDto> juegoDto;
    private LocalDate fechaCompra;
    private TipoMetodoPago tipoPagoCompra;
    private double precioBaseCompra;
    private int descuentoEnCompra;
    private TipoEstadoCompra estadoCompra;

    //Constructor

    public CompraDto(Long idCompra, Long idUsuarioCompra, Optional<UsuarioDto> usuarioDto, Long idJuegoCompra,
                     Optional<JuegoDto> juegoDto, LocalDate fechaCompra, TipoMetodoPago tipoPagoCompra,
                     double precioBaseCompra, int descuentoEnCompra, TipoEstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.idUsuarioCompra = idUsuarioCompra;
        this.usuarioDto = usuarioDto;
        this.idJuegoCompra = idJuegoCompra;
        this.juegoDto = juegoDto;
        this.fechaCompra = fechaCompra;
        this.tipoPagoCompra = tipoPagoCompra;
        this.precioBaseCompra = precioBaseCompra;
        this.descuentoEnCompra = descuentoEnCompra;
        this.estadoCompra = estadoCompra;
    }


    //Getters

    public Optional<UsuarioDto> getUsuarioDto() {
        return usuarioDto;
    }

    public Optional<JuegoDto> getJuegoDto() {
        return juegoDto;
    }

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
        CompraDto compraDto = (CompraDto) o;
        return Double.compare(precioBaseCompra, compraDto.precioBaseCompra) == 0
                && descuentoEnCompra == compraDto.descuentoEnCompra
                && Objects.equals(idCompra, compraDto.idCompra)
                && Objects.equals(idUsuarioCompra, compraDto.idUsuarioCompra)
                && Objects.equals(usuarioDto, compraDto.usuarioDto)
                && Objects.equals(idJuegoCompra, compraDto.idJuegoCompra)
                && Objects.equals(juegoDto, compraDto.juegoDto)
                && Objects.equals(fechaCompra, compraDto.fechaCompra)
                && tipoPagoCompra == compraDto.tipoPagoCompra
                && estadoCompra == compraDto.estadoCompra;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idUsuarioCompra, usuarioDto, idJuegoCompra, juegoDto, fechaCompra,
                tipoPagoCompra, precioBaseCompra, descuentoEnCompra, estadoCompra);
    }

    @Override
    public String toString() {
        return "CompraDto{" +
                "idCompra=" + idCompra +
                ", idUsuarioCompra=" + idUsuarioCompra +
                ", usuarioDto=" + usuarioDto +
                ", idJuegoCompra=" + idJuegoCompra +
                ", juegoDto=" + juegoDto +
                ", fechaCompra=" + fechaCompra +
                ", tipoPagoCompra=" + tipoPagoCompra +
                ", precioBaseCompra=" + precioBaseCompra +
                ", descuentoEnCompra=" + descuentoEnCompra +
                ", estadoCompra=" + estadoCompra +
                '}';
    }
}
