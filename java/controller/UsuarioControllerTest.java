package controller;

import org.davpen.controller.UsuarioController;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private IUsuarioRepo usuarioRepo;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioEntity usuarioValido;
    private UsuarioForm usuarioFormValido;

    @BeforeEach
    public void setup() {
        usuarioValido = new UsuarioEntity(1L, "usuario1", "usuario1@mail.com", "passUsuario1",
                "Usuario Uno", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2020, 1, 1), "avatarUsuario1", 10.0, TipoEstadoCuenta.ACTIVA);

        usuarioFormValido = new UsuarioForm("usuario1", "usuario1@mail.com",
                "passUsuario1", "Usuario Uno", "España",
                LocalDate.of(1980, 5, 5), LocalDate.of(2020, 1, 1),
                "avatarUsuario1", 10.0, TipoEstadoCuenta.ACTIVA);
    }

    @Test
    public void testRegistrarUsuario_DatosValidos_RetornaUsuarioDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorNombre("usuario1")).thenReturn(Optional.empty());
        when(usuarioRepo.obtenerTodos()).thenReturn(new ArrayList<>());
        when(usuarioRepo.crear(any(UsuarioForm.class))).thenReturn(Optional.of(usuarioValido));

        // Act
        UsuarioDto resultado = usuarioController.registrarUsuario(usuarioFormValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario1", resultado.getNombreCuentaUsuario());
        verify(usuarioRepo).crear(any(UsuarioForm.class));
    }

    @Test
    public void testRegistrarUsuario_UsuarioSinNombre_ThrowsValidationException() throws ValidationException {
        // Arrange
        var usuarioFormSinNombre = new UsuarioForm(null, "usuario1@mail.com",
                "passUsuario1", "Usuario Uno", "España",
                LocalDate.of(1980, 5, 5), LocalDate.of(2020, 1, 1),
                "avatarUsuario1", 10.0, TipoEstadoCuenta.ACTIVA);


        //UsuarioDto resultado = usuarioController.registrarUsuario(usuarioFormSinNombre);

        // Act & Assert
        var exception = assertThrows(ValidationException.class, () ->
                usuarioController.registrarUsuario(usuarioFormSinNombre));
        assertEquals("Nombre_Cuenta", exception.getErrores().getFirst().getCampo());
        assertEquals(ErrorType.REQUERIDO, exception.getErrores().getFirst().getMensaje());
    }

    @Test
    public void testRegistrarUsuario_UsuarioNombreDuplicado_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorNombre("usuario1")).thenReturn(Optional.of(usuarioValido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.registrarUsuario(usuarioFormValido));
    }

    @Test
    public void testRegistrarUsuario_EmailDuplicado_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorNombre("usuario1")).thenReturn(Optional.empty());
        when(usuarioRepo.obtenerTodos()).thenReturn(List.of(usuarioValido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.registrarUsuario(usuarioFormValido));
    }

    @Test
    public void testConsultarPerfilPorId_UsuarioExistente_RetornaUsuarioDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));

        // Act
        UsuarioDto resultado = usuarioController.consultarPerfil(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario1", resultado.getNombreCuentaUsuario());
        verify(usuarioRepo).obtenerPorId(1L);
    }

    @Test
    public void testConsultarPerfilPorId_UsuarioNoExistente_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.consultarPerfil(999L));
    }

    @Test
    public void testConsultarPerfilPorNombre_UsuarioExistente_RetornaUsuarioDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorNombre("usuario1")).thenReturn(Optional.of(usuarioValido));

        // Act
        UsuarioDto resultado = usuarioController.consultarPerfil("usuario1");

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario1", resultado.getNombreCuentaUsuario());
        verify(usuarioRepo).obtenerPorNombre("usuario1");
    }

    @Test
    public void testConsultarPerfilPorNombre_UsuarioNoExistente_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorNombre("usuarioInexistente")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.consultarPerfil("usuarioInexistente"));
    }

    @Test
    public void testConsultarSaldo_UsuarioExistente_RetornaUsuarioDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));

        // Act
        UsuarioDto resultado = usuarioController.consultarSaldo(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(10.0, resultado.getSaldoUsuario());
        verify(usuarioRepo).obtenerPorId(1L);
    }

    @Test
    public void testConsultarSaldo_UsuarioNoExistente_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.consultarSaldo(999L));
    }

    @Test
    public void testAnhadirSaldo_CantidadValida_RetornaUsuarioActualizado() throws ValidationException {
        // Arrange
        UsuarioEntity usuarioActualizado = new UsuarioEntity(1L, "usuario1", "usuario1@mail.com", "passUsuario1",
                "Usuario Uno", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2020, 1, 1), "avatarUsuario1", 20.0, TipoEstadoCuenta.ACTIVA);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepo.actualizar(eq(1L), any(UsuarioForm.class))).thenReturn(Optional.of(usuarioActualizado));

        // Act
        UsuarioDto resultado = usuarioController.anhadirSaldo(1L, 10.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(20.0, resultado.getSaldoUsuario());
        verify(usuarioRepo).actualizar(eq(1L), any(UsuarioForm.class));
    }

    @Test
    public void testAnhadirSaldo_UsuarioNoExistente_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.anhadirSaldo(999L, 10.0));
    }

    @Test
    public void testAnhadirSaldo_CantidadNegativa_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.anhadirSaldo(1L, -10.0));
    }

    @Test
    public void testAnhadirSaldo_CantidadMenorQueMínimo_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.anhadirSaldo(1L, 2.0));
    }

    @Test
    public void testAnhadirSaldo_CantidadMayorQueMaximo_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.anhadirSaldo(1L, 600.0));
    }

    @Test
    public void testAnhadirSaldo_CuentaSuspendida_ThrowsValidationException() {
        // Arrange
        UsuarioEntity usuarioSuspendido = new UsuarioEntity(2L, "usuario2", "usuario2@mail.com", "passUsuario2",
                "Usuario Dos", "España", LocalDate.of(1985, 5, 5),
                LocalDate.of(2020, 1, 1), "avatar2", 10.0, TipoEstadoCuenta.SUSPENDIDA);

        when(usuarioRepo.obtenerPorId(2L)).thenReturn(Optional.of(usuarioSuspendido));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            usuarioController.anhadirSaldo(2L, 10.0));
    }

        @Test
    public void testAnhadirSaldo_IdValido_CantidadValida_RetornaUsuarioDTOConSaldoActualizado() throws ValidationException {
        // Arrange
        Long idValido = 1L;
        Double cantidadValida = 5.00d;
        
        // Usuario inicial con saldo 10.00d
        UsuarioEntity usuarioInicial = new UsuarioEntity(1L, "usuario1", "usuario1@mail.com", "passUsuario1",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1", 10.00d, TipoEstadoCuenta.ACTIVA);
        
        // Usuario actualizado con saldo 15.00d (10 + 5)
        UsuarioEntity usuarioActualizado = new UsuarioEntity(1L, "usuario1", "usuario1@mail.com", "passUsuario1",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1", 15.00d, TipoEstadoCuenta.ACTIVA);
        
        UsuarioDto usuarioDtoEsperado = new UsuarioDto(1L, "usuario1", "usuario1@mail.com",
                "nombreUsuario1", "España", LocalDate.of(1980, 5, 5),
                LocalDate.of(2026, 3, 3), "avatarUsuario1", 15.00d, TipoEstadoCuenta.ACTIVA);
        
        when(usuarioRepo.obtenerPorId(idValido)).thenReturn(Optional.of(usuarioInicial));
        when(usuarioRepo.actualizar(eq(idValido), any(UsuarioForm.class))).thenReturn(Optional.of(usuarioActualizado));
        
        // Act
        UsuarioDto resultado = usuarioController.anhadirSaldo(idValido, cantidadValida);
        
        // Assert
        assertEquals(usuarioDtoEsperado, resultado);
        verify(usuarioRepo).obtenerPorId(idValido);
        verify(usuarioRepo).actualizar(eq(idValido), any(UsuarioForm.class));
    }

}
