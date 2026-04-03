package controller;

import org.davpen.controller.BibliotecaController;
import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoInstalacion;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.enums.TipoOrden;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.BibliotecaDto;
import org.davpen.modelo.dto.EstadisticasBibliotecaDto;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BibliotecaControllerTest {

    @Mock
    private IBibliotecaRepo bibliotecaRepo;

    @Mock
    private IUsuarioRepo usuarioRepo;

    @Mock
    private IJuegoRepo juegoRepo;

    @Mock
    private ICompraRepo compraRepo;

    @InjectMocks
    private BibliotecaController bibliotecaController;

    private UsuarioEntity usuarioValido;
    private JuegoEntity juegoValido;
    private BibliotecaEntity bibliotecaEntity;
    private BibliotecaForm bibliotecaForm;

    @BeforeEach
    public void setup() {
        usuarioValido = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 100.0, TipoEstadoCuenta.ACTIVA);

        juegoValido = new JuegoEntity(1L, "Game1", "Descripcion", "Developer",
                LocalDate.of(2020, 1, 1), 29.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        bibliotecaEntity = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, LocalDateTime.of(2023, 1, 1, 0, 0),
                TipoEstadoInstalacion.INSTALADO);

        bibliotecaForm = new BibliotecaForm(1L, 1L,
                LocalDate.of(2023, 1, 1), 0.0, null,
                TipoEstadoInstalacion.NO_INSTALADO);
    }

    @Test
    public void testAnhadirJuegoABiblioteca_DatosValidos_RetornaBibliotecaDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(new ArrayList<>());
        when(bibliotecaRepo.crear(any(BibliotecaForm.class))).thenReturn(Optional.of(bibliotecaEntity));

        // Act
        BibliotecaDto resultado = bibliotecaController.anhadirJuegoABiblioteca(1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdBiblio());
        verify(usuarioRepo).obtenerPorId(1L);
        verify(juegoRepo).obtenerPorId(1L);
        verify(bibliotecaRepo).crear(any(BibliotecaForm.class));
    }

    @Test
    public void testAnhadirJuegoABiblioteca_UsuarioNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.anhadirJuegoABiblioteca(999L, 1L));
        verify(usuarioRepo).obtenerPorId(999L);
    }

    @Test
    public void testAnhadirJuegoABiblioteca_JuegoNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.anhadirJuegoABiblioteca(1L, 999L));
    }

    @Test
    public void testAnhadirJuegoABiblioteca_JuegoDuplicado_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaEntity));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.anhadirJuegoABiblioteca(1L, 1L));
    }

    @Test
    public void testEliminarJuegoDeBiblioteca_DatosValidos_RetornaBibliotecaDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaEntity));
        when(bibliotecaRepo.eliminar(1L)).thenReturn(true);

        // Act
        BibliotecaDto resultado = bibliotecaController.eliminarJuegoDeBiblioteca(1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdBiblio());
        verify(bibliotecaRepo).eliminar(1L);
    }

    @Test
    public void testEliminarJuegoDeBiblioteca_EntradaNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.eliminarJuegoDeBiblioteca(1L, 1L));
    }

    @Test
    public void testActualizarTiempoJuego_DatosValidos_RetornaBibliotecaActualizada() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaEntity));

        BibliotecaEntity bibliotecaActualizada = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 15.5, null,
                TipoEstadoInstalacion.INSTALADO);
        when(bibliotecaRepo.actualizar(1L, any(BibliotecaForm.class))).thenReturn(Optional.of(bibliotecaActualizada));

        // Act
        BibliotecaDto resultado = bibliotecaController.actualizarTiempoJuego(1L, 1L, 5.0);

        // Assert
        assertNotNull(resultado);
        verify(bibliotecaRepo).actualizar(eq(1L), any(BibliotecaForm.class));
    }

    @Test
    public void testActualizarTiempoJuego_HorasNegativas_ThrowsValidationException() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaEntity));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.actualizarTiempoJuego(1L, 1L, -5.0));
    }

    @Test
    public void testConsultarUltimaSesion_DatosValidos_RetornaBibliotecaDto() throws ValidationException {
        // Arrange
        BibliotecaEntity conFecha = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, LocalDateTime.of(2024, 3, 15, 0, 0),
                TipoEstadoInstalacion.INSTALADO);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(conFecha));

        // Act
        BibliotecaDto resultado = bibliotecaController.consultarUltimaSesion(1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(LocalDateTime.of(2024, 3, 15, 0, 0), resultado.getUltiFechaJuegoBiblio());
    }

    @Test
    public void testConsultarUltimaSesion_NuncaJugado_ThrowsValidationException() throws ValidationException {
        // Arrange - sin fecha de última sesión
        BibliotecaEntity sinFecha = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 0.0, null,
                TipoEstadoInstalacion.INSTALADO);
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(sinFecha));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.consultarUltimaSesion(1L, 1L));
    }

    @Test
    public void testVerBibliotecaPersonal_OrdenAlfabetico() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaEntity));

        // Act
        List<BibliotecaDto> resultado = bibliotecaController.verBibliotecaPersonal(1L, TipoOrden.ALFABETICO);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    public void testVerBibliotecaPersonal_OrdenTiempoJuego() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioValido));
        List<BibliotecaEntity> bibliotecas = List.of(
                new BibliotecaEntity(1L, 1L, 1L, LocalDate.of(2023, 1, 1), 10.5, LocalDateTime.of(2023, 1, 1, 0, 0),
                        TipoEstadoInstalacion.INSTALADO),
                new BibliotecaEntity(2L, 1L, 2L, LocalDate.of(2023, 1, 1), 20.0, LocalDateTime.of(2023, 1, 1, 0, 0),
                        TipoEstadoInstalacion.INSTALADO)
        );
        when(bibliotecaRepo.obtenerTodos()).thenReturn(bibliotecas);

        // Act
        List<BibliotecaDto> resultado = bibliotecaController.verBibliotecaPersonal(1L, TipoOrden.TIEMPO_JUEGO);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testVerEstadisticasBiblioteca_DatosValidos() {
        // Arrange
        List<BibliotecaEntity> bibliotecas = List.of(
                new BibliotecaEntity(1L, 1L, 1L, LocalDate.of(2023, 1, 1), 10.5, LocalDateTime.of(2024, 3, 15, 0, 0),
                        TipoEstadoInstalacion.INSTALADO),
                new BibliotecaEntity(2L, 1L, 2L, LocalDate.of(2023, 1, 1), 20.0, LocalDateTime.of(2024, 3, 16, 0, 0),
                        TipoEstadoInstalacion.NO_INSTALADO)
        );

        when(bibliotecaRepo.obtenerTodos()).thenReturn(bibliotecas);
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoValido));
        when(juegoRepo.obtenerPorId(2L)).thenReturn(Optional.of(juegoValido));

        // Act
        EstadisticasBibliotecaDto resultado = bibliotecaController.verEstadisticasBiblioteca(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalJuegos());
        assertEquals(30.5, resultado.getHorasTotales());
    }

    @Test
    public void testVerBibliotecaPersonal_UsuarioNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> bibliotecaController.verBibliotecaPersonal(999L, TipoOrden.ALFABETICO));
    }
}
