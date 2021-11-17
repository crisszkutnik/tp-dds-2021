package views.MisMascotas;

import controller.ControllerAdopcionOfertaAdopcion;
import controller.ControllerOfertaAdopcion;
import controller.ControllerMascota;
import controller.Repositorios.SQLRepo;
import db.HibernateUtil;
import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactPerClass.ContactCardAdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactPerClass.ContactCardMascota;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InteresadosAdopcion {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        try {
            int id = Integer.parseInt(req.params(":idMascota"));
            PublicacionOfertaAdopcion publicacion = ControllerOfertaAdopcion.getPublicacionOfertaAdopcionByMascota(
                    ControllerMascota.getMascotaByID(id)
            );
            Persona persona = ((Usuario) req.session().attribute("userObj")).getPersona();

            // Basicamente, el usuario tiene que ser el duenio de la mascota que esta publicada
            if(!persona.getCuil().equals(publicacion.getMascota().getDuenio().getCuil()))
                throw new Exception(persona.getCuil() + " no es el creador de la PublicacionOfertaAdopcion " + publicacion.getId());

            List<AdopcionPublicacionOfertaAdopcion> interesados = ControllerAdopcionOfertaAdopcion.getByPublicacionOfertaAdopcion(publicacion);
            model.put("interesado", interesados);
            model.put("publicacion", publicacion);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }


        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "misMascotas/interesadosAdopcion.hbs")
        );
    }

    public static Object transferirMascota(Request req, Response res) {
        try {
            int idPublicacion = Integer.parseInt(req.params("idPublicacion"));
            int idPublicacionInteresado = Integer.parseInt(req.params(":idPublicacionInteresado"));

            PublicacionOfertaAdopcion publicacion = ControllerOfertaAdopcion.getOfertaAdopcionById(idPublicacion);
            AdopcionPublicacionOfertaAdopcion publicacionInteresado = ControllerAdopcionOfertaAdopcion.getById(idPublicacionInteresado);
            Mascota mascota = publicacion.getMascota();
            Persona interesado = publicacionInteresado.getAdoptante();

            List<ContactCardMascota> contactoViejo = mascota.getContacto();
            List<ContactCardAdopcionPublicacionOfertaAdopcion> contactoNuevoFormatoViejo = publicacionInteresado.getContacto();
            mascota.setContacto(
                    contactoNuevoFormatoViejo.stream().map(t -> t.convertToContactCardMascota(mascota)).collect(Collectors.toList())
            );
            mascota.setDuenio(interesado);

            List<AdopcionPublicacionOfertaAdopcion> todasMenosSeleccionada = ControllerAdopcionOfertaAdopcion.getByPublicacionOfertaAdopcion(publicacion);
            todasMenosSeleccionada = todasMenosSeleccionada.stream().filter(t -> t.getId() != publicacionInteresado.getId()).collect(Collectors.toList());

            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            try {
                // Eliminamos las AdopcionPublicacionOfertaAdopcion
                todasMenosSeleccionada.forEach(
                        p -> p.getContacto().forEach(c -> s.remove(c.getContact()))
                );
                todasMenosSeleccionada.forEach(s::remove);

                // Eliminamos la AdopcionPublicacionOfertaAdopcion seleccionada
                s.remove(publicacionInteresado);

                // Eliminamos los contactos viejos
                contactoViejo.forEach(c -> s.remove(c.getContact()));
                contactoViejo.forEach(s::remove);

                // Actualizamos estado
                mascota.setEnAdopcion(false);

                // Actualizamos la mascota
                s.merge(mascota);

                // Borramos la PublicacionOfertaAdopcion
                s.remove(publicacion);
                t.commit();
            } catch (Exception e) {
                t.rollback();
            }
            s.close();
           res.redirect("/misMascotas/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
        }
        return null;
    }
}
