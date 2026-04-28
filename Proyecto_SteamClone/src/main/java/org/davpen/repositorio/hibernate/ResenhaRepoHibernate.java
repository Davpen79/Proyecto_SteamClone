package org.davpen.repositorio.hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.interfaces.IResenhaRepo;
import org.davpen.transaction.ISessionManager;

import java.util.List;
import java.util.Optional;

public class ResenhaRepoHibernate implements IResenhaRepo {

    private ISessionManager sessionManager;

    public ResenhaRepoHibernate(ISessionManager sm) {
        this.sessionManager = sm;
    }

    @Override
    public Optional<ResenhaEntity> crear(ResenhaForm form) {
        var session = sessionManager.getSession();

        var resenhaNueva = new ResenhaEntity(form.getIdUsuarioResenha(), form.getIdJuegoResenha(),
                form.isRecomendacionResenha(), form.getTextoResenha(), form.getTiempoJugadoResenha(),
                form.getFechaPublicacionResenha(), form.getFechaUltiEdicResenha(), form.getEstadoResenha());

        session.persist(resenhaNueva);

        return Optional.of(resenhaNueva);
    }

    @Override
    public Optional<ResenhaEntity> obtenerPorId(Long id) {
        var session = sessionManager.getSession();

        var resenha = session.find(ResenhaEntity.class, id);
        return Optional.of(resenha);
    }

    @Override
    public List<ResenhaEntity> obtenerTodos() {
        var session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ResenhaEntity> cq = cb.createQuery(ResenhaEntity.class);
        Root<ResenhaEntity> root = cq.from(ResenhaEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();

    }

    @Override
    public Optional<ResenhaEntity> actualizar(Long id, ResenhaForm form) {
        var session = sessionManager.getSession();

        var resenhaInicial = this.obtenerPorId(id);
        if (resenhaInicial.isEmpty()) {
            throw new IllegalArgumentException("Reseña no encontrada");
        } else {
            session.merge(new ResenhaEntity(id, form.getIdUsuarioResenha(), form.getIdJuegoResenha(),
                    form.isRecomendacionResenha(), form.getTextoResenha(), form.getTiempoJugadoResenha(),
                    form.getFechaPublicacionResenha(), form.getFechaUltiEdicResenha(), form.getEstadoResenha()));

            return this.obtenerPorId(id);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sessionManager.getSession();

        var resenhaOpt = this.obtenerPorId(id);
        if (resenhaOpt.isEmpty()) {
            return false;
        }
        var resenha = resenhaOpt.get();
        session.remove(resenha);

        return true;
    }

    @Override
    public List<ResenhaEntity> obtenerTodasPorIdUsuario(Long idUsuario, List<ResenhaEntity> listaResenhas) {
        var session = sessionManager.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ResenhaEntity> cq = cb.createQuery(ResenhaEntity.class);
        Root<ResenhaEntity> root = cq.from(ResenhaEntity.class);

        cq.select(root).where(cb.equal(root.get("idUsuarioResenha"), idUsuario));

        return session.createQuery(cq).getResultStream().toList();

    }

    @Override
    public List<ResenhaEntity> obtenerTodasPorIdJuego(Long idJuego, List<ResenhaEntity> listaResenhas) {
        var session = sessionManager.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ResenhaEntity> cq = cb.createQuery(ResenhaEntity.class);
        Root<ResenhaEntity> root = cq.from(ResenhaEntity.class);

        cq.select(root).where(cb.equal(root.get("idJuegoResenha"), idJuego));

        return session.createQuery(cq).getResultStream().toList();

    }

}
