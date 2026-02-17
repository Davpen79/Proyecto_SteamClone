package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.intefaces.IJuegoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoRepoInMemory implements IJuegoRepo {

    private static final List<JuegoEntity> listaJuegos = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<JuegoEntity> crear(JuegoForm form) {

        var juegoNuevo = new JuegoEntity(idCounter++, form.getTituloJuego(), form.getDescripcionJuego(), form.getDesarrolladorJuego(),
                form.getFechaLanzaJuego(), form.getPrecioBaseJuego(), form.getDescuentoActualJuego(), form.getCategoriaJuego(),
                form.getClasEdadJuego(), form.getIdiomasJuego(), form.getEstadoJuego());
        listaJuegos.add(juegoNuevo);

        return Optional.of(juegoNuevo);
    }

    @Override
    public Optional<JuegoEntity> obtenerPorId(Long id) {

        return listaJuegos.stream()
                .filter(j -> j.getIdJuego().equals(id))
                .findFirst();
    }

    @Override
    public List<JuegoEntity> obtenerTodos() {

        return new ArrayList<>(listaJuegos);
    }

    @Override
    public Optional<JuegoEntity> actualizar(Long id, JuegoForm form) {

        var juegoInicial = obtenerPorId(id);
        if (juegoInicial.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }
        var juegoActualizado = new JuegoEntity(id, form.getTituloJuego(), form.getDescripcionJuego(), form.getDesarrolladorJuego(),
                form.getFechaLanzaJuego(), form.getPrecioBaseJuego(), form.getDescuentoActualJuego(), form.getCategoriaJuego(),
                form.getClasEdadJuego(), form.getIdiomasJuego(), form.getEstadoJuego());
        listaJuegos.removeIf(j -> j.getIdJuego().equals(id));
        listaJuegos.add(juegoActualizado);

        return Optional.of(juegoActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return listaJuegos.removeIf(j -> j.getIdJuego().equals(id));
    }
}

