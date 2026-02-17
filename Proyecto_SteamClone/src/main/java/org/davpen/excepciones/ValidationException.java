package org.davpen.excepciones;

import org.davpen.modelo.form.ErrorDto;

import java.util.List;

public class ValidationException extends Exception{

    public ValidationException(List<ErrorDto> errores) {

    }
}
