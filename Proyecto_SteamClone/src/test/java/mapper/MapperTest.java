package mapper;

import org.davpen.enums.*;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.*;
import org.davpen.modelo.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapperTest {

    private UsuarioEntity usuarioEntity;
    private JuegoEntity juegoEntity;
    private BibliotecaEntity bibliotecaEntity;
    private CompraEntity compraEntity;
    private ResenhaEntity resenhaEntity;

    @BeforeEach
    public void setUp() {
        usuarioEntity = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 100.0, TipoEstadoCuenta.ACTIVA);

        juegoEntity = new JuegoEntity(1L, "Elden Ring", "RPG de acción épico", "FromSoftware",
                LocalDate.of(2022, 2, 25), 59.99, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.DISPONIBLE);

        bibliotecaEntity = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, null,
                TipoEstadoInstalacion.INSTALADO);

        compraEntity = new CompraEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 28), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.COMPLETADA);

        resenhaEntity = new ResenhaEntity(1L, 1L, 1L, true,
                "Excelente juego, muy recomendado", 20.5,
                LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);
    }

    // ============ UsuarioDto Tests ============

    @Test
    public void testMapaUsuarioCompleto_UsuarioValido_RetornaUsuarioDto() {
        // Act
        UsuarioDto resultado = Mapper.mapaUsuarioCompleto(usuarioEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("usuario1", resultado.getNombreCuentaUsuario());
        assertEquals("usuario@mail.com", resultado.getEmailUsuario());
        assertEquals("Usuario Uno", resultado.getNombreRealUsuario());
        assertEquals("España", resultado.getPaisUsuario());
        assertEquals(LocalDate.of(1990, 1, 1), resultado.getFechaNacUsuario());
        assertEquals(LocalDate.of(2020, 1, 1), resultado.getFechaRegUsuario());
        assertEquals("avatar.png", resultado.getAvatarUsuario());
        assertEquals(100.0, resultado.getSaldoUsuario());
        assertEquals(TipoEstadoCuenta.ACTIVA, resultado.getEstadoCuentaUsuario());
    }

    @Test
    public void testMapaUsuarioCompleto_UsuarioNull_RetornaNull() {
        // Act
        UsuarioDto resultado = Mapper.mapaUsuarioCompleto(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    public void testMapaUsuarioCompleto_UsuarioSuspendido_RetornaUsuarioDtoConEstadoSuspendido() {
        // Arrange
        UsuarioEntity usuarioSuspendido = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 100.0, TipoEstadoCuenta.SUSPENDIDA);

        // Act
        UsuarioDto resultado = Mapper.mapaUsuarioCompleto(usuarioSuspendido);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoCuenta.SUSPENDIDA, resultado.getEstadoCuentaUsuario());
    }

    @Test
    public void testMapaUsuarioCompleto_UsuarioConSaldoAlto_RetornaUsuarioDtoConSaldoAlto() {
        // Arrange
        UsuarioEntity usuarioAltoSaldo = new UsuarioEntity(1L, "usuario1", "usuario@mail.com", "pass123",
                "Usuario Uno", "España", LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1), "avatar.png", 500.50, TipoEstadoCuenta.ACTIVA);

        // Act
        UsuarioDto resultado = Mapper.mapaUsuarioCompleto(usuarioAltoSaldo);

        // Assert
        assertNotNull(resultado);
        assertEquals(500.50, resultado.getSaldoUsuario());
    }

    // ============ JuegoDto Tests ============

    @Test
    public void testMapaJuegoCompleto_JuegoValido_RetornaJuegoDto() {
        // Act
        JuegoDto resultado = Mapper.mapaJuegoCompleto(juegoEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdJuego());
        assertEquals("Elden Ring", resultado.getTituloJuego());
        assertEquals("RPG de acción épico", resultado.getDescripcionJuego());
        assertEquals(59.99, resultado.getPrecioBaseJuego());
        assertEquals(LocalDate.of(2022, 2, 25), resultado.getFechaLanzaJuego());
        assertEquals(TipoCategoriaJuego.RPG, resultado.getCategoriaJuego());
        assertEquals(TipoClasificacionEdades.PEGI_16, resultado.getClasEdadJuego());
        assertEquals(TipoEstadoJuego.DISPONIBLE, resultado.getEstadoJuego());
        assertEquals(0.0, resultado.getDescuentoActualJuego());
    }

    @Test
    public void testMapaJuegoCompleto_JuegoNull_RetornaNull() {
        // Act
        JuegoDto resultado = Mapper.mapaJuegoCompleto(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    public void testMapaJuegoCompleto_JuegoConDescuento_RetornaJuegoDtoConDescuento() {
        // Arrange
        JuegoEntity juegoConDescuento = new JuegoEntity(1L, "Elden Ring", "RPG de acción épico", "FromSoftware",
                LocalDate.of(2022, 2, 25), 59.99, 20, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.DISPONIBLE);

        // Act
        JuegoDto resultado = Mapper.mapaJuegoCompleto(juegoConDescuento);

        // Assert
        assertNotNull(resultado);
        assertEquals(20, resultado.getDescuentoActualJuego());
    }

    @Test
    public void testMapaJuegoCompleto_JuegoNoDisponible_RetornaJuegoDtoConEstadoNoDisponible() {
        // Arrange
        JuegoEntity juegoNoDisponible = new JuegoEntity(1L, "Elden Ring", "RPG de acción épico", "FromSoftware",
                LocalDate.of(2022, 2, 25), 59.99, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.NO_DISPONIBLE);

        // Act
        JuegoDto resultado = Mapper.mapaJuegoCompleto(juegoNoDisponible);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoJuego.NO_DISPONIBLE, resultado.getEstadoJuego());
    }

    @Test
    public void testMapaJuegoCompleto_JuegoConIdiomasMultiples_RetornaJuegoDtoConIdiomas() {
        // Act
        JuegoDto resultado = Mapper.mapaJuegoCompleto(juegoEntity);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getIdiomasJuego());
        assertEquals(2, resultado.getIdiomasJuego().size());
        assertTrue(resultado.getIdiomasJuego().contains("Español"));
        assertTrue(resultado.getIdiomasJuego().contains("Inglés"));
    }

    // ============ BibliotecaDto Tests ============

    @Test
    public void testMapaSimpleBiblioteca_BibliotecaValida_RetornaBibliotecaDto() {
        // Act
        BibliotecaDto resultado = Mapper.mapaSimple(bibliotecaEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdBiblio());
        assertEquals(1L, resultado.getIdUsuarioBiblio());
        assertEquals(1L, resultado.getIdJuegoBiblio());
        assertEquals(LocalDate.of(2023, 1, 1), resultado.getFechaCompraJuegoBiblio());
        assertEquals(10.5, resultado.getTiempoJuegoBiblio());
        assertEquals(TipoEstadoInstalacion.INSTALADO, resultado.getEstadoInstJuegoBiblio());
    }

    @Test
    public void testMapaSimpleBiblioteca_BibliotecaNull_RetornaNull() {
        // Act
        BibliotecaDto resultado = Mapper.mapaSimple((BibliotecaEntity) null);

        // Assert
        assertNull(resultado);
    }

    @Test
    public void testMapaSimpleBiblioteca_BibliotecaNoInstalada_RetornaBibliotecaDtoNoInstalada() {
        // Arrange
        BibliotecaEntity bibliotecaNoInstalada = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, null,
                TipoEstadoInstalacion.NO_INSTALADO);

        // Act
        BibliotecaDto resultado = Mapper.mapaSimple(bibliotecaNoInstalada);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoInstalacion.NO_INSTALADO, resultado.getEstadoInstJuegoBiblio());
    }

    @Test
    public void testMapaSimpleBiblioteca_BibliotecaConTiempoJugado_RetornaBibliotecaDtoConTiempoJugado() {
        // Arrange
        BibliotecaEntity bibliotecaTiempo = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 50.75, null,
                TipoEstadoInstalacion.INSTALADO);

        // Act
        BibliotecaDto resultado = Mapper.mapaSimple(bibliotecaTiempo);

        // Assert
        assertNotNull(resultado);
        assertEquals(50.75, resultado.getTiempoJuegoBiblio());
    }

    @Test
    public void testMapaSimpleBiblioteca_BibliotecaConUltimaFecha_RetornaBibliotecaDtoConUltimaFecha() {
        // Arrange
        BibliotecaEntity bibliotecaConFecha = new BibliotecaEntity(1L, 1L, 1L,
                LocalDate.of(2023, 1, 1), 10.5, java.time.LocalDateTime.of(2024, 3, 20, 0, 0),
                TipoEstadoInstalacion.INSTALADO);

        // Act
        BibliotecaDto resultado = Mapper.mapaSimple(bibliotecaConFecha);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getUltiFechaJuegoBiblio());
    }

    // ============ CompraDto Tests ============

    @Test
    public void testMapaSimpleCompra_CompraValida_RetornaCompraDto() {
        // Act
        CompraDto resultado = Mapper.mapaCompraSimple(compraEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCompra());
        assertEquals(1L, resultado.getIdUsuarioCompra());
        assertEquals(1L, resultado.getIdJuegoCompra());
        assertEquals(LocalDate.of(2024, 3, 28), resultado.getFechaCompra());
        assertEquals(TipoMetodoPago.CARTERA_STEAM, resultado.getTipoPagoCompra());
        assertEquals(29.99, resultado.getPrecioBaseCompra());
        assertEquals(0.0, resultado.getDescuentoEnCompra());
        assertEquals(TipoEstadoCompra.COMPLETADA, resultado.getEstadoCompra());
    }

    @Test
    public void testMapaSimpleCompra_CompraNull_RetornaNull() {
        // Act
        CompraDto resultado = Mapper.mapaCompraSimple((CompraEntity) null);

        // Assert
        assertNull(resultado);
    }

    @Test
    public void testMapaSimpleCompra_CompraConMetodoPagoPayPal_RetornaCompraDtoConPayPal() {
        // Arrange
        CompraEntity compraPayPal = new CompraEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 28), TipoMetodoPago.PAYPAL,
                29.99, 0, TipoEstadoCompra.COMPLETADA);

        // Act
        CompraDto resultado = Mapper.mapaCompraSimple(compraPayPal);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoMetodoPago.PAYPAL, resultado.getTipoPagoCompra());
    }

    @Test
    public void testMapaSimpleCompra_CompraConDescuento_RetornaCompraDtoConDescuento() {
        // Arrange
        CompraEntity compraConDescuento = new CompraEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 28), TipoMetodoPago.CARTERA_STEAM,
                29.99, 15, TipoEstadoCompra.COMPLETADA);

        // Act
        CompraDto resultado = Mapper.mapaCompraSimple(compraConDescuento);

        // Assert
        assertNotNull(resultado);
        assertEquals(15, resultado.getDescuentoEnCompra());
    }

    @Test
    public void testMapaSimpleCompra_CompraReembolsada_RetornaCompraDtoReembolsada() {
        // Arrange
        CompraEntity compraReembolsada = new CompraEntity(1L, 1L, 1L,
                LocalDate.of(2024, 3, 28), TipoMetodoPago.CARTERA_STEAM,
                29.99, 0, TipoEstadoCompra.REEMBOLSADA);

        // Act
        CompraDto resultado = Mapper.mapaCompraSimple(compraReembolsada);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoCompra.REEMBOLSADA, resultado.getEstadoCompra());
    }

    // ============ ResenhaDto Tests ============

    @Test
    public void testMapaSimpleResena_ResenaValida_RetornaResenaDto() {
        // Act
        ResenhaDto resultado = Mapper.mapaSimple(resenhaEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdResenha());
        assertEquals(1L, resultado.getIdUsuarioResenha());
        assertEquals(1L, resultado.getIdJuegoResenha());
        assertTrue(resultado.isRecomendacionResenha());
        assertEquals("Excelente juego, muy recomendado", resultado.getTextoResenha());
        assertEquals(20.5, resultado.getTiempoJugadoResenha());
        assertEquals(LocalDate.of(2024, 3, 20), resultado.getFechaPublicacionResenha());
        assertEquals(TipoEstadoResenha.PUBLICADA, resultado.getEstadoResenha());
    }

    @Test
    public void testMapaSimpleResenha_ResenhaValida_RetornaResenhaDto() {
        // Act
        ResenhaDto resultado = Mapper.mapaSimple((ResenhaEntity) null);

        // Assert
        assertNull(resultado);
    }

    @Test
    public void testMapaSimpleResena_ResenaNoRecomendada_RetornaResenaDtoNoRecomendada() {
        // Arrange
        ResenhaEntity resenhaNoRecomendada = new ResenhaEntity(1L, 1L, 1L, false,
                "No me gust\u00f3", 5.0,
                LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);

        // Act
        ResenhaDto resultado = Mapper.mapaSimple(resenhaNoRecomendada);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isRecomendacionResenha());
    }

    @Test
    public void testMapaSimpleResena_ResenaOculta_RetornaResenaDtoOculta() {
        // Arrange
        ResenhaEntity resenhaOculta = new ResenhaEntity(1L, 1L, 1L, true,
                "Excelente juego, muy recomendado", 20.5,
                LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.OCULTA);

        // Act
        ResenhaDto resultado = Mapper.mapaSimple(resenhaOculta);

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoEstadoResenha.OCULTA, resultado.getEstadoResenha());
    }

    @Test
    public void testMapaSimpleResena_ResenaConEdicion_RetornaResenaDtoConFechaEdicion() {
        // Arrange
        ResenhaEntity resenhaConFecha = new ResenhaEntity(1L, 1L, 1L, true,
                "Excelente juego, muy recomendado", 20.5,
                LocalDate.of(2024, 3, 20), LocalDate.of(2024, 3, 25), TipoEstadoResenha.PUBLICADA);

        // Act
        ResenhaDto resultado = Mapper.mapaSimple(resenhaConFecha);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getFechaUltiEdicResenha());
    }

    @Test
    public void testMapaSimpleResena_ResenaDeTiempoNegativo_RetornaResenaDtoConTiempoNegativo() {
        // Arrange
        // Note: tiempoJugado cannot be negative, but testing with valid data
        ResenhaEntity resenhaBaja = new ResenhaEntity(1L, 1L, 1L, false,
                "No es para m\u00ed", 1.0,
                LocalDate.of(2024, 3, 20), null, TipoEstadoResenha.PUBLICADA);

        // Act
        ResenhaDto resultado = Mapper.mapaSimple(resenhaBaja);

        // Assert
        assertNotNull(resultado);
        assertEquals(1.0, resultado.getTiempoJugadoResenha());
    }

    // ============ Casos de Edición ============

    @Test
    public void testMapaUsuarioCompleto_MultiplesConversiones_ConservaIntegridad() {
        // Arrange
        UsuarioEntity usuario1 = usuarioEntity;
        UsuarioEntity usuario2 = new UsuarioEntity(2L, "usuario2", "usuario2@mail.com", "pass456",
                "Usuario Dos", "Portugal", LocalDate.of(1985, 5, 5),
                LocalDate.of(2021, 1, 1), "avatar2.png", 250.0, TipoEstadoCuenta.ACTIVA);

        // Act
        UsuarioDto dto1 = Mapper.mapaUsuarioCompleto(usuario1);
        UsuarioDto dto2 = Mapper.mapaUsuarioCompleto(usuario2);

        // Assert
        assertNotEquals(dto1.getIdUsuario(), dto2.getIdUsuario());
        assertNotEquals(dto1.getNombreCuentaUsuario(), dto2.getNombreCuentaUsuario());
        assertNotEquals(dto1.getSaldoUsuario(), dto2.getSaldoUsuario());
    }

    @Test
    public void testMapaJuegoCompleto_MultiplesConversiones_ConservaIntegridad() {
        // Arrange
        JuegoEntity juego1 = juegoEntity;
        JuegoEntity juego2 = new JuegoEntity(2L, "Hades", "Roguelike indie", "Supergiant Games",
                LocalDate.of(2020, 9, 17), 24.99, 10, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_12, new ArrayList<>(List.of("Español", "Inglés")), TipoEstadoJuego.DISPONIBLE);

        // Act
        JuegoDto dto1 = Mapper.mapaJuegoCompleto(juego1);
        JuegoDto dto2 = Mapper.mapaJuegoCompleto(juego2);

        // Assert
        assertNotEquals(dto1.getTituloJuego(), dto2.getTituloJuego());
        assertNotEquals(dto1.getPrecioBaseJuego(), dto2.getPrecioBaseJuego());
        assertNotEquals(dto1.getCategoriaJuego(), dto2.getCategoriaJuego());
    }
}
