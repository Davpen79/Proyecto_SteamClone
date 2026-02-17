package org.davpen.modelo.form;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoMetodoPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompraForm {

    //Atributos
    private Long idUsuarioCompra;
    private Long idJuegoCompra;
    private LocalDate fechaCompra;
    private TipoMetodoPago tipoPagoCompra;
    private double precioBaseCompra;
    private int descuentoEnCompra;
    private TipoEstadoCompra estadoCompra;

    //Constructor
    public CompraForm(Long idUsuarioCompra, Long idJuegoCompra, LocalDate fechaCompra, TipoMetodoPago tipoPagoCompra, double precioBaseCompra, int descuentoEnCompra, TipoEstadoCompra estadoCompra) {
        this.idUsuarioCompra = idUsuarioCompra;
        this.idJuegoCompra = idJuegoCompra;
        this.fechaCompra = fechaCompra;
        this.tipoPagoCompra = tipoPagoCompra;
        this.precioBaseCompra = precioBaseCompra;
        this.descuentoEnCompra = descuentoEnCompra;
        this.estadoCompra = estadoCompra;
    }

    //Getters

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

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validar Usuario
        if (idUsuarioCompra == null) {
            errores.add(new ErrorDto("Id_usuario", ErrorType.REQUERIDO));
        }

        //Validar Juego
        if (idJuegoCompra == null){
            errores.add(new ErrorDto("Id_juego", ErrorType.REQUERIDO));
        }

        //Validar Metodo de Pago
        if (tipoPagoCompra == null){
            errores.add(new ErrorDto("tipo_pago", ErrorType.REQUERIDO));
        }

        if (tipoPagoCompra != null && !Arrays.stream(TipoMetodoPago.values()).anyMatch(e -> e.equals(tipoPagoCompra))){
            errores.add(new ErrorDto("tipo_pago", ErrorType.NO_ENCONTRADO));
        }

        //Validar Precio
        if (Objects.isNull(precioBaseCompra) || Double.isNaN(precioBaseCompra)){
            errores.add(new ErrorDto("precio", ErrorType.REQUERIDO));
        }

        if (!Objects.isNull(precioBaseCompra) && !Double.isNaN(precioBaseCompra) && precioBaseCompra < 0){
            errores.add(new ErrorDto("precio", ErrorType.VALOR_NEGATIVO));
        }

        //TODO: Validar Descuento


        return errores;
    }


}
