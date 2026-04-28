package modelo.form;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.modelo.form.CompraForm;
import org.davpen.modelo.form.ErrorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompraFormTest {

    private CompraForm compraForm;

    @BeforeEach
    public void setUp() {
        // Inicializar el objeto CompraForm para las pruebas
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                TipoMetodoPago.CARTERA_STEAM, // tipoPagoCompra
                100.0, // precioBaseCompra
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );
    }

    @Test
    public void testValidar_ConDatosValidos() {
        List<ErrorDto> errores = compraForm.validar();

        // No debe haber errores si todos los datos son válidos
        assertTrue(errores.isEmpty());
    }

    @Test
    public void testValidar_SinIdUsuario() {
        compraForm = new CompraForm(
                null, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                TipoMetodoPago.CARTERA_STEAM, // tipoPagoCompra
                100.0, // precioBaseCompra
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        List<ErrorDto> errores = compraForm.validar();

        // Debe haber un error de "Id_usuario" REQUERIDO
        assertEquals(1, errores.size());
        assertEquals("Id_usuario", errores.get(0).getCampo());
    }

    @Test
    public void testValidar_SinIdJuego() {
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                null, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                TipoMetodoPago.CARTERA_STEAM, // tipoPagoCompra
                100.0, // precioBaseCompra
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        List<ErrorDto> errores = compraForm.validar();

        // Debe haber un error de "Id_juego" REQUERIDO
        assertEquals(1, errores.size());
        assertEquals("Id_juego", errores.getFirst().getCampo());
    }

    @Test
    public void testValidar_SinTipoPago() {
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                null, // tipoPagoCompra
                100.0, // precioBaseCompra
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        List<ErrorDto> errores = compraForm.validar();

        // Debe haber un error de "tipo_pago" REQUERIDO
        assertFalse(errores.isEmpty());
    }

    @Test
    public void testValidar_TipoPagoNoValido() {
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                null, // tipoPagoCompra (nulo en este caso)
                100.0, // precioBaseCompra
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        // Simulamos que no se encuentra un tipoPagoCompra válido
        List<ErrorDto> errores = compraForm.validar();

        // No debería haber un error en este test porque TipoMetodoPago es correcto
        assertFalse(errores.isEmpty());
    }

    @Test
    public void testValidar_PrecioInvalido() {
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                TipoMetodoPago.CARTERA_STEAM, // tipoPagoCompra
                -1.0, // precioBaseCompra (precio negativo)
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        List<ErrorDto> errores = compraForm.validar();

        // Debe haber un error de "precio" VALOR_NEGATIVO
        assertEquals(1, errores.size());
        assertEquals("precio", errores.get(0).getCampo());
    }

    @Test
    public void testValidar_PrecioNulo() {
        compraForm = new CompraForm(
                1L, // idUsuarioCompra
                1L, // idJuegoCompra
                LocalDate.now(), // fechaCompra
                TipoMetodoPago.CARTERA_STEAM, // tipoPagoCompra
                Double.NaN, // precioBaseCompra (valor nulo para precio)
                10, // descuentoEnCompra
                TipoEstadoCompra.COMPLETADA // estadoCompra
        );

        List<ErrorDto> errores = compraForm.validar();

        // Debe haber un error de "precio" REQUERIDO
        assertEquals(1, errores.size());
        assertEquals("precio", errores.get(0).getCampo());
    }
    
}
