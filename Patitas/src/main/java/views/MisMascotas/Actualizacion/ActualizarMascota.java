package views.MisMascotas.Actualizacion;

import controller.ControllerMascota;
import db.EntityManagerHelper;
import domain.Mascota.Mascota;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import spark.Request;
import spark.Response;
import utils.ContactCardHelper;
import utils.HandlebarsUtils.HandlebarsHelper;
import utils.ViewHelper;

import java.util.Map;

public class ActualizarMascota {
    public static String processRequest(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);

            int idMascota = Integer.parseInt(req.params(":idMascota"));
            Mascota mascota = ControllerMascota.getMascotaByID(idMascota);
            Persona personaObj = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!personaObj.equals(mascota.getDuenio()))
                throw new Exception(personaObj.getCuil() + " no es duenio de la Mascota " + mascota.getId());

            model.put("mascota", mascota);

            return HandlebarsHelper.compile(model, "misMascotas/Actualizacion/actualizarMascota");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }
    }

    public static Object actualizarMascota(Request req, Response res) {
        ViewHelper.setMultipartServlet(req);
        try {
            int idMascota = Integer.parseInt(req.params(":idMascota"));
            Mascota mascota = ControllerMascota.getMascotaByID(idMascota);
            Persona personaObj = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!personaObj.equals(mascota.getDuenio()))
                throw new Exception(personaObj.getCuil() + " no es duenio de la Mascota de ID " + mascota.getId());

            Map<String, String> reqInfo = ViewHelper.processMultipartRequest(req, "mascotaImg");

            ContactCardHelper.updateContactFromMascota(reqInfo, mascota);

            res.redirect("/misMascotas/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
        }

        return null;
    }
}
