package views.MisMascotas;

import controller.ControllerHogares;
import controller.ControllerMascotaPerdida;
import domain.Hogares.Hogar;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.List;
import java.util.Map;

public class SeleccionarHogarTransito {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        try {
            int idMascotaPerdida = Integer.parseInt(req.queryParams("idMascotaPerdida"));
            model.put("idMascotaPerdida", idMascotaPerdida);
            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(idMascotaPerdida);
            Persona persona = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!persona.getCuil().equals(publicacion.getRescatista().getCuil()))
                throw new Exception(persona.getCuil() + " no creo la publicacion de MascotaPerdida " + publicacion.getId());

//            ControllerHogares.actualizarHogares();
            List<Hogar> hogares = ControllerHogares.hogaresAdecuados(publicacion);
            model.put("hogar", hogares);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "misMascotas/seleccionarHogarTransito.hbs")
        );
    }

    public static String setHogarTransito(Request req, Response res) {
        try {
            int idMascotaPerdida = Integer.parseInt(req.queryParams("idMascotaPerdida"));
            String idHogar = req.queryParams("idHogar");
            Hogar hogar = ControllerHogares.getById(idHogar);

            if(hogar == null)
                throw new Exception("El idHogar no es correcto");

            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(idMascotaPerdida);

            Persona persona = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!persona.getCuil().equals(publicacion.getRescatista().getCuil()))
                throw new Exception(persona.getCuil() + " no creo la publicacion de MascotaPerdida " + publicacion.getId());

            ControllerMascotaPerdida.setHogarPublicacion(publicacion, hogar);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        res.redirect("/misMascotas/?success");
        return null;
    }
}
