package repositorio.inmemory;

import org.davpen.enums.TipoEstadoResenha;
import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.inmemory.ResenhaInRepoMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ResenhaRepoInMemoryTest {

    private ResenhaInRepoMemory resenhaRepo;
    private ResenhaForm resenhaFormValida;

    @BeforeEach
    public void setUp() {
        resenhaRepo = new ResenhaInRepoMemory();
        resenhaFormValida = new ResenhaForm(1L, 1L, true,
                "Excelente juego, muy recomendado para todos los jugadores",
                20.5, LocalDate.of(2024, 3, 20), null,
                TipoEstadoResenha.PUBLICADA);
    }

    @Test
    public void testCrear_ResenhaFormValida_RetornaOptionalOfResenhaEntity() {
        // Act
        Optional<ResenhaEntity> resultado = resenhaRepo.crear(resenhaFormValida);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdUsuarioResenha());
        assertEquals(1L, resultado.get().getIdJuegoResenha());
        assertTrue(resultado.get().isRecomendacionResenha());
    }

    @Test
    public void testObtenerTodos_RetornaListaConTodasLasResenas() {
        // Arrange
        ResenhaForm form1 = new ResenhaForm(1L, 1L, true,
                "Reseña válida con texto suficiente para pasar validación",
                20.5, LocalDate.of(2024, 3, 20), null,
                TipoEstadoResenha.PUBLICADA);
        ResenhaForm form2 = new ResenhaForm(2L, 2L, false,
                "Otra reseña válida con texto suficiente para la validación",
                5.0, LocalDate.of(2024, 3, 19), null,
                TipoEstadoResenha.PUBLICADA);

        // Act
        resenhaRepo.crear(form1);
        resenhaRepo.crear(form2);
        List<ResenhaEntity> resultado = resenhaRepo.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerPorId_IdValido_RetornaOptionalOfResenhaEntity() {
        // Arrange
        Optional<ResenhaEntity> creada = resenhaRepo.crear(resenhaFormValida);
        Long id = creada.get().getIdResenha();

        // Act
        Optional<ResenhaEntity> resultado = resenhaRepo.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdResenha());
    }

    @Test
    public void testObtenerPorId_IdNoExiste_RetornaOptionalEmpty() {
        // Act
        Optional<ResenhaEntity> resultado = resenhaRepo.obtenerPorId(999L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testObtenerTodasPorIdJuego_JuegoConResenas_RetornaLista() {
        // Arrange
        resenhaRepo.crear(resenhaFormValida);
        List<ResenhaEntity> listaCompleta = resenhaRepo.obtenerTodos();

        // Act
        List<ResenhaEntity> resultado = resenhaRepo.obtenerTodasPorIdJuego(1L, listaCompleta);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdJuegoResenha());
    }

    @Test
    public void testObtenerTodasPorIdUsuario_UsuarioConResenas_RetornaLista() {
        // Arrange
        resenhaRepo.crear(resenhaFormValida);
        List<ResenhaEntity> listaCompleta = resenhaRepo.obtenerTodos();

        // Act
        List<ResenhaEntity> resultado = resenhaRepo.obtenerTodasPorIdUsuario(1L, listaCompleta);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdUsuarioResenha());
    }

    @Test
    public void testActualizar_DatosValidos_RetornaOptionalOfResenhaActualizada() {
        // Arrange
        Optional<ResenhaEntity> creada = resenhaRepo.crear(resenhaFormValida);
        Long id = creada.get().getIdResenha();

        ResenhaForm formActualizada = new ResenhaForm(1L, 1L, false,
                "Reseña modificada con texto suficiente para la validación",
                15.0, LocalDate.of(2024, 3, 20), LocalDate.of(2024, 3, 25),
                TipoEstadoResenha.OCULTA);

        // Act
        Optional<ResenhaEntity> resultado = resenhaRepo.actualizar(id, formActualizada);

        // Assert
        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().isRecomendacionResenha());
        assertEquals(TipoEstadoResenha.OCULTA, resultado.get().getEstadoResenha());
    }

    @Test
    public void testEliminar_IdValido_RetornaTrue() {
        // Arrange
        Optional<ResenhaEntity> creada = resenhaRepo.crear(resenhaFormValida);
        Long id = creada.get().getIdResenha();

        // Act
        boolean resultado = resenhaRepo.eliminar(id);

        // Assert
        assertTrue(resultado);
        assertTrue(resenhaRepo.obtenerPorId(id).isEmpty());
    }

    @Test
    public void testEliminar_IdNoExiste_RetornaFalse() {
        // Act
        boolean resultado = resenhaRepo.eliminar(999L);

        // Assert
        assertFalse(resultado);
    }
}
