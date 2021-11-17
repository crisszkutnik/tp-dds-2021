package views.EncontreMascota;

import domain.ContactCard.ContactPerClass.ContactCardMascotaPerdida;
import domain.Imagen.Imagen;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import spark.Request;
import spark.Response;
import utils.ContactCardHelper;
import utils.GlobalConfig;
import utils.ViewHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParseRegistroMascota {
    public static MascotaPerdida.MascotaPerdidaDTO parse(Request req, Response res, Map<String, String> reqInfo) throws Exception {
        Persona persona;

        MascotaPerdida.MascotaPerdidaDTO dto = new MascotaPerdida.MascotaPerdidaDTO();

        persona = ViewHelper.getPersonaInRequest(req, reqInfo);
        dto.descripcion = reqInfo.get("Descripcion");
        dto.latitud = Double.parseDouble(reqInfo.get("lat"));
        dto.longitud = Double.parseDouble(reqInfo.get("lng"));
        dto.rescatista = persona;

        // Armamos los contactos
        List<ContactCardMascotaPerdida> contacts;
        contacts = ContactCardHelper.buildContactCardMascotaPerdidaList(reqInfo);
        dto.contacto = contacts;

        // Imagenes
        List<Imagen> imagenes = new ArrayList<>();
        List<String> imgName = ViewHelper.processImage(
                req,
                Arrays.asList("mascotaImg1", "mascotaImg2", "mascotaImg3", "mascotaImg4"),
                GlobalConfig.getImgPath()
        );

        // De esta manera el catch contempla el for
        for(String s : imgName) {
            Imagen img = new Imagen();
            img.setNombre(s);
            imagenes.add(img);
        }
        dto.fotos = imagenes;

        return dto;
    }
}
