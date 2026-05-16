package org.davpen.repositorio.interfaces;

import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;

import java.util.Optional;

public interface ICompraRepo extends ICrud<CompraEntity, CompraForm, Long> {

    /**
     * Obtiene todas las entidades a partir de un identificador de entidad (Usuario)
     * @param idUsuario Identificador de una entidad (Usuario)
     * @return Optional de la Lista de todas las entidades asociadas a ese identificador
     */
    Optional<CompraEntity> obtenerPorIdUsuario(Long idUsuario);
}
