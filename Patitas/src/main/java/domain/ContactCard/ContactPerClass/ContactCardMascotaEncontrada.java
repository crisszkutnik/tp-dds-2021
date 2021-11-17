package domain.ContactCard.ContactPerClass;

import domain.ContactCard.ContactCard;
import domain.MascotaEncontrada.MascotaEncontrada;
import domain.PersistenteID;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact_card_mascota_encontrada")
public class ContactCardMascotaEncontrada extends PersistenteID {
    @ManyToOne
    @JoinColumn(name = "id_mascota_encontrada", referencedColumnName = "id")
    public MascotaEncontrada mascotaEncontrada;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
    public ContactCard contact;

    public MascotaEncontrada getMascotaEncontrada() {
        return mascotaEncontrada;
    }

    public void setMascotaEncontrada(MascotaEncontrada mascotaEncontrada) {
        this.mascotaEncontrada = mascotaEncontrada;
    }

    public ContactCard getContact() {
        return contact;
    }

    public void setContact(ContactCard contact) {
        this.contact = contact;
    }
}

/*
@Entity
@Table(name = "contact_card_mascota_encontrada")
public class ContactCardMascotaEncontrada  {
    @EmbeddedId
    private PKClass pkclass;

    @Embeddable
     private static class PKClass implements Serializable {
        @ManyToOne
        @JoinColumn(name = "id_mascota_encontrada", referencedColumnName = "id")
        public MascotaEncontrada mascotaEncontrada;

        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
        public ContactCard contact;
    };

    public ContactCardMascotaEncontrada() {
        this.pkclass = new PKClass();
    }

    public MascotaEncontrada getMascotaEncontrada() {
        return pkclass.mascotaEncontrada;
    }

    public void setMascotaEncontrada(MascotaEncontrada mascotaEncontrada) {
        this.pkclass.mascotaEncontrada = mascotaEncontrada;
    }

    public ContactCard getContact() {
        return pkclass.contact;
    }

    public void setContact(ContactCard contact) {
        this.pkclass.contact = contact;
    }
}*/
