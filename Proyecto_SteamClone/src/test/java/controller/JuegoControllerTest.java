package controller;

import org.davpen.controller.JuegoController;
import org.davpen.enums.*;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.JuegoDto;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JuegoControllerTest {

    @Mock
    private IJuegoRepo juegoRepo;

    @InjectMocks
    private JuegoController juegoController;

    private JuegoEntity juego1;
    private JuegoEntity juego2;
    private JuegoEntity juego3;
    private JuegoForm juegoForm;

    @BeforeEach
    public void setup() {
        juego1 = new JuegoEntity(1L, "Elden Ring", "RPG de acción épico", "FromSoftware",
                LocalDate.of(2022, 2, 25), 59.99, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.DISPONIBLE);

        juego2 = new JuegoEntity(2L, "Hades", "Roguelike indie", "Supergiant Games",
                LocalDate.of(2020, 9, 17), 24.99, 10, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_12, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.DISPONIBLE);

        juego3 = new JuegoEntity(3L, "Chess Game", "Ajedrez online", "Chess Dev",
                LocalDate.of(2019, 5, 10), 0.00, 0, TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_3, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.NO_DISPONIBLE);

        juegoForm = new JuegoForm("New Game", "Descripcion nueva", "Developer",
                LocalDate.of(2024, 1, 1), 29.99, 0, TipoCategoriaJuego.AVENTURA,
                TipoClasificacionEdades.PEGI_18, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);
    }

    @Test
    public void testAnhadirJuego_DatosValidos_RetornaJuegoDto() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2));
        when(juegoRepo.crear(any(JuegoForm.class))).thenReturn(Optional.of(juego1));

        // Act
        JuegoDto resultado = juegoController.anhadirJuego(juegoForm);

        // Assert
        assertNotNull(resultado);
        verify(juegoRepo).crear(any(JuegoForm.class));
    }

    @Test
    public void testAnhadirJuego_JuegoDuplicado_ThrowsValidationException() {
        // Arrange
        JuegoForm formDuplicado = new JuegoForm("Elden Ring", "RPG diferente", "Developer",
                LocalDate.of(2024, 1, 1), 29.99, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            juegoController.anhadirJuego(formDuplicado));
    }

    @Test
    public void testListaJuegosPorCategoria_CategoriaRPG() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorCategoria(TipoCategoriaJuego.RPG);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Elden Ring", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testListaJuegosPorCategoria_CategoriaVacia() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorCategoria(TipoCategoriaJuego.SIMULACION);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testListaJuegosPorRangoPrecio_RangoValido() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(20.0, 30.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hades", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testListaJuegosPorRangoPrecio_RangoCompleto() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(0.0, 100.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
    }

    @Test
    public void testListaJuegosPorRangoPrecio_PrecioMinMayorQueMax_ThrowsException() {
        // Arrange
        //when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            juegoController.listaJuegosPorRangoPrecio(100.0, 50.0));
    }

    @Test
    public void testListaJuegosPorClasificacion_Pegi16() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorClasificacion(TipoClasificacionEdades.PEGI_16);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Elden Ring", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testListaJuegosPorEstado_Disponibles() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorEstado(TipoEstadoJuego.DISPONIBLE);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testListaJuegosPorEstado_NoDisponibles() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorEstado(TipoEstadoJuego.NO_DISPONIBLE);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Chess Game", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testListaJuegosPorPalabraEnDescripcion_PalabraEncontrada() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("indie");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hades", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testListaJuegosPorPalabraEnDescripcion_PalabraNoEncontrada() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("FPS");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testListaCatalogoCompleto_OrdenAlfabetico() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.ALFABETICO);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Chess Game", resultado.get(0).getTituloJuego());
        assertEquals("Elden Ring", resultado.get(1).getTituloJuego());
        assertEquals("Hades", resultado.get(2).getTituloJuego());
    }

    @Test
    public void testListaCatalogoCompleto_OrdenPrecio() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.PRECIO);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Chess Game", resultado.get(0).getTituloJuego());
        assertEquals("Hades", resultado.get(1).getTituloJuego());
    }

    @Test
    public void testListaCatalogoCompleto_OrdenFecha() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego1, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.FECHA);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Chess Game", resultado.get(0).getTituloJuego());
    }

    @Test
    public void testDetalleJuego_JuegoExistente() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juego1));

        // Act
        JuegoDto resultado = juegoController.detalleJuego(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Elden Ring", resultado.getTituloJuego());
        verify(juegoRepo).obtenerPorId(1L);
    }

    @Test
    public void testDetalleJuego_JuegoNoExistente_ThrowsValidationException() {
        // Arrange
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
            juegoController.detalleJuego(999L));
    }
}
