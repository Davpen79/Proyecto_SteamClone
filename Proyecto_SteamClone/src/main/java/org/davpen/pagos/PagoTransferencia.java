package org.davpen.pagos;

import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;

public class PagoTransferencia implements IPlataformaPago {

    private String cuentaBancaria;

    public PagoTransferencia() {
        this.cuentaBancaria = cuentaBancaria;
    }

    @Override
    public boolean procesarPago(CompraEntity compra, UsuarioEntity usuario, Double precioFinal) throws ValidationException {
        System.out.println("Pago realizado por Transferencia");
        return true;
    }
}
