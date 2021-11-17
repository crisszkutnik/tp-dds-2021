package views.MisMascotas;

import controller.ControllerAsociacion;
import controller.ControllerOfertaAdopcion;
import controller.ControllerMascota;
import domain.Asociacion.Asociacion;
import domain.Excepciones.NoEsDuenioDeMascotaException;
import domain.Mascota.Mascota;
import domain.Preguntas.Pregunta;
import domain.Preguntas.Respuesta;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DarEnAdopcion {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        int idMascota;
        Mascota mascota;
        try {
            idMascota = Integer.parseInt(req.queryParams("idMascota"));
            mascota = ControllerMascota.getMascotaByID(idMascota);

            Persona per = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!mascota.getDuenio().getCuil().equals(per.getCuil()))
                throw new NoEsDuenioDeMascotaException(mascota, per);

            model.put("Imagenes", ViewHelper.getListOfImageRelativePath(mascota.getImagenes()));
            model.put("Mascota", mascota);
        } catch(Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error=true");
            return null;
        }

        String ascName = req.queryParams("asociacionSeleccionada");
        List<Pregunta> preguntas = new ArrayList<>();
        if(ascName != null) {
            Asociacion as = ControllerAsociacion.getAsociacionByName(ascName);
            List<Pregunta> pregs = ControllerOfertaAdopcion.getPreguntasGenerales();
            preguntas.addAll(pregs);
            preguntas.addAll(ControllerOfertaAdopcion.getPreguntasAsociacion(as));
            model.put("asociacionSeleccionada", as);
            model.put("preguntas", preguntas);
        }

        List<Asociacion> asc = ControllerAsociacion.getAll();
        model.put("Asociaciones", asc);

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "misMascotas/darEnAdopcion.hbs")
        );
    }

    private static List<Respuesta> getRespuestasFromRequest(Request req) {
        List<Respuesta> ret = new ArrayList<>();

        for(String str : req.queryParams()) {
            if(!str.contains("rta"))
                continue;

            Respuesta rta = new Respuesta();
            int idPregunta = Integer.parseInt(str.substring(3));
            rta.setRespuesta(req.queryParams(str));
            rta.setPregunta(ControllerOfertaAdopcion.getPreguntaById(idPregunta));
            ret.add(rta);
        }

        return ret;
    }

    public static String addAdopcion(Request req, Response res) {
        try {
            List<Respuesta> rtas = getRespuestasFromRequest(req);
            Mascota mascota = ControllerMascota.getMascotaByID(Integer.parseInt(req.queryParams("idMascota")));
            Asociacion as = ControllerAsociacion.getById(Integer.parseInt(req.queryParams("idAsociacion")));
            Persona per = ((Usuario) req.session().attribute("userObj")).getPersona();

            if(!mascota.getDuenio().getCuil().equals(per.getCuil()))
                throw new NoEsDuenioDeMascotaException(mascota, per);

            ControllerOfertaAdopcion.agregarOferta(as, mascota, rtas);
        } catch(Exception e) {
            e.printStackTrace();
            res.redirect("/misMascotas/?error");
            return null;
        }

        res.redirect("/misMascotas/?success");
        return null;
    }
}
