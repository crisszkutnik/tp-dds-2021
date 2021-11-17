package domain.ContactCard;

import domain.ContactCard.contactMethods.Email.EmailContactMethod;
import domain.ContactCard.contactMethods.SMS.SMSContactMethod;
import domain.ContactCard.contactMethods.WhatsApp.WhatsAppContactMethod;
import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContactList {
    private final List<FormaNotificacion<?>> formasNotificacion;

    private static final ContactMethodContainer[] formas =  {
            new WhatsAppContactMethod(),
            new SMSContactMethod(),
            new EmailContactMethod()
    };

    public ContactList() {
        formasNotificacion = new ArrayList<>();
    }

    public String toHTML() {
        StringBuilder ret = new StringBuilder();
        for(ContactMethodContainer f :  formas) {
            Class<?> c =  f.getClass();
            if(formasNotificacion.stream().anyMatch(c::isInstance))
                ret.append(f.toCheckedHTML());
            else
                ret.append(f.toUncheckedHTML());
        }
        return ret.toString();
    }

    public String getStr() {
        return formasNotificacion.stream().map(
                forma -> forma.getClass().getName() + ","
        ).collect(Collectors.joining());
    }

    public void addFromStr(String str) {
        List<String> formas_str = Arrays.asList(str.split(","));
        formas_str.forEach(s -> formasNotificacion.add(stringToForma(s)));
    }

    private FormaNotificacion<?> stringToForma(String s) {
        try {
            Class<?> clase = Class.forName(s);
            return (FormaNotificacion<?>) clase.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<FormaNotificacion<?>> getFormasNotificacion() {
        return formasNotificacion;
    }

    public void agregarFormaNotificacion(FormaNotificacion<?> nueva) {
        this.formasNotificacion.add(nueva);
    }

    public void notificarTodosMascotaPerdida(Mascota.MascotaDTO mascota_dto, ContactCard.ContactCardDTO dto) {
        for(FormaNotificacion<?> forma : this.formasNotificacion) {
            try {
                forma.contactarMascotaPerdida(dto, mascota_dto);
            } catch(Exception ignored) {}
        }
    }

    public void notificarTodosRecomendacionesAdopcion(ContactCard.ContactCardDTO dto, List<PublicacionOfertaAdopcion> ofertas) {
        for(FormaNotificacion<?> forma : this.formasNotificacion) {
            try {
                forma.contactarRecomendacionesSemanales(dto, ofertas);
            } catch (Exception ignored) {}
        }
    }

    public void notificarTodosRegistro(Mascota.MascotaDTO mascotaDTO, ContactCard.ContactCardDTO contactDTO) {
        for(FormaNotificacion<?> forma : this.formasNotificacion) {
            try {
                forma.contactarRegistro(contactDTO, mascotaDTO);
            } catch (Exception e) {}
        }
    }

    public void notificarTodosAdopcion(Mascota.MascotaDTO mascota_dto, ContactCard.ContactCardDTO dto) {
        for (FormaNotificacion<?> forma : this.formasNotificacion) {
            try {
                forma.contactarAdopcion(dto, mascota_dto);
            } catch(Exception ignored) {}
        }

    }
}
