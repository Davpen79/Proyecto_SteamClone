package org.davpen.modelo.dto;

import org.davpen.modelo.entity.BibliotecaEntity;

import java.time.LocalDate;
import java.util.List;

public class PerfilUsuarioDto {

    //Atributos
    private String nombreUsuario;
    private String avatarUsuario;
    private String paisUsuario;
    private LocalDate fechaFechaRegUsuario;
    private List<BibliotecaEntity> bibliotecaUsuario;
    private EstadisticasBibliotecaDto estadisticasBibliotecaUsuario;

    //Constructor
    public PerfilUsuarioDto(String nombreUsuario, String avatarUsuario, String paisUsuario,
                            LocalDate fechaFechaRegUsuario, List<BibliotecaEntity> bibliotecaUsuario,
                            EstadisticasBibliotecaDto estadisticasBibliotecaUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.avatarUsuario = avatarUsuario;
        this.paisUsuario = paisUsuario;
        this.fechaFechaRegUsuario = fechaFechaRegUsuario;
        this.bibliotecaUsuario = bibliotecaUsuario;
        this.estadisticasBibliotecaUsuario = estadisticasBibliotecaUsuario;
    }

    //Getters

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getAvatarUsuario() {
        return avatarUsuario;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public LocalDate getFechaFechaRegUsuario() {
        return fechaFechaRegUsuario;
    }

    public List<BibliotecaEntity> getBibliotecaUsuario() {
        return bibliotecaUsuario;
    }

    public EstadisticasBibliotecaDto getEstadisticasBibliotecaUsuario() {
        return estadisticasBibliotecaUsuario;
    }

}
