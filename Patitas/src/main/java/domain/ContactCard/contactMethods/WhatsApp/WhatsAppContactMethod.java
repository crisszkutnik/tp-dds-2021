package domain.ContactCard.contactMethods.WhatsApp;

import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactMethodContainer;
import domain.ContactCard.FormaNotificacion;
import domain.ContactCard.contactMethods.WhatsApp.Methods.TwilioWhatsApp;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.io.IOException;
import java.util.List;

public class WhatsAppContactMethod extends ContactMethodContainer implements FormaNotificacion<DatosWhatsapp> {
    private static final FormaNotificacion<DatosWhatsapp> forma = new TwilioWhatsApp();

    public String toCheckedHTML() {
        return "<div>\n" +
                "   <div class=\"form-check\">\n" +
                "       <input class=\"form-check-input wp-check\" name=\"WhatsApp\" type=\"checkbox\" id=\"contactCheckbox\" checked>\n" +
                "       <input class=\"form-check-input wp-decoy\" style=\"display: none\" name=\"WhatsApp\" type=\"text\" value=\"off\" disabled>\n" +
                "       <label class=\"form-check-label\" for=\"contactCheckbox\">\n" +
                "           WhatsApp\n" +
                "       </label>\n" +
                "   </div>\n" +
                "</div>";
    }

    public String toUncheckedHTML() {
        return "<div>\n" +
                "   <div class=\"form-check\">\n" +
                "       <input class=\"form-check-input wp-check\" name=\"WhatsApp\" type=\"checkbox\" id=\"contactCheckbox\">\n" +
                "       <input class=\"form-check-input wp-decoy\" style=\"display: none\" name=\"WhatsApp\" type=\"text\" value=\"off\">\n" +
                "       <label class=\"form-check-label\" for=\"contactCheckbox\">\n" +
                "           WhatsApp\n" +
                "       </label>\n" +
                "   </div>\n" +
                "</div>";
    }

    public void contactarAdopcion(ContactCard.ContactCardDTO contacto, Mascota.MascotaDTO mascota) throws IOException {
        forma.contactarAdopcion(contacto, mascota);
    }

    public void contactarRegistro(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        forma.contactarRegistro(datosUsuario, mascota);
    }

    public void contactarRecomendacionesSemanales(ContactCard.ContactCardDTO datosUsuario, List<PublicacionOfertaAdopcion> ofertas) throws IOException {
        forma.contactarRecomendacionesSemanales(datosUsuario, ofertas);
    }

    public void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, DatosWhatsapp metadata) throws IOException {
        forma.contactarGenerico(datosUsuario, metadata);
    }

    public void contactarMascotaPerdida(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        forma.contactarMascotaPerdida(datosUsuario, mascota);
    }
}
