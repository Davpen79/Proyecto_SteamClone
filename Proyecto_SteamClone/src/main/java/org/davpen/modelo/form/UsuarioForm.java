package org.davpen.modelo.form;

import org.davpen.enums.TipoEstadoCuenta;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {
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
    public UsuarioForm(String nombreCuentaUsuario, String emailUsuario, String passwordUsuario, String nombreRealUsuario, String paisUsuario, LocalDate fechaNacUsuario, LocalDate fechaRegUsuario, String avatarUsuario, double saldoUsuario, TipoEstadoCuenta estadoCuentaUsuario) {
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

    //Getters y Setters
    public String getNombreCuentaUsuario() {
        return nombreCuentaUsuario;
    }

    public void setNombreCuentaUsuario(String nombreCuentaUsuario) {
        this.nombreCuentaUsuario = nombreCuentaUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }

    public String getNombreRealUsuario() {
        return nombreRealUsuario;
    }

    public void setNombreRealUsuario(String nombreRealUsuario) {
        this.nombreRealUsuario = nombreRealUsuario;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public void setPaisUsuario(String paisUsuario) {
        this.paisUsuario = paisUsuario;
    }

    public LocalDate getFechaNacUsuario() {
        return fechaNacUsuario;
    }

    public void setFechaNacUsuario(LocalDate fechaNacUsuario) {
        this.fechaNacUsuario = fechaNacUsuario;
    }

    public LocalDate getFechaRegUsuario() {
        return fechaRegUsuario;
    }

    public void setFechaRegUsuario(LocalDate fechaRegUsuario) {
        this.fechaRegUsuario = fechaRegUsuario;
    }

    public String getAvatarUsuario() {
        return avatarUsuario;
    }

    public void setAvatarUsuario(String avatarUsuario) {
        this.avatarUsuario = avatarUsuario;
    }

    public double getSaldoUsuario() {
        return saldoUsuario;
    }

    public void setSaldoUsuario(double saldoUsuario) {
        this.saldoUsuario = saldoUsuario;
    }

    public TipoEstadoCuenta getEstadoCuentaUsuario() {
        return estadoCuentaUsuario;
    }

    public void setEstadoCuentaUsuario(TipoEstadoCuenta estadoCuentaUsuario) {
        this.estadoCuentaUsuario = estadoCuentaUsuario;
    }

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones Nombre Usuario
        if (nombreCuentaUsuario == null || nombreCuentaUsuario.isBlank()) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.REQUERIDO));
        }

        if (nombreCuentaUsuario != null && nombreCuentaUsuario.length() < 3) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.DEMASIADO_CORTO));
        }

        if (nombreCuentaUsuario != null && nombreCuentaUsuario.length() > 20) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.DEMASIADO_LARGO));
        }

        if (nombreCuentaUsuario != null && Character.isDigit(nombreCuentaUsuario.charAt(0))) {
            errores.add(new ErrorDto("Nombre_Cuenta", ErrorType.FORMATO_INVALIDO));
        }

        //TODO Comprueba nombre alfanumerico-_

        //Validaciones email Usuario
        if (emailUsuario == null || emailUsuario.isBlank()) {
            errores.add(new ErrorDto("email_Cuenta", ErrorType.REQUERIDO));
        }

        if (emailUsuario != null && !emailUsuario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
            errores.add(new ErrorDto("email_Cuenta", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones contraseña usuario
        if (passwordUsuario == null || passwordUsuario.isBlank()){
            errores.add(new ErrorDto("password", ErrorType.REQUERIDO));
        }

        if (passwordUsuario !=null && !passwordUsuario.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\p{Punct}]+$")){
            errores.add(new ErrorDto("password", ErrorType.FORMATO_INVALIDO));
        }

        if (passwordUsuario !=null && passwordUsuario.length() < 8){
            errores.add(new ErrorDto("password", ErrorType.DEMASIADO_CORTO));
        }

        //Validaciones nombre Real de usuario
        if (nombreRealUsuario == null || nombreRealUsuario.isBlank()){
            errores.add(new ErrorDto("nombre_real", ErrorType.REQUERIDO));
        }

        if (nombreRealUsuario != null && nombreRealUsuario.length() < 2){
            errores.add(new ErrorDto("nombre_real", ErrorType.DEMASIADO_CORTO));
        }

        if (nombreRealUsuario != null && nombreRealUsuario.length() > 50){
            errores.add(new ErrorDto("nombre_real", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones del Pais
        if (paisUsuario == null || paisUsuario.isBlank()){
            errores.add(new ErrorDto("pais", ErrorType.REQUERIDO));
        }

        List<String> listaPaises = List.of("España","Francia","Portugal");
        if (!listaPaises.contains(paisUsuario)){
            errores.add(new ErrorDto("pais", ErrorType.NO_ENCONTRADO));
        }

        //Validaciones Fecha nacimiento
        if (fechaNacUsuario == null || fechaNacUsuario.toString().isBlank()){
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.REQUERIDO));
        }

        if (fechaNacUsuario != null && fechaNacUsuario.isAfter(LocalDate.now())){
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.FECHA_FUTURA));
        }

        if (fechaNacUsuario != null && Period.between(fechaNacUsuario,fechaRegUsuario).getYears() < 14){
            errores.add(new ErrorDto("fecha_nacimiento", ErrorType.MENOR_DE_EDAD));
        }

        //Validaciones Avatar

        if (avatarUsuario != null && avatarUsuario.length() > 100){
            errores.add(new ErrorDto("avatar", ErrorType.DEMASIADO_LARGO));
        }


        return errores;
    }
}
