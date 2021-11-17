package controller.Repositorios.PublicacionDeseoAdoptar;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Mascota.Mascota;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.Usuario.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SQLRepoPublicacionDeseoAdoptar extends SQLRepo<PublicacionDeseoAdoptar> implements GenericRepoPublicacionDeseoAdoptar {
    public SQLRepoPublicacionDeseoAdoptar() {
        this.dbName = "PublicacionDeseoAdoptar";
    }

    public PublicacionDeseoAdoptar getById(int id) {
        /*return (PublicacionDeseoAdoptar) executeQuery(
                "FROM PublicacionDeseoAdoptar WHERE id=" + id
        ).getSingleResult();*/
        return (PublicacionDeseoAdoptar) executeQuerySingleResult("FROM PublicacionDeseoAdoptar WHERE id=" + id);
    }

    public List<PublicacionDeseoAdoptar> findAll() {
        // Esto esta asi porque el createQuery necesita dos params para devolver lista
        /*return EntityManagerHelper.getEntityManager().createQuery(
                "FROM PublicacionDeseoAdoptar", PublicacionDeseoAdoptar.class
        ).getResultList();
         */
        /*Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        List<PublicacionDeseoAdoptar> ret;
        try {
            ret = s.createQuery("FROM PublicacionDeseoAdoptar", PublicacionDeseoAdoptar.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            s.close();
            return null;
        }
        s.close();
        return ret;*/
        return executeQueryListResult("FROM PublicacionDeseoAdoptar", PublicacionDeseoAdoptar.class);
    }

    public List<PublicacionDeseoAdoptar> getByPersona(Persona persona) {
        // Esto esta asi porque el createQuery necesita dos params para devolver lista
        /*return EntityManagerHelper.getEntityManager().createQuery(
                "FROM PublicacionDeseoAdoptar WHERE cuil_persona='" + persona.getCuil() + "'", PublicacionDeseoAdoptar.class
        ).getResultList();*/
        /*Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        List<PublicacionDeseoAdoptar> ret;
        try {
            ret = s.createQuery("FROM PublicacionDeseoAdoptar WHERE cuil_persona='" + persona.getCuil() + "'", PublicacionDeseoAdoptar.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            s.close();
            return null;
        }
        s.close();
        return ret;*/
        return executeQueryListResult("FROM PublicacionDeseoAdoptar WHERE cuil_persona='" + persona.getCuil() + "'", PublicacionDeseoAdoptar.class);
    }
}
