package modelo.form;

public enum ErrorType {

    REQUERIDO("El campo es obligatorio"),
    DUPLICADO("El elemento está duplicado"),
    DEMASIADO_LARGO("El valor es demasiado largo"),
    DEMASIADO_CORTO("El valor es demasiado corto"),
    FORMATO_INVALIDO("El formato es inválido"),
    NO_ENCONTRADO("No se encontró el valor"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    FECHA_FUTURA("La fecha no puede ser futura"),
    VALOR_NEGATIVO("El valor no puede ser negativo"),
    DEMASIADOS_DECIMALES("No puede tener mas de dos decimales"),
    SOLO_ENTEROS("El valor debe ser un numero entero"),
    FECHA_PASADA("La fecha no puede ser anterior a la compra"),
    CUENTA_INACTIVA("La cuenta debe estar activa"),
    NO_DISPONIBLE("El juego no está disponible"),
    MENOR_DE_EDAD("Debes tener al menos 13 años de edad");

    private final String mensaje;

    ErrorType(String mensaje) {
        this.mensaje = mensaje;
    }
}
