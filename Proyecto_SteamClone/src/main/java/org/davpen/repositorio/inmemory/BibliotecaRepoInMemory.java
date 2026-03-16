package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BibliotecaRepoInMemory implements IBibliotecaRepo {

    private static final List<BibliotecaEntity> LISTA_BIBLIOTECAS = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<BibliotecaEntity> crear(BibliotecaForm form) {

        var bibliotecaNueva = new BibliotecaEntity(idCounter++, form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(),
                form.getFechaAdquisicionJuegoBiblio(), form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(),
                form.getEstadoInstJuegoBiblio());
        LISTA_BIBLIOTECAS.add(bibliotecaNueva);

        return Optional.of(bibliotecaNueva);
    }

    @Override
    public Optional<BibliotecaEntity> obtenerPorId(Long id) {

        return LISTA_BIBLIOTECAS.stream().filter(b -> b.getIdBiblio().equals(id)).findFirst();
    }

    @Override
    public List<BibliotecaEntity> obtenerTodos() {
        return new ArrayList<>(LISTA_BIBLIOTECAS);
    }

    @Override
    public Optional<BibliotecaEntity> actualizar(Long id, BibliotecaForm form) {

        var bibliotecaInicial = obtenerPorId(id);
        if (bibliotecaInicial.isEmpty()) {
            throw new IllegalArgumentException();
        }
        var bibliotecaActualizada = new BibliotecaEntity(id, form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(),
                form.getFechaAdquisicionJuegoBiblio(), form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(),
                form.getEstadoInstJuegoBiblio());
        LISTA_BIBLIOTECAS.removeIf(b -> b.getIdBiblio().equals(id));
        LISTA_BIBLIOTECAS.add(bibliotecaActualizada);

        return Optional.of(bibliotecaActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return LISTA_BIBLIOTECAS.removeIf(b -> b.getIdBiblio().equals(id));
    }
}
