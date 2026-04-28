package modelo.form;

import org.davpen.enums.TipoEstadoResenha;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.ResenhaForm;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResenhaFormTest {

    private ResenhaForm crearResenhaValida() {
        return new ResenhaForm(
                1L,
                1L,
                true,
                "Este es un texto de reseña válido con más de cincuenta caracteres para pasar la validación.",
                10.5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );
    }

    @Test
    void validar_resenhaValida_sinErrores() {
        ResenhaForm form = crearResenhaValida();

        List<ErrorDto> errores = form.validar();

        assertTrue(errores.isEmpty());
    }

    @Test
    void validar_idUsuarioNulo_devuelveError() {
        ResenhaForm form = new ResenhaForm(
                null,
                1L,
                true,
                "Texto válido suficientemente largo para pasar la validación mínima requerida.",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertEquals(1, errores.size());
        assertEquals("Id_Usario", errores.getFirst().getCampo());
    }

    @Test
    void validar_idJuegoNulo_devuelveError() {
        ResenhaForm form = new ResenhaForm(
                1L,
                null,
                true,
                "Texto válido suficientemente largo para pasar la validación mínima requerida.",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertEquals(1, errores.size());
        assertEquals("Id_Juego", errores.getFirst().getCampo());
    }

    @Test
    void validar_textoNulo_devuelveErrorRequerido() {
        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                true,
                null,
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertEquals(1, errores.size());
        assertEquals("texto_reseña", errores.getFirst().getCampo());
    }

    @Test
    void validar_textoVacio_devuelveErrorRequerido() {
        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                true,
                "   ",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertEquals(1, errores.size());
        assertEquals("texto_reseña", errores.getFirst().getCampo());
    }

    @Test
    void validar_textoDemasiadoCorto_devuelveError() {
        String textoCorto = "Demasiado corto";

        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                true,
                textoCorto,
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        //assertTrue(
        //        errores.stream().anyMatch(e -> e.getMensaje() == ErrorType.DEMASIADO_CORTO)
        //);
        assertEquals("texto_reseña", errores.getFirst().getCampo());
    }

    @Test
    void validar_textoDemasiadoLargo_devuelveError() {
        String textoLargo = "a".repeat(ResenhaForm.RESENHA_LENGTH_MAX + 1);

        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                true,
                textoLargo,
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertTrue(
                errores.stream().anyMatch(e -> e.getMensaje() == ErrorType.DEMASIADO_LARGO)
        );
    }

    @Test
    void validar_multiplesErrores_devuelveTodos() {
        ResenhaForm form = new ResenhaForm(
                null,
                null,
                true,
                "corto",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();
        assertEquals(3, errores.size()); // usuario, juego, texto corto
    }

    @Test
    void validar_recomendacionNull_devuelveError() {
        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                null,
                "Texto válido suficientemente largo para pasar la validación mínima requerida.",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertTrue(
                errores.stream().anyMatch(e -> e.getMensaje() == ErrorType.REQUERIDO)
        );
    }

    @Test
    void validar_recomendacionTrue_sinError() {
        ResenhaForm form = crearResenhaValida();

        List<ErrorDto> errores = form.validar();

        assertTrue(
                errores.stream().noneMatch(e -> e.getCampo().equals("recomendacion"))
        );
    }

    @Test
    void validar_recomendacionFalse_sinError() {
        ResenhaForm form = new ResenhaForm(
                1L,
                1L,
                false,
                "Texto válido suficientemente largo para pasar la validación mínima requerida.",
                5,
                LocalDate.now(),
                LocalDate.now(),
                TipoEstadoResenha.PUBLICADA
        );

        List<ErrorDto> errores = form.validar();

        assertTrue(
                errores.stream().noneMatch(e -> e.getCampo().equals("recomendacion"))
        );
    }

}
