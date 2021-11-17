package domain.ContactCard.ContactPerClass;

import domain.ContactCard.ContactCard;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PersistenteID;

import javax.persistence.*;

@Entity
@Table(name = "contact_card_mascota_perdida")
public class ContactCardMascotaPerdida extends PersistenteID {
    @ManyToOne
    @JoinColumn(name = "id_mascota_perdida", referencedColumnName = "id")
    private MascotaPerdida mascotaPerdida;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
    private ContactCard contact;

    public ContactCardMascotaPerdida() {
        contact = new ContactCard();
    }

    public MascotaPerdida getMascotaPerdida() {
        return mascotaPerdida;
    }

    public void setMascotaPerdida(MascotaPerdida mascota) {
        this.mascotaPerdida = mascota;
    }

    public ContactCard getContact() {
        return contact;
    }

    public void setContact(ContactCard contact) {
        this.contact = contact;
    }
}
