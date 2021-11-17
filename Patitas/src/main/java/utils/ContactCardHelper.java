package utils;

import db.EntityManagerHelper;
import db.HibernateUtil;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactList;
import domain.ContactCard.ContactPerClass.*;
import domain.ContactCard.contactMethods.Email.EmailContactMethod;
import domain.ContactCard.contactMethods.SMS.SMSContactMethod;
import domain.ContactCard.contactMethods.WhatsApp.WhatsAppContactMethod;
import domain.Mascota.Mascota;
import domain.MascotaPerdida.MascotaPerdida;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactCardHelper {
    public static ContactCard buildContactCardFromMap(Map<String, String> reqInfo, int i) {
        ContactCard c = new ContactCard();
        c.setApellido(reqInfo.get("apellidoContacto" + i));
        c.setTelefono(reqInfo.get("telefonoContacto" + i));
        c.setEmail(reqInfo.get("emailContacto" + i));
        c.setNombre(reqInfo.get("nombreContacto" + i));

        if(reqInfo.get("SMS" + i).equals("on"))
            c.agregarFormaNotificacion(new SMSContactMethod());

        if(reqInfo.get("WhatsApp" + i).equals("on"))
            c.agregarFormaNotificacion(new WhatsAppContactMethod());

        if(reqInfo.get("Email" + i).equals("on"))
            c.agregarFormaNotificacion(new EmailContactMethod());

        return c;
    }

    public static List<ContactCardMascota> buildContactCardMascotaList(Mascota mascota, Map<String, String> reqInfo) {
        List<ContactCardMascota> l = new ArrayList<>();

        int i = 0;
        while(reqInfo.containsKey("nombreContacto" + i)) {
            ContactCardMascota m = new ContactCardMascota();
            ContactCard nueva = buildContactCardFromMap(reqInfo, i);
            m.setMascota(mascota);
            m.setContact(nueva);
            l.add(m);
            i++;
        }

        return l;
    }

    public static List<ContactCardMascotaPerdida> buildContactCardMascotaPerdidaList(Map<String, String> reqInfo) {
        List<ContactCardMascotaPerdida> l = new ArrayList<>();

        int i = 0;
        while(reqInfo.containsKey("nombreContacto" + i)) {
            ContactCardMascotaPerdida m = new ContactCardMascotaPerdida();
            ContactCard nueva = buildContactCardFromMap(reqInfo, i);
            m.setContact(nueva);
            l.add(m);
            i++;
        }

        return l;
    }

    public static List<ContactCardMascotaEncontrada> buildContactCardMascotaEncontradaList(Map<String, String> reqInfo) {
        List<ContactCardMascotaEncontrada> l = new ArrayList<>();

        int i = 0;
        while(reqInfo.containsKey("nombreContacto" + i)) {
            ContactCardMascotaEncontrada m = new ContactCardMascotaEncontrada();
            ContactCard nueva = buildContactCardFromMap(reqInfo, i);
            m.setContact(nueva);
            l.add(m);
            i++;
        }

        return l;
    }

    public static List<ContactCardAdopcionPublicacionOfertaAdopcion> buildContactCardAdopcionOfertaAdopcionList(Map<String, String> reqInfo) {
        List<ContactCardAdopcionPublicacionOfertaAdopcion> l = new ArrayList<>();

        int i = 0;
        while(reqInfo.containsKey("nombreContacto" + i)) {
            ContactCardAdopcionPublicacionOfertaAdopcion m = new ContactCardAdopcionPublicacionOfertaAdopcion();
            ContactCard nueva = buildContactCardFromMap(reqInfo, i);
            m.setContact(nueva);
            l.add(m);
            i++;
        }

        return l;
    }

    public static List<ContactCardPublicacionDeseoAdoptar> buildContactCardPublicacionDeseoAdoptarList(Map<String, String> reqInfo) {
        List<ContactCardPublicacionDeseoAdoptar> l = new ArrayList<>();

        int i = 0;
        while(reqInfo.containsKey("nombreContacto" + i)) {
            ContactCardPublicacionDeseoAdoptar m = new ContactCardPublicacionDeseoAdoptar();
            ContactCard nueva = buildContactCardFromMap(reqInfo, i);
            m.setContact(nueva);
            l.add(m);
            i++;
        }

        return l;
    }

    public static void updateContactFromMascota(Map<String, String> reqInfo, Mascota mascota) throws Exception {
        List<ContactCardMascota> contactMascota = mascota.getContacto();
        List<ContactCard> contact =  mascota.getContactCard();
        int i = 0;
        List<ContactCardMascota> nuevas = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        while(reqInfo.get("nombreContacto" + i) != null) {
            String nombre = reqInfo.get("nombreContacto" + i);
            String apellido = reqInfo.get("apellidoContacto" + i);
            String telefono = reqInfo.get("telefonoContacto" + i);
            String email = reqInfo.get("emailContacto" + i);
            String id = reqInfo.get("idContactCard" + i);

            ContactCard c;

            if(id != null) {
                c = contact.stream().filter(t -> t.getId() == Integer.parseInt(id)).findFirst().get(); // Aca crashea si no existe
                ids.add(c.getId());
            } else {
                ContactCardMascota nueva = new ContactCardMascota();
                c = new ContactCard();
                nueva.setContact(c);
                nueva.setMascota(mascota);
                nuevas.add(nueva);
            }
            c.setApellido(apellido);
            c.setNombre(nombre);
            c.setTelefono(telefono);
            c.setEmail(email);
            c.setContactList(new ContactList());

            if(reqInfo.get("SMS" + i).equals("on"))
                c.agregarFormaNotificacion(new SMSContactMethod());

            if(reqInfo.get("WhatsApp" + i).equals("on"))
                c.agregarFormaNotificacion(new WhatsAppContactMethod());

            if(reqInfo.get("Email" + i).equals("on"))
                c.agregarFormaNotificacion(new EmailContactMethod());
            i++;
        }

        // Java no te deja usar el !i
        if(i == 0)
            throw new Exception("Cada mascota tiene que tener al menos una ContactCard");

        List<ContactCardMascota> aRemover = contactMascota.stream().filter(t -> !ids.contains(t.getContact().getId())).collect(Collectors.toList());
        contactMascota.removeAll(aRemover);
        mascota.agregarContactos(nuevas);
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        for(ContactCardMascota rem : aRemover) {
            s.remove(rem.getContact());
            s.remove(rem);
        }
        nuevas.forEach(s::persist);
        s.merge(mascota);
        t.commit();
        s.close();
    }

    public static void updateContactFromMascotaPerdida(Map<String, String> reqInfo, MascotaPerdida publicacion) throws Exception {
        List<ContactCardMascotaPerdida> contactMascota = publicacion.getContacto();
        List<ContactCard> contact =  publicacion.getContactCard();
        int i = 0;
        List<ContactCardMascotaPerdida> nuevas = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        while(reqInfo.get("nombreContacto" + i) != null) {
            String nombre = reqInfo.get("nombreContacto" + i);
            String apellido = reqInfo.get("apellidoContacto" + i);
            String telefono = reqInfo.get("telefonoContacto" + i);
            String email = reqInfo.get("emailContacto" + i);
            String id = reqInfo.get("idContactCard" + i);

            ContactCard c;

            if(id != null) {
                c = contact.stream().filter(t -> t.getId() == Integer.parseInt(id)).findFirst().get(); // Aca crashea si no existe
                ids.add(c.getId());
            } else {
                ContactCardMascotaPerdida nueva = new ContactCardMascotaPerdida();
                c = new ContactCard();
                nueva.setContact(c);
                nueva.setMascotaPerdida(publicacion);
                nuevas.add(nueva);
            }
            c.setApellido(apellido);
            c.setNombre(nombre);
            c.setTelefono(telefono);
            c.setEmail(email);
            c.setContactList(new ContactList());

            if(reqInfo.get("SMS" + i).equals("on"))
                c.agregarFormaNotificacion(new SMSContactMethod());

            if(reqInfo.get("WhatsApp" + i).equals("on"))
                c.agregarFormaNotificacion(new WhatsAppContactMethod());

            if(reqInfo.get("Email" + i).equals("on"))
                c.agregarFormaNotificacion(new EmailContactMethod());
            i++;
        }

        // Java no te deja usar el !i
        if(i == 0)
            throw new Exception("Cada mascota tiene que tener al menos una ContactCard");

        List<ContactCardMascotaPerdida> aRemover = contactMascota.stream().filter(t -> !ids.contains(t.getContact().getId())).collect(Collectors.toList());
        contactMascota.removeAll(aRemover);
        publicacion.agregarContacto(nuevas);

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        try {
            for(ContactCardMascotaPerdida rem : aRemover) {
                s.remove(rem);
                s.remove(rem.getContact());
            }
            nuevas.forEach(s::persist);
            s.merge(publicacion);
            t.commit();
            s.close();
        } catch (Exception e) {
            t.rollback();
            s.close();
            e.printStackTrace();
            throw new Exception("Error modificando las ContactCardMascotaPerdida");
        }
    }
}
