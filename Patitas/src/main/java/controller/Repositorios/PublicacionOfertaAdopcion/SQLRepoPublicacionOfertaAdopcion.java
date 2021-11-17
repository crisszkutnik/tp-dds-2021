package controller.Repositorios.PublicacionOfertaAdopcion;

import controller.Repositorios.SQLRepo;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TipoAnimal;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;

public class SQLRepoPublicacionOfertaAdopcion extends SQLRepo<PublicacionOfertaAdopcion> implements GenericRepoPublicacionOfertaAdopcion {
    public SQLRepoPublicacionOfertaAdopcion() {
        this.dbName = "PublicacionOfertaAdopcion";
    }

    public PublicacionOfertaAdopcion getByMascota(Mascota mascota) {
        return (PublicacionOfertaAdopcion) executeQuerySingleResult("FROM PublicacionOfertaAdopcion WHERE id_mascota=" + mascota.getId());
    }

    public PublicacionOfertaAdopcion getById(int id) {
        return (PublicacionOfertaAdopcion) executeQuerySingleResult("FROM PublicacionOfertaAdopcion WHERE id=" + id);
    }

    public List<PublicacionOfertaAdopcion> getAll() {
        return executeQueryListResult("FROM PublicacionOfertaAdopcion", PublicacionOfertaAdopcion.class);
    }

    public List<PublicacionOfertaAdopcion> getByAsociacion(Asociacion a) {
        return executeQueryListResult("FROM PublicacionOfertaAdopcion WHERE id_asociacion=" + a.getId(), PublicacionOfertaAdopcion.class);
    }
}