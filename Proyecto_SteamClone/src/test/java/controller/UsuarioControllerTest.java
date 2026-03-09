package controller;

import org.davpen.controller.UsuarioController;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioControllerTest {

    private IUsuarioRepo usuarioRepo;
    private UsuarioDto usuarioDto;
    UsuarioController usuarioController = new UsuarioController(usuarioRepo);

    @Test
    public void testRegistrarUsuario_UsuarioCorrecto_UsuarioDto() throws ValidationException {

        final var usuarioCorrecto = new UsuarioForm("Carlos", "carlos@mail.com", "cArlos123",
                "carlos", "España", LocalDate.of(1950, 1, 1), LocalDate.now(),
                "avatar", 0.5, TipoEstadoCuenta.ACTIVA);

        final var usuarioRepo = new UsuarioEntity(1L, "Carlos", "carlos@mail.com",
                "password1", "cArlos123","España", LocalDate.of(1950, 1, 1),
                LocalDate.now(),"avatar", 0.5, TipoEstadoCuenta.ACTIVA);

        final var usuarioDto = new UsuarioDto(1L, "Carlos", "carlos@mail.com",
                "cArlos123","España", LocalDate.of(1950, 1, 1), LocalDate.now(),
                "avatar", 0.5, TipoEstadoCuenta.ACTIVA);

        assertEquals(usuarioDto, usuarioController.registrarUsuario(usuarioCorrecto));
    }
}
