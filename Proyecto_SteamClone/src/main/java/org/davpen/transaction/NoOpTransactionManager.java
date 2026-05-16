package org.davpen.transaction;

import org.davpen.excepciones.ValidationException;

import java.util.Optional;

/**
 * Implementaci�n no-op de {@link ITransactionManager}.
 * Se usa con repositorios en memoria donde no existe el concepto de
 * transacci�n.
 */
public class NoOpTransactionManager implements ITransactionManager {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T inTransaction(ExceptionSupplier<T> work) throws ValidationException {
        try {
            return work.get();
        }catch(ValidationException e){
            throw e;
        }
        catch (Exception e) {
            try {
                return (T) Optional.empty();
            } catch (ClassCastException ex) {
                return null;
            }
        }
    }
}