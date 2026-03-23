package org.davpen.utiles;

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
