package org.davpen.modelo.form;

import org.davpen.enums.TipoEstadoInstalacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaForm {

    //Atributos
    private Long idUsuarioBiblio;
    private Long idJuegoBiblio;
    private LocalDate fechaAdquisicionJuegoBiblio;
    private double tiempoJuegoBiblio;
    private LocalDateTime ultiFechaJuegoBiblio;
    private TipoEstadoInstalacion estadoInstJuegoBiblio;

    //Constructor
    public BibliotecaForm(Long idUsuarioBiblio, Long idJuegoBiblio, LocalDate fechaAdquisicionJuegoBiblio,
                          double tiempoJuegoBiblio, LocalDateTime ultiFechaJuegoBiblio,
                          TipoEstadoInstalacion estadoInstJuegoBiblio) {
        this.idUsuarioBiblio = idUsuarioBiblio;
        this.idJuegoBiblio = idJuegoBiblio;
        this.fechaAdquisicionJuegoBiblio = fechaAdquisicionJuegoBiblio;
        this.tiempoJuegoBiblio = tiempoJuegoBiblio;
        this.ultiFechaJuegoBiblio = ultiFechaJuegoBiblio;
        this.estadoInstJuegoBiblio = estadoInstJuegoBiblio;
    }

    //Getters

    public Long getIdUsuarioBiblio() {
        return idUsuarioBiblio;
    }

    public Long getIdJuegoBiblio() {
        return idJuegoBiblio;
    }

    public LocalDate getFechaAdquisicionJuegoBiblio() {
        return fechaAdquisicionJuegoBiblio;
    }

    public double getTiempoJuegoBiblio() {
        return tiempoJuegoBiblio;
    }

    public LocalDateTime getUltiFechaJuegoBiblio() {
        return ultiFechaJuegoBiblio;
    }

    public TipoEstadoInstalacion getEstadoInstJuegoBiblio() {
        return estadoInstJuegoBiblio;
    }

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones Id Usuario
        if (idUsuarioBiblio == null) {
            errores.add(new ErrorDto("Id_usuario", ErrorType.REQUERIDO));
        }

        //Validaciones Id Juego
        if (idJuegoBiblio == null) {
            errores.add(new ErrorDto("Id_juego", ErrorType.REQUERIDO));
        }

        //Validaciones Fecha adquisicion
        if (fechaAdquisicionJuegoBiblio == null) {
            errores.add(new ErrorDto("fecha_adquisicion", ErrorType.REQUERIDO));
        }

        // ESTO NO ES AQUI VA A CONTROLADOR
        if (fechaAdquisicionJuegoBiblio.isAfter(LocalDate.now())) {
            errores.add(new ErrorDto("fecha_compra", ErrorType.FECHA_FUTURA));
        }

        return errores;
    }
}
