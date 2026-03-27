package org.davpen.controller;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.CompraDto;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.form.CompraForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;

public class CompraController {

    private final ICompraRepo compraRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final IBibliotecaRepo bibliotecaRepo;

    public CompraController(ICompraRepo compraRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo,
                            IBibliotecaRepo bibliotecaRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
    }

    //Realizar compra => relacion con añadir juego a biblioteca
    public CompraDto realizarCompra(Long idUsuario, Long idJuego, TipoMetodoPago metodoPago) {
        //Validar
        var errores = new ArrayList<ErrorDto>();
        //usuario activo
        if (!usuarioRepo.obtenerPorId(idUsuario).get().getEstadoCuentaUsuario().equals(TipoEstadoCuenta.ACTIVA)) {
            errores.add(new ErrorDto("estado_cuenta", ErrorType.CUENTA_INACTIVA));
        }
        //juego comprable
        if (juegoRepo.obtenerPorId(idJuego).get().getEstadoJuego().equals(TipoEstadoJuego.NO_DISPONIBLE)) {
            errores.add(new ErrorDto("estado_juego", ErrorType.NO_DISPONIBLE));
        }
        //juego duplicado
        if (bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuego))) {
            errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
        }
        //saldo suficiente ¿¿SI USA CARTERA?? - Convertir en Procesar Pago
        var saldoCartera = usuarioRepo.obtenerPorId(idUsuario).get().getSaldoUsuario();
        var precioCompra = juegoRepo.obtenerPorId(idJuego).get().getPrecioBaseJuego();
        var descuentoCompra = juegoRepo.obtenerPorId(idJuego).get().getDescuentoActualJuego();
        var precioFinal = precioCompra - (precioCompra * descuentoCompra / 100);
        if (metodoPago == TipoMetodoPago.CARTERA_STEAM && saldoCartera < precioFinal) {
            errores.add(new ErrorDto("saldo_cartera", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        //¿¿¿OTROS METODOS DE PAGO???
        CompraForm compraForm = new CompraForm(idUsuario, idJuego, LocalDate.now(), metodoPago, precioCompra,
                descuentoCompra, TipoEstadoCompra.COMPLETADA);
        var compraEfectuada = compraRepo.crear(compraForm).orElse(null);
        var idCompraEfectuada = compraRepo.obtenerTodos().stream()
                .filter(c -> c.getIdUsuarioCompra().equals(idUsuario))
                .filter(c -> c.getIdJuegoCompra().equals(idJuego)).findFirst()
                .get().getIdCompra();

        return Mapper.mapaCompraSimple(compraEfectuada);
    }


    //TODO Procesar pago ¿¿Datos según metodo de pago??


    //Consultar detalles de compra
    public CompraDto consultarHistorialCompras(Long idCompra, Long idUsuario) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        //Compra existe
        if (!compraRepo.obtenerPorId(idCompra).isPresent()) {
            errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
        }
        //verificar pertenencia de compra a usuario
        var compraConsultada = compraRepo.obtenerPorId(idCompra);
        var idUsuarioEnCompra = compraConsultada.get().getIdUsuarioCompra();
        if (idUsuarioEnCompra != idUsuario) {
            errores.add(new ErrorDto("id", ErrorType.NO_PERTENECE));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var compra = compraConsultada.orElse(null);
        return Mapper.mapaCompraSimple(compra);

    }


    //solicitar reembolso - TODO Motivo reembolso + Plazo devolucion(14 dias) + Horas jugadas(2 horas)
    //TODO manejar descuento??
    public UsuarioDto solicitarReembolso(Long idCompra) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        //validar compra completada
        if (compraRepo.obtenerPorId(idCompra).get().getEstadoCompra() != TipoEstadoCompra.COMPLETADA) {
            errores.add(new ErrorDto("estado_compra", ErrorType.COMPRA_FALLIDA));
        }
        //Validar Condiciones Devolución 14 dias
        if (compraRepo.obtenerPorId(idCompra).get().getFechaCompra().isBefore(LocalDate.now().minusDays(14))) {
            errores.add(new ErrorDto("plazo", ErrorType.PLAZO_SUPERADO));
        }
        //Validar Condiciones de devolución 2 horas
        var idUsuarioCompra = compraRepo.obtenerPorId(idCompra).get().getIdUsuarioCompra();
        var idJuegoCompra = compraRepo.obtenerPorId(idCompra).get().getIdJuegoCompra();
        var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuarioCompra))
                .filter(b -> b.getIdJuegoBiblio().equals(idJuegoCompra))
                .findFirst();
        var tiempoJugado = entradaBiblioteca.get().getTiempoJuegoBiblio();
        if (tiempoJugado > 2.00) {
            errores.add(new ErrorDto("tiempo_jugado", ErrorType.TIEMPO_SUPERADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        //Procesar Reembolso
        var precioAReembolsar = compraRepo.obtenerPorId(idCompra).get().getPrecioBaseCompra();
        var saldoActualUsuario = usuarioRepo.obtenerPorId(idUsuarioCompra).get().getSaldoUsuario();
        var nuevoSaldoUsuario = saldoActualUsuario + precioAReembolsar;

        var usuarioCompra = usuarioRepo.obtenerPorId(idUsuarioCompra).get();
        var usuarioForm = new UsuarioForm(usuarioCompra.getNombreCuentaUsuario(), usuarioCompra.getEmailUsuario(),
                usuarioCompra.getPasswordUsuario(),
                usuarioCompra.getNombreRealUsuario(), usuarioCompra.getPaisUsuario(),
                usuarioCompra.getFechaNacUsuario(), usuarioCompra.getFechaRegUsuario(),
                usuarioCompra.getAvatarUsuario(), nuevoSaldoUsuario, usuarioCompra.getEstadoCuentaUsuario());
        usuarioRepo.actualizar(idUsuarioCompra, usuarioForm);

        var usuarioRepoActualizado = usuarioRepo.obtenerPorId(idUsuarioCompra).orElse(null);
        return Mapper.mapaUsuarioCompleto(usuarioRepoActualizado);
    }


    //Consultar historial de compras(Ficheros)


    //Generar factura (Ficheros)


}
