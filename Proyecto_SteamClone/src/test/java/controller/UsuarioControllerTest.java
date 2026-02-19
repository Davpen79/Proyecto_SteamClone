package controller;

import org.davpen.controller.UsuarioController;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioControllerTest {

    private IUsuarioRepo usuarioRepo;
    private UsuarioDto usuarioDto;
    UsuarioController usuarioController = new UsuarioController(usuarioRepo);

    @Test
    public void registrarUsuariotest() throws ValidationException {

        final var usuario = new UsuarioForm("Carlos", "carlos@mail.com", "cArlos123",
                "carlos", "es", LocalDate.of(1950, 1, 1), null,
                "avatar", 0.5, TipoEstadoCuenta.ACTIVA);

    }
}
