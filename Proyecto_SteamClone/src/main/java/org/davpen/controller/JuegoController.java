package org.davpen.controller;

import org.davpen.enums.TipoCategoriaJuego;
import org.davpen.enums.TipoClasificacionEdades;
import org.davpen.enums.TipoConsultaCatalogo;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.JuegoDto;
import org.davpen.modelo.entity.JuegoEntity;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.JuegoForm;
import org.davpen.repositorio.hibernate.JuegoRepoHibernate;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.transaction.HibernateTransactionManager;
import org.davpen.transaction.ISessionManager;
import org.davpen.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class JuegoController {

    public static final int DESCUENTO_MIN = 0;
    public static final int DESCUENTO_MAX = 100;
    private final IJuegoRepo juegoRepo;
    public ITransactionManager transMgr;

    public JuegoController(IJuegoRepo juegoRepo, ITransactionManager transMgr) {
        this.juegoRepo = juegoRepo;
        this.transMgr = transMgr;
    }


    /**
     * Registra un nuevo Juego tras validar el formulario y restricciones de juego.
     * Si hay errores, lanza ValidationException con la lista de errores.
     *
     * @param juegoForm Identificador de Juego
     * @return JuegoDto
     * @throws ValidationException
     */
    public JuegoDto anhadirJuego(JuegoForm juegoForm) throws ValidationException {
        //validar formato
        var errores = juegoForm.validar();

        var juego = transMgr.inTransaction(() -> {
            //validar modelo
            //juego unico
            if (juegoRepo.obtenerTodos().stream()
                    .anyMatch(j -> j.getTituloJuego().equals(juegoForm.getTituloJuego()))) {
                errores.add(new ErrorDto("titulo", ErrorType.DUPLICADO));
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var juegoOpt = juegoRepo.crear(juegoForm);
            return juegoOpt;//Linea corregida
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaJuegoCompleto(juego.orElse(null));//corregir en todo_ el código
    }

    /**
     * Devuelve los juegos cuyo tipo de categoría coincide con el proporcionado.
     *
     * @param categoria TipoCategoriaJuego por el que filtrar.
     * @return Lista de JuegoDto que pertenecen a la categoría indicada. Puede ser lista vacía si no hay coincidencias.
     */
    public List<JuegoDto> listaJuegosPorCategoria(TipoCategoriaJuego categoria) {
        return juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getCategoriaJuego().equals(categoria))
                .map(Mapper::mapaJuegoCompleto)
                .toList();
    }

    /**
     * Devuelve los juegos cuyo precio base está dentro del rango [precio_Min, precio_Max].
     *
     * @param precio_Min Precio mínimo (inclusive).
     * @param precio_Max Precio máximo (inclusive).
     * @return Lista de JuegoDto cuyo precio base está dentro del rango.
     * @throws IllegalArgumentException Si precio_Min > precio_Max.
     */
    public List<JuegoDto> listaJuegosPorRangoPrecio(double precio_Min, double precio_Max) throws IllegalArgumentException {

        if (precio_Min > precio_Max) {
            throw new IllegalArgumentException("El precio minimo no puede ser mayor que el precio máximo");
        }

        var resultadoBusqueda = juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getPrecioBaseJuego() <= precio_Max)
                .filter(j -> j.getPrecioBaseJuego() >= precio_Min)
                .map(Mapper::mapaJuegoCompleto)
                .toList();

        return resultadoBusqueda;
    }

    /**
     * Devuelve los juegos cuya clasificación por edades coincide con la proporcionada.
     *
     * @param clasificacionEdades TipoClasificacionEdades por el que filtrar.
     * @return Lista de JuegoDto que cumplen la clasificación indicada. Puede ser lista vacía.
     */
    public List<JuegoDto> listaJuegosPorClasificacion(TipoClasificacionEdades clasificacionEdades) {
        return juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getClasEdadJuego().equals(clasificacionEdades))
                .map(Mapper::mapaJuegoCompleto)
                .toList();
    }

    /**
     * Devuelve los juegos cuyo estado coincide con el proporcionado.
     *
     * @param estadoJuego TipoEstadoJuego por el que filtrar.
     * @return Lista de JuegoDto que tienen el estado indicado. Puede ser lista vacía.
     */
    public List<JuegoDto> listaJuegosPorEstado(TipoEstadoJuego estadoJuego) {
        return juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getEstadoJuego().equals(estadoJuego))
                .map(Mapper::mapaJuegoCompleto)
                .toList();
    }

    /**
     * Busca juegos que contienen un texto en su descripción.
     *
     * @param palabraBuscada Subcadena a buscar dentro de la descripción del juego.
     * @return Lista de JuegoDto cuya descripción contiene el texto indicado.
     */
    public List<JuegoDto> listaJuegosPorPalabraEnDescripcion(String palabraBuscada) {
        return juegoRepo.obtenerTodos().stream()
                .filter(j -> j.getDescripcionJuego().contains(palabraBuscada))
                .map(Mapper::mapaJuegoCompleto)
                .toList();
    }

    /**
     * Consulta el catálogo completo de Juegos y lo ordena según el criterio indicado.
     *
     * @param consulta TipoConsultaCatalogo criterio de orden: ALFABETICO, PRECIO o FECHA.
     * @return Lista ordenada de JuegoDto según el criterio; lista vacía si no hay juegos.
     */
    public List<JuegoDto> listaCatalogoCompleto(TipoConsultaCatalogo consulta) {

        List<JuegoDto> listaConsultaDto = new ArrayList<>(List.of());

        if (consulta == TipoConsultaCatalogo.ALFABETICO) {
            var resultadoConsulta = juegoRepo.obtenerTodos().stream()
                    .sorted(Comparator.comparing(JuegoEntity::getTituloJuego))
                    .toList();
            for (JuegoEntity juegoEntity : resultadoConsulta) {
                listaConsultaDto.add(Mapper.mapaJuegoCompleto(juegoEntity));
            }
        } else if (consulta == TipoConsultaCatalogo.PRECIO) {
            var resultadoConsulta = juegoRepo.obtenerTodos().stream()
                    .sorted(Comparator.comparing(JuegoEntity::getPrecioBaseJuego))
                    .toList();
            for (JuegoEntity juegoEntity : resultadoConsulta) {
                listaConsultaDto.add(Mapper.mapaJuegoCompleto(juegoEntity));
            }
        } else if (consulta == TipoConsultaCatalogo.FECHA) {
            var resultadoConsulta = juegoRepo.obtenerTodos().stream()
                    .sorted(Comparator.comparing(JuegoEntity::getFechaLanzaJuego))
                    .toList();
            for (JuegoEntity juegoEntity : resultadoConsulta) {
                listaConsultaDto.add(Mapper.mapaJuegoCompleto(juegoEntity));
            }

        }

        return listaConsultaDto;
    }

    /**
     * Devuelve los detalles del juego indicado por su id. Si el juego no se encuentra lanza ValidationException
     *
     * @param id Identificador único del juego.
     * @return JuegoDto con la información completa del juego.
     * @throws ValidationException Si no existe un juego con el id proporcionado
     */
    public JuegoDto detalleJuego(Long id) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        var juegoInfo = juegoRepo.obtenerPorId(id);
        if (!juegoInfo.isPresent()) {
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var juego = juegoInfo.orElse(null);
        return Mapper.mapaJuegoCompleto(juego);

    }

    /**
     * Aplica un descuento porcentual al precio base del juego indicado.
     * Valida que:
     * - El juego exista.
     * - El juego esté disponible.
     * - El valor de descuento esté entre DESCUENTO_MIN y DESCUENTO_MAX.
     *
     * @param id        Identificador del juego a actualizar.
     * @param descuento Porcentaje de descuento a aplicar (entero).
     * @return JuegoDto con el juego actualizado y precio recalculado.
     * @throws ValidationException Si existen errores de validación; la excepción contiene la lista de errores
     */
    public JuegoDto aplicarDescuento(Long id, int descuento) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        var juegoOpt = juegoRepo.obtenerPorId(id);
        if (!juegoOpt.isPresent()) {
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
        if (juegoOpt.get().getEstadoJuego() == TipoEstadoJuego.NO_DISPONIBLE) {
            errores.add(new ErrorDto("estado", ErrorType.NO_DISPONIBLE));
        }
        if (descuento < DESCUENTO_MIN) {
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_NEGATIVO));
        }
        if (descuento > DESCUENTO_MAX) {
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var juegoActual = juegoOpt.get();
        var nuevoPrecio = juegoActual.getPrecioBaseJuego()
                - (juegoActual.getPrecioBaseJuego() * descuento / 100);

        var juegoActualizadoForm = new JuegoForm(juegoActual.getTituloJuego(),
                juegoActual.getDescripcionJuego(), juegoActual.getDesarrolladorJuego(),
                juegoActual.getFechaLanzaJuego(), nuevoPrecio, descuento, juegoActual.getCategoriaJuego(),
                juegoActual.getClasEdadJuego(), juegoActual.getIdiomasJuego(),
                juegoActual.getEstadoJuego());

        var juegoActualizado = juegoRepo.actualizar(id, juegoActualizadoForm).orElse(null);

        return Mapper.mapaJuegoCompleto(juegoActualizado);
    }

    /**
     * Cambia el estado del juego indicado por id al nuevoEstado proporcionado.
     * Valida que:
     * - El juego exista.
     * - El nuevo estado sea un valor válido dentro de TipoEstadoJuego.
     *
     * @param id          Identificador del juego a actualizar.
     * @param nuevoEstado TipoEstadoJuego a establecer.
     * @return JuegoDto con el juego actualizado.
     * @throws ValidationException Si existen errores de validación; la excepción contiene la lista de errores.
     */
    public JuegoDto cambiarEstadoJuego(Long id, TipoEstadoJuego nuevoEstado) throws ValidationException {

        var errores = new ArrayList<ErrorDto>();
        if (!juegoRepo.obtenerPorId(id).isPresent()) {
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
        if (!Arrays.stream(TipoEstadoJuego.values()).anyMatch(t -> t.equals(nuevoEstado))) {
            errores.add(new ErrorDto("estado", ErrorType.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        var juegoActual = juegoRepo.obtenerPorId(id);
        var juegoActualizadoForm = new JuegoForm(juegoActual.get().getTituloJuego(),
                juegoActual.get().getDescripcionJuego(), juegoActual.get().getDesarrolladorJuego(),
                juegoActual.get().getFechaLanzaJuego(), juegoActual.get().getPrecioBaseJuego(),
                juegoActual.get().getDescuentoActualJuego(), juegoActual.get().getCategoriaJuego(),
                juegoActual.get().getClasEdadJuego(), juegoActual.get().getIdiomasJuego(), nuevoEstado);

        var juegoActualizado = juegoRepo.actualizar(id, juegoActualizadoForm).orElse(null);

        return Mapper.mapaJuegoCompleto(juegoActualizado);
    }

    static void main() throws ValidationException {
        ITransactionManager transMgr = new HibernateTransactionManager();
        var c = new JuegoController(new JuegoRepoHibernate((ISessionManager) transMgr), transMgr);

        var juegoForm = new JuegoForm("Elden Ring", "RPG de acción épico", "FromSoftware",
                LocalDate.of(2022, 2, 25), 60d, 0, TipoCategoriaJuego.RPG,
                TipoClasificacionEdades.PEGI_16, new ArrayList<>(List.of("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE);
        var juego2Form = new JuegoForm("Hades", "Roguelike indie", "Supergiant Games",
                LocalDate.of(2020, 9, 17), 24.99, 10, TipoCategoriaJuego.ACCION,
                TipoClasificacionEdades.PEGI_12, new ArrayList<>(List.of("Español", "Inglés")),
                TipoEstadoJuego.DISPONIBLE);
        var juego3Form = new JuegoForm( "Chess Game", "Ajedrez online", "Chess Dev",
                LocalDate.of(2019, 5, 10), 0.00, 0, TipoCategoriaJuego.ESTRATEGIA,
                TipoClasificacionEdades.PEGI_3, new ArrayList<>(List.of("Español", "Inglés")),
                TipoEstadoJuego.NO_DISPONIBLE);

        var juego1 = c.anhadirJuego(juegoForm);
        System.out.println(juego1);
        var juego2 = c.anhadirJuego(juego2Form);
        System.out.println(juego2);
        var juego3 = c.anhadirJuego(juego3Form);
        System.out.println(juego3);
        var juegoRepetido = c.anhadirJuego(juegoForm);
        System.out.println(juegoRepetido);

    }

}
