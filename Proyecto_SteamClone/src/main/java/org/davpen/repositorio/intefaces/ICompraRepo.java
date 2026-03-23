package org.davpen.repositorio.intefaces;

import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;

import java.util.Optional;

public interface ICompraRepo extends ICrud<CompraEntity, CompraForm, Long> {

    Optional<CompraEntity> obtenerPorIdUsuario(Long idUsuario);
}
