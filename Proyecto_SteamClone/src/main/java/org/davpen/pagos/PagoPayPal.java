package org.davpen.pagos;

public class PagoPayPal implements IMetodoPago{

    private String cuentaPayPal;

    public PagoPayPal(String cuentaPayPal) {
        this.cuentaPayPal = cuentaPayPal;
    }

    @Override
    public boolean procesarPago(double coste) {
        return false;
    }
}
