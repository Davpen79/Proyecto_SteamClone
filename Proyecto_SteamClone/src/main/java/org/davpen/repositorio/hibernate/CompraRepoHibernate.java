package org.davpen.repositorio.hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.repositorio.interfaces.ICompraRepo;
import org.davpen.transaction.ISessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepoHibernate implements ICompraRepo {

    //private final List<CompraEntity> LISTA_COMPRAS = new ArrayList<>();
    private ISessionManager sessionManager;

    public CompraRepoHibernate(ISessionManager sm) {
        this.sessionManager = sm;
    }

    @Override
    public Optional<CompraEntity> crear(CompraForm form) {
        var session = sessionManager.getSession();

        var compraNueva = new CompraEntity(form.getIdUsuarioCompra(), form.getIdJuegoCompra(),
                form.getFechaCompra(), form.getTipoPagoCompra(), form.getPrecioBaseCompra(),
                form.getDescuentoEnCompra(), form.getEstadoCompra());

        session.persist(compraNueva);
        return Optional.of(compraNueva);
    }

    @Override
    public Optional<CompraEntity> obtenerPorId(Long id) {
        var session = sessionManager.getSession();

        var compra = session.find(CompraEntity.class, id);

        return Optional.of(compra);
    }

    @Override
    public List<CompraEntity> obtenerTodos() {
        var session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntity> cq = cb.createQuery(CompraEntity.class);
        Root<CompraEntity> root = cq.from(CompraEntity.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<CompraEntity> actualizar(Long id, CompraForm form) {
        var session = sessionManager.getSession();

        var compraInicial = this.obtenerPorId(id);
        if (compraInicial.isEmpty()) {
            throw new IllegalArgumentException("No se encuentra esta compra");
        } else {
            session.merge(new CompraEntity(id, form.getIdUsuarioCompra(), form.getIdJuegoCompra(),
                    form.getFechaCompra(), form.getTipoPagoCompra(), form.getPrecioBaseCompra(),
                    form.getDescuentoEnCompra(), form.getEstadoCompra()));


            return this.obtenerPorId(id);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sessionManager.getSession();

        var compra = this.obtenerPorId(id);
        if (compra.isEmpty()) {
            return false;
        }
        session.remove(compra);
        return  true;
    }

    @Override
    public Optional<CompraEntity> obtenerPorIdUsuario(Long idUsuario) {
        var session = sessionManager.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
        Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);

        cq.select(root).where(cb.equal(root.get("id_usuario"), idUsuario));

        return session.createQuery(cq).getResultStream().findFirst();//Revisar para arreglar
    }

}
