package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UsuarioRepoInMemory implements IUsuarioRepo {

    private static final List<UsuarioEntity> usuarios = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<UsuarioEntity> crear(UsuarioForm form) {
        return Optional.empty();
    }

    @Override
    public Optional<UsuarioEntity> obtenerPorId(Long Long) {
        return Optional.empty();
    }

    @Override
    public List<UsuarioEntity> obtenerTodos() {
        return List.of();
    }

    @Override
    public Optional<UsuarioEntity> actualizar(Long Long, UsuarioForm form) {
        return Optional.empty();
    }

    @Override
    public boolean eliminar(Long Long) {
        return false;
    }
}
