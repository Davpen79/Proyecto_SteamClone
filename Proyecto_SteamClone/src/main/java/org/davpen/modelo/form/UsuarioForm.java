package org.davpen.modelo.form;

import org.davpen.enums.TipoEstadoCuenta;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {
    public static final int CUENTA_LENGTH_MIN = 3;
    public static final int CUENTA_LENGTH_MAX = 20;
    public static final int PASS_LENGTH_MIN = 8;
    public static final int NOMBRE_LENGTH_MIN = 2;
    public static final int NOMBRE_LENGTH_MAX = 50;
    public static final int EDAD_MIN = 14;
    public static final int AVATAR_MAX = 100;
    //Atributos
    private String nombreCuentaUsuario;
    private String emailUsuario;
    private String passwordUsuario;
    private String nombreRealUsuario;
    private String paisUsuario;
    private LocalDate fechaNacUsuario;
    private LocalDate fechaRegUsuario;
    private String avatarUsuario;
    private double saldoUsuario;
    private TipoEstadoCuenta estadoCuentaUsuario;

    //Constructor
    public UsuarioForm(String nombreCuentaUsuario, String emailUsuario, String passwordUsuario,
                       String nombreRealUsuario, String paisUsuario, LocalDate fechaNacUsuario,
                       LocalDate fechaRegUsuario, String avatarUsuario, double saldoUsuario,
                       TipoEstadoCuenta estadoCuentaUsuario) {
        this.nombreCuentaUsuario = nombreCuentaUsuario;
        this.emailUsuario = emailUsuario;
        this.passwordUsuario = passwordUsuario;
        this.nombreRealUsuario = nombreRealUsuario;
        this.paisUsuario = paisUsuario;
        this.fechaNacUsuario = fechaNacUsuario;
        this.fechaRegUsuario = fechaRegUsuario;
        this.avatarUsuario = avatarUsuario;
        this.saldoUsuario = saldoUsuario;
        this.estadoCuentaUsuario = estadoCuentaUsuario;
    }

    //Getters


    public String getNombreCuentaUsuario() {
        return nombreCuentaUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public String getNombreRealUsuario() {
        return nombreRealUsuario;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public LocalDate getFechaNacUsuario() {
        return fechaNacUsuario;
    }

    public LocalDate getFechaRegUsuario() {
        return fechaRegUsuario;
    }

    public String getAvatarUsuario() {
        return avatarUsuario;
    }

    public double getSaldoUsuario() {
        return saldoUsuario;
    }

    public TipoEstadoCuenta getEstadoCuentaUsuario() {
        return estadoCuentaUsuario;
    }

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones Nombre Usuario
        if (nombreCuentaUsuario == null || nombreCuentaUsuario.isBlank()) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.REQUERIDO));
        }

        if (nombreCuentaUsuario != null && nombreCuentaUsuario.length() < CUENTA_LENGTH_MIN) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.DEMASIADO_CORTO));
        }

        if (nombreCuentaUsuario != null && nombreCuentaUsuario.length() > CUENTA_LENGTH_MAX) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.DEMASIADO_LARGO));
        }

        if (nombreCuentaUsuario != null && Character.isDigit(nombreCuentaUsuario.charAt(0))) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.FORMATO_INVALIDO));
        }

        if (nombreCuentaUsuario != null && !nombreCuentaUsuario.matches("[A-Za-z0-9_-]+")) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones email Usuario
        if (emailUsuario == null || emailUsuario.isBlank()) {
            errores.add(new ErrorDto("email_Cuenta", ErrorType.REQUERIDO));
        }

        if (emailUsuario != null && !emailUsuario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errores.add(new ErrorDto("email_Cuenta", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones contraseña usuario
        if (passwordUsuario == null || passwordUsuario.isBlank()) {
            errores.add(new ErrorDto("password", ErrorType.REQUERIDO));
        }

        if (passwordUsuario != null && !passwordUsuario.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" + "[A-Za-z\\d\\p" +
                "{Punct}]+$")) {
            errores.add(new ErrorDto("password", ErrorType.FORMATO_INVALIDO));
        }

        if (passwordUsuario != null && passwordUsuario.length() < PASS_LENGTH_MIN) {
            errores.add(new ErrorDto("password", ErrorType.DEMASIADO_CORTO));
        }

        //Validaciones nombre Real de usuario
        if (nombreRealUsuario == null || nombreRealUsuario.isBlank()) {
            errores.add(new ErrorDto("nombre_real", ErrorType.REQUERIDO));
        }

        if (nombreRealUsuario != null && nombreRealUsuario.length() < NOMBRE_LENGTH_MIN) {
            errores.add(new ErrorDto("nombre_real", ErrorType.DEMASIADO_CORTO));
        }

        if (nombreRealUsuario != null && nombreRealUsuario.length() > NOMBRE_LENGTH_MAX) {
            errores.add(new ErrorDto("nombre_real", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones del Pais
        if (paisUsuario == null || paisUsuario.isBlank()) {
            errores.add(new ErrorDto("pais", ErrorType.REQUERIDO));
        }

        //Validaciones Fecha nacimiento
        if (fechaNacUsuario == null || fechaNacUsuario.toString().isBlank()) {
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.REQUERIDO));
        }

        if (fechaNacUsuario != null && fechaNacUsuario.isAfter(LocalDate.now())) {
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.FECHA_FUTURA));
        }

        if (fechaNacUsuario != null && Period.between(fechaNacUsuario, fechaRegUsuario).getYears() < EDAD_MIN) {
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.MENOR_DE_EDAD));
        }

        //Validaciones Avatar

        if (avatarUsuario != null && avatarUsuario.length() > AVATAR_MAX) {
            errores.add(new ErrorDto("avatar", ErrorType.DEMASIADO_LARGO));
        }

        return errores;
    }
}
