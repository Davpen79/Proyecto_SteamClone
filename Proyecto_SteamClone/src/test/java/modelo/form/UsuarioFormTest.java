package modelo.form;

import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioFormTest {

    private UsuarioForm usuarioFormValido;
    private UsuarioForm usuarioFormInvalido;

    @BeforeEach
    public void setup() {
        // Usuario válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com", "Password123!", "Juan Perez", "España", LocalDate.of(2000, Month.JANUARY, 1), LocalDate.of(2022, Month.JANUARY, 1), "avatar123", 1000.0
                , TipoEstadoCuenta.ACTIVA);

        // Usuario inválido con varios errores
        usuarioFormInvalido = new UsuarioForm("1Usuario",  // Nombre con número al inicio (invalid)
                "usuario@com",  // Email inválido
                "pass",  // Contraseña demasiado corta
                "J",  // Nombre real demasiado corto
                "",  // País vacío
                LocalDate.of(2030, Month.JANUARY, 1),  // Fecha de nacimiento futura
                LocalDate.of(2022, Month.JANUARY, 1), "a".repeat(101),  // Avatar demasiado largo
                -100.0,  // Saldo negativo
                TipoEstadoCuenta.SUSPENDIDA);
    }

    @Test
    public void testValidarUsuarioFormValido() {
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.isEmpty(), "No debería haber errores para un usuario válido");
    }

    @Test
    public void testValidarUsuarioFormInvalido() {
        List<ErrorDto> errores = usuarioFormInvalido.validar();

        assertFalse(errores.isEmpty(), "Debería haber errores para un usuario inválido");

        // Verificar errores específicos
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("Nombre_Cuenta")
                && error.getMensaje() == ErrorType.FORMATO_INVALIDO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("email_Cuenta")
                && error.getMensaje() == ErrorType.FORMATO_INVALIDO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("password")
                && error.getMensaje() == ErrorType.DEMASIADO_CORTO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("nombre_real")
                && error.getMensaje() == ErrorType.DEMASIADO_CORTO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("pais")
                && error.getMensaje() == ErrorType.REQUERIDO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("fecha_nacimiento")
                && error.getMensaje() == ErrorType.FECHA_FUTURA));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("avatar")
                && error.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    public void testValidarNombreCuentaUsuario() {
        // Caso válido
        usuarioFormValido = new UsuarioForm("Usuario123", "email@example.com",
                "Password123!", "Nombre Real", "España",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("Nombre_Cuenta")));

        // Caso con error
        usuarioFormInvalido = new UsuarioForm("1Usuario", "email@example.com",
                "Password123!", "Nombre Real", "España",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("Nombre_Cuenta")
                && error.getMensaje() == ErrorType.FORMATO_INVALIDO));
    }

    @Test
    public void testValidarEmailUsuario() {
        // Caso válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Nombre Real", "España"
                , LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("email_Cuenta")));

        // Caso con error
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@com",
                "Password123!", "Nombre Real", "España",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("email_Cuenta")
                && error.getMensaje() == ErrorType.FORMATO_INVALIDO));
    }

    @Test
    public void testValidarPasswordUsuario() {
        // Caso con contraseña válida
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Nombre Real", "España"
                , LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("password")));

        // Caso con contraseña demasiado corta
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "pass", "Nombre Real", "España",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("password")
                && error.getMensaje() == ErrorType.DEMASIADO_CORTO));
    }

    // Prueba similar para los demás campos

    @Test
    public void testValidarNombreRealUsuario() {
        // Caso con nombre real válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.now(), LocalDate.now(), "avatar.png",
                1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("nombre_real")));

        // Caso con nombre real demasiado corto
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "J", "España",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("nombre_real")
                && error.getMensaje() == ErrorType.DEMASIADO_CORTO));

        // Caso con nombre real demasiado largo
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!",
                "Juan Carlos ".repeat(51), "España", LocalDate.now(), LocalDate.now(),
                "avatar.png", 1000.0,
                TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("nombre_real")
                && error.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    public void testValidarPaisUsuario() {
        // Caso con país válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.now(), LocalDate.now(), "avatar.png",
                1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("España")));

        // Caso con país vacío
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez", "",
                LocalDate.now(), LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("pais")
                && error.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    public void testValidarFechaNacimientoUsuario() {
        // Caso con fecha válida (mayor de 14 años)
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez", "España",
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalDate.now(), "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("fecha_nacimiento")));

        // Caso con fecha de nacimiento futura
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.of(2030, Month.JANUARY, 1), LocalDate.now(),
                "avatar.png", 1000.0,
                TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("fecha_nacimiento")
                && error.getMensaje() == ErrorType.FECHA_FUTURA));

        // Caso con menor de edad (menos de 14 años)
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.of(2020, Month.JANUARY, 1), LocalDate.now(),
                "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("fecha_nacimiento")
                && error.getMensaje() == ErrorType.MENOR_DE_EDAD));
    }

    @Test
    public void testValidarAvatarUsuario() {
        // Caso con avatar válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez", "España",
                LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now(),
                "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("avatar")));

        // Caso con avatar demasiado largo
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now(),
                "a".repeat(101), 1000.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("avatar")
                && error.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    public void testValidarSaldoUsuario() {
        // Caso con saldo válido
        usuarioFormValido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez", "España", 
                LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now(),
                "avatar.png", 1000.0, TipoEstadoCuenta.ACTIVA);
        List<ErrorDto> errores = usuarioFormValido.validar();
        assertTrue(errores.stream().noneMatch(error -> error.getCampo().equals("saldoUsuario")));

        // Caso con saldo negativo
        usuarioFormInvalido = new UsuarioForm("Usuario123", "usuario@example.com",
                "Password123!", "Juan Perez",
                "España", LocalDate.of(2000, Month.JANUARY, 1), LocalDate.now(),
                "avatar.png", -100.0, TipoEstadoCuenta.ACTIVA);
        errores = usuarioFormInvalido.validar();
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("saldoUsuario")
                && error.getMensaje() == ErrorType.REQUERIDO));
    }
}

