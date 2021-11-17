// Esta clase me hace llorar

package domain.AdopcionPublicacionOfertaAdopcion;

import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardAdopcionPublicacionOfertaAdopcion;
import domain.PersistenteID;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "adopcion_publicacion_oferta_adopcion")
public class AdopcionPublicacionOfertaAdopcion extends PersistenteID {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_oferta_adopcion", referencedColumnName = "id")
    private PublicacionOfertaAdopcion ofertaAdopcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuil_persona", referencedColumnName = "cuil")
    private Persona adoptante;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "publicacion", cascade = {CascadeType.ALL})
    private List<ContactCardAdopcionPublicacionOfertaAdopcion> contacto;

    public static class AdopcionPublicacionOfertaAdopcionDTO {
        public PublicacionOfertaAdopcion ofertaAdopcion;
        public Persona adoptante;
        public List<ContactCardAdopcionPublicacionOfertaAdopcion> contacto;
    }

    public AdopcionPublicacionOfertaAdopcion() {
        this.contacto = new ArrayList<>();
    }

    public List<ContactCard> getContactCard() {
        return contacto.stream().map(t -> t.getContact()).collect(Collectors.toList());
    }

    public void setContacto(List<ContactCardAdopcionPublicacionOfertaAdopcion> contacto) {
        this.contacto = contacto;
    }

    public List<ContactCardAdopcionPublicacionOfertaAdopcion> getContacto() {
        return contacto;
    }

    public void addContacto(ContactCardAdopcionPublicacionOfertaAdopcion p) {
        contacto.add(p);
    }

    public void addContacto(List<ContactCardAdopcionPublicacionOfertaAdopcion> p) {
        contacto.addAll(p);
    }

    public PublicacionOfertaAdopcion getOfertaAdopcion() {
        return ofertaAdopcion;
    }

    public void setOfertaAdopcion(PublicacionOfertaAdopcion ofertaAdopcion) {
        this.ofertaAdopcion = ofertaAdopcion;
    }

    public Persona getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Persona adoptante) {
        this.adoptante = adoptante;
    }
}
