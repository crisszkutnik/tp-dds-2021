package controller.Repositorios.Mascota;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Hogares.Hogar;
import domain.Mascota.Mascota;
import domain.Usuario.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;

public class SQLRepoMascota extends SQLRepo<Mascota> implements GenericRepoMascota {
    public SQLRepoMascota() {
        this.dbName = "Mascota";
    }

    public List<Mascota> getMascotasByDuenio(Persona duenio) throws NoResultException {
        return executeQueryListResult("FROM Mascota WHERE cuil_duenio='" + duenio.getCuil() + "'", Mascota.class);
    }

    public Mascota getMascotaByID(int id) throws NoResultException {
        return (Mascota) executeQuerySingleResult("FROM Mascota WHERE id=" + id);
    }
}
