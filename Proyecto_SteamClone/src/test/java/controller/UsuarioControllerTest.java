package controller;

import org.davpen.controller.UsuarioController;
import org.davpen.excepciones.ValidationException;
import org.davpen.repositorio.inmemory.UsuarioRepoInMemory;
import org.davpen.repositorio.intefaces.IUsuarioRepo;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private IUsuarioRepo usuarioRepo = new UsuarioRepoInMemory();

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    public void testRegistrarUsuario_UsuarioCorrecto_UsuarioDto() throws ValidationException {



    }
}
