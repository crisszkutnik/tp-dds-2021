package views;

import controller.ControllerPersona;
import controller.ControllerUsuario;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import domain.Usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ViewHelper;
import utils.passwordUtilities.HashPassword;
import utils.passwordUtilities.PasswordVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Auth {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);

        String redirectURL = req.queryParams("redirect");
        if (redirectURL != null)
            model.put("redirect", redirectURL);

        if (req.queryParams("loginError") != null) {
            model.put("loginError", true);

            List<String> errorMsgs = new ArrayList<>();
            if (req.queryParams("weakPwd") != null)
                errorMsgs.add("La contraseña es muy débil, probar con una más fuerte.");
            if (req.queryParams("repUsr") != null)
                errorMsgs.add("El nombre de usuario ingresado ya existe.");

            model.put("errorMsgs", errorMsgs);
        }

        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "auth.hbs")
        );
    }

    public static String registerRequest(Request req, Response res) {
        String username = req.queryParams("username");

        Usuario user = ControllerUsuario.buscarUsuario(username);
        // ERR: Ya existe el usuario
        if (user != null) {
            System.out.println("Se quiso crear un usuario con nombre " + username + ". ERR: Ya existente");
            res.redirect("/authapi/notlogged/auth?loginError&repUsr");
            return "";
        }

        // ERR: Contraseña debil
        if (!PasswordVerifier.isSecurePassword(req.queryParams("password"))) {
            System.out.println("La contraseña es muy débil.");
            res.redirect("/authapi/notlogged/auth?loginError&weakPwd");
            return "";
        }

        Persona persona = ControllerPersona.getPersonaByCUIL(req.queryParams("cuil"));
        if (persona == null) { // Hay que crear la persona
            Persona.PersonaDTO dtoPer = new Persona.PersonaDTO();
            dtoPer.nombre = req.queryParams("name");
            dtoPer.apellido = req.queryParams("surname");
            dtoPer.cuil = req.queryParams("cuil");
            dtoPer.numeroDoc = req.queryParams("doc");
            dtoPer.tipoDocumento = TipoDocumento.valueOf(req.queryParams("tipoDoc").toUpperCase());
            persona = ControllerPersona.crearPersona(dtoPer);
        }

        String passHash = HashPassword.hashPassword(req.queryParams("password"));
        ControllerUsuario.crearUsuario(username, passHash, persona);

        String redirect = req.queryParams("redirect");
        if (redirect == null)
            redirect = "/";
        else
            redirect = "/authapi/notlogged/auth?redirect=" + redirect;

        res.redirect(redirect);
        return "";
    }

    public static String loginRequest(Request req, Response res) {
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        Usuario user = ControllerUsuario.buscarUsuario(username);
        // ERR: No existe usuario
        if(user == null) {
            res.redirect("/authapi/notlogged/auth?loginError");
            return "";
        }
        String passHash = user.getPasswordHash();

        // ERR: Password incorrecta
        if(!HashPassword.correctPassword(password, passHash)) {
            res.redirect("/authapi/notlogged/auth?loginError");
            return "";
        }

        // Creamos la sesion y le asignamos el usuario
        req.session(true);
        req.session().attribute("userObj", user);
        req.session().attribute("user", user.getUsername());
        req.session().attribute("authLevel", user.getAutorizacion());
        req.session().attribute("isVoluntario", user.getAsociacion() != null);

        String redirect = req.queryParams("redirect");
        if(redirect == null)
            redirect = "/";
        res.redirect(redirect);
        return "";
    }

    public static String logout(Request req, Response res) {
        // Destroy session
        req.session().removeAttribute("user");
        req.session().removeAttribute("userObj");
        req.session().removeAttribute("authLevel");
        req.session().removeAttribute("isVoluntario");
        res.redirect("/");
        return "";
    }
}
