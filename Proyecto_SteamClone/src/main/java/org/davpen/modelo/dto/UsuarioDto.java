package org.davpen.modelo.dto;

import org.davpen.enums.TipoEstadoCuenta;

import java.time.LocalDate;

public class UsuarioDto {
    //Atributos
    private Long idUsuario;
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
    public UsuarioDto(Long idUsuario, String nombreCuentaUsuario, String emailUsuario, String nombreRealUsuario, String paisUsuario,
                      LocalDate fechaNacUsuario, LocalDate fechaRegUsuario, String avatarUsuario, double saldoUsuario, TipoEstadoCuenta estadoCuentaUsuario) {
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

    //Getters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombreCuentaUsuario() {
        return nombreCuentaUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
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
}
