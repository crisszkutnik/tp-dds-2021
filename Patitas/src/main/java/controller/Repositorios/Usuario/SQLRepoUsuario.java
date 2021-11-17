package controller.Repositorios.Usuario;

import controller.Repositorios.SQLRepo;
import domain.Usuario.Usuario;

import javax.persistence.NoResultException;

public class SQLRepoUsuario extends SQLRepo<Usuario> implements GenericRepoUsuario {
    public SQLRepoUsuario() {
        this.dbName = "Usuario";
    }

    public Usuario fetchByUsername(String username) throws NoResultException {
        return (Usuario) executeQuerySingleResult("FROM Usuario WHERE username='" + username + "'");
    }
}
