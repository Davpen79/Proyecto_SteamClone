package org.davpen.pagos;

import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;

public class PagoTarjetaCredito implements IPlataformaPago {

    private String tarjetaCredito;

    public PagoTarjetaCredito() {
        this.tarjetaCredito = tarjetaCredito;
    }

     @Override
    public boolean procesarPago(CompraEntity compra, UsuarioEntity usuario, Double precioFinal) throws ValidationException {
         System.out.println("Pago realizado con Tarjeta");
        return true;
    }
}
