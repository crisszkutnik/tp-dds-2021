package views.EncontreMascota;

import controller.ControllerMascota;
import controller.ControllerMascotaPerdida;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import domain.MascotaPerdida.MascotaPerdida;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.Map;

public class SinChapita {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);
        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "encontreMascota/sinChapita.hbs")
        );
    }

    public static String registerMascotaPerdida(Request req, Response res) {
        try {
            ViewHelper.setMultipartServlet(req);
            MascotaPerdida.MascotaPerdidaDTO dto;
            Map<String, String> reqInfo = ViewHelper.processMultipartRequest(req, "mascotaImg");
            dto = ParseRegistroMascota.parse(req, res, reqInfo);
            MascotaPerdida mp = ControllerMascotaPerdida.generarMascotaPerdidaDesconocida(dto);
            mp.setTipo(TipoAnimal.valueOf(reqInfo.get("Tipo").toUpperCase()));
            mp.setTamanio(TamanioMascota.valueOf(reqInfo.get("Tamanio").toUpperCase()));
            ControllerMascotaPerdida.persistirMascotaPerdida(mp);
        } catch(Exception e) {
            e.printStackTrace();
            res.redirect("/?error");
            return null;
        }

        res.redirect("/?success");
        return null;
    }
}
