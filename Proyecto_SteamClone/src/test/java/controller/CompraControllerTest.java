package controller;

import org.davpen.controller.CompraController;
import org.davpen.enums.*;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.CompraDto;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompraControllerTest {

    @Mock
    private ICompraRepo compraRepo;

    @Mock
    private IUsuarioRepo usuarioRepo;

    @Mock
    private IJuegoRepo juegoRepo;

    @Mock
    private IBibliotecaRepo bibliotecaRepo;


    @InjectMocks
    private CompraController compraController;

    private UsuarioEntity usuarioActivo;
    private JuegoEntity juegoDisponible;
    private CompraEntity compraEntity;

    @BeforeEach
    public void setup() {
        usuarioActivo = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 100.0, TipoEstadoCuenta.ACTIVA);

        juegoDisponible = new JuegoEntity(1L, "Game1", "Descripcion", "Developer",
                LocalDate.of(2020, 1, 1), 29.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        compraEntity = new CompraEntity(1L, 1L, 1L,
                LocalDate.now().minusDays(10), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.COMPLETADA);
    }

    @Test
    public void testRealizarCompra_DatosValidos_RetornaCompraDto() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioActivo));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoDisponible));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(new ArrayList<>());
        when(compraRepo.crear(any())).thenReturn(Optional.of(compraEntity));
        when(compraRepo.obtenerTodos()).thenReturn(List.of(compraEntity));

        // Act
        CompraDto resultado = compraController.realizarCompra(1L, 1L, TipoMetodoPago.CARTERA_STEAM);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCompra());
        verify(compraRepo).crear(any());
    }

    @Test
    public void testRealizarCompra_JuegoDuplicado_NoRealiza() {
        // Arrange
        BibliotecaEntity bibliotecaExistente = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, null,
                TipoEstadoInstalacion.INSTALADO);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioActivo));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoDisponible));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaExistente));

        // Act
        assertThrows(Exception.class, () ->
                compraController.realizarCompra(1L, 1L, TipoMetodoPago.CARTERA_STEAM));
    }

    @Test
    public void testRealizarCompra_JuegoNoDisponible_NoRealiza() {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(2L, "Game2", "Descripcion", "Developer",
                LocalDate.of(2020, 1, 1), 29.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.NO_DISPONIBLE);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioActivo));
        when(juegoRepo.obtenerPorId(2L)).thenReturn(Optional.of(juegoNoDisponible));

        // Act & Assert
        assertThrows(Exception.class, () ->
                compraController.realizarCompra(1L, 2L, TipoMetodoPago.CARTERA_STEAM));
    }

    @Test
    public void testRealizarCompra_SaldoInsuficiente_NoRealiza() {
        // Arrange
        UsuarioEntity usuarioSinSaldo = new UsuarioEntity(2L, "usuario2", "usuario2@mail.com", "pass123",
                "Usuario Dos", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 5.0, TipoEstadoCuenta.ACTIVA);

        JuegoEntity juegoCaroEntity = new JuegoEntity(2L, "Game2", "Descripcion", "Developer",
                LocalDate.of(2020, 1, 1), 50.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        when(usuarioRepo.obtenerPorId(2L)).thenReturn(Optional.of(usuarioSinSaldo));
        when(juegoRepo.obtenerPorId(2L)).thenReturn(Optional.of(juegoCaroEntity));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(Exception.class, () ->
                compraController.realizarCompra(2L, 2L, TipoMetodoPago.CARTERA_STEAM));
    }

    @Test
    public void testConsultarHistorialCompras_DatosValidos_RetornaCompraDto() throws ValidationException {
        // Arrange
        when(compraRepo.obtenerPorId(1L)).thenReturn(Optional.of(compraEntity));

        // Act
        CompraDto resultado = compraController.consultarHistorialCompras(1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCompra());
        verify(compraRepo).obtenerPorId(1L);
    }

    @Test
    public void testConsultarHistorialCompras_CompraNoExiste_ThrowsValidationException() {
        // Arrange
        when(compraRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                compraController.consultarHistorialCompras(999L, 1L));
    }

    @Test
    public void testConsultarHistorialCompras_NoPertenece_ThrowsValidationException() {
        // Arrange
        when(compraRepo.obtenerPorId(1L)).thenReturn(Optional.of(compraEntity));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                compraController.consultarHistorialCompras(1L, 999L));
    }

    @Test
    public void testSolicitarReembolso_DatosValidos_RetornaUsuarioDto() throws ValidationException {
        // Arrange
        BibliotecaEntity bibliotecaBG = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.now().minusDays(7), 1.5, null,
                TipoEstadoInstalacion.INSTALADO);

        when(compraRepo.obtenerPorId(1L)).thenReturn(Optional.of(compraEntity));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaBG));
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioActivo));
        when(usuarioRepo.actualizar(any(), any())).thenReturn(Optional.of(usuarioActivo));

        // Act
        UsuarioDto resultado = compraController.solicitarReembolso(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        verify(usuarioRepo).actualizar(eq(1L), any());
    }

    @Test
    public void testSolicitarReembolso_CompraNoCompletada_ThrowsValidationException() {
        // Arrange
        CompraEntity compraReembolsada = new CompraEntity(2L, 1L, 1L,
                LocalDate.of(2024, 3, 28), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.REEMBOLSADA);

        when(compraRepo.obtenerPorId(2L)).thenReturn(Optional.of(compraReembolsada));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                compraController.solicitarReembolso(2L));
    }

    @Test
    public void testSolicitarReembolso_PlazoSuperado_ThrowsValidationException() {
        // Arrange
        CompraEntity compraAntigua = new CompraEntity(3L, 1L, 1L,
                LocalDate.of(2024, 3, 1), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.COMPLETADA);

        BibliotecaEntity bibliotecaValida = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 1), 0.5, null,
                TipoEstadoInstalacion.INSTALADO);

        when(compraRepo.obtenerPorId(3L)).thenReturn(Optional.of(compraAntigua));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaValida));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                compraController.solicitarReembolso(3L));
    }

    @Test
    public void testSolicitarReembolso_TiempoJugadoSuperado_ThrowsValidationException() throws ValidationException {
        // Arrange
        BibliotecaEntity bibliotecaMuchaTiempo = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 27), 3.5, null,
                TipoEstadoInstalacion.INSTALADO);

        when(compraRepo.obtenerPorId(1L)).thenReturn(Optional.of(compraEntity));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaMuchaTiempo));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                compraController.solicitarReembolso(1L));
    }

    @Test
    void procesarPago_compraNoEncontrada_lanzaValidationException() {
        Long idCompra = 1L;
        when(compraRepo.obtenerPorId(idCompra)).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            compraController.procesarPago(idCompra, TipoMetodoPago.CARTERA_STEAM);
        });

        List<ErrorDto> errores = ex.getErrores();
        assertNotNull(errores);
        assertFalse(errores.isEmpty());
        assertEquals("id", errores.get(0).getCampo());
        assertEquals(ErrorType.NO_ENCONTRADO, errores.get(0).getMensaje());
    }

    @Test
    void procesarPago_carteraSaldoSuficiente_devuelveDto() throws ValidationException {

        Long idCompra = 1L;
        when(compraRepo.obtenerPorId(idCompra)).thenReturn(Optional.of(compraEntity));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juegoDisponible));
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuarioActivo));

        CompraDto expectedDto = new CompraDto(1L, 1L, Optional.empty(), 1L,
                Optional.empty(), LocalDate.now().minusDays(10), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.COMPLETADA);


        CompraDto result = compraController.procesarPago(idCompra, TipoMetodoPago.CARTERA_STEAM);

        assertNotNull(result);
        assertEquals(expectedDto, result);

    }

    @Test
    void procesarPago_carteraSaldoInsuficiente_lanzaValidationException() {
        Long idCompra = 2L;

        var usuarioActivo2 = new UsuarioEntity(2L, "usuario1", "usuario@mail.com",
                "pass123", "Usuario Uno", "España",
                LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1),
                "avatar.png", 10.0, TipoEstadoCuenta.ACTIVA);

        var juegoDisponible2 = new JuegoEntity(2L, "Game1", "Descripcion",
                "Developer", LocalDate.of(2020, 1, 1), 29.99,
                0, TipoCategoriaJuego.ACCION, TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        var compraEntity2 = new CompraEntity(2L, 2L, 2L,
                LocalDate.now().minusDays(10), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.COMPLETADA);

        when(compraRepo.obtenerPorId(idCompra)).thenReturn(Optional.of(compraEntity2));
        when(juegoRepo.obtenerPorId(2L)).thenReturn(Optional.of(juegoDisponible2));
        when(usuarioRepo.obtenerPorId(2L)).thenReturn(Optional.of(usuarioActivo2));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            compraController.procesarPago(idCompra, TipoMetodoPago.CARTERA_STEAM);
        });

        List<ErrorDto> errores = ex.getErrores();
        assertNotNull(errores);
        assertFalse(errores.isEmpty());
        assertEquals("saldo_cartera", errores.get(0).getCampo());
        assertEquals(ErrorType.SALDO_INSUFICIENTE, errores.get(0).getMensaje());
    }
}
