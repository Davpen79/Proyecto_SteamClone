package controller;

import org.davpen.controller.ResenhaController;
import org.davpen.enums.*;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.dto.ResenhaDto;
import org.davpen.modelo.entity.BibliotecaEntity;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.entity.ResenhaEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.interfaces.IBibliotecaRepo;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.repositorio.interfaces.IResenhaRepo;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResenhaControllerTest {

    @Mock
    private IResenhaRepo resenhaRepo;

    @Mock
    private IUsuarioRepo usuarioRepo;

    @Mock
    private IJuegoRepo juegoRepo;

    @Mock
    private IBibliotecaRepo bibliotecaRepo;

    @InjectMocks
    private ResenhaController resenhaController;

    private UsuarioEntity usuario;
    private JuegoEntity juego;
    private ResenhaEntity resenhaEntity;
    private ResenhaEntity resenhaEntity2;
    private ResenhaForm resenhaForm;

    @BeforeEach
    public void setup() {
        usuario = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 100.0, TipoEstadoCuenta.ACTIVA);

        juego = new JuegoEntity(1L, "Game1", "Descripcion", "Developer",
                LocalDate.of(2020, 1, 1), 29.99, 0, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español")), TipoEstadoJuego.DISPONIBLE);

        resenhaEntity = new ResenhaEntity(1L, 1L, 1L, true,
                "Excelente juego,Excelente juegoExcelente juegoExcelente juegoExcelente juegoExcelente juegoExcelente" +
                        " juego", 20.5, LocalDate.of(2024, 3, 20),
                null, TipoEstadoResenha.PUBLICADA);

        resenhaEntity2 = new ResenhaEntity(2L, 2L, 1L, false,
                "Mal juego,Mal juegoMal juegoMal juegoMal juegoMal juegoMal juegoMal juegoMal juegoMal juego",
                20.5, LocalDate.of(2024, 3, 22),
                null, TipoEstadoResenha.PUBLICADA);

        resenhaForm = new ResenhaForm(1L, 1L, true,
                "Excelente juego,Excelente juego,Excelente juego,Excelente juego,Excelente juego",
                20.5, LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);
    }

    @Test
    public void testEscribirResena_DatosValidos_RetornaResenaDto() throws ValidationException {

        var bibliotecaValida = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5,
                LocalDateTime.of(2023, 1, 1, 0, 0), TipoEstadoInstalacion.INSTALADO);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juego));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaValida));
        when(resenhaRepo.crear(any())).thenReturn(Optional.of(resenhaEntity));

        ResenhaDto resultado = resenhaController.escribirResenha(resenhaForm);

        assertNotNull(resultado);
    }

    @Test
    public void testEscribirResena_DatosValidos_AssertResenhaPublicada() throws ValidationException {

        var bibliotecaValida = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5,
                LocalDateTime.of(2023, 1, 1, 0, 0), TipoEstadoInstalacion.INSTALADO);

        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juego));
        when(bibliotecaRepo.obtenerTodos()).thenReturn(List.of(bibliotecaValida));
        when(resenhaRepo.crear(any())).thenReturn(Optional.of(resenhaEntity));

        ResenhaDto resultado = resenhaController.escribirResenha(resenhaForm);

        assertNotNull(resultado);
        assertEquals(TipoEstadoResenha.PUBLICADA, resultado.getEstadoResenha());
    }

    @Test
    public void testEscribirResena_UsuarioNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResenhaForm formInvalido = new ResenhaForm(999L, 1L, true, "Excelente juego",
                20.5, LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.escribirResenha(formInvalido));
    }

    @Test
    public void testEscribirResenha_TextoVacio_ThrowsValidationException() {

        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juego));

        ResenhaForm formInvalido = new ResenhaForm(1L, 1L, true, "",
                20.5, LocalDate.of(2024, 3, 20), LocalDate.now(), TipoEstadoResenha.PUBLICADA);

        // Act & Assert
        var exception = assertThrows(ValidationException.class, () ->
                resenhaController.escribirResenha(formInvalido));
        assertEquals(ErrorType.REQUERIDO, exception.getErrores().get(0).getMensaje());
        assertEquals("texto_reseña", exception.getErrores().getFirst().getCampo());
    }

    @Test
    public void testEscribirResenha_TextoDemasiadoGrande_ThrowsValidationException() {

        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        when(juegoRepo.obtenerPorId(1L)).thenReturn(Optional.of(juego));
        var textoGigante = "A".repeat(8100);

        ResenhaForm formInvalido = new ResenhaForm(1L, 1L, true, textoGigante,
                20.5, LocalDate.of(2024, 3, 20), LocalDate.now(), TipoEstadoResenha.PUBLICADA);

        // Act & Assert
        var exception = assertThrows(ValidationException.class, () ->
                resenhaController.escribirResenha(formInvalido));
        assertEquals("texto_reseña", exception.getErrores().getFirst().getCampo());
        assertEquals(ErrorType.DEMASIADO_LARGO, exception.getErrores().getFirst().getMensaje());
    }

    @Test
    public void testEscribirResena_JuegoNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        when(juegoRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResenhaForm formInvalido = new ResenhaForm(1L, 999L, true, "Excelente juego",
                20.5, LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.escribirResenha(formInvalido));
    }

    @Test
    public void testOcultarResena_DatosValidos_RetornaResenaOculta() throws ValidationException {
        // Arrange
        var idResenha = 1L;
        var idUsuario = 1L;
        ResenhaEntity resenhaOculta = new ResenhaEntity(idResenha, 1L, 1L, true,
                "Excelente juego", 20.5, LocalDate.of(2024, 3, 20),
                null, TipoEstadoResenha.OCULTA);
        when(resenhaRepo.obtenerPorId(idResenha)).thenReturn(Optional.of(resenhaEntity));

        List<ResenhaEntity> listaResenhas = new ArrayList<>();
        listaResenhas.add(resenhaEntity);
        when(resenhaRepo.obtenerTodos()).thenReturn(listaResenhas);
        when(resenhaRepo.obtenerTodasPorIdUsuario(idUsuario, listaResenhas)).thenReturn(listaResenhas);

        // Se espera que al actualizar devuelva la entidad con estado OCULTA
        when(resenhaRepo.actualizar(eq(idResenha), any(ResenhaForm.class)))
                .thenReturn(Optional.of(resenhaOculta));

        // Act
        ResenhaDto resultado = resenhaController.ocultarResenha(idResenha, idUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(idResenha, resultado.getIdResenha());
        assertEquals(TipoEstadoResenha.OCULTA, resultado.getEstadoResenha());

        // Verificaciones de interacción
        verify(resenhaRepo).obtenerPorId(idResenha);
        verify(resenhaRepo).obtenerTodos();
        verify(resenhaRepo).obtenerTodasPorIdUsuario(idUsuario, listaResenhas);
        verify(resenhaRepo).actualizar(eq(idResenha), any(ResenhaForm.class));
    }

    @Test
    public void testOcultarResena_ResenaNoExiste_ThrowsValidationException() {
        // Arrange
        when(resenhaRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.ocultarResenha(999L, 1L));
    }

    @Test
    public void testOcultarResena_ResenaNoPertenece_ThrowsValidationException() {
        // Arrange
        when(resenhaRepo.obtenerPorId(1L)).thenReturn(Optional.of(resenhaEntity));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.obtenerTodasPorIdUsuario(999L, List.of(resenhaEntity))).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.ocultarResenha(1L, 999L));
    }

    @Test
    public void testOcultarResena_ResenaNoPublicada_ThrowsValidationException() {
        // Arrange
        ResenhaEntity resenhaOculta = new ResenhaEntity(1L, 1L, 1L, true,
                "Excelente juego", 20.5, LocalDate.of(2024, 3, 20),
                null, TipoEstadoResenha.OCULTA);

        when(resenhaRepo.obtenerPorId(1L)).thenReturn(Optional.of(resenhaOculta));

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.ocultarResenha(1L, 1L));
    }

    @Test
    public void testEliminarResena_DatosValidos_RetornaTrue() throws ValidationException {
        // Arrange
        when(resenhaRepo.obtenerPorId(1L)).thenReturn(Optional.of(resenhaEntity));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.obtenerTodasPorIdUsuario(1L, List.of(resenhaEntity))).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.eliminar(1L)).thenReturn(true);

        // Act
        boolean resultado = resenhaController.eliminarResenha(1L, 1L);

        // Assert
        assertTrue(resultado);
        verify(resenhaRepo).eliminar(1L);
    }

    @Test
    public void testEliminarResena_ResenaNoExiste_ThrowsValidationException() {
        // Arrange
        when(resenhaRepo.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.eliminarResenha(999L, 1L));
    }

    @Test
    public void testEliminarResena_ResenaNoPertenece_ThrowsValidationException() {
        // Arrange
        when(resenhaRepo.obtenerPorId(1L)).thenReturn(Optional.of(resenhaEntity));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.obtenerTodasPorIdUsuario(999L, List.of(resenhaEntity))).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.eliminarResenha(1L, 999L));
    }

    @Test
    public void testVerResenasJuego_JuegoExistenteSinFiltro_RetornaLista() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.obtenerTodasPorIdJuego(1L, List.of(resenhaEntity))).thenReturn(List.of(resenhaEntity));

        // Act
        List<ResenhaDto> resultado = resenhaController.verResenhasJuego(1L, Optional.empty());

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(resenhaRepo).obtenerTodos();
    }

    @Test
    public void testVerResenasJuego_JuegoNoExiste_ThrowsValidationException() {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.verResenhasJuego(999L, Optional.empty()));
    }

    @Test
    public void testVerResenasJuego_SinResenas_RetornaListaVacia() throws ValidationException {
        // Arrange
        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego));
        when(resenhaRepo.obtenerTodos()).thenReturn(new ArrayList<>());
        when(resenhaRepo.obtenerTodasPorIdJuego(1L, new ArrayList<>())).thenReturn(new ArrayList<>());

        // Act
        List<ResenhaDto> resultado = resenhaController.verResenhasJuego(1L, Optional.empty());

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testVerResenhasJuego_JuegoConFiltroRecomendadas_ListaSoloRecomendadas() throws ValidationException {

        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego));
        when(resenhaRepo.obtenerTodasPorIdJuego(1L, new ArrayList<>()))
                .thenReturn(List.of(resenhaEntity, resenhaEntity2));

        List<ResenhaDto> resultado =
                resenhaController.verResenhasJuego(
                        1L,
                        Optional.of(TipoRecomendacionJuego.RECOMENDADO)
                );

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isRecomendacionResenha());
    }

    @Test
    void testVerResenhasJuego_JuegoConFiltroNoRecomendadas_ListaSoloNoRecomendadas() throws ValidationException {

        when(juegoRepo.obtenerTodos()).thenReturn(List.of(juego));
        when(resenhaRepo.obtenerTodasPorIdJuego(1L, new ArrayList<>()))
                .thenReturn(List.of(resenhaEntity, resenhaEntity2));

        List<ResenhaDto> resultado =
                resenhaController.verResenhasJuego(
                        1L,
                        Optional.of(TipoRecomendacionJuego.NO_RECOMENDADO)
                );

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isRecomendacionResenha());
    }

    @Test
    public void testVerResenasUsuario_UsuarioExistente_RetornaLista() throws ValidationException {
        // Arrange
        when(usuarioRepo.obtenerTodos()).thenReturn(List.of(usuario));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity));
        when(resenhaRepo.obtenerTodasPorIdUsuario(1L, List.of(resenhaEntity))).thenReturn(List.of(resenhaEntity));

        // Act
        List<ResenhaDto> resultado = resenhaController.verResenhasUsuario(1L, Optional.empty());

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepo).obtenerTodos();
    }

    @Test
    public void testVerResenasUsuario_UsuarioNoExiste_ThrowsValidationException() {
        // Arrange
        when(usuarioRepo.obtenerTodos()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                resenhaController.verResenhasUsuario(999L, Optional.empty()));
    }

    @Test
    public void testVerResenasUsuario_SoloResenaPublicadas() throws ValidationException {
        // Arrange
        ResenhaEntity resenaOculta = new ResenhaEntity(2L, 1L, 1L, false,
                "Juego malo", 5.0, LocalDate.of(2024, 3, 15),
                null, TipoEstadoResenha.OCULTA);

        when(usuarioRepo.obtenerTodos()).thenReturn(List.of(usuario));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity, resenaOculta));
        when(resenhaRepo.obtenerTodasPorIdUsuario(1L, List.of(resenhaEntity, resenaOculta)))
                .thenReturn(List.of(resenhaEntity, resenaOculta));

        // Act
        List<ResenhaDto> resultado = resenhaController.verResenhasUsuario(1L, Optional.of(TipoEstadoResenha.PUBLICADA));

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testVerResenasUsuario_SoloResenaOcultas() throws ValidationException {
        // Arrange
        ResenhaEntity resenaOculta = new ResenhaEntity(2L, 1L, 1L, false,
                "Juego malo", 5.0, LocalDate.of(2024, 3, 15),
                null, TipoEstadoResenha.OCULTA);

        when(usuarioRepo.obtenerTodos()).thenReturn(List.of(usuario));
        when(resenhaRepo.obtenerTodos()).thenReturn(List.of(resenhaEntity, resenaOculta));
        when(resenhaRepo.obtenerTodasPorIdUsuario(1L, List.of(resenhaEntity, resenaOculta)))
                .thenReturn(List.of(resenhaEntity, resenaOculta));

        // Act
        List<ResenhaDto> resultado = resenhaController.verResenhasUsuario(1L, Optional.of(TipoEstadoResenha.OCULTA));

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}
