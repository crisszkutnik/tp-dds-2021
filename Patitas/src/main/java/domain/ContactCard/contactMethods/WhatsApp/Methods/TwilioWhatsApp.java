package domain.ContactCard.contactMethods.WhatsApp.Methods;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import domain.ContactCard.ContactCard;
import domain.ContactCard.FormaNotificacion;
import domain.Mascota.Mascota;
import domain.ContactCard.contactMethods.WhatsApp.DatosWhatsapp;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.io.IOException;
import java.util.List;


public class TwilioWhatsApp implements FormaNotificacion<DatosWhatsapp> {
    // No es seguro tenerlo aca pero bueno!
    private static final String ACCOUNT_SID = "AC3f5d46060b8da48ed54b8212e94405d7";
    private static final String AUTH_TOKEN = "047a5314e8d2d8f21b4d3fd53f7e6662";
    private static final String TWILIO_NUMBER = "+14155238886";

    public void contactarAdopcion(ContactCard.ContactCardDTO contacto, Mascota.MascotaDTO mascota) throws IOException {
        DatosWhatsapp metadata = new DatosWhatsapp();
        metadata.mensaje = "Su mascota " + mascota.nombre + " quiere ser adoptada!";
        this.contactarGenerico(contacto, metadata);
    }

    public void contactarRegistro(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosWhatsapp datos = new DatosWhatsapp();
        datos.mensaje = "La mascota " + mascota.nombre + " fue vinculada a este numero de telefono";
        contactarGenerico(datosUsuario, datos);
    }

    public void contactarRecomendacionesSemanales(ContactCard.ContactCardDTO datosUsuario, List<PublicacionOfertaAdopcion> ofertas) throws IOException {
        DatosWhatsapp datos = new DatosWhatsapp();
        datos.mensaje = "Recomendaciones de adopcion\n\n";
        ofertas.forEach(o -> datos.mensaje += "- " + o.getMascota().getNombre() + "\n");
        contactarGenerico(datosUsuario, datos);
    }

    public void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, DatosWhatsapp metadata) throws IOException {
        System.out.println("Attempting send to " + datosUsuario.telefono);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+datosUsuario.telefono), // destinatario
                new com.twilio.type.PhoneNumber("whatsapp:"+TWILIO_NUMBER),         // nro. twilio
                metadata.mensaje                                                    // msg
        ).create();

        String sid = message.getSid();
        String err = message.getErrorMessage();
        if (err != null) {
            System.out.format("Error enviando WhatsApp -- SID = [%s], ERR = [%s]\n", sid, err);
            throw new IOException();
        }
    }

    public void contactarMascotaPerdida(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosWhatsapp metadata = new DatosWhatsapp();
        metadata.mensaje = "Su mascota " + mascota.nombre + " fue encontrada";
        this.contactarGenerico(datosUsuario, metadata);
    }
}
