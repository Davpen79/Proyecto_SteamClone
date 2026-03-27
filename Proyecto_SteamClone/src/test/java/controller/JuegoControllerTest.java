package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.davpen.controller.JuegoController;
import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoConsultaCatalogo;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.JuegoDto;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.JuegoForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.davpen.repositorio.intefaces.IJuegoRepo;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para JuegoController")
class JuegoControllerTest {

    @Mock
    private IJuegoRepo juegoRepo;

    private JuegoController juegoController;

    private JuegoEntity juegoMock;
    private JuegoForm juegoFormMock;
    private JuegoDto juegoDtoMock;

    @BeforeEach
    void setUp() {
        juegoController = new JuegoController(juegoRepo);

        // Crear datos de prueba

        juegoMock = new JuegoEntity(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE
        );

        juegoFormMock = new JuegoForm(
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE
        );

        juegoDtoMock = new JuegoDto(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE
        );
    }

    // ==================== TESTS: anhadirJuego ====================

    @Test
    @DisplayName("Debe añadir un juego válido al catálogo")
    void testAnhadirJuegoValido() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());
        when(juegoRepo.crear(juegoFormMock)).thenReturn(Optional.of(juegoMock));

        // Act
        JuegoDto resultado = juegoController.anhadirJuego(juegoFormMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("The Witcher 3", resultado.getTituloJuego());
        verify(juegoRepo, times(1)).crear(juegoFormMock);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el título ya existe")
    void testAnhadirJuegoDuplicado() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(juegoFormMock));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("titulo") &&
                        e.getMensaje() == ErrorType.DUPLICADO));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el formulario es inválido")
    void testAnhadirJuegoFormularioInvalido() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "",  // título vacío
                "Descripción",
                "Desarrollador",
                LocalDate.now(),
                -10d,  // precio negativo
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Collections.emptyList()),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    // ==================== TESTS: listaJuegosPorCategoria ====================

    @Test
    @DisplayName("Debe retornar juegos filtrados por categoría")
    void testListaJuegosPorCategoria() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Elden Ring", "Descripción",
                "FromSoftware", LocalDate.now(), 59.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Inglés")), TipoEstadoJuego.DISPONIBLE);

        JuegoEntity juego3 = new JuegoEntity(3L, "FIFA 24", "Descripción",
                "EA Sports", LocalDate.now(), 69.99, 0,
                TipoCategoriaJuego.DEPORTES, TipoClasificacionEdades.PEGI_3,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorCategoria(TipoCategoriaJuego.RPG);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(j -> j.getCategoriaJuego() == TipoCategoriaJuego.RPG));
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay juegos en la categoría")
    void testListaJuegosPorCategoriaVacia() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorCategoria(TipoCategoriaJuego.DEPORTES);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ==================== TESTS: listaJuegosPorRangoPrecio ====================

    @Test
    @DisplayName("Debe retornar juegos dentro del rango de precio")
    void testListaJuegosPorRangoPrecio() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Juego Barato", "Descripción",
                "Desarrollador", LocalDate.now(), 19.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_12,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(20.0, 60.0);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("The Witcher 3", resultado.get(0).getTituloJuego());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el precio mínimo es mayor que el máximo")
    void testListaJuegosPorRangoPrecioInvalido() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> juegoController.listaJuegosPorRangoPrecio(100.0, 50.0));

        assertEquals("El precio minimo no puede ser mayor que el precio máximo",
                exception.getMessage());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay juegos en el rango")
    void testListaJuegosPorRangoPrecioVacio() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(10.0, 20.0);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ==================== TESTS: listaJuegosPorClasificacion ====================

    @Test
    @DisplayName("Debe retornar juegos filtrados por clasificación de edad")
    void testListaJuegosPorClasificacion() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Mario Bros", "Descripción",
                "Nintendo", LocalDate.now(), 49.99, 0,
                TipoCategoriaJuego.PLATAFORMAS, TipoClasificacionEdades.PEGI_3,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorClasificacion(TipoClasificacionEdades.PEGI_3);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Mario Bros", resultado.get(0).getTituloJuego());
    }

    // ==================== TESTS: listaJuegosPorEstado ====================

    @Test
    @DisplayName("Debe retornar juegos filtrados por estado")
    void testListaJuegosPorEstado() {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(2L, "Juego Descontinuado", "Descripción",
                "Desarrollador", LocalDate.now(), 49.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.NO_DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juegoNoDisponible));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorEstado(TipoEstadoJuego.DISPONIBLE);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("The Witcher 3", resultado.get(0).getTituloJuego());
    }

    // ==================== TESTS: listaJuegosPorPalabraEnDescripcion ====================

    @Test
    @DisplayName("Debe retornar juegos que contienen la palabra en descripción")
    void testListaJuegosPorPalabraEnDescripcion() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Otro Juego", "Juego de aventura épica",
                "Desarrollador", LocalDate.now(), 49.99, 0,
                TipoCategoriaJuego.AVENTURA, TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("acción");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("The Witcher 3", resultado.get(0).getTituloJuego());
    }

    // ==================== TESTS: listaCatalogoCompleto ====================

    @Test
    @DisplayName("Debe retornar catálogo ordenado alfabéticamente")
    void testListaCatalogoCompletoAlfabetico() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Elden Ring", "Descripción",
                "FromSoftware", LocalDate.now(), 59.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Inglés")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.ALFABETICO);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Elden Ring", resultado.get(0).getTituloJuego());
        assertEquals("The Witcher 3", resultado.get(1).getTituloJuego());
    }

    @Test
    @DisplayName("Debe retornar catálogo ordenado por precio")
    void testListaCatalogoCompletoPrecio() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Juego Barato", "Descripción",
                "Desarrollador", LocalDate.now(), 19.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_12,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.PRECIO);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(19.99, resultado.get(0).getPrecioBaseJuego());
        assertEquals(49.99, resultado.get(1).getPrecioBaseJuego());
    }

    @Test
    @DisplayName("Debe retornar catálogo ordenado por fecha de lanzamiento")
    void testListaCatalogoCompletoFecha() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Juego Nuevo", "Descripción",
                "Desarrollador", LocalDate.of(2024, 1, 1), 49.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_12,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.FECHA);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).getFechaLanzaJuego().isBefore(resultado.get(1).getFechaLanzaJuego()));
    }

    // ==================== TESTS: detalleJuego (continuación) ====================

    @Test
    @DisplayName("Debe lanzar excepción si el juego no existe")
    void testDetalleJuegoNoExistente() {
        // Arrange
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.detalleJuego(999L));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("id") &&
                        e.getMensaje() == ErrorType.NO_ENCONTRADO));
    }

    // ==================== TESTS: aplicarDescuento ====================

    @Test
    @DisplayName("Debe aplicar descuento válido a un juego")
    void testAplicarDescuentoValido() throws ValidationException {
        // Arrange
        int descuento = 20;
        double precioEsperado = 49.99 - (49.99 * 20 / 100); // 39.992

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(new JuegoEntity(
                        1L,
                        "The Witcher 3",
                        "RPG de acción",
                        "CD Projekt Red",
                        LocalDate.of(2015, 5, 19),
                        precioEsperado,
                        descuento,
                        TipoCategoriaJuego.RPG,
                        TipoClasificacionEdades.PEGI_18,
                        new ArrayList<>(Arrays.asList("Español", "Inglés")),
                        TipoEstadoJuego.DISPONIBLE
                )));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, descuento);

        // Assert
        assertNotNull(resultado);
        assertEquals(descuento, resultado.getDescuentoActualJuego());
        assertEquals(precioEsperado, resultado.getPrecioBaseJuego(), 0.01);
        verify(juegoRepo, times(3)).obtenerPorId(1L);
        verify(juegoRepo, times(1)).actualizar(eq(1L), any(JuegoForm.class));
    }

    @Test
    @DisplayName("Debe aplicar descuento mínimo (0%)")
    void testAplicarDescuentoMinimo() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(juegoMock));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, JuegoController.DESCUENTO_MIN);

        // Assert
        assertNotNull(resultado);
        assertEquals(JuegoController.DESCUENTO_MIN, resultado.getDescuentoActualJuego());
    }

    @Test
    @DisplayName("Debe aplicar descuento máximo (100%)")
    void testAplicarDescuentoMaximo() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(new JuegoEntity(
                        1L,
                        "The Witcher 3",
                        "RPG de acción",
                        "CD Projekt Red",
                        LocalDate.of(2015, 5, 19),
                        0.0,
                        100,
                        TipoCategoriaJuego.RPG,
                        TipoClasificacionEdades.PEGI_18,
                        new ArrayList<>(Arrays.asList("Español", "Inglés")),
                        TipoEstadoJuego.DISPONIBLE
                )));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, JuegoController.DESCUENTO_MAX);

        // Assert
        assertNotNull(resultado);
        assertEquals(JuegoController.DESCUENTO_MAX, resultado.getDescuentoActualJuego());
        assertEquals(0.0, resultado.getPrecioBaseJuego());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el juego no existe al aplicar descuento")
    void testAplicarDescuentoJuegoNoExistente() {
        // Arrange
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.aplicarDescuento(999L, 20));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("id") &&
                        e.getMensaje() == ErrorType.NO_ENCONTRADO));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el juego no está disponible")
    void testAplicarDescuentoJuegoNoDisponible() {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.NO_DISPONIBLE
        );

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoNoDisponible));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.aplicarDescuento(1L, 20));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("estado") &&
                        e.getMensaje() == ErrorType.NO_DISPONIBLE));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el descuento es negativo")
    void testAplicarDescuentoNegativo() {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.aplicarDescuento(1L, -5));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("descuento") &&
                        e.getMensaje() == ErrorType.VALOR_NEGATIVO));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el descuento supera el máximo (100%)")
    void testAplicarDescuentoSuperaMaximo() {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.aplicarDescuento(1L, 150));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("descuento") &&
                        e.getMensaje() == ErrorType.VALOR_DEMASIADO_ALTO));
    }

    @Test
    @DisplayName("Debe lanzar excepción con múltiples errores en aplicarDescuento")
    void testAplicarDescuentoMultiplesErrores() {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(
                999L,
                "Juego",
                "Descripción",
                "Desarrollador",
                LocalDate.now(),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.NO_DISPONIBLE
        );

        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.of(juegoNoDisponible));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.aplicarDescuento(999L, 150));

        assertEquals(2, exception.getErrores().size());
        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getMensaje() == ErrorType.NO_DISPONIBLE));
        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getMensaje() == ErrorType.VALOR_DEMASIADO_ALTO));
    }

    // ==================== TESTS: cambiarEstadoJuego ====================

    @Test
    @DisplayName("Debe cambiar el estado de un juego a NO_DISPONIBLE")
    void testCambiarEstadoJuegoANoDisponible() throws ValidationException {
        // Arrange
        JuegoEntity juegoActualizado = new JuegoEntity(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.NO_DISPONIBLE
        );

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(juegoActualizado));

        // Act
        JuegoDto resultado = juegoController.cambiarEstadoJuego(1L, TipoEstadoJuego.NO_DISPONIBLE);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoJuego.NO_DISPONIBLE, resultado.getEstadoJuego());
        verify(juegoRepo, times(1)).obtenerPorId(1L);
        verify(juegoRepo, times(1)).actualizar(eq(1L), any(JuegoForm.class));
    }

    @Test
    @DisplayName("Debe cambiar el estado de un juego a DISPONIBLE")
    void testCambiarEstadoJuegoADisponible() throws ValidationException {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.NO_DISPONIBLE
        );

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoNoDisponible));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(juegoMock));

        // Act
        JuegoDto resultado = juegoController.cambiarEstadoJuego(1L, TipoEstadoJuego.DISPONIBLE);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoJuego.DISPONIBLE, resultado.getEstadoJuego());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el juego no existe al cambiar estado")
    void testCambiarEstadoJuegoNoExistente() {
        // Arrange
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.cambiarEstadoJuego(999L, TipoEstadoJuego.DISPONIBLE));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("id") &&
                        e.getMensaje() == ErrorType.NO_ENCONTRADO));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el estado es inválido")
    void testCambiarEstadoJuegoEstadoInvalido() {
        // Arrange
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));

        // Act & Assert
        // Nota: Este test asume que se valida el estado. Si tu enum no permite null,
        // este test puede no ser necesario. Ajusta según tu implementación.
        ValidationException exception = assertThrows(ValidationException.class,
                () -> juegoController.cambiarEstadoJuego(1L, null));

        assertTrue(exception.getErrores().stream()
                .anyMatch(e -> e.getCampo().equals("estado")));
    }

    @Test
    @DisplayName("Debe mantener otros atributos del juego al cambiar estado")
    void testCambiarEstadoMantienePropiedades() throws ValidationException {
        // Arrange
        JuegoEntity juegoActualizado = new JuegoEntity(
                1L,
                "The Witcher 3",
                "RPG de acción",
                "CD Projekt Red",
                LocalDate.of(2015, 5, 19),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español", "Inglés")),
                TipoEstadoJuego.NO_DISPONIBLE
        );

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(juegoActualizado));

        // Act
        JuegoDto resultado = juegoController.cambiarEstadoJuego(1L, TipoEstadoJuego.NO_DISPONIBLE);

        // Assert
        assertEquals("The Witcher 3", resultado.getTituloJuego());
        assertEquals(49.99, resultado.getPrecioBaseJuego());
        assertEquals("CD Projekt Red", resultado.getDesarrolladorJuego());
        assertEquals(TipoCategoriaJuego.RPG, resultado.getCategoriaJuego());
    }

    // ==================== TESTS: Casos límite y edge cases ====================

    @Test
    @DisplayName("Debe manejar lista vacía de juegos en catálogo")
    void testListaCatalogoCompletoVacio() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act
        List<JuegoDto> resultado = juegoController.listaCatalogoCompleto(TipoConsultaCatalogo.ALFABETICO);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe manejar búsqueda con palabra vacía en descripción")
    void testListaJuegosPorPalabraVacia() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("");

        // Assert
        assertEquals(1, resultado.size()); // Toda cadena contiene ""
    }

    @Test
    @DisplayName("Debe ser sensible a mayúsculas en búsqueda por palabra")
    void testListaJuegosPorPalabraSensibleMayusculas() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultadoMinuscula = juegoController.listaJuegosPorPalabraEnDescripcion("acción");
        List<JuegoDto> resultadoMayuscula = juegoController.listaJuegosPorPalabraEnDescripcion("ACCIÓN");

        // Assert
        assertEquals(1, resultadoMinuscula.size());
        assertEquals(0, resultadoMayuscula.size()); // No encuentra por sensibilidad a mayúsculas
    }

    @Test
    @DisplayName("Debe retornar juegos con palabra parcial en descripción")
    void testListaJuegosPorPalabraParcial() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("acci");

        // Assert
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si ningún juego contiene la palabra")
    void testListaJuegosPorPalabraSinResultados() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("inexistente");

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar múltiples juegos con la misma palabra en descripción")
    void testListaJuegosPorPalabraMultiples() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Dark Souls", "RPG de acción oscuro",
                "FromSoftware", LocalDate.now(), 59.99, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Inglés")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorPalabraEnDescripcion("acción");

        // Assert
        assertEquals(2, resultado.size());
    }

    // ==================== TESTS: Rango de precio con valores límite ====================

    @Test
    @DisplayName("Debe retornar juego cuando precio es igual al mínimo")
    void testListaJuegosPorRangoPrecioIgualMinimo() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(49.99, 60.0);

        // Assert
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe retornar juego cuando precio es igual al máximo")
    void testListaJuegosPorRangoPrecioIgualMaximo() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(40.0, 49.99);

        // Assert
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe retornar juego cuando precio está en el rango exacto")
    void testListaJuegosPorRangoPrecioExacto() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(49.99, 49.99);

        // Assert
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si precio está fuera del rango")
    void testListaJuegosPorRangoPrecioFueraDelRango() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(50.0, 100.0);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar múltiples juegos en rango de precio")
    void testListaJuegosPorRangoPrecioMultiples() {
        // Arrange
        JuegoEntity juego2 = new JuegoEntity(2L, "Juego Medio", "Descripción",
                "Desarrollador", LocalDate.now(), 75.0, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_12,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        JuegoEntity juego3 = new JuegoEntity(3L, "Juego Caro", "Descripción",
                "Desarrollador", LocalDate.now(), 150.0, 0,
                TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_12,
                new ArrayList<>(Arrays.asList("Español")), TipoEstadoJuego.DISPONIBLE);

        when(juegoRepo.obtenerTodos()).thenReturn(Arrays.asList(juegoMock, juego2, juego3));

        // Act
        List<JuegoDto> resultado = juegoController.listaJuegosPorRangoPrecio(40.0, 100.0);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(j -> j.getPrecioBaseJuego() >= 40.0 && j.getPrecioBaseJuego() <= 100.0));
    }

    // ==================== TESTS: Cálculo de descuento ====================

    @Test
    @DisplayName("Debe calcular correctamente el descuento del 50%")
    void testAplicarDescuentoCalculo50Porciento() throws ValidationException {
        // Arrange
        int descuento = 50;
        double precioEsperado = 49.99 * 0.5; // 24.995

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(new JuegoEntity(
                        1L, "The Witcher 3", "RPG de acción", "CD Projekt Red",
                        LocalDate.of(2015, 5, 19), precioEsperado, descuento,
                        TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                        new ArrayList<>(Arrays.asList("Español", "Inglés")),
                        TipoEstadoJuego.DISPONIBLE
                )));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, descuento);

        // Assert
        assertEquals(precioEsperado, resultado.getPrecioBaseJuego(), 0.01);
    }

    @Test
    @DisplayName("Debe calcular correctamente el descuento del 10%")
    void testAplicarDescuentoCalculo10Porciento() throws ValidationException {
        // Arrange
        int descuento = 10;
        double precioEsperado = 49.99 * 0.9; // 44.991

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(new JuegoEntity(
                        1L, "The Witcher 3", "RPG de acción", "CD Projekt Red",
                        LocalDate.of(2015, 5, 19), precioEsperado, descuento,
                        TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                        new ArrayList<>(Arrays.asList("Español", "Inglés")),
                        TipoEstadoJuego.DISPONIBLE
                )));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, descuento);

        // Assert
        assertEquals(precioEsperado, resultado.getPrecioBaseJuego(), 0.01);
    }

    @Test
    @DisplayName("Debe calcular correctamente el descuento del 99%")
    void testAplicarDescuentoCalculo99Porciento() throws ValidationException {
        // Arrange
        int descuento = 99;
        double precioEsperado = 49.99 * 0.01; // 0.4999

        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoMock));
        when(juegoRepo.actualizar(eq(1L), any(JuegoForm.class)))
                .thenReturn(Optional.of(new JuegoEntity(
                        1L, "The Witcher 3", "RPG de acción", "CD Projekt Red",
                        LocalDate.of(2015, 5, 19), precioEsperado, descuento,
                        TipoCategoriaJuego.RPG, TipoClasificacionEdades.PEGI_18,
                        new ArrayList<>(Arrays.asList("Español", "Inglés")),
                        TipoEstadoJuego.DISPONIBLE
                )));

        // Act
        JuegoDto resultado = juegoController.aplicarDescuento(1L, descuento);

        // Assert
        assertEquals(precioEsperado, resultado.getPrecioBaseJuego(), 0.01);
    }

    // ==================== TESTS: Validación de formulario ====================

    @Test
    @DisplayName("Debe validar que el título no esté vacío")
    void testAnhadirJuegoTituloVacio() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "",
                "Descripción válida",
                "Desarrollador",
                LocalDate.now(),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    @Test
    @DisplayName("Debe validar que el precio no sea negativo")
    void testAnhadirJuegoPrecioNegativo() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "Juego válido",
                "Descripción",
                "Desarrollador",
                LocalDate.now(),
                -10.0,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    @Test
    @DisplayName("Debe validar que la descripción no esté vacía")
    void testAnhadirJuegoDescripcionVacia() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "Juego válido",
                "",
                "Desarrollador",
                LocalDate.now(),
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    @Test
    @DisplayName("Debe validar que la fecha de lanzamiento no sea nula")
    void testAnhadirJuegoFechaLanzamientoNula() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "Juego válido",
                "Descripción",
                "Desarrollador",
                null,
                49.99,
                0,
                TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    @Test
    @DisplayName("Debe validar que la categoría no sea nula")
    void testAnhadirJuegoCategoriaInvalida() {
        // Arrange
        JuegoForm formularioInvalido = new JuegoForm(
                "Juego válido",
                "Descripción",
                "Desarrollador",
                LocalDate.now(),
                49.99,
                0,
                null,
                TipoClasificacionEdades.PEGI_18,
                new ArrayList<>(Arrays.asList("Español")),
                TipoEstadoJuego.DISPONIBLE
        );

        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> juegoController.anhadirJuego(formularioInvalido));
    }

    // ==================== TESTS: Interacción con repositorio ====================

    @Test
    @DisplayName("Debe llamar al repositorio una sola vez para obtener todos los juegos")
    void testListaJuegosPorCategoriaRepositorioLlamada() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juegoMock));

        // Act
        juegoController.listaJuegosPorCategoria(TipoCategoriaJuego.RPG);

        // Assert
        verify(juegoRepo, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("Debe llamar al repositorio para crear un juego")
    void testAnhadirJuegoRepositorioCrear() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(Collections.emptyList());
        when(juegoRepo.crear(juegoFormMock)).thenReturn(Optional.of(juegoMock));

        // Act
        juegoController.anhadirJuego(juegoFormMock);

        // Assert
        verify(juegoRepo, times(1)).crear(juegoFormMock);
    }


}
