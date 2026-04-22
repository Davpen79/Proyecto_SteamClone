package org.davpen.modelo.entity;

import jakarta.persistence.*;
import org.davpen.enums.TipoEstadoCuenta;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "usuarios")
@Entity
public class UsuarioEntity {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    @Column(name = "nombre_cuenta")
    private String nombreCuentaUsuario;
    @Column(name = "email")
    private String emailUsuario;
    @Column(name = "password")
    private String passwordUsuario;
    @Column(name = "nombre_real")
    private String nombreRealUsuario;
    @Column(name = "pais")
    private String paisUsuario;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacUsuario;
    @Column(name = "fecha_registro")
    private LocalDate fechaRegUsuario;
    @Column(name = "avatar")
    private String avatarUsuario;
    @Column(name = "saldo")
    private double saldoUsuario;
    @Column(name = "estado")
    private TipoEstadoCuenta estadoCuentaUsuario;

    //Constructor
    public UsuarioEntity(){}

    //Constructor
    public UsuarioEntity(Long idUsuario, String nombreCuentaUsuario, String emailUsuario, String passwordUsuario,
                         String nombreRealUsuario, String paisUsuario, LocalDate fechaNacUsuario,
                         LocalDate fechaRegUsuario, String avatarUsuario, double saldoUsuario,
                         TipoEstadoCuenta estadoCuentaUsuario) {
        this.idUsuario = idUsuario;
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

    //Constructor
    public UsuarioEntity(String nombreCuentaUsuario, String emailUsuario, String passwordUsuario,
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

    public TipoEstadoCuenta getEstadoCuentaUsuario() {
        return estadoCuentaUsuario;
    }

    public double getSaldoUsuario() {
        return saldoUsuario;
    }

    public String getAvatarUsuario() {
        return avatarUsuario;
    }

    public LocalDate getFechaRegUsuario() {
        return fechaRegUsuario;
    }

    public LocalDate getFechaNacUsuario() {
        return fechaNacUsuario;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public String getNombreRealUsuario() {
        return nombreRealUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getNombreCuentaUsuario() {
        return nombreCuentaUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Double.compare(saldoUsuario, that.saldoUsuario) == 0 && Objects.equals(idUsuario, that.idUsuario)
                && Objects.equals(nombreCuentaUsuario, that.nombreCuentaUsuario)
                && Objects.equals(emailUsuario, that.emailUsuario)
                && Objects.equals(passwordUsuario, that.passwordUsuario)
                && Objects.equals(nombreRealUsuario, that.nombreRealUsuario)
                && Objects.equals(paisUsuario, that.paisUsuario)
                && Objects.equals(fechaNacUsuario, that.fechaNacUsuario)
                && Objects.equals(fechaRegUsuario, that.fechaRegUsuario)
                && Objects.equals(avatarUsuario, that.avatarUsuario)
                && estadoCuentaUsuario == that.estadoCuentaUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombreCuentaUsuario, emailUsuario, passwordUsuario, nombreRealUsuario,
                paisUsuario, fechaNacUsuario, fechaRegUsuario, avatarUsuario, saldoUsuario, estadoCuentaUsuario);
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "idUsuario=" + idUsuario +
                ", nombreCuentaUsuario='" + nombreCuentaUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", passwordUsuario='" + passwordUsuario + '\'' +
                ", nombreRealUsuario='" + nombreRealUsuario + '\'' +
                ", paisUsuario='" + paisUsuario + '\'' +
                ", fechaNacUsuario=" + fechaNacUsuario +
                ", fechaRegUsuario=" + fechaRegUsuario +
                ", avatarUsuario='" + avatarUsuario + '\'' +
                ", saldoUsuario=" + saldoUsuario +
                ", estadoCuentaUsuario=" + estadoCuentaUsuario +
                '}';
    }
}
