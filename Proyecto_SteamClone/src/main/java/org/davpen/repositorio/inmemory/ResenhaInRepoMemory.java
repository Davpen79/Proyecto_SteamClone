package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.intefaces.IResenhaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResenhaInRepoMemory implements IResenhaRepo {

    private static final List<ResenhaEntity> LISTA_RESENHAS = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<ResenhaEntity> crear(ResenhaForm form) {

        var resenhaNueva = new ResenhaEntity(idCounter++, form.getIdUsuarioResenha(), form.getIdJuegoResenha(),
                form.isRecomendacionResenha(), form.getTextoResenha(), form.getTiempoJugadoResenha(),
                form.getFechaPublicacionResenha(), form.getFechaUltiEdicResenha(), form.getEstadoResenha());
        LISTA_RESENHAS.add(resenhaNueva);

        return Optional.of(resenhaNueva);
    }

    @Override
    public Optional<ResenhaEntity> obtenerPorId(Long id) {
        return LISTA_RESENHAS.stream()
                .filter(r -> r.getIdResenha().equals(id))
                .findFirst();
    }

    @Override
    public List<ResenhaEntity> obtenerTodos() {
        return new ArrayList<>(LISTA_RESENHAS);
    }

    @Override
    public Optional<ResenhaEntity> actualizar(Long id, ResenhaForm form) {

        var resenhaInicial = obtenerPorId(id);
        if (resenhaInicial.isEmpty()){
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        var resenhaActualizada = new ResenhaEntity(id, form.getIdUsuarioResenha(), form.getIdJuegoResenha(),
                form.isRecomendacionResenha(), form.getTextoResenha(), form.getTiempoJugadoResenha(),
                form.getFechaPublicacionResenha(), form.getFechaUltiEdicResenha(), form.getEstadoResenha());
        LISTA_RESENHAS.removeIf(r -> r.getIdResenha().equals(id));
        LISTA_RESENHAS.add(resenhaActualizada);

        return Optional.of(resenhaActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return LISTA_RESENHAS.removeIf(r -> r.getIdResenha().equals(id));
    }

    @Override
    public List<ResenhaEntity> obtenerTodasPorIdUsuario(Long idUsuario, List<ResenhaEntity> listaResenhas) {

        return listaResenhas.stream()
                .filter(r -> r.getIdUsuarioResenha().equals(idUsuario))
                .toList();
    }

    @Override
    public List<ResenhaEntity> obtenerTodasPorIdJuego(Long idJuego, List<ResenhaEntity> listaResenhas) {
        return listaResenhas.stream()
                .filter(r -> r.getIdJuegoResenha().equals(idJuego))
                .toList();
    }
}
