package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.repositorio.intefaces.ICompraRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepoInMemory implements ICompraRepo {

    private final List<CompraEntity> LISTA_COMPRAS = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public Optional<CompraEntity> crear(CompraForm form) {

        var compraNueva = new CompraEntity(idCounter++, form.getIdUsuarioCompra(), form.getIdJuegoCompra(),
                form.getFechaCompra(), form.getTipoPagoCompra(), form.getPrecioBaseCompra(),
                form.getDescuentoEnCompra(), form.getEstadoCompra());
        LISTA_COMPRAS.add(compraNueva);

        return Optional.of(compraNueva);
    }

    @Override
    public Optional<CompraEntity> obtenerPorId(Long id) {

        return LISTA_COMPRAS.stream().filter(c -> c.getIdCompra().equals(id)).findFirst();
    }

    @Override
    public List<CompraEntity> obtenerTodos() {
        return new ArrayList<>(LISTA_COMPRAS);
    }

    //Como Actualizas una compra
    @Override
    public Optional<CompraEntity> actualizar(Long id, CompraForm form) {

        var compraInicial = obtenerPorId(id);
        if (compraInicial.isEmpty()) {
            throw new IllegalArgumentException("No se encuentra esta compra");
        }
        var compraActualizada = new CompraEntity(id, form.getIdUsuarioCompra(), form.getIdJuegoCompra(),
                form.getFechaCompra(), form.getTipoPagoCompra(), form.getPrecioBaseCompra(),
                form.getDescuentoEnCompra(), form.getEstadoCompra());
        LISTA_COMPRAS.removeIf(c -> c.getIdCompra().equals(id));
        LISTA_COMPRAS.add(compraActualizada);

        return Optional.of(compraActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return LISTA_COMPRAS.removeIf(c -> c.getIdCompra().equals(id));
    }

    @Override
    public Optional<CompraEntity> obtenerPorIdUsuario(Long idUsuario) {
        return LISTA_COMPRAS.stream().filter(c -> c.getIdUsuarioCompra()
                .equals(idUsuario)).findFirst();
    }
}
