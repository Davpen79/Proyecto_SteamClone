package org.davpen.repositorio.hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.transaction.ISessionManager;

import java.util.List;
import java.util.Optional;

public class JuegoRepoHibernate implements IJuegoRepo {

    private ISessionManager sessionManager;

    public JuegoRepoHibernate(ISessionManager sm) {
        this.sessionManager = sm;
    }

    @Override
    public Optional<JuegoEntity> crear(JuegoForm form) {
        var session = sessionManager.getSession();

        var juegoNuevo = new JuegoEntity(form.getTituloJuego(), form.getDescripcionJuego(),
                form.getDesarrolladorJuego(), form.getFechaLanzaJuego(), form.getPrecioBaseJuego(),
                form.getDescuentoActualJuego(), form.getCategoriaJuego(), form.getClasEdadJuego(),
                form.getIdiomasJuego(), form.getEstadoJuego());

        session.persist(juegoNuevo);

        return Optional.of(juegoNuevo);
    }

    @Override
    public Optional<JuegoEntity> obtenerPorId(Long id) {
        var session = sessionManager.getSession();

        var juego = session.find(JuegoEntity.class, id);
        return Optional.of(juego);
    }

    @Override
    public List<JuegoEntity> obtenerTodos() {
        var session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<JuegoEntity> cq = cb.createQuery(JuegoEntity.class);
        Root<JuegoEntity> root = cq.from(JuegoEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<JuegoEntity> actualizar(Long id, JuegoForm form) {
        var session = sessionManager.getSession();

        var juegoInicial = this.obtenerPorId(id);
        if (juegoInicial.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        } else {
            session.merge(new JuegoEntity(id, form.getTituloJuego(), form.getDescripcionJuego(),
                    form.getDesarrolladorJuego(), form.getFechaLanzaJuego(), form.getPrecioBaseJuego(),
                    form.getDescuentoActualJuego(), form.getCategoriaJuego(), form.getClasEdadJuego(),
                    form.getIdiomasJuego(), form.getEstadoJuego()));

            //LISTA_JUEGOS.removeIf(j -> j.getIdJuego().equals(id));
            //LISTA_JUEGOS.add(juegoActualizado);

            return this.obtenerPorId(id);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sessionManager.getSession();

        var juego = this.obtenerPorId(id);
        if (juego.isEmpty()) {
            return false;
        }
        session.remove(juego);

        return true;
    }
}
