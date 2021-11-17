package domain.ContactCard.contactMethods.Email.Methods;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import domain.ContactCard.ContactCard;

import com.sendgrid.*;
import domain.ContactCard.FormaNotificacion;
import domain.ContactCard.contactMethods.Email.DatosMail;
import domain.ContactCard.contactMethods.Email.EmailContactMethod;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import org.eclipse.jetty.util.IO;

import java.io.IOException;
import java.util.List;

public class SendgridEmail implements FormaNotificacion<DatosMail> {
    public void contactarAdopcion(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosMail datos = new DatosMail();
        datos.subject = "AVISO: Su mascota quiere ser adoptada";
        datos.content_type = "text/plain";
        datos.content = "Su mascota " + mascota.nombre + " quiere ser adoptada";
        contactarGenerico(datosUsuario, datos);
    }

    public void contactarRegistro(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosMail datos = new DatosMail();
        datos.subject = "AVISO: Una mascota fue registrada";
        datos.content_type = "text/plain";
        datos.content = "La mascota " + mascota.nombre + " fue vinculada a este metodo de contacto";
        contactarGenerico(datosUsuario, datos);
    }

    public void contactarRecomendacionesSemanales(ContactCard.ContactCardDTO datosUsuario, List<PublicacionOfertaAdopcion> ofertas) throws IOException {
        DatosMail datos = new DatosMail();
        datos.subject = "AVISO: Tus recomendaciones de adopcion";
        datos.content_type = "text/html";
        datos.content = "<h1>Mascotas sugeridas para adoptar</h1>";
        datos.content += "<ul>";
        ofertas.forEach(o -> datos.content += "<li>" + o.getMascota().getNombre() + "</li>");
        datos.content += "</ul>";
        contactarGenerico(datosUsuario, datos);
    }

    public void contactarMascotaPerdida(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        DatosMail datos = new DatosMail();
        datos.subject = "AVISO: Su mascota fue encontrada";
        datos.content_type = "text/plain";
        datos.content = "Su mascota " + mascota.nombre + " fue encontrada";
        contactarGenerico(datosUsuario, datos);
    }

    @Override
    public void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, DatosMail datos) throws IOException {
        Email from = new Email("cszkutnik@frba.utn.edu.ar");
        String subject = datos.subject;
        Email to = new Email(datosUsuario.email);
        Content content = new Content(datos.content_type, datos.content);
        Mail mail = new Mail(from, subject, to, content);

        String API_KEY = "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";

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

}
