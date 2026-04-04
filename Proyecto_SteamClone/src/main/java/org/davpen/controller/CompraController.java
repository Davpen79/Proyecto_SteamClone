package org.davpen.controller;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.CompraDto;
import org.davpen.modelo.dto.UsuarioDto;
import org.davpen.modelo.entity.CompraEntity;
import org.davpen.modelo.form.CompraForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.modelo.form.UsuarioForm;
import org.davpen.pagos.*;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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
    public CompraDto realizarCompra(Long idUsuario, Long idJuego, TipoMetodoPago metodoPago) throws ValidationException {
        //Validar
        var errores = new ArrayList<ErrorDto>();
        //usuario existe y activo
        if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()) {
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
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

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        var precioCompra = juegoRepo.obtenerPorId(idJuego).get().getPrecioBaseJuego();
        var descuentoCompra = juegoRepo.obtenerPorId(idJuego).get().getDescuentoActualJuego();

        CompraForm compraForm = new CompraForm(idUsuario, idJuego, LocalDate.now(), metodoPago, precioCompra,
                descuentoCompra, TipoEstadoCompra.COMPLETADA);
        var compraEfectuada = compraRepo.crear(compraForm).orElse(null);
        var idCompraEfectuada = compraRepo.obtenerTodos().stream()
                .filter(c -> c.getIdUsuarioCompra().equals(idUsuario))
                .filter(c -> c.getIdJuegoCompra().equals(idJuego)).findFirst()
                .get().getIdCompra();

        return Mapper.mapaCompraSimple(compraEfectuada);
    }


    //Procesar pago ¿¿Datos según metodo de pago??
    public CompraDto procesarPago(Long idCompra, TipoMetodoPago metodoPago) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();
        Optional<CompraEntity> compraAProcesarOpt = compraRepo.obtenerPorId(idCompra);
        if (!compraAProcesarOpt.isPresent()) {
            errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }

        var compraAProcesar = compraAProcesarOpt.get();
        var idUsuario = compraAProcesar.getIdUsuarioCompra();
        var usuarioCompra = usuarioRepo.obtenerPorId(idUsuario).get();
        var idJuego = compraAProcesar.getIdJuegoCompra();
        var juegoCompra = juegoRepo.obtenerPorId(idJuego).get();
        var precioCompra = juegoCompra.getPrecioBaseJuego();
        var descuentoCompra = juegoCompra.getDescuentoActualJuego();
        var precioFinal = precioCompra - (precioCompra * descuentoCompra / 100);

        IPlataformaPago plataformaPago;
        switch (metodoPago) {
            case CARTERA_STEAM:
                plataformaPago = new PagoCarteraSteam(idUsuario, usuarioRepo);
                break;
            case PAYPAL:
                plataformaPago = new PagoPayPal();
                break;
            case TARJETA_CREDITO:
                plataformaPago = new PagoTarjetaCredito();
                break;
            case TRANSFERENCIA:
                plataformaPago = new PagoTransferencia();
                break;
            default:
                errores.add(new ErrorDto("metodo_pago", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
        }
        plataformaPago.procesarPago(compraAProcesar,usuarioCompra,precioFinal);

        return Mapper.mapaCompraSimple(compraAProcesar);
    }


    //Consultar detalles de compra
    public CompraDto consultarHistorialCompras(Long idCompra, Long idUsuario) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        var compraConsultada = compraRepo.obtenerPorId(idCompra);
        //Compra existe
        if (!compraConsultada.isPresent()) {
            errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
        //verificar pertenencia de compra a usuario

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


    //solicitar reembolso - Plazo devolucion(14 dias) + Horas jugadas(2 horas)
    public UsuarioDto solicitarReembolso(Long idCompra) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        //validar compra existe
        var compraAReembolsar = compraRepo.obtenerPorId(idCompra);
        if (!compraAReembolsar.isPresent()) {
            errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
        //validar compra completada
        if (compraAReembolsar.get().getEstadoCompra() != TipoEstadoCompra.COMPLETADA) {
            errores.add(new ErrorDto("estado_compra", ErrorType.COMPRA_FALLIDA));
            throw new ValidationException(errores);
        }
        //Validar Condiciones Devolución 14 dias
        if (compraAReembolsar.get().getFechaCompra().isBefore(LocalDate.now().minusDays(14))) {
            errores.add(new ErrorDto("plazo", ErrorType.PLAZO_SUPERADO));
        }
        //Validar Condiciones de devolución 2 horas
        var idUsuarioCompra = compraAReembolsar.get().getIdUsuarioCompra();
        var idJuegoCompra = compraAReembolsar.get().getIdJuegoCompra();
        var entradaBiblioteca = bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuarioCompra))
                .filter(b -> b.getIdJuegoBiblio().equals(idJuegoCompra))
                .findFirst();
        if (entradaBiblioteca.isPresent()) {
            var tiempoJugado = entradaBiblioteca.get().getTiempoJuegoBiblio();
            if (tiempoJugado > 2.00) {
                errores.add(new ErrorDto("tiempo_jugado", ErrorType.TIEMPO_SUPERADO));
            }
        } else {
            errores.add(new ErrorDto("biblioteca", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        //Procesar Reembolso
        var precioAReembolsar = compraAReembolsar.get().getPrecioBaseCompra();
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
