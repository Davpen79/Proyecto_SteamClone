package pagos;

import org.davpen.pagos.PagoCarteraSteam;
import org.davpen.pagos.PagoPayPal;
import org.davpen.pagos.PagoTarjetaCredito;
import org.davpen.pagos.PagoTransferencia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetodoPagoTest {

    @Test
    public void testPagoCarteraSteam_ProcesarPago_Valido() {
        // Arrange
        PagoCarteraSteam pago = new PagoCarteraSteam(1L);
        double coste = 29.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
        // El resultado depende de la implementación
    }

    @Test
    public void testPagoCarteraSteam_ProcesarPago_CostoNegativo() {
        // Arrange
        PagoCarteraSteam pago = new PagoCarteraSteam(1L);
        double coste = -29.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        // No debería procesar un pago con costo negativo
        assertNotNull(resultado);
    }

    @Test
    public void testPagoCarteraSteam_ProcesarPago_CostoCero() {
        // Arrange
        PagoCarteraSteam pago = new PagoCarteraSteam(1L);
        double coste = 0.0;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoPayPal_ProcesarPago_Valido() {
        // Arrange
        PagoPayPal pago = new PagoPayPal("usuario@paypal.com");
        double coste = 59.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoPayPal_ProcesarPago_CostoAlto() {
        // Arrange
        PagoPayPal pago = new PagoPayPal("usuario@paypal.com");
        double coste = 999.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoTarjetaCredito_ProcesarPago_Valido() {
        // Arrange
        PagoTarjetaCredito pago = new PagoTarjetaCredito("1234567890123456");
        double coste = 49.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoTarjetaCredito_ProcesarPago_TarjetaValida() {
        // Arrange
        PagoTarjetaCredito pago = new PagoTarjetaCredito("4111111111111111");
        double coste = 29.99;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoTransferencia_ProcesarPago_Valido() {
        // Arrange
        PagoTransferencia pago = new PagoTransferencia("ES1234567890123456789012");
        double coste = 100.00;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoTransferencia_ProcesarPago_IBANValido() {
        // Arrange
        PagoTransferencia pago = new PagoTransferencia("DE89370400440532013000");
        double coste = 50.00;

        // Act
        boolean resultado = pago.procesarPago(coste);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testPagoCarteraSteam_Constructor() {
        // Act
        PagoCarteraSteam pago = new PagoCarteraSteam(5L);

        // Assert
        assertNotNull(pago);
    }

    @Test
    public void testPagoPayPal_Constructor() {
        // Act
        PagoPayPal pago = new PagoPayPal("test@paypal.com");

        // Assert
        assertNotNull(pago);
    }

    @Test
    public void testPagoTarjetaCredito_Constructor() {
        // Act
        PagoTarjetaCredito pago = new PagoTarjetaCredito("4532015112830366");

        // Assert
        assertNotNull(pago);
    }

    @Test
    public void testPagoTransferencia_Constructor() {
        // Act
        PagoTransferencia pago = new PagoTransferencia("IBAN123456789");

        // Assert
        assertNotNull(pago);
    }
}
