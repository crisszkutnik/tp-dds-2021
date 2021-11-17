package domain.ContactCard.contactMethods.Email;

import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactMethodContainer;
import domain.ContactCard.FormaNotificacion;
import domain.ContactCard.contactMethods.Email.Methods.SendgridEmail;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.io.IOException;
import java.util.List;

public class EmailContactMethod extends ContactMethodContainer implements FormaNotificacion<DatosMail> {
    private static final FormaNotificacion<DatosMail> forma = new SendgridEmail();

    public String toCheckedHTML() {
        return "<div>\n" +
                "           <div class=\"form-check\">\n" +
                "               <input class=\"form-check-input email-check\" name=\"Email\" type=\"checkbox\" id=\"contactCheckbox\" checked>\n" +
                "               <input class=\"form-check-input email-decoy\" style=\"display: none\" name=\"Email\" type=\"text\" value=\"off\" disabled>\n" +
                "               <label class=\"form-check-label\" for=\"contactCheckbox\">\n" +
                "                   Email\n" +
                "               </label>\n" +
                "           </div>\n" +
                "       </div>";
    }

    public String toUncheckedHTML() {
        return "<div>\n" +
        "           <div class=\"form-check\">\n" +
        "               <input class=\"form-check-input email-check\" name=\"Email\" type=\"checkbox\" id=\"contactCheckbox\">\n" +
        "               <input class=\"form-check-input email-decoy\" style=\"display: none\" name=\"Email\" type=\"text\" value=\"off\">\n" +
        "               <label class=\"form-check-label\" for=\"contactCheckbox\">\n" +
        "                   Email\n" +
        "               </label>\n" +
        "           </div>\n" +
        "       </div>";
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

    public void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, DatosMail metadata) throws IOException {
        forma.contactarGenerico(datosUsuario, metadata);
    }

    public void contactarMascotaPerdida(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException {
        forma.contactarMascotaPerdida(datosUsuario, mascota);
    }
}
