package repositorio.inmemory;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.inmemory.UsuarioRepoInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioRepoInMemoryTest {

    @BeforeEach
    public void setUp(){
        // Ya no necesitamos reset() porque cada instancia tiene su propia lista
    }

    @Test
    public void testCrear_UsuarioFormValido_OptionalOfUsuarioEntity() {

        var idValida = 1L;
        var formularioValido = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        Optional<UsuarioEntity> usuarioOptional = Optional.of(new UsuarioEntity(idValida, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA));

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        assertEquals(usuarioOptional, usuarioRepoInMemory.crear(formularioValido));

    }

    @Test
    public void testObtenerTodos_RetornaListaConTodosLosUsuarios(){

        UsuarioEntity usuarioEntity1 = new UsuarioEntity(1L, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);
        UsuarioEntity usuarioEntity2 = new UsuarioEntity(2L, "usuario2",
                "usuario2@mail.com", "passUsuario2", "nombreUsuario2",
                "España", LocalDate.of(1980, 6, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);
        UsuarioEntity usuarioEntity3 = new UsuarioEntity(3L, "usuario3",
                "usuario3@mail.com", "passUsuario3", "nombreUsuario3",
                "España", LocalDate.of(1980, 7, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        List<UsuarioEntity> listaUsuarios = List.of(usuarioEntity1, usuarioEntity2, usuarioEntity3);

        var formularioValido1 = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);
        var formularioValido2 = new UsuarioForm( "usuario2",
                "usuario2@mail.com", "passUsuario2", "nombreUsuario2",
                "España", LocalDate.of(1980, 6, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);
        var formularioValido3 = new UsuarioForm("usuario3",
                "usuario3@mail.com", "passUsuario3", "nombreUsuario3",
                "España", LocalDate.of(1980, 7, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        usuarioRepoInMemory.crear(formularioValido1);
        usuarioRepoInMemory.crear(formularioValido2);
        usuarioRepoInMemory.crear(formularioValido3);

        assertEquals(listaUsuarios, usuarioRepoInMemory.obtenerTodos());

    }

    @Test // Optional Empty
    public void testObtenerPorId_IdValida_OptionalOfUsuarioEntity(){

        var idValida = 1L;
        Optional<UsuarioEntity> usuarioOptional = Optional.of(new UsuarioEntity(idValida, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA));

        UsuarioEntity usuarioEntity1 = new UsuarioEntity(1L, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioForm usuarioForm = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        usuarioRepoInMemory.crear(usuarioForm);

        assertEquals(usuarioOptional, usuarioRepoInMemory.obtenerPorId(idValida));
    }

    @Test // Optional Empty
    public void testObtenerPorNombre_OptionalOfUsuarioEntity(){

        var idValida = 1L;
        Optional<UsuarioEntity> usuarioOptional = Optional.of(new UsuarioEntity(idValida, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA));

        UsuarioEntity usuarioEntity1 = new UsuarioEntity(1L, "usuario1",
                "usuario1@mail.com", "passUsuario1", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioForm usuarioForm = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        usuarioRepoInMemory.crear(usuarioForm);

        assertEquals(usuarioOptional, usuarioRepoInMemory.obtenerPorNombre("usuario1"));
    }

    @Test
    public void testActualizar_OptionalOfUsuarioEntity(){

        var idValida = 1L;
        Optional<UsuarioEntity> usuarioOptional = Optional.of(new UsuarioEntity(idValida, "usuario1",
                "usuario1@mail.com", "passUsuario1new", "nombreUsuario1",
                "España", LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA));

        UsuarioForm usuarioForm1 = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioForm usuarioForm2 = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1new", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        usuarioRepoInMemory.crear(usuarioForm1);

        assertEquals(usuarioOptional, usuarioRepoInMemory.actualizar(idValida,usuarioForm2));

    }

    @Test
    public void testEliminar_ReturnsTrue(){

        var idValida = 1L;
        UsuarioForm usuarioForm1 = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        usuarioRepoInMemory.crear(usuarioForm1);
        boolean resultado = usuarioRepoInMemory.eliminar(idValida);

        assertTrue(resultado);
        assertTrue(usuarioRepoInMemory.obtenerPorId(idValida).isEmpty());

    }

}
