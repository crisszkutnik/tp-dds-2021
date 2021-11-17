package domain.MascotaEncontrada;

import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascotaEncontrada;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PersistenteID;
import domain.Usuario.Persona;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascota_encontrada")
public class MascotaEncontrada extends PersistenteID {
    @ManyToOne
    @JoinColumn(name = "id_mascota_perdida", referencedColumnName = "id")
    private MascotaPerdida publicacion;

    @ManyToOne
    @JoinColumn(name = "cuil_duenio", referencedColumnName = "cuil")
    private Persona duenio;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mascotaEncontrada", cascade = CascadeType.PERSIST)
    private final List<ContactCardMascotaEncontrada> contacto;

    public static class MascotaEncontradaDTO {
        public MascotaPerdida publicacion;
        public Persona duenio;
        public List<ContactCardMascotaEncontrada> contacto;
    }

    public MascotaEncontrada() {
        contacto = new ArrayList<>();
    }

    public void addContact(ContactCardMascotaEncontrada c) {
        c.setMascotaEncontrada(this);
        contacto.add(c);
    }

    public void addContact(List<ContactCardMascotaEncontrada> c) {
        for(ContactCardMascotaEncontrada a : c)
            a.setMascotaEncontrada(this);

        contacto.addAll(c);
    }

    public List<ContactCardMascotaEncontrada> getContacto() {
        return contacto;
    }

    public List<ContactCard> getContactCard() {
        List<ContactCard> r = new ArrayList<>();
        for(ContactCardMascotaEncontrada m : contacto)
            r.add(m.getContact());
        return r;
    }

    public MascotaPerdida getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(MascotaPerdida publicacion) {
        this.publicacion = publicacion;
    }

    public Persona getDuenio() {
        return duenio;
    }

    public void setDuenio(Persona duenio) {
        this.duenio = duenio;
    }
}
