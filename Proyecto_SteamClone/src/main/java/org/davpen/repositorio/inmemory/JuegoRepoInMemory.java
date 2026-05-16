package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.interfaces.IJuegoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoRepoInMemory implements IJuegoRepo {

    private final List<JuegoEntity> LISTA_JUEGOS = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public Optional<JuegoEntity> crear(JuegoForm form) {

        var juegoNuevo = new JuegoEntity(idCounter++, form.getTituloJuego(), form.getDescripcionJuego(),
                form.getDesarrolladorJuego(), form.getFechaLanzaJuego(), form.getPrecioBaseJuego(),
                form.getDescuentoActualJuego(), form.getCategoriaJuego(), form.getClasEdadJuego(),
                form.getIdiomasJuego(), form.getEstadoJuego());
        LISTA_JUEGOS.add(juegoNuevo);

        return Optional.of(juegoNuevo);
    }

    @Override
    public Optional<JuegoEntity> obtenerPorId(Long id) {

        return LISTA_JUEGOS.stream().filter(j -> j.getIdJuego().equals(id)).findFirst();
    }

    @Override
    public List<JuegoEntity> obtenerTodos() {

        return new ArrayList<>(LISTA_JUEGOS);
    }

    @Override
    public Optional<JuegoEntity> actualizar(Long id, JuegoForm form) {

        var juegoInicial = obtenerPorId(id);
        if (juegoInicial.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }
        var juegoActualizado = new JuegoEntity(id, form.getTituloJuego(), form.getDescripcionJuego(),
                form.getDesarrolladorJuego(), form.getFechaLanzaJuego(), form.getPrecioBaseJuego(),
                form.getDescuentoActualJuego(), form.getCategoriaJuego(), form.getClasEdadJuego(),
                form.getIdiomasJuego(), form.getEstadoJuego());
        LISTA_JUEGOS.removeIf(j -> j.getIdJuego().equals(id));
        LISTA_JUEGOS.add(juegoActualizado);

        return Optional.of(juegoActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return LISTA_JUEGOS.removeIf(j -> j.getIdJuego().equals(id));
    }
}

