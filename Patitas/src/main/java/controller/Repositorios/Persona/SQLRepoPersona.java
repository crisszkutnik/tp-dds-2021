package controller.Repositorios.Persona;

import controller.Repositorios.SQLRepo;
import domain.Usuario.Persona;

import javax.persistence.NoResultException;

public class SQLRepoPersona extends SQLRepo<Persona> implements GenericRepoPersona {
    public SQLRepoPersona() {
        this.dbName = "Persona";
    }

    public Persona getSingleFromRepo(String cuil) throws NoResultException {
        return (Persona) executeQuerySingleResult("FROM Persona WHERE cuil='" + cuil + "'");
    }
}
