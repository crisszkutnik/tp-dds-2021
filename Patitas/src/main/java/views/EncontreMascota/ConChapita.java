package views.EncontreMascota;

import controller.ControllerMascotaPerdida;
import controller.ControllerMascota;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TipoAnimal;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.*;

public class ConChapita {
    public static String processRequest(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);
            String id = req.params(":idMascota");
            Mascota mascota = ControllerMascota.getMascotaByID(Integer.parseInt(id));
            Persona p = mascota.getDuenio();

            model.put("nombreMascota", mascota.getNombre());
            model.put("apodoMascota", mascota.getApodo());
            model.put("nombreDuenio", p.getNombre());
            model.put("apellidoDuenio", p.getApellido());
            model.put("idMascota", id);

            List<String> imgPath = ViewHelper.getListOfImageRelativePath(mascota.getImagenes());
            model.put("firstImage", imgPath.remove(0));
            model.put("imgs", imgPath);

            String tipoMascota = mascota.getTipoAnimal() == TipoAnimal.PERRO ? "\uD83D\uDC36 Perro" : "\uD83D\uDC31 Gato";
            String sexoMascota = mascota.getSexo() == Sexo.MACHO ? "♂ Macho" : "♀ Hembra";
            model.put("tipoMascota", tipoMascota);
            model.put("sexoMascota", sexoMascota);

            return new HandlebarsTemplateEngine().render(
                    new ModelAndView(model, "encontreMascota/conChapita.hbs")
            );
        } catch (Exception e) {
            res.redirect("/?error");
            return null;
        }

    }

    public static String registerMascotaPerdida(Request req, Response res) {
        ViewHelper.setMultipartServlet(req);

        MascotaPerdida.MascotaPerdidaDTO dto;

        try {
            Map<String, String> reqInfo = ViewHelper.processMultipartRequest(req, "mascotaImg");
            dto = ParseRegistroMascota.parse(req, res, reqInfo);
            dto.mascota = ControllerMascota.getMascotaByID(Integer.parseInt(reqInfo.get("idMascota")));
        } catch(Exception e) {
            e.printStackTrace();
            res.redirect("/");
            return null;
        }

        ControllerMascotaPerdida.generarMascotaPerdidaConocida(dto);

        res.redirect("/misMascotas/");
        return null;
    }
}
