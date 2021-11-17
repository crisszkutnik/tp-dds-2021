package controller.Repositorios.MascotaEncontrada;

import controller.Repositorios.SQLRepo;
import db.HibernateUtil;
import domain.MascotaEncontrada.MascotaEncontrada;
import domain.MascotaPerdida.MascotaPerdida;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;

public class SQLRepoMascotaEncontrada extends SQLRepo<MascotaEncontrada> implements GenericRepoMascotaEncontrada {
    public SQLRepoMascotaEncontrada() {
            this.dbName = "MascotaEncontrada";
    }

    public MascotaEncontrada getByid(int id) {
        return (MascotaEncontrada) executeQuerySingleResult("FROM MascotaEncontrada WHERE id=" + id);
    }

    public boolean existeMascotaEncontrada(MascotaPerdida p) {
        try {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            s.createQuery("FROM MascotaEncontrada WHERE id_mascota_perdida=" + p.getId()).setMaxResults(1).getSingleResult();
            t.commit();
            s.close();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
