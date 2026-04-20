package org.davpen.transaction;

import org.davpen.excepciones.ValidationException;

public interface ExceptionSupplier<T> {

    T get() throws ValidationException;
}