package org.davpen.modelo.dto;

import java.time.LocalDate;
import java.util.List;

public class PerfilUsuarioDto {

    //Atributos
    private String nombreUsuario;
    private String avatarUsuario;
    private String paisUsuario;
    private LocalDate fechaFechaRegUsuario;
    private List<BibliotecaDto> bibliotecasUsuario;
    private EstadisticasBibliotecaDto estadisticasBibliotecaUsuario;

    //Constructor
    public PerfilUsuarioDto(String nombreUsuario, String avatarUsuario, String paisUsuario,
                            LocalDate fechaFechaRegUsuario, List<BibliotecaDto> bibliotecaUsuario,
                            EstadisticasBibliotecaDto estadisticasBibliotecaUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.avatarUsuario = avatarUsuario;
        this.paisUsuario = paisUsuario;
        this.fechaFechaRegUsuario = fechaFechaRegUsuario;
        this.bibliotecasUsuario = bibliotecaUsuario;
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

    public List<BibliotecaDto> getBibliotecasUsuario() {
        return bibliotecasUsuario;
    }

    public EstadisticasBibliotecaDto getEstadisticasBibliotecaUsuario() {
        return estadisticasBibliotecaUsuario;
    }

}
