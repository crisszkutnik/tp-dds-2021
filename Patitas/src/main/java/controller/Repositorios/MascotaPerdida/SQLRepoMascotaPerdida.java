package controller.Repositorios.MascotaPerdida;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Mascota.Mascota;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

public class SQLRepoMascotaPerdida extends SQLRepo<MascotaPerdida> implements GenericRepoMascotaPerdida {
    public SQLRepoMascotaPerdida() { this.dbName = "mascotas_perdidas"; }

    public MascotaPerdida getById(int id) {
        return (MascotaPerdida) executeQuerySingleResult("FROM MascotaPerdida WHERE id=" + id);
    }

    public List<MascotaPerdida> getPendientes(int offset, int cantidad) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        List<MascotaPerdida> ret;
        try {
            ret = s.createQuery("FROM MascotaPerdida WHERE aprobada=FALSE", MascotaPerdida.class)
                    .getResultList().stream().skip(offset).limit(cantidad).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            s.close();
            return null;
        }
        s.close();
        return ret;
    }

    public List<MascotaPerdida> getAprobadasSinChapita(int offset, int cantidad) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        List<MascotaPerdida> ret;
        try {
            ret = s.createQuery("FROM MascotaPerdida WHERE aprobada=TRUE AND id_mascota IS NULL", MascotaPerdida.class)
                    .getResultList().stream().skip(offset).limit(cantidad).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            s.close();
            return null;
        }
        s.close();
        return ret;
    }

    public List<MascotaPerdida> getByPersona(Persona persona) {
        return executeQueryListResult("FROM MascotaPerdida WHERE cuil_rescatista=" + persona.getCuil(), MascotaPerdida.class);
    }
}
