package org.davpen.repositorio.hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.davpen.transaction.ISessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UsuarioRepoHibernate implements IUsuarioRepo {

    private ISessionManager sessionManager;
    public UsuarioRepoHibernate(ISessionManager sm){
        this.sessionManager = sm;
    }

    @Override
    public Optional<UsuarioEntity> crear(UsuarioForm form) {
        var session = sessionManager.getSession();

        var usuarioNuevo = new UsuarioEntity(form.getNombreCuentaUsuario(), form.getEmailUsuario(),
                form.getPasswordUsuario(), form.getNombreRealUsuario(), form.getPaisUsuario(),
                form.getFechaNacUsuario(), LocalDate.now(), form.getAvatarUsuario(), 0.00,
                TipoEstadoCuenta.ACTIVA);
        session.persist(usuarioNuevo);
        return Optional.of(usuarioNuevo);
    }

    @Override
    public Optional<UsuarioEntity> obtenerPorId(Long id) {
        var session = sessionManager.getSession();
        var usuario = session.find(UsuarioEntity.class, id);
        return Optional.of(usuario);
    }

    @Override
    public List<UsuarioEntity> obtenerTodos() {
        var session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
        Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);

        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<UsuarioEntity> actualizar(Long id, UsuarioForm form) {
        var session = sessionManager.getSession();

        var usuarioInicial = this.obtenerPorId(id);
        if (usuarioInicial.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }else {
            session.merge(new UsuarioEntity(id, form.getNombreCuentaUsuario(), form.getEmailUsuario(),
                    form.getPasswordUsuario(), form.getNombreRealUsuario(), form.getPaisUsuario(),
                    form.getFechaNacUsuario(), form.getFechaRegUsuario(), form.getAvatarUsuario(), form.getSaldoUsuario()
                    , form.getEstadoCuentaUsuario()));
        }

        return this.obtenerPorId(id);
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sessionManager.getSession();

        var usuario = this.obtenerPorId(id);
        if (usuario.isEmpty()){
            return false;
        }
        session.remove(usuario);
        return true;
    }

    @Override
    public Optional<UsuarioEntity> obtenerPorNombre(String nombreCuentaUsuario) {
        var session = sessionManager.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
        Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);

        cq.select(root).where(cb.equal(root.get("nombreCuentaUsuario"), nombreCuentaUsuario));

        return session.createQuery(cq).getResultStream().findFirst();
    }
}
