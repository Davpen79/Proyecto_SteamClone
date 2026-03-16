package org.davpen.modelo.dto;

import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.entity.EstadisticasBibliotecaEntity;

import java.time.LocalDate;
import java.util.List;

public class PerfilUsuarioDto {

    //Atributos
    private String nombreUsuario;
    private String avatarUsuario;
    private String paisUsuario;
    private LocalDate fechaFechaRegUsuario;
    private List<BibliotecaEntity> bibliotecaUsuario;
    private EstadisticasBibliotecaEntity estadisticasBibliotecaUsuario;

    //Constructor
    public PerfilUsuarioDto(String nombreUsuario, String avatarUsuario, String paisUsuario,
                            LocalDate fechaFechaRegUsuario, List<BibliotecaEntity> bibliotecaUsuario,
                            EstadisticasBibliotecaEntity estadisticasBibliotecaUsuario) {
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

    public EstadisticasBibliotecaEntity getEstadisticasBibliotecaUsuario() {
        return estadisticasBibliotecaUsuario;
    }

}
