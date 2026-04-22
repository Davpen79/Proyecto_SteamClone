package org.davpen.repositorio.interfaces;

import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ResenhaForm;

import java.util.List;

public interface IResenhaRepo extends ICrud<ResenhaEntity, ResenhaForm, Long> {

    List<ResenhaEntity> obtenerTodasPorIdUsuario(Long id, List<ResenhaEntity> lista);

    List<ResenhaEntity> obtenerTodasPorIdJuego (Long id, List<ResenhaEntity> lista);
}
