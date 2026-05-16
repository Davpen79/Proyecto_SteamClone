package org.davpen.mapper;

import org.davpen.modelo.dto.*;
import org.davpen.modelo.entity.*;

import java.util.Optional;

public class Mapper {

    public static UsuarioDto mapaUsuarioCompleto(UsuarioEntity usuario) {

        if (usuario == null) {
            return null;
        }

        return new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getNombreCuentaUsuario(),
                usuario.getEmailUsuario(),
                usuario.getNombreRealUsuario(),
                usuario.getPaisUsuario(),
                usuario.getFechaNacUsuario(),
                usuario.getFechaRegUsuario(),
                usuario.getAvatarUsuario(),
                usuario.getSaldoUsuario(),
                usuario.getEstadoCuentaUsuario());
    }

    public static JuegoDto mapaJuegoCompleto(JuegoEntity juego) {

        if (juego == null) {
            return null;
        }

        return new JuegoDto(
                juego.getIdJuego(),
                juego.getTituloJuego(),
                juego.getDescripcionJuego(),
                juego.getDesarrolladorJuego(),
                juego.getFechaLanzaJuego(),
                juego.getPrecioBaseJuego(),
                juego.getDescuentoActualJuego(),
                juego.getCategoriaJuego(),
                juego.getClasEdadJuego(),
                juego.getIdiomasJuego(),
                juego.getEstadoJuego());
    }

    public static BibliotecaDto mapaSimple(BibliotecaEntity biblioteca) {

        if (biblioteca == null) {
            return null;
        }

        return new BibliotecaDto(
                biblioteca.getIdBiblio(),
                biblioteca.getIdUsuarioBiblio(),
                Optional.empty(),
                biblioteca.getIdJuegoBiblio(),
                Optional.empty(),
                biblioteca.getFechaCompraJuegoBiblio(),
                biblioteca.getTiempoJuegoBiblio(),
                biblioteca.getUltiFechaJuegoBiblio(),
                biblioteca.getEstadoInstJuegoBiblio());
    }

    public static BibliotecaDto mapaCompleto(BibliotecaEntity biblioteca, UsuarioEntity usuario, JuegoEntity juego) {

        if (biblioteca == null) {
            return null;
        }

        return new BibliotecaDto(
                biblioteca.getIdBiblio(),
                biblioteca.getIdUsuarioBiblio(),
                Optional.of(mapaUsuarioCompleto(usuario)),
                biblioteca.getIdJuegoBiblio(),
                Optional.of(mapaJuegoCompleto(juego)),
                biblioteca.getFechaCompraJuegoBiblio(),
                biblioteca.getTiempoJuegoBiblio(),
                biblioteca.getUltiFechaJuegoBiblio(),
                biblioteca.getEstadoInstJuegoBiblio());
    }

    public static CompraDto mapaCompraSimple(CompraEntity compra) {

        if (compra == null) {
            return null;
        }

        return new CompraDto(
                compra.getIdCompra(),
                compra.getIdUsuarioCompra(),
                Optional.empty(),
                compra.getIdJuegoCompra(),
                Optional.empty(),
                compra.getFechaCompra(),
                compra.getTipoPagoCompra(),
                compra.getPrecioBaseCompra(),
                compra.getDescuentoEnCompra(),
                compra.getEstadoCompra());
    }

    public static CompraDto mapaCompraCompleto(CompraEntity compra, UsuarioEntity usuario, JuegoEntity juego) {

        if (compra == null) {
            return null;
        }

        return new CompraDto(
                compra.getIdCompra(),
                compra.getIdUsuarioCompra(),
                Optional.of(mapaUsuarioCompleto(usuario)),
                compra.getIdJuegoCompra(),
                Optional.of(mapaJuegoCompleto(juego)),
                compra.getFechaCompra(),
                compra.getTipoPagoCompra(),
                compra.getPrecioBaseCompra(),
                compra.getDescuentoEnCompra(),
                compra.getEstadoCompra());
    }

    public static ResenhaDto mapaSimple(ResenhaEntity resenha) {

        if (resenha == null) {
            return null;
        }

        return new ResenhaDto(
                resenha.getIdResenha(),
                resenha.getIdUsuarioResenha(),
                Optional.empty(),
                resenha.getIdJuegoResenha(),
                Optional.empty(),
                resenha.isRecomendacionResenha(),
                resenha.getTextoResenha(),
                resenha.getTiempoJugadoResenha(),
                resenha.getFechaPublicacionResenha(),
                resenha.getFechaUltiEdicResenha(),
                resenha.getEstadoResenha());
    }

    public static ResenhaDto mapaCompleto(ResenhaEntity resenha, UsuarioEntity usuario, JuegoEntity juego) {

        if (resenha == null) {
            return null;
        }
        return new ResenhaDto(
                resenha.getIdResenha(),
                resenha.getIdUsuarioResenha(),
                Optional.of(mapaUsuarioCompleto(usuario)),
                resenha.getIdJuegoResenha(),
                Optional.of(mapaJuegoCompleto(juego)),
                resenha.isRecomendacionResenha(),
                resenha.getTextoResenha(),
                resenha.getTiempoJugadoResenha(),
                resenha.getFechaPublicacionResenha(),
                resenha.getFechaUltiEdicResenha(),
                resenha.getEstadoResenha()
        );

    }

}
