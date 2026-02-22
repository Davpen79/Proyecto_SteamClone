package org.davpen.excepciones;

import org.davpen.modelo.form.ErrorDto;

import java.util.List;

public class ValidationException extends Exception {

    private List<ErrorDto> errores;

    public ValidationException(List<ErrorDto> errores) {
        super("Errores de validación");
        this.errores = errores;
    }
    public List<ErrorDto> getErrores() {
        return errores;
    }
}
