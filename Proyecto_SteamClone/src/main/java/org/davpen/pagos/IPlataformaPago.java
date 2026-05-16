package org.davpen.pagos;

import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;

public interface IPlataformaPago {

    /**
     * Procesa el pago de un articulo tras recibir la entidad Compra, la entidad Usuario y el precio
     * @param compra Entidad Compra que se ha realizado
     * @param usuario Entidad Usuario que realiza la compra
     * @param precioFinal Precio final de la compra
     * @return Devuelve True si el pago finaliza exitosamente, False en caso contrario
     * @throws ValidationException Lanza Validation Exception en caso de encontar errores de validacion durante el proceso
     */
    boolean procesarPago(CompraEntity compra, UsuarioEntity usuario, Double precioFinal) throws ValidationException;


}
