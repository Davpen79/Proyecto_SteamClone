package repositorio.inmemory;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.inmemory.UsuarioRepoInMemory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioRepoInMemoryTest {


    @Test
    public void testCrear_UsuarioFormValido_OptionalOfUsuarioEntity(){

        var idValida = 1L;
        var formularioValido = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5),LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        Optional<UsuarioEntity> usuarioOptional = new UsuarioEntity(idValida,"usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5),LocalDate.now(),
                "avatarUsuario1", 0.00d, TipoEstadoCuenta.ACTIVA);

        UsuarioRepoInMemory usuarioRepoInMemory = new UsuarioRepoInMemory();
        assertEquals(usuarioOptional, usuarioRepoInMemory.crear(formularioValido));

    }

}
