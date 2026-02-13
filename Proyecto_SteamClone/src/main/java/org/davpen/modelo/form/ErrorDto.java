package org.davpen.modelo.form;

public class ErrorDto {

    private String campo;
    private ErrorType mensaje;

    public String getCampo() {
        return campo;
    }

    public ErrorType getMensaje() {
        return mensaje;
    }

    public ErrorDto(String campo, ErrorType mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }
}
