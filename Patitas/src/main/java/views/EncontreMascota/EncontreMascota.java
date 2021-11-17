package views.EncontreMascota;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;
import spark.Request;
import spark.Response;

import java.util.Map;

public class EncontreMascota {
    public static String renderLandingPage(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "encontreMascota/encontreMascotaPage.hbs")
        );
    }
}
