package org.davpen.pagos;

public class PagoTarjetaCredito implements IMetodoPago{

    private String tarjetaCredito;

    public PagoTarjetaCredito(String tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    @Override
    public boolean procesarPago(double coste) {
        return false;
    }
}
