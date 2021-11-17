package controller.Repositorios.Usuario;

import controller.Repositorios.SQLRepo;
import domain.Usuario.Usuario;

public interface GenericRepoUsuario {
    Usuario fetchByUsername(String username);
}
