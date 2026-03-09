package org.davpen.modelo.form;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoJuego;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JuegoForm {

    public static final int DESCRIPCION_MAX = 2000;
    public static final int TITULO_LENGTH_MAX = 100;
    public static final int TITULO_LENGTH_MIN = 1;
    public static final int DESARROLLADOR_LENGTH_MIN = 2;
    public static final int DESARROLLADOR_LENGTH_MAX = 100;
    public static final double PRECIO_MIN = 0.00d;
    public static final double PRECIO_MAX = 999.99d;
    public static final int DESCUENTO_MIN = 0;
    public static final int DESCUENTO_MAX = 100;
    public static final int IDIOMAS_LENGTH_MAX = 200;
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

    //Getters

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

    public Double getPrecioBaseJuego() {
        return precioBaseJuego;
    }

    public Integer getDescuentoActualJuego() {
        return descuentoActualJuego;
    }

    public TipoCategoriaJuego getCategoriaJuego() {
        return categoriaJuego;
    }

    public TipoClasificacionEdades getClasEdadJuego() {
        return clasEdadJuego;
    }

    public ArrayList getIdiomasJuego() {
        return idiomasJuego;
    }

    public TipoEstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    //Funciones Validacion
    public List<ErrorDto> validar() {
        List<ErrorDto> errores = new ArrayList<>();

        //Validaciones Titulo
        if (tituloJuego == null || tituloJuego.isBlank()){
            errores.add(new ErrorDto("titulo", ErrorType.REQUERIDO));
        }

        if (tituloJuego != null && tituloJuego.length() < TITULO_LENGTH_MIN){
            errores.add(new ErrorDto("titulo", ErrorType.DEMASIADO_CORTO));
        }

        if (tituloJuego != null && tituloJuego.length() > TITULO_LENGTH_MAX){
            errores.add(new ErrorDto("titulo", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones Descripcion
        if (descripcionJuego != null && descripcionJuego.length() > DESCRIPCION_MAX){
            errores.add(new ErrorDto("descripcion", ErrorType.DEMASIADO_LARGO));
        }

        //Validaciones Desarrollador
        if (desarrolladorJuego == null || desarrolladorJuego.isBlank()){
            errores.add(new ErrorDto("desarrollador", ErrorType.REQUERIDO));
        }

        if (desarrolladorJuego != null && desarrolladorJuego.length() < DESARROLLADOR_LENGTH_MIN){
            errores.add(new ErrorDto("desarrollador", ErrorType.DEMASIADO_CORTO));
        }

        if (desarrolladorJuego != null && desarrolladorJuego.length() > DESARROLLADOR_LENGTH_MAX){
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

        if (precioBaseJuego != null && precioBaseJuego < PRECIO_MIN){
            errores.add(new ErrorDto("precio_base", ErrorType.VALOR_NEGATIVO));
        }

        if (precioBaseJuego != null && precioBaseJuego > PRECIO_MAX){
            errores.add(new ErrorDto("precio_base", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        if (precioBaseJuego != null && !precioBaseJuego.toString().matches("\\d+(\\.\\d{0,2})?")){
            errores.add(new ErrorDto("precio_base", ErrorType.DEMASIADOS_DECIMALES));
        }

        //Validaciones Precio descuento
        if (descuentoActualJuego != null && descuentoActualJuego < DESCUENTO_MIN){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_NEGATIVO));
        }

        if (descuentoActualJuego > DESCUENTO_MAX){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        boolean descuentoEsEntero = descuentoActualJuego instanceof Integer;
        if (descuentoActualJuego != null && !descuentoEsEntero){
            errores.add(new ErrorDto("descuento", ErrorType.SOLO_ENTEROS));
        }

        //Validaciones de Edad
        if (clasEdadJuego == null){
            errores.add(new ErrorDto("edad", ErrorType.REQUERIDO));
        }
        //
        if (clasEdadJuego != null && !Arrays.stream(TipoClasificacionEdades.values()).anyMatch(e -> e.equals(clasEdadJuego))){
            errores.add(new ErrorDto("edad", ErrorType.NO_ENCONTRADO));
        }

        //Validaciones Idiomas - Lista ¿?
        if (idiomasJuego != null && idiomasJuego.isEmpty()){
            errores.add(new ErrorDto("idioma", ErrorType.CAMPO_VACIO));
        }
        if (idiomasJuego != null && !idiomasJuego.isEmpty() && idiomasJuego.toString().length() > IDIOMAS_LENGTH_MAX){
            errores.add(new ErrorDto("idioma", ErrorType.DEMASIADO_LARGO));
        }

        return  errores;
    }


}
