package modelo.dto;

import enums.TipoEstadoCuenta;

import java.time.LocalDateTime;

public class UsuarioDto {
    //Atributos
    private long idUsuario;
    private String nombreCuentaUsuario;
    private String emailUsuario;
    private String nombreRealUsuario;
    private String paisUsuario;
    private LocalDateTime fechaNacUsuario;
    private LocalDateTime fechaRegUsuario;
    private String avatarUsuario;
    private double saldoUsuario;
    private TipoEstadoCuenta estadoCuentaUsuario;

    //Constructor
    public UsuarioDto(long idUsuario, String nombreCuentaUsuario, String emailUsuario, String nombreRealUsuario, String paisUsuario, LocalDateTime fechaNacUsuario, LocalDateTime fechaRegUsuario, String avatarUsuario, double saldoUsuario, TipoEstadoCuenta estadoCuentaUsuario) {
        this.idUsuario = idUsuario;
        this.nombreCuentaUsuario = nombreCuentaUsuario;
        this.emailUsuario = emailUsuario;
        this.nombreRealUsuario = nombreRealUsuario;
        this.paisUsuario = paisUsuario;
        this.fechaNacUsuario = fechaNacUsuario;
        this.fechaRegUsuario = fechaRegUsuario;
        this.avatarUsuario = avatarUsuario;
        this.saldoUsuario = saldoUsuario;
        this.estadoCuentaUsuario = estadoCuentaUsuario;
    }

    //Getters y Setters
    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public LocalDateTime getFechaNacUsuario() {
        return fechaNacUsuario;
    }

    public void setFechaNacUsuario(LocalDateTime fechaNacUsuario) {
        this.fechaNacUsuario = fechaNacUsuario;
    }

    public LocalDateTime getFechaRegUsuario() {
        return fechaRegUsuario;
    }

    public void setFechaRegUsuario(LocalDateTime fechaRegUsuario) {
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
