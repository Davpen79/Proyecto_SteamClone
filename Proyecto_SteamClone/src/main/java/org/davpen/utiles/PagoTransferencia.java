package org.davpen.utiles;

public class PagoTransferencia implements IMetodoPago{

    private String cuentaBancaria;

    public PagoTransferencia(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    @Override
    public boolean procesarPago(double coste) {
        return false;
    }
}
