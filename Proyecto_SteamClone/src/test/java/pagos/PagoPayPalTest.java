package pagos;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.excepciones.ValidationException;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.entity.UsuarioEntity;
import org.davpen.pagos.PagoPayPal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagoPayPalTest {

    @Test
    public void testProcesarPagoPaypal_ResultTrue() throws ValidationException {

        UsuarioEntity usuario = new UsuarioEntity(1L,"usuario1","usuario1@mail.com",
                "passUsuario1","Tony Montana",
                "España", LocalDate.of(2000,10,10),LocalDate.of(2020,1,6),
                "avatar1", 100d, TipoEstadoCuenta.ACTIVA);

        CompraEntity compra = new CompraEntity(1L, 1L,1L,
                LocalDate.of(2025,1,2), TipoMetodoPago.CARTERA_STEAM, 15d, 0,
                TipoEstadoCompra.COMPLETADA); // rellenar solo si es necesario por la interfaz
        Double precioFinal = 15d;

        PagoPayPal pago = new PagoPayPal();

        boolean resultado = pago.procesarPago(compra, usuario, precioFinal);

        assertTrue(resultado);
    }

}
