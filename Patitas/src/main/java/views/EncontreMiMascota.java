package views;

import controller.ControllerMascotaEncontrada;
import controller.ControllerMascotaPerdida;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascotaEncontrada;
import domain.Mascota.Mascota;
import domain.MascotaEncontrada.MascotaEncontrada;
import domain.MascotaPerdida.MascotaPerdida;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ContactCardHelper;
import utils.ViewHelper;

import javax.swing.text.View;
import java.util.Map;

public class EncontreMiMascota {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        try {
            int idPublicacion = Integer.parseInt(req.params(":idPublicacion"));
            MascotaPerdida publicacion = ControllerMascotaPerdida.getById(idPublicacion);
            model.put("publicacion", publicacion);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/?error");
            return null;
        }
        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "encontreMiMascota.hbs")
        );
    }

    public static String register(Request req, Response res) {
        try {
            MascotaEncontrada.MascotaEncontradaDTO dto = new MascotaEncontrada.MascotaEncontradaDTO();
            Map<String, String> reqInfo = ViewHelper.createReqInfoForNormalRequest(req);

            dto.duenio = ViewHelper.getPersonaInRequest(req, reqInfo);
            dto.publicacion = ControllerMascotaPerdida.getById(Integer.parseInt(req.queryParams("idMascotaPerdida")));
            dto.contacto = ContactCardHelper.buildContactCardMascotaEncontradaList(reqInfo);

            ControllerMascotaEncontrada.persistirMascotaEncontrada(
                ControllerMascotaEncontrada.crearMascotaEncontrada(dto)
            );
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/?error");
            return null;
        }

        res.redirect("/?success");
        return null;
    }
}