package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BibliotecaRepoInMemory implements IBibliotecaRepo {

    private static final List<BibliotecaEntity> listaBibliotecas = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<BibliotecaEntity> crear(BibliotecaForm form) {

        var bibliotecaNueva = new BibliotecaEntity(idCounter++, form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(), form.getFechaAdquisicionJuegoBiblio(),
                form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(), form.getEstadoInstJuegoBiblio());
        listaBibliotecas.add(bibliotecaNueva);

        return Optional.of(bibliotecaNueva);
    }

    @Override
    public Optional<BibliotecaEntity> obtenerPorId(Long id) {

        return listaBibliotecas.stream()
                .filter(b -> b.getIdBiblio().equals(id))
                .findFirst();
    }

    @Override
    public List<BibliotecaEntity> obtenerTodos() {
        return new ArrayList<>(listaBibliotecas);
    }

    @Override
    public Optional<BibliotecaEntity> actualizar(Long id, BibliotecaForm form) {

        var bibliotecaInicial = obtenerPorId(id);
        if (bibliotecaInicial.isEmpty()){
            throw new IllegalArgumentException();
        }
        var bibliotecaActualizada = new BibliotecaEntity(id, form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(), form.getFechaAdquisicionJuegoBiblio(),
                form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(), form.getEstadoInstJuegoBiblio());
        listaBibliotecas.removeIf(b -> b.getIdBiblio().equals(id));
        listaBibliotecas.add(bibliotecaActualizada);

        return Optional.of(bibliotecaActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return listaBibliotecas.removeIf(b -> b.getIdBiblio().equals(id));
    }
}
