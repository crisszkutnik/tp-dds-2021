package views;

import controller.ControllerAdmin;
import controller.ControllerAsociacion;
import controller.ControllerOfertaAdopcion;
import controller.ControllerUsuario;
import db.HibernateUtil;
import domain.Asociacion.Asociacion;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.GlobalConfig;
import utils.HandlebarsUtils.HandlebarsHelper;
import utils.ViewHelper;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionPanel {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        JSONObject imgObj = GlobalConfig.getImageValues();
        model.put("altoImg", imgObj.getInt("height"));
        model.put("anchoImg", imgObj.getInt("width"));

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "actionPanel/actionPanel.hbs")
        );
    }

    public static String addAdmin(Request req, Response res) {
        String username = req.queryParams("user");
        Usuario usuario = ControllerAdmin.setUsuarioAdmin(username);

        if(usuario != null)
            res.redirect("/actionPanel/?success");
        else
            res.redirect("/actionPanel/?error");

        return "";
    }

    public static String removeAdmin(Request req, Response res) {
        String username = req.queryParams("user");
        Usuario usuario = ControllerAdmin.removeUsuarioAdmin(username);

        if(usuario != null)
            res.redirect("/actionPanel/?success");
        else
            res.redirect("/actionPanel/?error");

        return "";
    }

    public static String addAsociacion(Request req, Response res) {
        try {
            String nombreAsociacion = req.queryParams("nombreAsoc");
            Usuario user = ControllerUsuario.buscarUsuario(req.queryParams("user"));
            Asociacion as = ControllerAsociacion.agregarAsociacion(nombreAsociacion);
            ControllerUsuario.hacerVoluntario(user, as);
            ControllerUsuario.actualizarUsuario(user);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/?error");
            return null;
        }

        res.redirect("/actionPanel/?success");
        return null;
    }

    public static String changeImgParams(Request req, Response res) {
        int alto, ancho;

        try {
            alto = Integer.parseInt(req.queryParams("alto"));
            ancho = Integer.parseInt(req.queryParams("ancho"));
        } catch (NumberFormatException ignored) {
            res.redirect("/actionPanel/?error");
            return "";
        }

        GlobalConfig.changeAllImageValues(alto, ancho);
        res.redirect("/actionPanel/?success");

        return "";
    }

    public static String addVoluntario(Request req, Response res) {
        Usuario user = req.session().attribute("userObj");

        try {
            Usuario nuevo = ControllerUsuario.buscarUsuario(req.queryParams("user"));
            ControllerUsuario.hacerVoluntario(nuevo, user.getAsociacion());
            ControllerUsuario.actualizarUsuario(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/?error");
            return null;
        }

        res.redirect("/actionPanel/?success");
        return null;
    }

    public static String removeVoluntario(Request req, Response res) {
        try {
            Usuario user = ControllerUsuario.buscarUsuario(req.queryParams("user"));

            if(user.getUsername().equals(req.session().attribute("user")))
                throw new Exception("Un voluntario no puede borrarse a si mismo");

            user.setAsociacion(null);

            if(user.getAutorizacion() == NivelAutorizacion.VOLUNTARIO)
                user.setAutorizacion(NivelAutorizacion.USUARIO);
            ControllerUsuario.actualizarUsuario(user);
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/?error");
            return null;
        }

        res.redirect("/actionPanel/?success");
        return null;
    }

    public static Object addPreguntaGeneral(Request req, Response res) {
        try {
            ControllerOfertaAdopcion.agregarPreguntaGeneral(
                    req.queryParams("pregunta"),
                    ViewHelper.getRespestasPosiblesFromRequest(req)
            );
            res.redirect("/actionPanel/?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/?error");
        }

        return null;
    }

    public static Object addPreguntaAsociacion(Request req, Response res) {
        try {
            Usuario user = ((Usuario) req.session().attribute("userObj"));
            String pregunta = req.queryParams("pregunta");
            List<String> rtas = ViewHelper.getRespestasPosiblesFromRequest(req);

            ControllerOfertaAdopcion.agregarPreguntaAsociacion(pregunta, rtas, user.getAsociacion());
            res.redirect("/actionPanel/voluntario/preguntaAsociacionPanel?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/voluntario/preguntaAsociacionPanel?error");
        }

        return null;
    }

    public static Object modificarPreguntaGeneralPanel(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);
            model.put(
                    "pregunta",
                    ControllerOfertaAdopcion.getPreguntasGenerales()
            );
            return HandlebarsHelper.compile(model, "/actionPanel/modificarPreguntaGeneral");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/?error");
            return null;
        }
    }

    public static Object removerPreguntaGeneral(Request req, Response res) {
        try {
            int idPregunta = Integer.parseInt(req.queryParams("idPregunta"));
            // Bulk operation
            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            try {
                s.createQuery("DELETE FROM Respuesta WHERE id_pregunta=" + idPregunta).executeUpdate();
                s.createNativeQuery("DELETE FROM pregunta_respuestas_posibles WHERE pregunta_id=" + idPregunta).executeUpdate();
                s.createQuery("DELETE FROM Pregunta WHERE id=" + idPregunta).executeUpdate();
                t.commit();
            } catch (Exception e) {
               t.rollback();
               s.close();
               throw new Exception();
            }
            s.close();
            res.redirect("/actionPanel/admin/preguntaGeneralPanel?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/admin/preguntaGeneralPanel?error");
        }
        return null;
    }

    public static Object removerPreguntaAsociacion(Request req, Response res) {
        try {
            int idPregunta = Integer.parseInt(req.queryParams("idPregunta"));
            Asociacion asPersona = ((Usuario) req.session().attribute("userObj")).getAsociacion();

            if(ControllerOfertaAdopcion.getPreguntaById(idPregunta).getAsociacion().getId() != asPersona.getId())
                throw new Exception("Persona no es voluntario de la Asociacion" + asPersona.getId());

            // Bulk operation
            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            try {
                s.createQuery("DELETE FROM Respuesta WHERE id_pregunta=" + idPregunta).executeUpdate();
                s.createNativeQuery("DELETE FROM pregunta_respuestas_posibles WHERE pregunta_id=" + idPregunta).executeUpdate();
                s.createQuery("DELETE FROM Pregunta WHERE id=" + idPregunta).executeUpdate();
                t.commit();
            } catch (Exception e) {
                t.rollback();
                s.close();
                throw new Exception();
            }
            s.close();
            res.redirect("/actionPanel/voluntario/preguntaAsociacionPanel?success");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/voluntario/preguntaAsociacionPanel?error");
        }
        return null;
    }

    public static Object modificarPreguntaAsociacionPanel(Request req, Response res) {
        try {
            Map<String, Object> model = ViewHelper.generateModel(req);
            Asociacion asociacion = ((Usuario) req.session().attribute("userObj")).getAsociacion();
            model.put(
                    "pregunta",
                    ControllerOfertaAdopcion.getPreguntasAsociacion(asociacion)
            );
            model.put("asociacion", asociacion);
            return HandlebarsHelper.compile(model, "actionPanel/modificarPreguntaAsociacion");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/actionPanel/voluntario/preguntaAsociacionPanel?error");
            return null;
        }
    }
}
