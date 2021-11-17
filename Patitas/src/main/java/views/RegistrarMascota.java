package views;

import controller.ControllerMascota;
import domain.ContactCard.ContactPerClass.ContactCardMascota;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.ContactCardHelper;
import utils.GlobalConfig;
import utils.ViewHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RegistrarMascota {
    public static String processRequest(Request req, Response res) {
        Map<String, Object> model = ViewHelper.generateModel(req);
        return new HandlebarsTemplateEngine().render(
                new ModelAndView(model, "registrarMascota.hbs")
        );
    }

    public static String addMascota(Request req, Response res) {
        ViewHelper.setMultipartServlet(req);
        Mascota.MascotaDTO dtoMascota = new Mascota.MascotaDTO();
        Map<String, String> reqInfo;
        try {
            reqInfo = ViewHelper.processMultipartRequest(req, "mascotaImg");
            dtoMascota.tipoAnimal = TipoAnimal.valueOf(reqInfo.get("Tipo").toUpperCase());
            dtoMascota.anioNacimiento = Integer.parseInt(reqInfo.get("Nacimiento").substring(0, 4));
            dtoMascota.sexo = Sexo.valueOf(reqInfo.get("Sexo").toUpperCase());

            dtoMascota.tamanio = TamanioMascota.valueOf(reqInfo.get("Tamanio").toUpperCase());
            dtoMascota.apodo = reqInfo.get("Apodo");
            dtoMascota.nombre = reqInfo.get("Nombre");
            dtoMascota.descripcion = reqInfo.get("Descripcion");

            dtoMascota.duenio = ViewHelper.getPersonaInRequest(req, reqInfo);
        } catch(Exception e) {
            System.out.println("ERR: Error generando DTO mascota");
            e.printStackTrace();
            res.redirect("/registrarMascota/?error");
            return null;
        }

        Mascota mascota;
        try {
            mascota = ControllerMascota.crearMascota(dtoMascota);
            // ERR
            if(mascota == null)
                throw new Exception("ERR: Error generando objeto mascota");
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/registrarMascota/?error");
            return null;
        }

        // Armamos los contactos
        List<ContactCardMascota> contacts;
        try {
            contacts = ContactCardHelper.buildContactCardMascotaList(mascota, reqInfo);
            mascota.agregarContactos(contacts);
        } catch(Exception e) {
            System.out.println("ERR: La cantidad de argumentos de las ContactCard no coinciden en RegistrarMascota");
            e.printStackTrace();
            res.redirect("/registrarMascota/?error");
            return null;
        }

        // Recibimos las imagenes
        try {
            List<String> imgName = ViewHelper.processImage(
                    req,
                    Arrays.asList("mascotaImg1", "mascotaImg2", "mascotaImg3", "mascotaImg4"),
                    GlobalConfig.getImgPath()
            );

            // De esta manera el catch contempla el for
            for(String s : imgName) {
                System.out.println("Agregando [" + s + "]");
                mascota.agregarImagen(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/registrarMascota/?error");
            return null;
        }

        ControllerMascota.persistirObjMascota(mascota);
        ControllerMascota.generarQR(mascota);
        mascota.getContactCard().forEach(c -> c.notificarTodoRegistro(mascota.toDTO()));

        res.redirect("/?success");
        return null;
    }
}
