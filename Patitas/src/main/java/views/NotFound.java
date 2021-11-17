package views;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.Map;

public class NotFound {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);
        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "notFound.hbs")
        );
    }
}
