package views;

import controller.ControllerOfertaAdopcion;
import domain.ContactCard.ContactPerClass.ContactCardPublicacionDeseoAdoptar;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ContactCardHelper;
import utils.ViewHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuieroAdoptar {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        try {
            Preferencias p = ViewHelper.getPreferenciasFromRequest(req);
            Persona per = ((Usuario) req.session().attribute("userObj")).getPersona();
            List<PublicacionOfertaAdopcion> ofertas = ControllerOfertaAdopcion.getOfertasAdopcionByParams(p, per);

            model.put("busqueda", true);
            model.put("publicaciones", ofertas);
            model.put("preferencias", p);
        } catch (Exception e) {
            model.put("busqueda", false);
        }

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "quieroAdoptar/quieroAdoptar.hbs")
        );
    }

    public static String registrarSolicitud(Request req, Response res) {
        try {
            Map<String,String> reqInfo = ViewHelper.createReqInfoForNormalRequest(req);
            Usuario user = req.session().attribute("userObj");
            Preferencias p = ViewHelper.getPreferenciasFromRequest(req);
            List<ContactCardPublicacionDeseoAdoptar> contact = ContactCardHelper.buildContactCardPublicacionDeseoAdoptarList(reqInfo);
            PublicacionDeseoAdoptar publicacion = ControllerOfertaAdopcion.crearDeseoAdoptar(
                    user.getPersona(), p, contact
            );
            ControllerOfertaAdopcion.persistirDeseoAdoptar(publicacion);
            res.redirect("/misMascotas/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        return null;
    }

    public static String darDeBajaSolicitud(Request req, Response res) {
        try {
            int pub_id = Integer.parseInt(req.queryParams("idPublicacion"));
            PublicacionDeseoAdoptar pub = ControllerOfertaAdopcion.getDeseoAdoptarById(pub_id);
            ControllerOfertaAdopcion.eliminarDeseoAdoptar(pub);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        res.redirect("/misMascotas/?success");
        return null;
    }
}
