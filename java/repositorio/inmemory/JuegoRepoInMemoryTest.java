package repositorio.inmemory;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.inmemory.JuegoRepoInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JuegoRepoInMemoryTest {

    private JuegoRepoInMemory juegoRepo;
    private JuegoForm juegoFormValido;

    @BeforeEach
    public void setUp() {
        juegoRepo = new JuegoRepoInMemory();
        juegoFormValido = new JuegoForm("The Legend of Zelda", "Aventura épica", "Nintendo",
                LocalDate.of(2023, 5, 12), 59.99, 0, TipoCategoriaJuego.AVENTURA,
                TipoClasificacionEdades.PEGI_12, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);
    }

    @Test
    public void testCrear_JuegoFormValido_RetornaOptionalOfJuegoEntity() {
        // Act
        Optional<JuegoEntity> resultado = juegoRepo.crear(juegoFormValido);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("The Legend of Zelda", resultado.get().getTituloJuego());
        assertEquals(59.99, resultado.get().getPrecioBaseJuego());
    }

    @Test
    public void testObtenerTodos_RetornaListaConTodosLosJuegos() {
        // Arrange
        JuegoForm form1 = new JuegoForm("Game 1", "Desc 1", "Dev1",
                LocalDate.of(2023, 1, 1), 29.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);
        JuegoForm form2 = new JuegoForm("Game 2", "Desc 2", "Dev2",
                LocalDate.of(2023, 2, 1), 49.99, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        // Act
        juegoRepo.crear(form1);
        juegoRepo.crear(form2);
        List<JuegoEntity> resultado = juegoRepo.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerPorId_IdValido_RetornaOptionalOfJuegoEntity() {
        // Arrange
        Optional<JuegoEntity> creado = juegoRepo.crear(juegoFormValido);
        Long id = creado.get().getIdJuego();

        // Act
        Optional<JuegoEntity> resultado = juegoRepo.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdJuego());
        assertEquals("The Legend of Zelda", resultado.get().getTituloJuego());
    }

    @Test
    public void testObtenerPorId_IdNoExiste_RetornaOptionalEmpty() {
        // Act
        Optional<JuegoEntity> resultado = juegoRepo.obtenerPorId(999L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testActualizar_DatosValidos_RetornaOptionalOfJuegoActualizado() {
        // Arrange
        Optional<JuegoEntity> creado = juegoRepo.crear(juegoFormValido);
        Long id = creado.get().getIdJuego();

        JuegoForm formActualizada = new JuegoForm("The Legend of Zelda", "Aventura épica mejorada", "Nintendo",
                LocalDate.of(2023, 5, 12), 49.99, 0, TipoCategoriaJuego.AVENTURA,
                TipoClasificacionEdades.PEGI_12, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        // Act
        Optional<JuegoEntity> resultado = juegoRepo.actualizar(id, formActualizada);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(49.99, resultado.get().getPrecioBaseJuego());
        assertEquals("Aventura épica mejorada", resultado.get().getDescripcionJuego());
    }

    @Test
    public void testEliminar_IdValido_RetornaTrue() {
        // Arrange
        Optional<JuegoEntity> creado = juegoRepo.crear(juegoFormValido);
        Long id = creado.get().getIdJuego();

        // Act
        boolean resultado = juegoRepo.eliminar(id);

        // Assert
        assertTrue(resultado);
        assertTrue(juegoRepo.obtenerPorId(id).isEmpty());
    }

    @Test
    public void testEliminar_IdNoExiste_RetornaFalse() {
        // Act
        boolean resultado = juegoRepo.eliminar(999L);

        // Assert
        assertFalse(resultado);
    }
}
