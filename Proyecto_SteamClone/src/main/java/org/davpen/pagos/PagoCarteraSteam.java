package org.davpen.pagos;

public class PagoCarteraSteam implements IMetodoPago{

    private Long idUsuario;

    public PagoCarteraSteam(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean procesarPago(double coste) {
        return false;
    }
}
