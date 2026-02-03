package modelo.form;

import enums.TipoEstadoCuenta;

import java.time.LocalDate;

public class UsuarioFormDTO {
    //Atributos
    private String nombreCuentaUsuario;
    private String emailUsuario;
    //private String passwordUsuario;
    private String nombreRealUsuario;
    private String paisUsuario;
    private LocalDate fechaNacUsuario;
    private LocalDate fechaRegUsuario;
    private String avatarUsuario;
    private double saldoUsuario;
    private TipoEstadoCuenta estadoCuentaUsuario;

    //Constructor
    public UsuarioFormDTO(String nombreCuentaUsuario, String emailUsuario, String nombreRealUsuario, String paisUsuario, LocalDate fechaNacUsuario, LocalDate fechaRegUsuario, String avatarUsuario, double saldoUsuario, TipoEstadoCuenta estadoCuentaUsuario) {
        this.nombreCuentaUsuario = nombreCuentaUsuario;
        this.emailUsuario = emailUsuario;
        //this.passwordUsuario = passwordUsuario;
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

    //public String getPasswordUsuario() {
    //    return passwordUsuario;
    //}

    //public void setPasswordUsuario(String passwordUsuario) {
    //    this.passwordUsuario = passwordUsuario;
    //}

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
}
