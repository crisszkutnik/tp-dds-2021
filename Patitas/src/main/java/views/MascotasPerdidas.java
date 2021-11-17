package views;

import controller.ControllerMascotaPerdida;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MascotasPerdidas {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        List<MascotaPerdida> lista = ControllerMascotaPerdida.obtenerAprobadasSinChapita(0, 1000);
        Usuario user = req.session().attribute("userObj");

        if(user != null)
            lista = lista.stream().filter(t -> !t.getRescatista().equals(user.getPersona())).collect(Collectors.toList());

        model.put("publicaciones", lista);

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "mascotasPerdidas.hbs")
        );
    }
}
