package modelo.form;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.JuegoForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JuegoFormTest {

    private JuegoForm juegoForm;

    @BeforeEach
    void setUp() {
        // Setup básico antes de cada test
        juegoForm = new JuegoForm(
                "Test Game",
                "This is a description of the test game",
                "Test Developer",
                LocalDate.now(),
                50.0,
                20,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(List.of("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE
        );
    }

    @Test
    void testTituloJuegoNull() {
        juegoForm = new JuegoForm(
                null,
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("titulo")
                && e.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    void testTituloJuegoDemasiadoCorto() {
        juegoForm = new JuegoForm(
                "T",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("titulo")
                && e.getMensaje() == ErrorType.DEMASIADO_CORTO));
    }

    @Test
    void testTituloJuegoDemasiadoLargo() {
        String largoTitulo = "T".repeat(101); // 101 caracteres
        juegoForm = new JuegoForm(
                largoTitulo,
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("titulo")
                && e.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    void testDescripcionJuegoDemasiadoLargo() {
        String descripcionLarga = "D".repeat(2001); // 2001 caracteres
        juegoForm = new JuegoForm(
                "Test Game",
                descripcionLarga,
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("descripcion")
                && e.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    void testDesarrolladorJuegoNull() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                null,
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("desarrollador")
                && e.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    void testDesarrolladorJuegoDemasiadoCorto() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "T",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("desarrollador")
                && e.getMensaje() == ErrorType.DEMASIADO_CORTO));
    }

    @Test
    void testDesarrolladorJuegoDemasiadoLargo() {
        String largoDesarrollador = "D".repeat(101); // 101 caracteres
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                largoDesarrollador,
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("desarrollador")
                && e.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

    @Test
    void testFechaLanzamientoNull() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Desarrollador",
                null,
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );
        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("fecha_lanzamiento")
                && e.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    void testPrecioBaseNull() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                null,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("precio_base")
                && e.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    void testPrecioBaseJuegoFueraRango() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                1000.0, // Precio mayor a PRECIO_MAX
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("precio_base")
                && e.getMensaje() == ErrorType.VALOR_DEMASIADO_ALTO));
    }

    @Test
    void testPrecioBaseJuegoNegativo() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                -1.0,  // Precio negativo
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("precio_base")
                && e.getMensaje() == ErrorType.VALOR_NEGATIVO));
    }

    @Test
    void testPrecioBaseJuegoDemasiadosDecimales() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                10.568,  // Precio erroneo
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("precio_base")
                && e.getMensaje() == ErrorType.DEMASIADOS_DECIMALES));
    }

    @Test
    void testDescuentoActualJuegoFueraRango() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                110,  // Descuento mayor a DESCUENTO_MAX
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("descuento")
                && e.getMensaje() == ErrorType.VALOR_DEMASIADO_ALTO));
    }

    @Test
    void testDescuentoActualJuegoNegativo() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                -1,  // Descuento negativo
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("descuento")
                && e.getMensaje() == ErrorType.VALOR_NEGATIVO));
    }

    @Test
    void testClasificacionEdadJuegoNull() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                null, // Clasificación de edad nula
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("edad")
                && e.getMensaje() == ErrorType.REQUERIDO));
    }

    @Test
    void testClasificacionEdadJuegoNoValida() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                null, // Clasificación no válida
                new ArrayList<>(),
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("edad")
                && e.getMensaje() == ErrorType.NO_ENCONTRADO));
    }

    @Test
    void testIdiomasJuegoVacio() {
        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                new ArrayList<>(), // Idiomas vacío
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("idioma")
                && e.getMensaje() == ErrorType.CAMPO_VACIO));
    }

    @Test
    void testIdiomasJuegoDemasiadoLargo() {
        ArrayList<String> idiomasLargos = new ArrayList<>();
        idiomasLargos.add("Español");
        idiomasLargos.add("Inglés".repeat(50)); // Idioma muy largo

        juegoForm = new JuegoForm(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                LocalDate.now(),
                20.0,
                10,
                TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_16,
                idiomasLargos,
                TipoEstadoJuego.DISPONIBLE
        );

        List<ErrorDto> errores = juegoForm.validar();
        assertTrue(errores.stream().anyMatch(e -> e.getCampo().equals("idioma")
                && e.getMensaje() == ErrorType.DEMASIADO_LARGO));
    }

}
