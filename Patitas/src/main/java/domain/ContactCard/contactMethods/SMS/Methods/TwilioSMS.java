package domain.ContactCard.contactMethods.SMS.Methods;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import domain.ContactCard.ContactCard;
import domain.ContactCard.FormaNotificacion;
import domain.ContactCard.contactMethods.SMS.DatosSMS;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.io.IOException;
import java.util.List;

public class TwilioSMS implements FormaNotificacion<DatosSMS> {
    // TODO: sacar esto de aca
    private static final String ACCOUNT_SID = "AC3f5d46060b8da48ed54b8212e94405d7";
    private static final String AUTH_TOKEN = "047a5314e8d2d8f21b4d3fd53f7e6662";
    private static final String TWILIO_NUMBER = "+15127725641";

    // TODO: desacoplar un poco?

    public void contactarAdopcion(ContactCard.ContactCardDTO contacto, Mascota.MascotaDTO mascota) throws IOException {
        DatosSMS datosSMS = new DatosSMS();
        datosSMS.mensaje = "Su mascota " + mascota.nombre + " quiere ser adoptada!";
        this.contactarGenerico(contacto, datosSMS);
    }

    public void contactarRegistro(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosSMS datosSMS = new DatosSMS();
        datosSMS.mensaje = "La mascota " + mascota.nombre + " fue vinculada a este numero de telefono";
        contactarGenerico(datosUsuario, datosSMS);
    }

    public void contactarRecomendacionesSemanales(ContactCard.ContactCardDTO datosUsuario, List<PublicacionOfertaAdopcion> ofertas) throws IOException {
        DatosSMS datosSMS = new DatosSMS();
        datosSMS.mensaje = "Recomendaciones de adopcion\n\n";
        ofertas.forEach(o -> datosSMS.mensaje += "- " + o.getMascota().getNombre() + "\n");
        contactarGenerico(datosUsuario, datosSMS);
    }

    public void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, DatosSMS metadata)
            throws IOException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(datosUsuario.telefono),
                new com.twilio.type.PhoneNumber(TWILIO_NUMBER),
                metadata.mensaje)
                .create();

        String sid = message.getSid();
        String err = message.getErrorMessage();
        if (err != null) {
            System.out.format("Error enviando SMS -- SID = [%s], ERR = [%s]\n", sid, err);
            throw new IOException();
        }
    }

    public void contactarMascotaPerdida(ContactCard.ContactCardDTO contacto, Mascota.MascotaDTO mascota)
        throws IOException {
        DatosSMS datosSMS = new DatosSMS();
        datosSMS.mensaje = "Su mascota " + mascota.nombre + " fue encontada.";
        this.contactarGenerico(contacto, datosSMS);
    }
}
