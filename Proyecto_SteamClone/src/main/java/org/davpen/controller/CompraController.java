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
import org.davpen.repositorio.hibernate.BibliotecaRepoHibernate;
import org.davpen.repositorio.hibernate.CompraRepoHibernate;
import org.davpen.repositorio.hibernate.JuegoRepoHibernate;
import org.davpen.repositorio.hibernate.UsuarioRepoHibernate;
import org.davpen.repositorio.interfaces.IBibliotecaRepo;
import org.davpen.repositorio.interfaces.ICompraRepo;
import org.davpen.repositorio.interfaces.IJuegoRepo;
import org.davpen.repositorio.interfaces.IUsuarioRepo;
import org.davpen.transaction.HibernateTransactionManager;
import org.davpen.transaction.ISessionManager;
import org.davpen.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class CompraController {

    public static final int DESCUENTO_MAX = 100;
    public static final int DESCUENTO_MIN = 0;
    private final ICompraRepo compraRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final IBibliotecaRepo bibliotecaRepo;
    public ITransactionManager transMgr;

    public CompraController(ICompraRepo compraRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo,
                            IBibliotecaRepo bibliotecaRepo, ITransactionManager transMgr) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
        this.transMgr = transMgr;
    }

    /**
     * Realiza una compra: valida usuario, estado de cuenta, disponibilidad del juego y duplicados en la biblioteca,
     * crea la compra y devuelve un DTO de la compra realizada.
     * Si encuentra algun error de validacion lanza ValidationException con la lista de errores
     *
     * @param idUsuario  Identificador del usuario que realiza la compra.
     * @param idJuego    Identificador del juego a comprar.
     * @param metodoPago Metodo de pago seleccionado.
     * @return CompraDto de la compra creada.
     * @throws ValidationException Si existen errores de validación; la excepción contiene la lista de errores.
     */
    public CompraDto realizarCompra(Long idUsuario, Long idJuego, TipoMetodoPago metodoPago) throws ValidationException {
        //Validar
        var errores = new ArrayList<ErrorDto>();

        var compra = transMgr.inTransaction(() -> {
            //usuario existe y activo
            if (!usuarioRepo.obtenerPorId(idUsuario).isPresent()) {
                errores.add(new ErrorDto("id_usuario", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            if (!usuarioRepo.obtenerPorId(idUsuario).get().getEstadoCuentaUsuario().equals(TipoEstadoCuenta.ACTIVA)) {
                errores.add(new ErrorDto("estado_cuenta", ErrorType.CUENTA_INACTIVA));
            }
            //juego existe y comprable
            if (!juegoRepo.obtenerPorId(idJuego).isPresent()) {
                errores.add(new ErrorDto("id_juego", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            if (juegoRepo.obtenerPorId(idJuego).get().getEstadoJuego().equals(TipoEstadoJuego.NO_DISPONIBLE)) {
                errores.add(new ErrorDto("estado_juego", ErrorType.NO_DISPONIBLE));
            }
            //juego duplicado
            if (bibliotecaRepo.obtenerTodos().stream()
                    .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                    .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuego))) {
                errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
            }
            //Comprobar Descuento
            var descuentoCompra = juegoRepo.obtenerPorId(idJuego).get().getDescuentoActualJuego();
            if (descuentoCompra > DESCUENTO_MAX) {
                errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_ALTO));
            }
            if (descuentoCompra < DESCUENTO_MIN) {
                errores.add(new ErrorDto("descuento", ErrorType.VALOR_NEGATIVO));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
            var precioCompra = juegoRepo.obtenerPorId(idJuego).get().getPrecioBaseJuego();


            var compraForm = new CompraForm(idUsuario, idJuego, LocalDate.now(), metodoPago, precioCompra,
                    descuentoCompra, TipoEstadoCompra.PENDIENTE);

            var compraEfectuada = compraRepo.crear(compraForm);
            return compraEfectuada;

        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaCompraSimple(compra.orElse(null));
    }

    /**
     * Procesa el pago de una compra existente usando la plataforma adecuada según el metodo de pago y actualiza el
     * estado.
     * <p>
     * Validaciones:
     * - La compra debe existir.
     * - El metodo de pago debe ser conocido.
     * <p>
     * - Calcula el precio final aplicando el descuento del juego y delega el procesamiento a la implementación de
     * IPlataformaPago correspondiente.
     *
     * @param idCompra   Identificador de la compra a procesar.
     * @param metodoPago Metodo de pago a utilizar.
     * @return CompraDto con la compra procesada y actualizada.
     * @throws ValidationException Si la compra no existe o el metodo de pago no es válido.
     */
    public CompraDto procesarPago(Long idCompra, TipoMetodoPago metodoPago) throws ValidationException {
        var errores = new ArrayList<ErrorDto>();

        var compraProcesada = transMgr.inTransaction(() -> {
            Optional<CompraEntity> compraAProcesarOpt = compraRepo.obtenerPorId(idCompra);
            if (!compraAProcesarOpt.isPresent()) {
                errores.add(new ErrorDto("id", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }

            var compraAProcesar = compraAProcesarOpt.get();
            if (compraAProcesar.getEstadoCompra().equals(TipoEstadoCompra.COMPLETADA)) {
                errores.add(new ErrorDto("estado_compra", ErrorType.COMPRA_COMPLETADA));
                throw new ValidationException(errores);
            }
            var idUsuario = compraAProcesar.getIdUsuarioCompra();
            var usuarioCompra = usuarioRepo.obtenerPorId(idUsuario).get();
            var precioCompra = compraAProcesar.getPrecioBaseCompra();
            var descuentoCompra = compraAProcesar.getDescuentoEnCompra();
            var precioFinal = precioCompra - (precioCompra * descuentoCompra / 100);

            IPlataformaPago plataformaPago;
            switch (metodoPago) {
                case CARTERA_STEAM:
                    plataformaPago = new PagoCarteraSteam(idUsuario, usuarioRepo, compraRepo);
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
            var pagoExitoso = plataformaPago.procesarPago(compraAProcesar, usuarioCompra, precioFinal);
            if (!pagoExitoso) {
                throw new ValidationException(errores);
            } else {
                CompraForm compraActualizadaForm = new CompraForm(compraAProcesar.getIdUsuarioCompra(),
                        compraAProcesar.getIdJuegoCompra(), compraAProcesar.getFechaCompra(),
                        compraAProcesar.getTipoPagoCompra(), compraAProcesar.getPrecioBaseCompra(),
                        compraAProcesar.getDescuentoEnCompra(),
                        TipoEstadoCompra.COMPLETADA);

                return compraRepo.actualizar(idCompra, compraActualizadaForm);
            }
        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaCompraSimple(compraProcesada.orElse(null));
    }

    /**
     * Consulta una compra por id comprobando que pertenece al usuario solicitado.
     * <p>
     * Validaciones:
     * - La compra debe existir.
     * - La compra debe pertenecer al usuario indicado.
     *
     * @param idCompra  Identificador de la compra a consultar.
     * @param idUsuario Identificador del usuario.
     * @return CompraDto con la información de la compra.
     * @throws ValidationException Si la compra no existe o no pertenece al usuario.
     */
    public CompraDto consultarCompra(Long idCompra, Long idUsuario) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();

        var compra = transMgr.inTransaction(() -> {
            var compraConsultada = compraRepo.obtenerPorId(idCompra);
            //Compra existe
            if (!compraConsultada.isPresent()) {
                errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //verificar pertenencia de consultaCompra a usuario

            var idUsuarioEnCompra = compraConsultada.get().getIdUsuarioCompra();
            if (idUsuarioEnCompra != idUsuario) {
                errores.add(new ErrorDto("id", ErrorType.NO_PERTENECE));
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }

            var consultaCompra = compraConsultada.orElse(null);
            return consultaCompra;
        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaCompraSimple(compra);
    }

    /**
     * Solicita un reembolso para una compra existente si cumple las condiciones de plazo y tiempo jugado.
     * <p>
     * Validaciones:
     * - La compra debe existir.
     * - La compra debe estar en estado COMPLETADA.
     * - La fecha de compra no debe superar el plazo de 14 días.
     * - La entrada en la biblioteca debe existir.
     * - El tiempo jugado registrado en la biblioteca debe ser <= 2.00 horas.
     * <p>
     * - Si las validaciones pasan, actualiza el saldo del usuario sumando el precio base de la compra y devuelve el
     * UsuarioDto actualizado.
     *
     * @param idCompra Identificador de la compra a reembolsar.
     * @return UsuarioDto con el usuario actualizado tras el reembolso.
     * @throws ValidationException Si cualquiera de las validaciones falla; la excepción contiene la lista de errores.
     */
    public UsuarioDto solicitarReembolso(Long idCompra) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();

        var usuario = transMgr.inTransaction(() -> {
            //validar compra existe
            var compraAReembolsarOpt = compraRepo.obtenerPorId(idCompra);
            if (!compraAReembolsarOpt.isPresent()) {
                errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
                throw new ValidationException(errores);
            }
            //validar compra completada
            if (compraAReembolsarOpt.get().getEstadoCompra() != TipoEstadoCompra.COMPLETADA) {
                errores.add(new ErrorDto("estado_compra", ErrorType.COMPRA_FALLIDA));
                throw new ValidationException(errores);
            }
            //Validar Condiciones Devolución 14 dias
            if (compraAReembolsarOpt.get().getFechaCompra().isBefore(LocalDate.now().minusDays(14))) {
                errores.add(new ErrorDto("plazo", ErrorType.PLAZO_SUPERADO));
            }
            //Validar Condiciones de devolución 2 horas
            var idUsuarioCompra = compraAReembolsarOpt.get().getIdUsuarioCompra();
            var idJuegoCompra = compraAReembolsarOpt.get().getIdJuegoCompra();
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
            var precioAReembolsar = compraAReembolsarOpt.get().getPrecioBaseCompra();
            var saldoActualUsuario = usuarioRepo.obtenerPorId(idUsuarioCompra).get().getSaldoUsuario();
            var nuevoSaldoUsuario = saldoActualUsuario + precioAReembolsar;

            var usuarioCompra = usuarioRepo.obtenerPorId(idUsuarioCompra).get();
            var usuarioForm = new UsuarioForm(usuarioCompra.getNombreCuentaUsuario(), usuarioCompra.getEmailUsuario(),
                    usuarioCompra.getPasswordUsuario(),
                    usuarioCompra.getNombreRealUsuario(), usuarioCompra.getPaisUsuario(),
                    usuarioCompra.getFechaNacUsuario(), usuarioCompra.getFechaRegUsuario(),
                    usuarioCompra.getAvatarUsuario(), nuevoSaldoUsuario, usuarioCompra.getEstadoCuentaUsuario());
            usuarioRepo.actualizar(idUsuarioCompra, usuarioForm);

            //Modifica estado compra a Reembolsada
            var compraAReembolsar = compraAReembolsarOpt.get();
            var compraReembolsadaForm = new CompraForm(compraAReembolsar.getIdUsuarioCompra(), compraAReembolsar.getIdJuegoCompra(),
                                        compraAReembolsar.getFechaCompra(), compraAReembolsar.getTipoPagoCompra(),
                                        compraAReembolsar.getPrecioBaseCompra(), compraAReembolsar.getDescuentoEnCompra(),
                                        TipoEstadoCompra.REEMBOLSADA);
            compraRepo.actualizar(idCompra, compraReembolsadaForm);

            var usuarioRepoActualizado = usuarioRepo.obtenerPorId(idUsuarioCompra);
            return usuarioRepoActualizado;
        });
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Mapper.mapaUsuarioCompleto(usuario.orElse(null));
    }

    static void main() throws ValidationException {

        ITransactionManager transMgr = new HibernateTransactionManager();
        var c = new CompraController(new CompraRepoHibernate((ISessionManager) transMgr),
                new UsuarioRepoHibernate((ISessionManager) transMgr),
                new JuegoRepoHibernate((ISessionManager) transMgr),
                new BibliotecaRepoHibernate((ISessionManager) transMgr), transMgr);
        //
        //var compraForm = new CompraForm(2L, 1L,
        //        LocalDate.now().minusDays(10), TipoMetodoPago.CARTERA_STEAM,
        //        34.99, 0, TipoEstadoCompra.PENDIENTE);
        //
        //var compra1 = c.realizarCompra(compraForm.getIdUsuarioCompra(), compraForm.getIdJuegoCompra(),
        //        compraForm.getTipoPagoCompra());
        //
        //System.out.println(compra1);

        //System.out.println(c.consultarCompra(1L, 1L));
        //System.out.println(c.consultarCompra(2L, 2L));

        //var resultado = c.procesarPago(1L, TipoMetodoPago.CARTERA_STEAM);
        //System.out.println(resultado.getEstadoCompra());

        //c.solicitarReembolso(1L);

    }


}
