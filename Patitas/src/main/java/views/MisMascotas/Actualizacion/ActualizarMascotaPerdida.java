package views.MisMascotas.Actualizacion;

import controller.ControllerMascotaPerdida;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import spark.Request;
import spark.Response;
import utils.ContactCardHelper;
import utils.HandlebarsUtils.HandlebarsHelper;
import utils.ViewHelper;

import java.util.Map;

public class ActualizarMascotaPerdida {
    public static String processRequest(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);

            int idPublicacion = Integer.parseInt(req.params(":idPublicacion"));
            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(idPublicacion);
            Persona personaObj = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!personaObj.equals(publicacion.getRescatista()))
                throw new Exception("Persona " + personaObj.getCuil() + " no es el creador de la MascotaPerdida de ID " + publicacion.getId());

            model.put("publicacion", publicacion);

            return HandlebarsHelper.compile(model, "misMascotas/Actualizacion/actualizarMascotaPerdida");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }
    }

    public static String actualizarMascotaPerdida(Request req, Response res) {
        ViewHelper.setMultipartServlet(req);
        try {
            int idPublicacion = Integer.parseInt(req.params(":idPublicacion"));
            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(idPublicacion);
            Persona personaObj = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!personaObj.equals(publicacion.getRescatista()))
                throw new Exception("Persona " + personaObj.getCuil() + " no es el creador de la MascotaPerdida de ID " + publicacion.getId());

            Map<String, String> reqInfo = ViewHelper.processMultipartRequest(req, "mascotaImg");
            ContactCardHelper.updateContactFromMascotaPerdida(reqInfo, publicacion);
            res.redirect("/misMascotas/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
        }
        return null;
    }
}
