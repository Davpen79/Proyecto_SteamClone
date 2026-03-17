package modelo.form;

import org.davpen.enums.TipoEstadoInstalacion;
import org.davpen.modelo.form.BibliotecaForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BibliotecaFormTest {

    private BibliotecaForm bibliotecaForm;

    private Long idUsuarioBiblio = 1L;
    private Long idJuegoBiblio = 2L;
    private LocalDate fechaAdquisicionJuegoBiblio = LocalDate.of(2022, 5, 15);
    private double tiempoJuegoBiblio = 120.5;
    private LocalDateTime ultiFechaJuegoBiblio = LocalDateTime.now().minusHours(5);
    private TipoEstadoInstalacion estadoInstJuegoBiblio = TipoEstadoInstalacion.INSTALADO;

    @BeforeEach
    void setUp() {
        // Inicializar antes de cada test
        bibliotecaForm = new BibliotecaForm(idUsuarioBiblio, idJuegoBiblio, fechaAdquisicionJuegoBiblio,
                tiempoJuegoBiblio, ultiFechaJuegoBiblio, estadoInstJuegoBiblio);
    }

    // Test Biblioteca Valida
    @Test
    void testConstructorAndGetters() {
        assertEquals(idUsuarioBiblio, bibliotecaForm.getIdUsuarioBiblio());
        assertEquals(idJuegoBiblio, bibliotecaForm.getIdJuegoBiblio());
        assertEquals(fechaAdquisicionJuegoBiblio, bibliotecaForm.getFechaAdquisicionJuegoBiblio());
        assertEquals(tiempoJuegoBiblio, bibliotecaForm.getTiempoJuegoBiblio());
        assertEquals(ultiFechaJuegoBiblio, bibliotecaForm.getUltiFechaJuegoBiblio());
        assertEquals(estadoInstJuegoBiblio, bibliotecaForm.getEstadoInstJuegoBiblio());
    }

    // Test para validar campos nulos
    @Test
    void testValidarCamposNulos() {
        BibliotecaForm formConCamposNulos = new BibliotecaForm(null, null,
                null, 0, null, null);
        List<ErrorDto> errores = formConCamposNulos.validar();

        assertEquals(3, errores.size());
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("Id_usuario")
                && error.getMensaje() == ErrorType.REQUERIDO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("Id_juego")
                && error.getMensaje() == ErrorType.REQUERIDO));
        assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("fecha_adquisicion")
                && error.getMensaje() == ErrorType.REQUERIDO));
    }

    // Test para validar fecha futura
    //@Test
    //void testFechaFutura() {
    //    LocalDate fechaFutura = LocalDate.now().plusDays(1);
    //    BibliotecaForm formConFechaFutura = new BibliotecaForm(idUsuarioBiblio, idJuegoBiblio, fechaFutura,
    //            tiempoJuegoBiblio, ultiFechaJuegoBiblio, estadoInstJuegoBiblio);
//
    //    List<ErrorDto> errores = formConFechaFutura.validar();
//
    //    // Comprobar que el error por fecha futura se genera
    //    assertTrue(errores.stream().anyMatch(error -> error.getCampo().equals("fecha_compra")
    //            && error.getMensaje() == ErrorType.FECHA_FUTURA));
    //}

    // Test para validar fecha válida
    @Test
    void testFechaValida() {
        LocalDate fechaValida = LocalDate.now().minusDays(1);
        BibliotecaForm formConFechaValida = new BibliotecaForm(idUsuarioBiblio, idJuegoBiblio, fechaValida,
                tiempoJuegoBiblio, ultiFechaJuegoBiblio, estadoInstJuegoBiblio);

        List<ErrorDto> errores = formConFechaValida.validar();

        // Comprobar que no haya errores por fecha
        assertTrue(errores.isEmpty());
    }

}
