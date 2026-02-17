package org.davpen.repositorio.inmemory;

import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.repositorio.intefaces.ICompraRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepoInMemory implements ICompraRepo {

    private static final List<CompraEntity> listaCompras = new ArrayList<>();
    private static Long idCounter = 1L;

    @Override
    public Optional<CompraEntity> crear(CompraForm form) {

        var compraNueva = new CompraEntity(idCounter++, form.getIdUsuarioCompra(), form.getIdJuegoCompra(), form.getFechaCompra(),
                form.getTipoPagoCompra(), form.getPrecioBaseCompra(), form.getDescuentoEnCompra(), form.getEstadoCompra());
        listaCompras.add(compraNueva);

        return Optional.of(compraNueva);
    }

    @Override
    public Optional<CompraEntity> obtenerPorId(Long id) {

        return listaCompras.stream()
                .filter(c -> c.getIdCompra().equals(id))
                .findFirst();
    }

    @Override
    public List<CompraEntity> obtenerTodos() {
        return new ArrayList<>(listaCompras);
    }
    //Como Actualizas una compra
    @Override
    public Optional<CompraEntity> actualizar(Long id, CompraForm form) {

        var compraInicial = obtenerPorId(id);
        if (compraInicial.isEmpty()){
            throw new IllegalArgumentException("No se encuentra esta compra");
        }
        var compraActualizada = new CompraEntity(id, form.getIdUsuarioCompra(), form.getIdJuegoCompra(), form.getFechaCompra(),
                form.getTipoPagoCompra(), form.getPrecioBaseCompra(), form.getDescuentoEnCompra(), form.getEstadoCompra());
        listaCompras.removeIf(c -> c.getIdCompra().equals(id));
        listaCompras.add(compraActualizada);

        return Optional.of(compraActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return listaCompras.removeIf(c -> c.getIdCompra().equals(id));
    }
}
