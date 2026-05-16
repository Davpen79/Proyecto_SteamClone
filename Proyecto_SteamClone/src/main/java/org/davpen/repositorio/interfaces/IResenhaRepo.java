package org.davpen.repositorio.interfaces;

import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ResenhaForm;

import java.util.List;

public interface IResenhaRepo extends ICrud<ResenhaEntity, ResenhaForm, Long> {

    /**
     * Obtiene una lista de las entidades asociadas a un identificador de entidad (Usuario) a partir de
     * una lista con todas las entidades existentes
     * @param id Identificador de entidad (Usuario)
     * @param lista Lista de todas las entidades.
     * @return Lista de todas las entidades asociadas al identificador
     */
    List<ResenhaEntity> obtenerTodasPorIdUsuario(Long id, List<ResenhaEntity> lista);

    /**
     * Obtiene una lista de las entidades asociadas a un identificador de entidad (Juego) a partir de
     * una lista con todas las entidades existentes
     * @param id Identificador de entidad (Juego)
     * @param lista Lista de todas las entidades.
     * @return Lista de todas las entidades asociadas al identificador
     */
    List<ResenhaEntity> obtenerTodasPorIdJuego (Long id, List<ResenhaEntity> lista);
}
