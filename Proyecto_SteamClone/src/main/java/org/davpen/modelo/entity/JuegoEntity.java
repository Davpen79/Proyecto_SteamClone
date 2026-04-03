package org.davpen.modelo.entity;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoJuego;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class JuegoEntity {

    //Atributos
    private Long idJuego;
    private String tituloJuego;
    private String descripcionJuego;
    private String desarrolladorJuego;
    private LocalDate fechaLanzaJuego;
    private double precioBaseJuego;
    private int descuentoActualJuego;
    private TipoCategoriaJuego categoriaJuego;
    private TipoClasificacionEdades clasEdadJuego;
    private ArrayList<String> idiomasJuego;
    private TipoEstadoJuego estadoJuego;

    //Constructor
    public JuegoEntity(Long idJuego, String tituloJuego, String descripcionJuego, String desarrolladorJuego,
                       LocalDate fechaLanzaJuego, double precioBaseJuego, int descuentoActualJuego,
                       TipoCategoriaJuego categoriaJuego, TipoClasificacionEdades clasEdadJuego,
                       ArrayList<String> idiomasJuego, TipoEstadoJuego estadoJuego) {
        this.idJuego = idJuego;
        this.tituloJuego = tituloJuego;
        this.descripcionJuego = descripcionJuego;
        this.desarrolladorJuego = desarrolladorJuego;
        this.fechaLanzaJuego = fechaLanzaJuego;
        this.precioBaseJuego = precioBaseJuego;
        this.descuentoActualJuego = descuentoActualJuego;
        this.categoriaJuego = categoriaJuego;
        this.clasEdadJuego = clasEdadJuego;
        this.idiomasJuego = idiomasJuego;
        this.estadoJuego = estadoJuego;
    }

    //Getters

    public Long getIdJuego() {
        return idJuego;
    }

    public String getTituloJuego() {
        return tituloJuego;
    }

    public String getDescripcionJuego() {
        return descripcionJuego;
    }

    public String getDesarrolladorJuego() {
        return desarrolladorJuego;
    }

    public LocalDate getFechaLanzaJuego() {
        return fechaLanzaJuego;
    }

    public double getPrecioBaseJuego() {
        return precioBaseJuego;
    }

    public int getDescuentoActualJuego() {
        return descuentoActualJuego;
    }

    public TipoCategoriaJuego getCategoriaJuego() {
        return categoriaJuego;
    }

    public TipoClasificacionEdades getClasEdadJuego() {
        return clasEdadJuego;
    }

    public ArrayList<String> getIdiomasJuego() {
        return idiomasJuego;
    }

    public TipoEstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JuegoEntity that = (JuegoEntity) o;
        return Double.compare(precioBaseJuego, that.precioBaseJuego) == 0
                && descuentoActualJuego == that.descuentoActualJuego
                && Objects.equals(idJuego, that.idJuego)
                && Objects.equals(tituloJuego, that.tituloJuego)
                && Objects.equals(descripcionJuego, that.descripcionJuego)
                && Objects.equals(desarrolladorJuego, that.desarrolladorJuego)
                && Objects.equals(fechaLanzaJuego, that.fechaLanzaJuego)
                && categoriaJuego == that.categoriaJuego
                && clasEdadJuego == that.clasEdadJuego
                && Objects.equals(idiomasJuego, that.idiomasJuego)
                && estadoJuego == that.estadoJuego;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJuego, tituloJuego, descripcionJuego, desarrolladorJuego, fechaLanzaJuego,
                precioBaseJuego, descuentoActualJuego, categoriaJuego, clasEdadJuego, idiomasJuego, estadoJuego);
    }

    @Override
    public String toString() {
        return "JuegoEntity{" +
                "idJuego=" + idJuego +
                ", tituloJuego='" + tituloJuego + '\'' +
                ", descripcionJuego='" + descripcionJuego + '\'' +
                ", desarrolladorJuego='" + desarrolladorJuego + '\'' +
                ", fechaLanzaJuego=" + fechaLanzaJuego +
                ", precioBaseJuego=" + precioBaseJuego +
                ", descuentoActualJuego=" + descuentoActualJuego +
                ", categoriaJuego=" + categoriaJuego +
                ", clasEdadJuego=" + clasEdadJuego +
                ", idiomasJuego=" + idiomasJuego +
                ", estadoJuego=" + estadoJuego +
                '}';
    }
}
