package domain.ContactCard.ContactPerClass;

import domain.AdopcionPublicacionOfertaAdopcion.AdopcionPublicacionOfertaAdopcion;
import domain.ContactCard.ContactCard;
import domain.Mascota.Mascota;
import domain.PersistenteID;

import javax.persistence.*;

@Entity
@Table(name = "contact_card_adopcion_publicacion_oferta_adopcion")
public class ContactCardAdopcionPublicacionOfertaAdopcion extends PersistenteID  {
    @ManyToOne
    @JoinColumn(name = "id_adopcion_publicacion_oferta_adopcion", referencedColumnName = "id")
    private AdopcionPublicacionOfertaAdopcion publicacion;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_contact_card", referencedColumnName = "id")
    private ContactCard contact;

    public ContactCardMascota convertToContactCardMascota(Mascota mascota) {
        ContactCardMascota ret = new ContactCardMascota();
        ret.setMascota(mascota);
        ret.setContact(this.contact);
        return ret;
    }

    public ContactCardAdopcionPublicacionOfertaAdopcion() {
        contact = new ContactCard();
    }

    public AdopcionPublicacionOfertaAdopcion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(AdopcionPublicacionOfertaAdopcion publicacion) {
        this.publicacion = publicacion;
    }

    public ContactCard getContact() {
        return contact;
    }

    public void setContact(ContactCard contact) {
        this.contact = contact;
    }
}
