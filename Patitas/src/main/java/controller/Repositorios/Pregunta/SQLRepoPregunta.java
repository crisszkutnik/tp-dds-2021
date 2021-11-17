package controller.Repositorios.Pregunta;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.Preguntas.Pregunta;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;

public class SQLRepoPregunta extends SQLRepo<Pregunta> implements GenericRepoPregunta {
    public SQLRepoPregunta() {
        this.dbName = "Pregunta";
    }

    public void deleteById(int id) {
        executeQuery("DELETE FROM Pregunta WHERE id=" + id);
    }

    public List<Pregunta> getByAsociacion(Asociacion a) {
        return executeQueryListResult("FROM Pregunta WHERE id_asociacion=" + a.getId(), Pregunta.class);
    }

    public List<Pregunta> getPreguntasGenericas() {
        return executeQueryListResult("FROM Pregunta WHERE id_asociacion IS NULL", Pregunta.class);
    }

    public Pregunta getById(int id) {
        return (Pregunta) executeQuerySingleResult("FROM Pregunta WHERE id=" + id);
    }
}
