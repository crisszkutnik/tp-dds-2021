package utils;

import controller.ControllerPersona;
import controller.ControllerUsuario;
import domain.Imagen.Imagen;
import domain.Mascota.Sexo;
import domain.Mascota.TipoAnimal;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import domain.Usuario.Usuario;
import spark.Request;
import spark.Response;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static spark.Spark.halt;

public class ViewHelper {
    public static Map<String, Object> generateModel(Request req) {
        Map<String, Object> model = new HashMap<>();
        boolean isLogged = req.session().attribute("user") != null;
        model.put("isLogged", isLogged);

        String redirectURL = req.attribute("redirectURL");
        if(redirectURL != null)
            model.put("redirect", redirectURL);

        if(req.queryParams("success") != null)
            model.put("success", true);
        else if(req.queryParams("error") != null)
            model.put("error", true);

        if(!isLogged)
            return model;

        boolean isAdmin = req.session().attribute("authLevel") == NivelAutorizacion.ADMIN;
        boolean isVoluntario = req.session().attribute("isVoluntario");

        model.put("user", req.session().attribute("user"));
        model.put("isAdmin", isAdmin);
        model.put("isVoluntario", isVoluntario);
        model.put("showActionPanel", isAdmin || isVoluntario);

        return model;
    }

    public static void addRedirectAttribute(Request req, Response res) {
        req.attribute("redirectURL", req.url());
    }

    public static void haltIfLogged(Request req, Response res) {
        if(req.session().attribute("user") != null)
            halt(401, "Ya iniciaste sesión.");
    }

    public static void haltIfNotLogged(Request req, Response res) {
        if(req.session().attribute("user") == null) {
            res.redirect("/authapi/notlogged/auth?redirect=" + req.url() + "&loginError=true");
            halt(401, "No iniciaste sesión.");
        }
    }

    public static void haltIfNotAdmin(Request req, Response res) {
        Usuario user = ControllerUsuario.buscarUsuario(req.session().attribute("user"));

        if(user.getAutorizacion() != NivelAutorizacion.ADMIN)
            halt(403, "‼ No podés acceder a esta pantalla.");
    }

    public static void haltIfNotVoluntario(Request req, Response res) {
        Usuario user = req.session().attribute("userObj");

        if(user.getAutorizacion() == NivelAutorizacion.USUARIO)
            halt(403, "‼ No podés acceder a esta pantalla.");
    }

    public static List<String> processImage(Request req, List<String> attributesNames, String path) throws IOException, ServletException {
        File uploadDir = new File(path);
        uploadDir.mkdir();

        List<String> ret = new ArrayList<>();

        for(String s : attributesNames) {
            if(req.raw().getPart(s).getSize() <= 0)
                continue;

            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", ".jpg");
            InputStream input = req.raw().getPart(s).getInputStream();
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            ret.add(tempFile.getFileName().toString());
        }
        return ret;
    }

    public static Map<String, String> processMultipartRequest(Request req, List<String> keysToIgnore) throws ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        Collection<Part> parts = req.raw().getParts();

        for(Part p : parts) {
            String name = p.getName();

            // Ignoramos este valor
            if(keysToIgnore.stream().anyMatch(name::contains))
                continue;

            String content = IOUtils.toString(p.getInputStream());
            map.put(name, content);
        }

        return map;
    }

    public static Map<String, String> processMultipartRequest(Request req, String keyToIgnore) throws ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        Collection<Part> parts = req.raw().getParts();

        for(Part p : parts) {
            String name = p.getName();

            if(name.contains(keyToIgnore))
                continue;

            String content = IOUtils.toString(p.getInputStream());
            map.put(name, content);
        }

        return map;
    }

    public static Map<String, String> createReqInfoForNormalRequest(Request req) {
        Map<String, String> map = new HashMap<>();

        for(String s : req.queryParams())
            map.put(s, req.queryParams(s));

        return map;
    }

    public static List<String> getListOfImageRelativePath(List<Imagen> imgs) {
        List<String> ret = new ArrayList<>();
        String dir = "/" + GlobalConfig.getImgDir() + "/";

        for(Imagen img : imgs)
            ret.add(dir + img.getNombre());

        return ret;
    }

    public static void setMultipartServlet(Request req) {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    }

    public static Persona getPersonaInRequest(Request req, Map<String, String> reqInfo) {
        Usuario user = (Usuario) req.session().attribute("userObj");

        if(user != null)
            return user.getPersona();

        Persona.PersonaDTO dto = new Persona.PersonaDTO();
        dto.nombre = reqInfo.get("name");
        dto.apellido = reqInfo.get("surname");
        dto.cuil = reqInfo.get("cuil");
        dto.numeroDoc = reqInfo.get("doc");
        dto.tipoDocumento = TipoDocumento.valueOf(reqInfo.get("tipoDoc").toUpperCase());
        return ControllerPersona.getPersonaOrCreate(dto);
    }

    public static Preferencias getPreferenciasFromRequest(Request req) throws Exception {
        String[] prefs = { "Sexo", "Tipo" };
        Preferencias obj = new Preferencias();
        obj.setPublicacion(null); // de request, no viene de una publicacion

        obj.setTienePatio(Boolean.parseBoolean(req.queryParams("Patio")));

        for (String pref : prefs) {
            String str = req.queryParams(pref);
            if (str.equals("NINGUNO")) // No hay preferencia
                str = null;
            switch(pref) {
                case "Sexo":
                    obj.setSexo(str == null ? null : Sexo.valueOf(str.toUpperCase()));
                    break;

                case "Tipo":
                    obj.setTipoAnimal(str == null ? null : TipoAnimal.valueOf(str.toUpperCase()));
                    break;

                default:
                    throw new Exception("Nombre de preferencia incorrecto");
            }
        }

        return obj;
    }

    public static List<String> getRespestasPosiblesFromRequest(Request req) {
        int i = 1;
        String rta = req.queryParams("rta0");
        List<String> rtas = new ArrayList<>();
        while(rta != null) {
            rtas.add(rta);
            rta = req.queryParams("rta" + i);
            i++;
        }
        return rtas;
    }
}
