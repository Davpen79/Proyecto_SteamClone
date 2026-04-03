package repositorio.inmemory;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.repositorio.inmemory.CompraRepoInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CompraRepoInMemoryTest {

    private CompraRepoInMemory compraRepo;
    private CompraForm compraFormValida;

    @BeforeEach
    public void setUp() {
        compraRepo = new CompraRepoInMemory();
        compraFormValida = new CompraForm(1L, 1L, LocalDate.of(2024, 3, 28),
                TipoMetodoPago.CARTERA_STEAM, 29.99, 0, TipoEstadoCompra.COMPLETADA);
    }

    @Test
    public void testCrear_CompraFormValida_RetornaOptionalOfCompraEntity() {
        // Act
        Optional<CompraEntity> resultado = compraRepo.crear(compraFormValida);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdUsuarioCompra());
        assertEquals(1L, resultado.get().getIdJuegoCompra());
        assertEquals(29.99, resultado.get().getPrecioBaseCompra());
    }

    @Test
    public void testObtenerTodos_RetornaListaConTodasLasCompras() {
        // Arrange
        CompraForm form1 = new CompraForm(1L, 1L, LocalDate.of(2024, 3, 28),
                TipoMetodoPago.CARTERA_STEAM, 29.99, 0, TipoEstadoCompra.COMPLETADA);
        CompraForm form2 = new CompraForm(1L, 2L, LocalDate.of(2024, 3, 27),
                TipoMetodoPago.PAYPAL, 59.99, 10, TipoEstadoCompra.COMPLETADA);

        // Act
        compraRepo.crear(form1);
        compraRepo.crear(form2);
        List<CompraEntity> resultado = compraRepo.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerPorId_IdValido_RetornaOptionalOfCompraEntity() {
        // Arrange
        Optional<CompraEntity> creada = compraRepo.crear(compraFormValida);
        Long id = creada.get().getIdCompra();

        // Act
        Optional<CompraEntity> resultado = compraRepo.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdCompra());
    }

    @Test
    public void testObtenerPorId_IdNoExiste_RetornaOptionalEmpty() {
        // Act
        Optional<CompraEntity> resultado = compraRepo.obtenerPorId(999L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testObtenerPorIdUsuario_UsuarioConCompras_RetornaOptionalOfCompra() {
        // Arrange
        compraRepo.crear(compraFormValida);

        // Act
        Optional<CompraEntity> resultado = compraRepo.obtenerPorIdUsuario(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdUsuarioCompra());
    }

    @Test
    public void testObtenerPorIdUsuario_UsuarioSinCompras_RetornaOptionalEmpty() {
        // Act
        Optional<CompraEntity> resultado = compraRepo.obtenerPorIdUsuario(999L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testActualizar_DatosValidos_RetornaOptionalOfCompraActualizada() {
        // Arrange
        Optional<CompraEntity> creada = compraRepo.crear(compraFormValida);
        Long id = creada.get().getIdCompra();

        CompraForm formActualizada = new CompraForm(1L, 1L, LocalDate.of(2024, 3, 28),
                TipoMetodoPago.TARJETA_CREDITO, 29.99, 0, TipoEstadoCompra.REEMBOLSADA);

        // Act
        Optional<CompraEntity> resultado = compraRepo.actualizar(id, formActualizada);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(TipoEstadoCompra.REEMBOLSADA, resultado.get().getEstadoCompra());
    }

    @Test
    public void testEliminar_IdValido_RetornaTrue() {
        // Arrange
        Optional<CompraEntity> creada = compraRepo.crear(compraFormValida);
        Long id = creada.get().getIdCompra();

        // Act
        boolean resultado = compraRepo.eliminar(id);

        // Assert
        assertTrue(resultado);
        assertTrue(compraRepo.obtenerPorId(id).isEmpty());
    }

    @Test
    public void testEliminar_IdNoExiste_RetornaFalse() {
        // Act
        boolean resultado = compraRepo.eliminar(999L);

        // Assert
        assertFalse(resultado);
    }
}
