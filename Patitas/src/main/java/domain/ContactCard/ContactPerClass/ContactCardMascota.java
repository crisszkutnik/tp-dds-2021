package domain.ContactCard.ContactPerClass;

import domain.ContactCard.ContactCard;
import domain.Mascota.Mascota;
import domain.PersistenteID;

import javax.persistence.*;

@Entity
@Table(name = "contact_card_mascota")
public class ContactCardMascota extends PersistenteID {
    @ManyToOne
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    private Mascota mascota;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
    private ContactCard contact;

    public ContactCardMascota() {
        contact = new ContactCard();
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public ContactCard getContact() {
        return contact;
    }

    public void setContact(ContactCard contact) {
        this.contact = contact;
    }
}
