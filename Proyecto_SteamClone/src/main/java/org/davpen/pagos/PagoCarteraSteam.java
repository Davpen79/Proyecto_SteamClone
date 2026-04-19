package org.davpen.pagos;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.util.ArrayList;

public class PagoCarteraSteam implements IPlataformaPago {

    private Long idUsuario;
    private IUsuarioRepo usuarioRepo;
    private ICompraRepo compraRepo;

    public PagoCarteraSteam(Long idUsuario, IUsuarioRepo usuarioRepo, ICompraRepo compraRepo) {
        this.idUsuario = idUsuario;
        this.usuarioRepo = usuarioRepo;
        this.compraRepo = compraRepo;
    }

    @Override
    public boolean procesarPago(CompraEntity compra, UsuarioEntity usuario, Double precioFinal) throws ValidationException {

        try {
            var saldoCartera = usuario.getSaldoUsuario();
            var errores = new ArrayList<ErrorDto>();
            if (saldoCartera < precioFinal) {
                errores.add(new ErrorDto("saldo_cartera", ErrorType.SALDO_INSUFICIENTE));
                throw new ValidationException(errores);
            }
            var nuevoSaldo = saldoCartera - precioFinal;
            var usuarioActualizadoForm = new UsuarioForm(usuario.getNombreCuentaUsuario(),
                    usuario.getEmailUsuario(), usuario.getPasswordUsuario(),
                    usuario.getNombreRealUsuario(), usuario.getPaisUsuario(),
                    usuario.getFechaNacUsuario(), usuario.getFechaRegUsuario(),
                    usuario.getAvatarUsuario(), nuevoSaldo, usuario.getEstadoCuentaUsuario());
            usuarioRepo.actualizar(idUsuario, usuarioActualizadoForm);

            return true;

        } catch (ValidationException e) {
            return false;
        }
    }
}
