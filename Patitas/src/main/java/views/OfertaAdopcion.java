package views;

import controller.ControllerAdopcionOfertaAdopcion;
import controller.ControllerOfertaAdopcion;
import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactPerClass.ContactCardAdopcionPublicacionOfertaAdopcion;
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

public class OfertaAdopcion {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        try {
            int id = Integer.parseInt(req.params(":idOferta"));
            PublicacionOfertaAdopcion pub = ControllerOfertaAdopcion.getOfertaAdopcionById(id);
            Persona personaObj = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(personaObj.equals(pub.getMascota().getDuenio()))
                throw new Exception("Una persona no puede adoptar una Mascota de la que es duenio");

            model.put("publicacion", pub);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/"); // con que haga un 404 me alcanza
            return null;
        }

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "quieroAdoptar/ofertaAdopcionCompleta.hbs")
        );
    }

    public static String crearAdopcionOfertaAdopcion(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params(":idOferta"));
            Map<String, String> reqInfo = ViewHelper.createReqInfoForNormalRequest(req);

            AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcionDTO dto = new AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcionDTO();
            dto.contacto = ContactCardHelper.buildContactCardAdopcionOfertaAdopcionList(reqInfo);
            dto.ofertaAdopcion = ControllerOfertaAdopcion.getOfertaAdopcionById(id);
            dto.adoptante = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(dto.adoptante.equals(dto.ofertaAdopcion.getMascota().getDuenio()))
                throw new Exception("Una persona no puede adoptar una Mascota de la que es duenio");

            ControllerAdopcionOfertaAdopcion.persisitrAdopcionPublicacionOfertaAdopcion(
                    ControllerAdopcionOfertaAdopcion.crearAdopcionPublicacionOfertaAdopcion(dto)
            );
        }catch (Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        res.redirect("/misMascotas/?success");
        return null;
    }
}
