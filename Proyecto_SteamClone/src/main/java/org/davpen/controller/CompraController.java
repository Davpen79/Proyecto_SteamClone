package org.davpen.controller;

import org.davpen.enums.TipoEstadoCompra;
import org.davpen.enums.TipoEstadoCuenta;
import org.davpen.enums.TipoEstadoJuego;
import org.davpen.enums.TipoMetodoPago;
import org.davpen.excepciones.ValidationException;
import org.davpen.mapper.Mapper;
import org.davpen.modelo.dto.CompraDto;
import org.davpen.modelo.form.CompraForm;
import org.davpen.modelo.form.ErrorDto;
import org.davpen.modelo.form.ErrorType;
import org.davpen.repositorio.intefaces.IBibliotecaRepo;
import org.davpen.repositorio.intefaces.ICompraRepo;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraController {

    private final ICompraRepo compraRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;
    private final IBibliotecaRepo bibliotecaRepo;

    public CompraController(ICompraRepo compraRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo, IBibliotecaRepo bibliotecaRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
    }

    //Realizar compra => relacion con añadir juego a biblioteca
    public Long realizarCompra(Long idUsuario, Long idJuego, TipoMetodoPago metodoPago){
        //Validar
        var errores = new ArrayList<ErrorDto>();
        //usuario activo
        if (!usuarioRepo.obtenerPorId(idUsuario).get().getEstadoCuentaUsuario().equals(TipoEstadoCuenta.ACTIVA)){
            errores.add(new ErrorDto("estado_cuenta", ErrorType.CUENTA_INACTIVA));
        }
        //juego comprable
        if (juegoRepo.obtenerPorId(idJuego).get().getEstadoJuego().equals(TipoEstadoJuego.NO_DISPONIBLE)){
            errores.add(new ErrorDto("estado_juego", ErrorType.NO_DISPONIBLE));
        }
        //juego duplicado
        if (bibliotecaRepo.obtenerTodos().stream()
                .filter(b -> b.getIdUsuarioBiblio().equals(idUsuario))
                .anyMatch(b -> b.getIdJuegoBiblio().equals(idJuego))){
            errores.add(new ErrorDto("id_juego", ErrorType.DUPLICADO));
        }
        //saldo suficiente ¿¿SI USA CARTERA??
        var saldoCartera = usuarioRepo.obtenerPorId(idUsuario).get().getSaldoUsuario();
        var precioCompra = juegoRepo.obtenerPorId(idJuego).get().getPrecioBaseJuego();
        var descuentoCompra = juegoRepo.obtenerPorId(idJuego).get().getDescuentoActualJuego();
        var precioFinal = precioCompra - (precioCompra * descuentoCompra /100);
        if (metodoPago == TipoMetodoPago.CARTERA_STEAM && saldoCartera < precioFinal){
            errores.add(new ErrorDto("saldo_cartera", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        CompraForm compraForm = new CompraForm(idUsuario,idJuego, LocalDate.now(),metodoPago,precioCompra,
                                                descuentoCompra, TipoEstadoCompra.COMPLETADA);
        compraRepo.crear(compraForm);
        var idCompraEfectuada = compraRepo.obtenerTodos().stream()
                                .filter(c -> c.getIdUsuarioCompra().equals(idUsuario))
                                .filter(c -> c.getIdJuegoCompra().equals(idJuego)).findFirst()
                                .get().getIdCompra();

        return idCompraEfectuada;
    }


    //TODO Procesar pago ¿¿Datos según metodo de pago??



    //Consultar detalles de compra - TODO Factura + INFO detallada
    public CompraDto consultarHistorialCompras(Long idCompra, Long idUsuario) throws ValidationException {
        //Validaciones
        var errores = new ArrayList<ErrorDto>();
        //Compra existe
        if (!compraRepo.obtenerPorId(idCompra).isPresent()){
            errores.add(new ErrorDto("id_compra", ErrorType.NO_ENCONTRADO));
        }
        //verificar pertenencia de compra a usuario
        var compraConsultada = compraRepo.obtenerPorId(idCompra);
        var idUsuarioEnCompra = compraConsultada.get().getIdUsuarioCompra();
        if (idUsuarioEnCompra != idUsuario){
            errores.add(new ErrorDto("id", ErrorType.NO_PERTENECE));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        var compra = compraConsultada.orElse(null);
        return Mapper.mapaSimple(compra);

    }


    //solicitar reembolso



    //Consultar historial de compras(Ficheros)



    //Generar factura (Ficheros)



}
