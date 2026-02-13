package org.davpen.modelo.form;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoJuego;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JuegoForm {

    //Atributos
    private String tituloJuego;
    private String descripcionJuego;
    private String desarrolladorJuego;
    private LocalDate fechaLanzaJuego;
    private Double precioBaseJuego;
    private Integer descuentoActualJuego;
    private TipoCategoriaJuego categoriaJuego;
    private TipoClasificacionEdades clasEdadJuego;
    private ArrayList idiomasJuego;
    private TipoEstadoJuego estadoJuego;

    //Constructor
    public JuegoForm(String tituloJuego, String descripcionJuego, String desarrolladorJuego, LocalDate fechaLanzaJuego, double precioBaseJuego, int descuentoActualJuego,
                     TipoCategoriaJuego categoriaJuego, TipoClasificacionEdades clasEdadJuego, ArrayList idiomasJuego, TipoEstadoJuego estadoJuego) {
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

    public Double getPrecioBaseJuego() {
        return precioBaseJuego;
    }

    public void setPrecioBaseJuego(Double precioBaseJuego) {
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

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones Titulo
        if (tituloJuego == null || tituloJuego.isBlank()){
            errores.add(new ErrorDto("titulo", ErrorType.REQUERIDO));
        }

        if (tituloJuego != null && tituloJuego.length() < 1){
            errores.add(new ErrorDto("titulo", ErrorType.DEMASIADO_CORTO));
        }

        if (tituloJuego != null && tituloJuego.length() > 100){
            errores.add(new ErrorDto("titulo", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones Descripcion
        if (descripcionJuego != null && descripcionJuego.length() > 2000){
            errores.add(new ErrorDto("descripcion", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones Desarrollador
        if (desarrolladorJuego == null || desarrolladorJuego.isBlank()){
            errores.add(new ErrorDto("desarrollador", ErrorType.REQUERIDO));
        }

        if (desarrolladorJuego != null && desarrolladorJuego.length() < 2){
            errores.add(new ErrorDto("desarrollador", ErrorType.DEMASIADO_CORTO));
        }

        if (desarrolladorJuego != null && desarrolladorJuego.length() > 100){
            errores.add(new ErrorDto("desarrollador", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones Fecha Lanzamiento - Puede ser fecha pasada¿?
        if (fechaLanzaJuego == null){
            errores.add(new ErrorDto("fecha_lanzamiento", ErrorType.REQUERIDO));
        }

        //Validaciones Precio base
        if (precioBaseJuego == null){
            errores.add(new ErrorDto("precio_base", ErrorType.REQUERIDO));
        }

        if (precioBaseJuego != null && precioBaseJuego < 0.00d){
            errores.add(new ErrorDto("precio_base", ErrorType.VALOR_NEGATIVO));
        }

        if (precioBaseJuego != null && precioBaseJuego > 999.99d){
            errores.add(new ErrorDto("precio_base", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        //TODO max 2 decimales - Usar DecimalFormat ¿?

        //Validaciones Precio descuento
        if (descuentoActualJuego < 0){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_NEGATIVO));
        }

        if (descuentoActualJuego > 100){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        boolean descuentoEsEntero = descuentoActualJuego instanceof Integer;
        if (!descuentoEsEntero){
            errores.add(new ErrorDto("descuento", ErrorType.SOLO_ENTEROS));
        }

        //Validaciones de Edad
        if (clasEdadJuego == null){
            errores.add(new ErrorDto("edad", ErrorType.REQUERIDO));
        }
        //
        if (!Arrays.stream(TipoClasificacionEdades.values()).anyMatch(e -> e.equals(clasEdadJuego))){
            errores.add(new ErrorDto("edad", ErrorType.NO_ENCONTRADO));
        }

        //Validaciones Idiomas - Lista ¿?

        //Validaciones Estado

        return  errores;
    }


}
