package org.davpen.pagos;

import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;

public class PagoPayPal implements IPlataformaPago {

    private String cuentaPayPal;

    public PagoPayPal() {
        this.cuentaPayPal = cuentaPayPal;
    }

    @Override
    public boolean procesarPago(CompraEntity compra, UsuarioEntity usuario, Double precioFinal) throws ValidationException {
        System.out.println("Pago realizado con PayPal");
        return true;
    }
}
