package modelo.form;

import enums.TipoCategoriaJuego;
import enums.TipoClasificacionEdades;
import enums.TipoEstadoJuego;

import java.time.LocalDate;
import java.util.ArrayList;

public class JuegoFormDTO {

    //Atributos
    private String tituloJuego;
    private String descripcionJuego;
    private String desarrolladorJuego;
    private LocalDate fechaLanzaJuego;
    private double precioBaseJuego;
    private int descuentoActualJuego;
    private TipoCategoriaJuego categoriaJuego;
    private TipoClasificacionEdades clasEdadJuego;
    private ArrayList idiomasJuego;
    private TipoEstadoJuego estadoJuego;

    //Constructor
    public JuegoFormDTO(String tituloJuego, String descripcionJuego, String desarrolladorJuego, LocalDate fechaLanzaJuego, double precioBaseJuego, int descuentoActualJuego, TipoCategoriaJuego categoriaJuego, TipoClasificacionEdades clasEdadJuego, ArrayList idiomasJuego, TipoEstadoJuego estadoJuego) {
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

    //Getters y Setters

    public String getTituloJuego() {
        return tituloJuego;
    }

    public void setTituloJuego(String tituloJuego) {
        this.tituloJuego = tituloJuego;
    }

    public String getDescripcionJuego() {
        return descripcionJuego;
    }

    public void setDescripcionJuego(String descripcionJuego) {
        this.descripcionJuego = descripcionJuego;
    }

    public String getDesarrolladorJuego() {
        return desarrolladorJuego;
    }

    public void setDesarrolladorJuego(String desarrolladorJuego) {
        this.desarrolladorJuego = desarrolladorJuego;
    }

    public LocalDate getFechaLanzaJuego() {
        return fechaLanzaJuego;
    }

    public void setFechaLanzaJuego(LocalDate fechaLanzaJuego) {
        this.fechaLanzaJuego = fechaLanzaJuego;
    }

    public double getPrecioBaseJuego() {
        return precioBaseJuego;
    }

    public void setPrecioBaseJuego(double precioBaseJuego) {
        this.precioBaseJuego = precioBaseJuego;
    }

    public int getDescuentoActualJuego() {
        return descuentoActualJuego;
    }

    public void setDescuentoActualJuego(int descuentoActualJuego) {
        this.descuentoActualJuego = descuentoActualJuego;
    }

    public TipoCategoriaJuego getCategoriaJuego() {
        return categoriaJuego;
    }

    public void setCategoriaJuego(TipoCategoriaJuego categoriaJuego) {
        this.categoriaJuego = categoriaJuego;
    }

    public TipoClasificacionEdades getClasEdadJuego() {
        return clasEdadJuego;
    }

    public void setClasEdadJuego(TipoClasificacionEdades clasEdadJuego) {
        this.clasEdadJuego = clasEdadJuego;
    }

    public ArrayList getIdiomasJuego() {
        return idiomasJuego;
    }

    public void setIdiomasJuego(ArrayList idiomasJuego) {
        this.idiomasJuego = idiomasJuego;
    }

    public TipoEstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public void setEstadoJuego(TipoEstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }
}
