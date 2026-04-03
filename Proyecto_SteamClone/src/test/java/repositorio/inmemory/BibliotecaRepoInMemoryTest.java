package repositorio.inmemory;

import org.davpen.enums.TipoEstadoInstalacion;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.repositorio.inmemory.BibliotecaRepoInMemory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaRepoInMemoryTest {

    private BibliotecaRepoInMemory bibliotecaRepo;
    private BibliotecaForm bibliotecaFormValida;
    List<BibliotecaEntity> listaResultado = null;

    @BeforeEach
    public void setUp() {
        bibliotecaRepo = new BibliotecaRepoInMemory();
        bibliotecaFormValida = new BibliotecaForm(1L, 1L, LocalDate.of(2023, 1, 1),
                10.5, null, TipoEstadoInstalacion.INSTALADO);
        listaResultado = null;
    }

    @Test
    public void testCrear_BibliotecaFormValida_RetornaOptionalOfBibliotecaEntity() {
        // Act
        Optional<BibliotecaEntity> resultado = bibliotecaRepo.crear(bibliotecaFormValida);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdUsuarioBiblio());
        assertEquals(1L, resultado.get().getIdJuegoBiblio());
        assertEquals(10.5, resultado.get().getTiempoJuegoBiblio());
    }

    @Test
    public void testObtenerTodos_RetornaListaConTodasLasBibliotecas() {
        // Arrange
        BibliotecaForm form1 = new BibliotecaForm(1L, 1L, LocalDate.of(2023, 1, 1),
                10.5, null, TipoEstadoInstalacion.INSTALADO);
        BibliotecaForm form2 = new BibliotecaForm(1L, 2L, LocalDate.of(2023, 2, 1),
                20.0, null, TipoEstadoInstalacion.NO_INSTALADO);

        // Act
        bibliotecaRepo.crear(form1);
        bibliotecaRepo.crear(form2);
        var listaResultado = bibliotecaRepo.obtenerTodos();

        // Assert
        assertNotNull(listaResultado);
        assertEquals(2, listaResultado.size());
    }

    @Test
    public void testObtenerPorId_IdValido_RetornaOptionalOfBibliotecaEntity() {
        // Arrange
        Optional<BibliotecaEntity> creada = bibliotecaRepo.crear(bibliotecaFormValida);
        Long id = creada.get().getIdBiblio();

        // Act
        Optional<BibliotecaEntity> resultado = bibliotecaRepo.obtenerPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdBiblio());
    }

    @Test
    public void testObtenerPorId_IdNoExiste_RetornaOptionalEmpty() {
        // Act
        Optional<BibliotecaEntity> resultado = bibliotecaRepo.obtenerPorId(999L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testActualizar_DatosValidos_RetornaOptionalOfBibliotecaActualizada() {
        // Arrange
        Optional<BibliotecaEntity> creada = bibliotecaRepo.crear(bibliotecaFormValida);
        Long id = creada.get().getIdBiblio();

        BibliotecaForm formActualizada = new BibliotecaForm(1L, 1L, LocalDate.of(2023, 1, 1),
                50.0, null, TipoEstadoInstalacion.INSTALADO);

        // Act
        Optional<BibliotecaEntity> resultado = bibliotecaRepo.actualizar(id, formActualizada);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(50.0, resultado.get().getTiempoJuegoBiblio());
    }

    @Test
    public void testEliminar_IdValido_RetornaTrue() {
        // Arrange
        Optional<BibliotecaEntity> creada = bibliotecaRepo.crear(bibliotecaFormValida);
        Long id = creada.get().getIdBiblio();

        // Act
        boolean resultado = bibliotecaRepo.eliminar(id);

        // Assert
        assertTrue(resultado);
        assertTrue(bibliotecaRepo.obtenerPorId(id).isEmpty());
    }

    @Test
    public void testEliminar_IdNoExiste_RetornaFalse() {
        // Act
        boolean resultado = bibliotecaRepo.eliminar(999L);

        // Assert
        assertFalse(resultado);
    }
}
