package domain.ContactCard.ContactPerClass;

import domain.ContactCard.ContactCard;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PersistenteID;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;

import javax.persistence.*;

@Entity
@Table(name = "contact_card_publicacion_deseo_adoptar")
public class ContactCardPublicacionDeseoAdoptar extends PersistenteID {
    @ManyToOne
    @JoinColumn(name = "id_publicacion_deseo_adoptar", referencedColumnName = "id")
    private PublicacionDeseoAdoptar publicacionDeseoAdoptar;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
    private ContactCard contact;

    public ContactCardPublicacionDeseoAdoptar() {
        contact = new ContactCard();
    }

    public PublicacionDeseoAdoptar getPublicacionDeseoAdoptar() {
        return publicacionDeseoAdoptar;
    }

    public void setPublicacionDeseoAdoptar(PublicacionDeseoAdoptar publicacionDeseoAdoptar) {
        this.publicacionDeseoAdoptar = publicacionDeseoAdoptar;
    }

    public ContactCard getContact() {
        return contact;
    }

    public void setContact(ContactCard contact) {
        this.contact = contact;
    }
}

