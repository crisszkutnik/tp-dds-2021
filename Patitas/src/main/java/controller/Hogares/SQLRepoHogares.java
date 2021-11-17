package controller.Hogares;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import domain.Hogares.Hogar;

import javax.persistence.NoResultException;
import java.util.List;

public class SQLRepoHogares extends SQLRepo<Hogar> implements GenericRepoHogares {
    public SQLRepoHogares() { this.dbName = "hogares"; }

    public Hogar getHogarById(String id) {
        return (Hogar) executeQuerySingleResult("FROM Hogar WHERE id='" + id + "'");
    }

    public List<Hogar> findAll() {
        return executeQueryListResult("FROM Hogar", Hogar.class);
    }
}
