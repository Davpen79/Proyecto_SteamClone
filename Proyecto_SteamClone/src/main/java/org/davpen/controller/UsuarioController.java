package org.davpen.controller;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.util.ArrayList;

import static org.davpen.repositorio.inmemory.UsuarioRepoInMemory.listaPaises;

public class UsuarioController {

    private final IUsuarioRepo usuarioRepo;

    public UsuarioController(IUsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    //Registrar nuevo usuario
    public UsuarioDto registrarUsuario(UsuarioForm usuarioForm) throws ValidationException {
        //validar formato
        var errores = usuarioForm.validar();
        //validar modelo
        //usuario unico
        usuarioRepo.obtenerPorNombre(usuarioForm.getNombreCuentaUsuario())
                .ifPresent(u -> errores.add(new ErrorDto("nombre", ErrorType.DUPLICADO)));
        //email unico
        if (usuarioRepo.obtenerTodos().stream()
                .anyMatch(u -> u.getEmailUsuario().equals(usuarioForm.getEmailUsuario()))) {
            errores.add(new ErrorDto("email", ErrorType.DUPLICADO));
        }
        //comprobar pais existe en lista
        if (!listaPaises.contains(usuarioForm.getPaisUsuario())){
            errores.add(new ErrorDto("pais", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var usuarioOpt = usuarioRepo.crear(usuarioForm);
        var usuario = usuarioOpt.orElse(null);

        return Mapper.mapaUsuarioCompleto(usuario);
    }


    //Consultar perfil?? - Estadisticas??


    //Consultar saldo

    private double consultarSaldo(Long id) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!usuarioRepo.obtenerPorId(id).isPresent()){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return usuarioRepo.obtenerPorId(id).get().getSaldoUsuario();

    }

    //Añadir saldo

    private double anhadirSaldo(Long id, Double cantidadAnhadida) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!usuarioRepo.obtenerPorId(id).isPresent()){
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (cantidadAnhadida.isNaN()){
            errores.add(new ErrorDto("cantidad", ErrorType.FORMATO_INVALIDO));
        }
        if (cantidadAnhadida < 0){
            errores.add(new ErrorDto("cantidad", ErrorType.VALOR_NEGATIVO));
        }
        if (cantidadAnhadida < 5.00){
            errores.add(new ErrorDto("cantidad", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (cantidadAnhadida > 500.00){
            errores.add(new ErrorDto("cantidad", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if (usuarioRepo.obtenerPorId(id).get().getEstadoCuentaUsuario() != TipoEstadoCuenta.ACTIVA){
            errores.add(new ErrorDto("id", ErrorType.CUENTA_INACTIVA));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var nuevoSaldo = usuarioRepo.obtenerPorId(id).get().getSaldoUsuario() + cantidadAnhadida;
        var usuarioActual = usuarioRepo.obtenerPorId(id);

        var usuarioActualizadoForm = new UsuarioForm(usuarioActual.get().getNombreCuentaUsuario(),usuarioActual.get().getEmailUsuario(),
                                    usuarioActual.get().getPasswordUsuario(), usuarioActual.get().getNombreRealUsuario(),
                                    usuarioActual.get().getPaisUsuario(), usuarioActual.get().getFechaNacUsuario(), usuarioActual.get().getFechaRegUsuario(),
                                    usuarioActual.get().getAvatarUsuario(), nuevoSaldo, usuarioActual.get().getEstadoCuentaUsuario());

        usuarioRepo.actualizar(id, usuarioActualizadoForm);
        return nuevoSaldo;

    }

}
