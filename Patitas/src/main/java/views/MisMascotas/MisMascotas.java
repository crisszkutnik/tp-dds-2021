package views.MisMascotas;

import controller.*;
import db.HibernateUtil;
import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactPerClass.ContactCardMascotaEncontrada;
import domain.Excepciones.NoEsDuenioDeMascotaException;
import domain.Mascota.Mascota;
import domain.MascotaEncontrada.MascotaEncontrada;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import jdk.nashorn.internal.objects.Global;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.GlobalConfig;
import utils.HandlebarsUtils.HandlebarsHelper;
import utils.ViewHelper;

import javax.swing.text.View;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MisMascotas {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);
        Persona persona = ((Usuario) req.session().attribute("userObj")).getPersona();

        model.put(
            "mascotas",
            ControllerMascota.getByDuenio(persona)
        );

        model.put(
            "n_interesados",
            ControllerAdopcionOfertaAdopcion.getNInteresadosFromListMascota(ControllerMascota.getByDuenio(persona))
        );

        List<MascotaPerdida> p = ControllerMascotaPerdida.getByPersona(persona);
        List<ControllerMascotaEncontrada.Reclamos> reclamos = ControllerMascotaEncontrada.reclamosParaPerdidasOrNull(p);
        List<Boolean> te_odio_java = reclamos.stream().map(r -> r.getListaReclamos().size()!=0).collect(Collectors.toList());

        model.put(
            "mascotasEncontradas",
            p
        );

        model.put(
            "mascotasReclamadas",
            reclamos
        );

        model.put(
            "fueReclamado",
            te_odio_java
        );

        model.put(
            "publicacionesDeseoAdoptar",
            ControllerOfertaAdopcion.getDeseoAdoptarByPersona(persona)
        );

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "misMascotas/misMascotas.hbs")
        );
    }

    public static Object verQR(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);
            int id = Integer.parseInt(req.params(":id"));

            if(GlobalConfig.productionEnv())
                model.put("link", "https://tp-dds-2021.herokuapp.com/qr/" + id + ".PNG");
            else
                model.put("link", "http://localhost:4567/qr/" + id + ".PNG");

            return HandlebarsHelper.compile(model, "misMascotas/verQR");
        } catch (Exception e) {
            res.redirect("/?error");
            return null;
        }
    }

    public static String removeAdopcion(Request req, Response res) {
        try {
            int idMascota = Integer.parseInt(req.queryParams("idMascota"));
            Mascota mascota = ControllerMascota.getMascotaByID(idMascota);
            Persona per = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!mascota.getDuenio().getCuil().equals(per.getCuil()))
                throw new NoEsDuenioDeMascotaException(mascota, per);

            ControllerOfertaAdopcion.removerOfertaAdopcion(mascota);
        } catch(Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        res.redirect("/misMascotas/?success");
        return null;
    }

    public static Object removerMascotaPerdida(Request req, Response res) {
        try {
            Usuario user = (Usuario) req.session().attribute("userObj");
            int idMascotaPerdida = Integer.parseInt(req.queryParams("idMascotaPerdida"));
            MascotaPerdida mp = ControllerMascotaPerdida.getById(idMascotaPerdida);

            if(!mp.getRescatista().equals(user.getPersona()))
                throw new Exception();

            List<MascotaEncontrada> me = ControllerMascotaEncontrada.getMascotaEncontradaByMascotaPerdida(mp);
            // Eliminar la MascotaPerdidas -> Se eliminan las ContactCard y las fotos
            // Eliminar las ContactCardMascotaEncontrada
            // Eliminar las MascotaEncontrada

            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();

            try {
                me.forEach(m -> {
                    m.getContacto().forEach(s::remove);
                    s.remove(m);
                });
                s.remove(mp);
                t.commit();
                s.close();
            } catch (Exception e) {
                t.rollback();
                s.close();
                throw new Exception("Error borrando los MascotaPerdida");
            }
            res.redirect("/misMascotas/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
        }
        return null;
    }
}
