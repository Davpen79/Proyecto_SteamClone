package pagos;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.pagos.PagoCarteraSteam;
import org.davpen.repositorio.interfaces.ICompraRepo;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class PagoCarteraSteamTest {

    private IUsuarioRepo usuarioRepo;
    private Long idUsuario = 42L;
    private PagoCarteraSteam plataformaPago;
    private ICompraRepo compraRepo;

    @BeforeEach
    void setUp() {
        usuarioRepo = mock(IUsuarioRepo.class);
        compraRepo = mock(ICompraRepo.class);
        plataformaPago = new PagoCarteraSteam(idUsuario, usuarioRepo, compraRepo);
    }

    @Test
    void procesarPago_conSaldoSuficiente_actualizaUsuarioYRetornaTrue() throws ValidationException {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity(1L,"usuario1","usuario1@mail.com",
                "passUsuario1","Tony Montana",
                "España", LocalDate.of(2000,10,10),LocalDate.of(2020,1,6),
                 "avatar1", 100d, TipoEstadoCuenta.ACTIVA);

        CompraEntity compra = new CompraEntity(1L, 1L,1L,
                LocalDate.of(2025,1,2), TipoMetodoPago.CARTERA_STEAM, 15d, 0,
                TipoEstadoCompra.COMPLETADA); // rellenar solo si es necesario por la interfaz
        Double precioFinal = 15d;

        // Act
        boolean resultado = plataformaPago.procesarPago(compra, usuario, precioFinal);

        // Assert
        assertTrue(resultado);

        ArgumentCaptor<UsuarioForm> captor = ArgumentCaptor.forClass(UsuarioForm.class);
        verify(usuarioRepo, times(1)).actualizar(eq(idUsuario), captor.capture());
        UsuarioForm actualizado = captor.getValue();
        assertEquals(85.0, actualizado.getSaldoUsuario()); // 100 - 15 = 85
        // comprobar que otros campos se conservan
        assertEquals(usuario.getNombreCuentaUsuario(), actualizado.getNombreCuentaUsuario());
        assertEquals(usuario.getEmailUsuario(), actualizado.getEmailUsuario());
    }

    @Test
    void procesarPago_conSaldoInsuficiente_lanzaValidationExceptionConErrorDeSaldo() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity(1L,"usuario1","usuario1@mail.com",
                "passUsuario1","Tony Montana",
                "España", LocalDate.of(2000,10,10),LocalDate.of(2020,1,6),
                "avatar1", 10d, TipoEstadoCuenta.ACTIVA);

        CompraEntity compra = new CompraEntity(1L, 1L,1L,
                LocalDate.of(2025,1,2), TipoMetodoPago.CARTERA_STEAM, 15d, 0,
                TipoEstadoCompra.COMPLETADA); // rellenar solo si es necesario por la interfaz
        Double precioFinal = 15d;

        // Act & Assert
        ValidationException ex = assertThrows(ValidationException.class,
                () -> plataformaPago.procesarPago(compra, usuario, precioFinal));


        List<ErrorDto> errores = ex.getErrores();
        assertNotNull(errores);
        assertFalse(errores.isEmpty());
        ErrorDto primer = errores.get(0);
        assertEquals("saldo_cartera", primer.getCampo());
        assertEquals(ErrorType.SALDO_INSUFICIENTE, primer.getMensaje());

        verify(usuarioRepo, never()).actualizar(anyLong(), any());
    }

}
