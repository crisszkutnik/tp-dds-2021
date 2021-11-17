package domain.PublicacionDeseoAdoptar;

import converters.LocalDateConverter;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardPublicacionDeseoAdoptar;
import domain.PersistenteID;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.Usuario.Persona;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "publicacion_deseo_adoptar")
public class PublicacionDeseoAdoptar extends PersistenteID {
    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "cuil_persona", referencedColumnName = "cuil")
    private Persona persona;

    @Column
    private boolean vigente;

    @OneToOne(mappedBy = "publicacion", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Preferencias pref;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "publicacionDeseoAdoptar", cascade = {CascadeType.ALL})
    private List<ContactCardPublicacionDeseoAdoptar> contacto;

    public PublicacionDeseoAdoptar() {
        pref = new Preferencias();
        contacto = new ArrayList<>();
    }

   public void addContacto(ContactCardPublicacionDeseoAdoptar nuevo) {
        contacto.add(nuevo);
   }

   public void addContacto(List<ContactCardPublicacionDeseoAdoptar> nuevo) {
        contacto.addAll(nuevo);
   }

   public List<ContactCardPublicacionDeseoAdoptar> getContacto() {
        return contacto;
   }

   public List<ContactCard> getContactCard() {
        return contacto.stream().map(ContactCardPublicacionDeseoAdoptar::getContact).collect(Collectors.toList());
   }

    public Preferencias getPref() {
        return pref;
    }

    public void setPref(Preferencias pref) {
        this.pref = pref;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }
}
