package views.EncontreMascota;

import controller.ControllerMascotaPerdida;
import domain.MascotaPerdida.MascotaPerdida;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.List;
import java.util.Map;

public class AprobarPublicaciones {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        List<MascotaPerdida> sinchapita_pendientes = ControllerMascotaPerdida.obtenerPendientes(
                0, 1000
        );
        model.put("pendientes", sinchapita_pendientes);

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "encontreMascota/aprobarPublicaciones.hbs")
        );
    }

    public static Object aceptarPublicacion(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params(":idPublicacion"));
            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(id);
            ControllerMascotaPerdida.aprobarPublicacion(publicacion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.redirect("/encontreMascota/aprobarPublicaciones/");
        return null;
    }
}
