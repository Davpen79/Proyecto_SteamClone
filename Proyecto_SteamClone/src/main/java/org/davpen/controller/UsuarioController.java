package org.davpen.controller;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.inmemory.UsuarioRepoInMemory;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.davpen.transaction.ITransactionManager;

import java.util.ArrayList;
import java.util.Optional;


public class UsuarioController {

    public static final double SALDO_MINIMO = 5.00;
    public static final double SALDO_MAXIMO = 500.00;
    private final IUsuarioRepo usuarioRepo;
    public ITransactionManager transMgr;

    public UsuarioController(IUsuarioRepo usuarioRepo, ITransactionManager transMgr) {
        this.usuarioRepo = usuarioRepo;
        this.transMgr = transMgr;
    }

    /**
     * Registra un nuevo usuario tras validar el formulario y restricciones de usuario.
     * Si hay errores, lanza ValidationException con la lista de errores.
     *
     * @param usuarioForm Formulario con datos de Usuario
     * @return UsuarioDto
     * @throws ValidationException
     */
    public UsuarioDto registrarUsuario(UsuarioForm usuarioForm) throws ValidationException {
        //validar formato
        var errores = usuarioForm.validar();

        var usuario = transMgr.inTransaction(() -> {
            //validar modelo
            //usuario unico
            if (usuarioRepo.obtenerPorNombre(usuarioForm.getNombreCuentaUsuario()).isPresent()) {
                errores.add(new ErrorDto("nombre", ErrorType.DUPLICADO));
            }
            //email unico
            if (usuarioRepo.obtenerTodos().stream()
                    .anyMatch(u -> u.getEmailUsuario().equals(usuarioForm.getEmailUsuario()))) {
                errores.add(new ErrorDto("email", ErrorType.DUPLICADO));
            }
            //comprobar pais existe en lista
            if (!UsuarioRepoInMemory.listaPaises.contains(usuarioForm.getPaisUsuario())) {
                errores.add(new ErrorDto("pais", ErrorType.NO_ENCONTRADO));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            var usuarioOpt = usuarioRepo.crear(usuarioForm);
            return usuarioOpt;
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario.orElse(null));
    }

    /**
     * Obtiene el perfil de usuario por su id. Si no se encuentra el Usuario lanza ValidationException.
     *
     * @param id Identificador de Usuario
     * @return UsuarioDto
     * @throws ValidationException
     */
    public UsuarioDto consultarPerfil(Long id) throws ValidationException {
        //Comprobamos si el usuario existe
        var errores = new ArrayList<ErrorDto>();

        var usuario = transMgr.inTransaction(() -> {

            var usuarioConsultado = usuarioRepo.obtenerPorId(id);
            if (!usuarioConsultado.isPresent()) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var usuarioEncontrado = usuarioConsultado.orElse(null);
            return usuarioEncontrado;

        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario);
    }

    /**
     * Obtiene el perfil de usuario por nombre de cuenta. Si no se encuentra el Usuario lanza ValidationException.
     *
     * @param nombreCuentaUsuario Nombre de la cuenta del Usuario buscado.
     * @return UsuarioDto
     * @throws ValidationException
     */
    public UsuarioDto consultarPerfil(String nombreCuentaUsuario) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var usuario = transMgr.inTransaction(() -> {

            //Comprobamos si el usuario existe
            var usuarioConsultado = usuarioRepo.obtenerPorNombre(nombreCuentaUsuario);
            if (!usuarioConsultado.isPresent()) {
                errores.add(new ErrorDto("nombre", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var usuarioEncontrado = usuarioConsultado.orElse(null);
            return usuarioEncontrado;

        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario);
    }

    /**
     * Obtiene información del usuario incluyendo el saldo de su cuenta. Si no se encuentra el usuario lanza
     * ValidationException.
     *
     * @param id Identificador de Usuario
     * @return UsuarioDto
     * @throws ValidationException
     */
    public UsuarioDto consultarSaldo(Long id) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();

        var usuario = transMgr.inTransaction(() -> {

            var usuarioEntityOpt = usuarioRepo.obtenerPorId(id);
            if (!usuarioEntityOpt.isPresent()) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var usuarioEntity = usuarioEntityOpt.get();
            return usuarioEntity;

        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario);

    }

    /**
     * Añade saldo a la cuenta del usuario aplicando validaciones. Si hay errores, lanza ValidationException con la
     * lista de errores.
     *
     * @param id               Identificador de Usuario
     * @param cantidadAnhadida Saldo (double) a añadir a la cuenta
     * @return UsuarioDto con saldo actualizado
     * @throws ValidationException
     */
    public UsuarioDto anhadirSaldo(Long id, Double cantidadAnhadida) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();

        var usuario = transMgr.inTransaction(() -> {

            Optional<UsuarioEntity> usuarioEntityOpt = usuarioRepo.obtenerPorId(id);
            if (!usuarioEntityOpt.isPresent()) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            if (cantidadAnhadida.isNaN()) {
                errores.add(new ErrorDto("cantidad", ErrorType.FORMATO_INVALIDO));
            }
            if (cantidadAnhadida < 0) {
                errores.add(new ErrorDto("cantidad", ErrorType.VALOR_NEGATIVO));
            }
            if (cantidadAnhadida < SALDO_MINIMO) {
                errores.add(new ErrorDto("cantidad", ErrorType.VALOR_DEMASIADO_BAJO));
            }
            if (cantidadAnhadida > SALDO_MAXIMO) {
                errores.add(new ErrorDto("cantidad", ErrorType.VALOR_DEMASIADO_ALTO));
            }
            if (!cantidadAnhadida.toString().matches("\\d+(\\.\\d{0,2})?")){
                errores.add(new ErrorDto("cantidad", ErrorType.DEMASIADOS_DECIMALES));
            }
            if (usuarioEntityOpt.get().getEstadoCuentaUsuario() != TipoEstadoCuenta.ACTIVA) {
                errores.add(new ErrorDto("id", ErrorType.CUENTA_INACTIVA));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var nuevoSaldo = usuarioEntityOpt.get().getSaldoUsuario() + cantidadAnhadida;
            var usuarioActual = usuarioEntityOpt.get();

            var usuarioActualizadoForm = new UsuarioForm(usuarioActual.getNombreCuentaUsuario(),
                    usuarioActual.getEmailUsuario(),
                    usuarioActual.getPasswordUsuario(), usuarioActual.getNombreRealUsuario(),
                    usuarioActual.getPaisUsuario(), usuarioActual.getFechaNacUsuario(), usuarioActual.getFechaRegUsuario(),
                    usuarioActual.getAvatarUsuario(), nuevoSaldo, usuarioActual.getEstadoCuentaUsuario());

            var usuarioActualizado = usuarioRepo.actualizar(id, usuarioActualizadoForm);
            return usuarioActualizado;

        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario.orElse(null));

    }

}
