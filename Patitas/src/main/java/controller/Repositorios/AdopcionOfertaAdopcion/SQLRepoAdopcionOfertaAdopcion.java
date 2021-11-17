package controller.Repositorios.AdopcionOfertaAdopcion;

import controller.Repositorios.SQLRepo;
import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class SQLRepoAdopcionOfertaAdopcion extends SQLRepo<AdopcionPublicacionOfertaAdopcion> {
    public SQLRepoAdopcionOfertaAdopcion() {
        this.dbName = "AdopcionPublicacionOfertaAdopcion";
    }

    public List<AdopcionPublicacionOfertaAdopcion> getAll() {
        return executeQueryListResult(
                "FROM AdopcionPublicacionOfertaAdopcion",
                AdopcionPublicacionOfertaAdopcion.class
        );
    }

    public AdopcionPublicacionOfertaAdopcion getById(int id) {
        return (AdopcionPublicacionOfertaAdopcion) executeQuerySingleResult("FROM AdopcionPublicacionOfertaAdopcion WHERE id=" + id);
    }

    public List<AdopcionPublicacionOfertaAdopcion> getByPublicacionOfertaAdopcion(PublicacionOfertaAdopcion p) {
        return executeQueryListResult("FROM AdopcionPublicacionOfertaAdopcion WHERE id_oferta_adopcion=" + p.getId(), AdopcionPublicacionOfertaAdopcion.class);
    }
}
