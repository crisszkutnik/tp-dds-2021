package controller.Repositorios.Asociaciones;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.MascotaPerdida.MascotaPerdida;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

public class SQLRepoAsociaciones extends SQLRepo<Asociacion> implements GenericRepoAsociaciones {
    public SQLRepoAsociaciones() {
        this.dbName = "Asociacion";
    }

    public Asociacion getAsociacionByName(String name) throws NoResultException {
        return (Asociacion) executeQuerySingleResult("FROM Asociacion WHERE nombre='" + name + "'");
    }

    public List<Asociacion> getAll() throws NoResultException {
        return executeQueryListResult("FROM Asociacion", Asociacion.class);
    }

    public Asociacion getById(int id) {
        return (Asociacion) executeQuerySingleResult("FROM Asociacion WHERE id=" + id);
    }
}
