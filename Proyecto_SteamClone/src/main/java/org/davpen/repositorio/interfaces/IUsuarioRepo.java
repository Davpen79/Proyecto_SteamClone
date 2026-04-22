package org.davpen.repositorio.interfaces;

import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;

import java.util.Optional;

public interface IUsuarioRepo extends ICrud<UsuarioEntity, UsuarioForm, Long>{

    Optional<UsuarioEntity> obtenerPorNombre(String nombreCuentaUsuario);
}
