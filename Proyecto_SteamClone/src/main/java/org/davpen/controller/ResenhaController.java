package org.davpen.controller;

import org.davpen.modelo.dto.ResenhaDto;
import org.davpen.modelo.form.ResenhaForm;
import org.davpen.repositorio.intefaces.IJuegoRepo;
import org.davpen.repositorio.intefaces.IResenhaRepo;
import org.davpen.repositorio.intefaces.IUsuarioRepo;

public class ResenhaController {

    private final IResenhaRepo resenhaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegoRepo juegoRepo;

    public ResenhaController(IResenhaRepo resenhaRepo, IUsuarioRepo usuarioRepo, IJuegoRepo juegoRepo) {
        this.resenhaRepo = resenhaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
    }

    //Escribir reseña



    //Ocultar reseña



    //Eliminar reseña



    //Ver reseñas de un juego



    //Ver reseñas de un usuario



    //Consultar estadisticas de reseñas (Ficheros)
}
