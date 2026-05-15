package org.davpen.repositorio.interfaces;

import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;

import java.util.Optional;

public interface IUsuarioRepo extends ICrud<UsuarioEntity, UsuarioForm, Long>{

    /**
     * Obtiene una entidad por el String asociado (Nombre de la Cuenta)
     * @param nombreCuentaUsuario El nombre de la cuenta de usuario
     * @return Un Optional con la entidad encontrada, o vacío si no existe.
     */
    Optional<UsuarioEntity> obtenerPorNombre(String nombreCuentaUsuario);
}
