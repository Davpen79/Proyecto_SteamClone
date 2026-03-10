package org.davpen.repositorio.inmemory;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UsuarioRepoInMemory implements IUsuarioRepo {

    private static final List<UsuarioEntity> LISTA_USUARIOS = new ArrayList<>();
    private static Long idCounter = 1L;
    public static List<String> listaPaises = List.of("España", "Francia", "Portugal");

    @Override
    public Optional<UsuarioEntity> crear(UsuarioForm form) {

        var usuario = new UsuarioEntity(idCounter++, form.getNombreCuentaUsuario(), form.getEmailUsuario(), form.getPasswordUsuario(),
                form.getNombreRealUsuario(), form.getPaisUsuario(), form.getFechaNacUsuario(), LocalDate.now(), form.getAvatarUsuario(),
                0.00, TipoEstadoCuenta.ACTIVA);
        LISTA_USUARIOS.add(usuario);
        return Optional.of(usuario);
    }

    @Override
    public Optional<UsuarioEntity> obtenerPorId(Long id) {

        return LISTA_USUARIOS.stream()
                .filter(u -> u.getIdUsuario()
                .equals(id)).findFirst();
    }

    @Override
    public List<UsuarioEntity> obtenerTodos() {

        return new ArrayList<>(LISTA_USUARIOS);
    }

    @Override
    public Optional<UsuarioEntity> actualizar(Long id, UsuarioForm form) {
        var usuarioInicial = obtenerPorId(id);
        if (usuarioInicial.isEmpty()){
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        var usuarioActualizado = new UsuarioEntity(id, form.getNombreCuentaUsuario(), form.getEmailUsuario(), form.getPasswordUsuario(),
                form.getNombreRealUsuario(), form.getPaisUsuario(), form.getFechaNacUsuario(),form.getFechaRegUsuario(), form.getAvatarUsuario(),
                form.getSaldoUsuario(), form.getEstadoCuentaUsuario());

        LISTA_USUARIOS.removeIf(u -> u.getIdUsuario().equals(id));
        LISTA_USUARIOS.add(usuarioActualizado);

        return Optional.of(usuarioActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return LISTA_USUARIOS.removeIf(u -> u.getIdUsuario().equals(id));
    }

    @Override
    public Optional<UsuarioEntity> obtenerPorNombre(String nombreCuentaUsuario) {
        return LISTA_USUARIOS.stream()
                .filter(u -> u.getNombreCuentaUsuario().equals(nombreCuentaUsuario))
                .findFirst();
    }
}
