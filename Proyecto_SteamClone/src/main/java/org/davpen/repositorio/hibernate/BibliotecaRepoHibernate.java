package org.davpen.repositorio.hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.repositorio.interfaces.IBibliotecaRepo;
import org.davpen.transaction.ISessionManager;

import java.util.List;
import java.util.Optional;

public class BibliotecaRepoHibernate implements IBibliotecaRepo {

    private ISessionManager sessionManager;

    public BibliotecaRepoHibernate(ISessionManager sm) {
        this.sessionManager = sm;
    }

    @Override
    public Optional<BibliotecaEntity> crear(BibliotecaForm form) {
        var session = sessionManager.getSession();

        var bibliotecaNueva = new BibliotecaEntity(form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(),
                form.getFechaAdquisicionJuegoBiblio(), form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(),
                form.getEstadoInstJuegoBiblio());

        session.persist(bibliotecaNueva);

        return Optional.of(bibliotecaNueva);
    }

    @Override
    public Optional<BibliotecaEntity> obtenerPorId(Long id) {
        var session = sessionManager.getSession();
        var biblioteca = session.find(BibliotecaEntity.class, id);
        return Optional.of(biblioteca);
    }

    @Override
    public List<BibliotecaEntity> obtenerTodos() {
        var session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntity> cq = cb.createQuery(BibliotecaEntity.class);
        Root<BibliotecaEntity> root = cq.from(BibliotecaEntity.class);

        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<BibliotecaEntity> actualizar(Long id, BibliotecaForm form) {
        var session = sessionManager.getSession();

        var bibliotecaInicial = this.obtenerPorId(id);
        if (bibliotecaInicial.isEmpty()) {
            throw new IllegalArgumentException("Biblioteca no encontrada");
        } else {
            session.merge(new BibliotecaEntity(id, form.getIdUsuarioBiblio(), form.getIdJuegoBiblio(),
                    form.getFechaAdquisicionJuegoBiblio(), form.getTiempoJuegoBiblio(), form.getUltiFechaJuegoBiblio(),
                    form.getEstadoInstJuegoBiblio()));
            return this.obtenerPorId(id);
        }

    }

    @Override
    public boolean eliminar(Long id) {
        var session = sessionManager.getSession();

        var bibliotecaOpt = this.obtenerPorId(id);
        if (bibliotecaOpt.isEmpty()) {
            return false;
        }
        var biblioteca = bibliotecaOpt.get();
        session.remove(biblioteca);

        return true;

    }

}
