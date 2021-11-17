package controller;

import controller.Repositorios.AdopcionOfertaAdopcion.SQLRepoAdopcionOfertaAdopcion;
import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardAdopcionPublicacionOfertaAdopcion;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerAdopcionOfertaAdopcion {
    public static final SQLRepoAdopcionOfertaAdopcion repositorio = new SQLRepoAdopcionOfertaAdopcion();

    public static AdopcionPublicacionOfertaAdopcion crearAdopcionPublicacionOfertaAdopcion(AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcionDTO dto) {
        AdopcionPublicacionOfertaAdopcion ret = new AdopcionPublicacionOfertaAdopcion();
        ret.setOfertaAdopcion(dto.ofertaAdopcion);
        ret.setAdoptante(dto.adoptante);
        for(ContactCardAdopcionPublicacionOfertaAdopcion c : dto.contacto)
            c.setPublicacion(ret);
        ret.addContacto(dto.contacto);

        return ret;
    }

    public static List<Integer> getNInteresadosFromListMascota(List<Mascota> m) {
        return m.stream().map(
                ControllerAdopcionOfertaAdopcion::interesadosPorMascota
        ).collect(Collectors.toList());
    }

    public static Integer interesadosPorMascota(Mascota m) {
        return (int) repositorio.getAll().stream().filter(
                p -> p.getOfertaAdopcion().getMascota().getId() == (m.getId())
        ).count();
    }

    public static void persisitrAdopcionPublicacionOfertaAdopcion(AdopcionPublicacionOfertaAdopcion p) {
        repositorio.persist(p);
    }

    public static AdopcionPublicacionOfertaAdopcion getById(int id) {
        return repositorio.getById(id);
    }

    public static List<AdopcionPublicacionOfertaAdopcion> getByPublicacionOfertaAdopcion(PublicacionOfertaAdopcion p) {
        return repositorio.getByPublicacionOfertaAdopcion(p);
    }

    public static void removerTodasAdopcionOfertaAdopcion(PublicacionOfertaAdopcion pub) throws Exception {
        List<AdopcionPublicacionOfertaAdopcion> lista = getByPublicacionOfertaAdopcion(pub);

        // Bulk operation
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        try {
           for (AdopcionPublicacionOfertaAdopcion p : lista) {
               p.getContacto().forEach(s::remove);
               p.getContactCard().forEach(s::remove);
               s.remove(p);
           }
           t.commit();
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
            throw new Exception("No se pudieron eliminar las AdopcionPublicacionOfertaAdopcion de la PublicacionOfertaAdopcion " + pub.getId());
        }
    }

    public static void removerAdopcionOfertaAdopcion(AdopcionPublicacionOfertaAdopcion p) { repositorio.remove(p); }
}