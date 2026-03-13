package controller;

import org.davpen.controller.UsuarioController;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private IUsuarioRepo usuarioRepo;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    public void testRegistrarUsuario_UsuarioFormCorrecto_UsuarioDto_LlamaUsuarioRepoCrear() throws ValidationException {

        //Arrange - Creamos un formulario valido para hacer la prueba
        var formularioValido = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.of(2026, 3, 3),
                "avatarUsuario1", 10.00d, TipoEstadoCuenta.ACTIVA);
        var usuarioDTO = new UsuarioDto(1L, "usuario1", "usuario1@mail.com",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA);
        when(usuarioRepo.crear(formularioValido)).thenReturn(Optional.of(new UsuarioEntity(1L,
                "usuario1", "usuario1@mail.com", "passUsuario1",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA)));

        //Act - el controlador crea un usuario nuevo
        var resultadoRegistro = usuarioController.registrarUsuario(formularioValido);
        //Assert
        assertEquals(usuarioDTO, resultadoRegistro);
        verify(usuarioRepo).crear(formularioValido);

    }

    @Test
    public void testRegistrarUsuario_UsuarioFormInvalido_ThrowsValidationException() throws ValidationException {

        //Arrange - Creamos un formulario invalido para hacer la prueba
        var formularioInValido = new UsuarioForm(null, "usuario1@mail.com",
                "passUsuario1", "nombreUsuario1", "España",
                LocalDate.of(1980, 5, 5), LocalDate.of(2026, 3, 3),
                "avatarUsuario1", 10.00d, TipoEstadoCuenta.ACTIVA);
        var usuarioDTO = new UsuarioDto(1L, "usuario1", "usuario1@mail.com",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA);

        //Act - Assert

        assertThrows(ValidationException.class, () -> usuarioController.registrarUsuario(formularioInValido));
    }

    @Test
    public void testConsultarSaldo_IdUsuarioValido_UsuarioDTO_LlamaUsuarioRepoObtenerPorId() throws ValidationException {
        //Arrange
        var idValido = 1L;
        var usuarioDTO = new UsuarioDto(1L, "usuario1", "usuario1@mail.com",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA);
        when(usuarioRepo.obtenerPorId(idValido)).thenReturn(Optional.of(new UsuarioEntity(1L,
                "usuario1", "usuario1@mail.com", "passUsuario1",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA)));

        //Act
        var resultadoSaldo = usuarioController.consultarSaldo(idValido);

        //Assert
        assertEquals(usuarioDTO, resultadoSaldo);
        verify(usuarioRepo, atLeast(1)).obtenerPorId(idValido);

    }

    //No se porque da Null
    @Test
    public void testAnhadirSaldo_IdValido_CantidadValida_RetornaUsuarioDTOConSaldoActualizado() throws ValidationException {
        //Arrange
        var idValido = 1L;
        Double cantidadValida = 5.00d;
        var usuarioDTO = new UsuarioDto(1L, "usuario1", "usuario1@mail.com",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                15.00d, TipoEstadoCuenta.ACTIVA);
        when(usuarioRepo.obtenerPorId(idValido)).thenReturn(Optional.of(new UsuarioEntity(1L,
                "usuario1", "usuario1@mail.com", "passUsuario1",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1",
                10.00d, TipoEstadoCuenta.ACTIVA)));
        //Act
        var usuarioActualizado = usuarioController.anhadirSaldo(idValido, cantidadValida);

        //Assert
        assertEquals(usuarioDTO,usuarioActualizado);
        verify(usuarioRepo, atLeast(1)).obtenerPorId(idValido);
    }

}
