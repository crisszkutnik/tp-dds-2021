package domain.ContactCard;

import converters.ContactListAttributeConverter;
import domain.Mascota.Mascota;
import domain.PersistenteID;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "contact_card")
public class ContactCard extends PersistenteID {
    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String email;

    @Column
    private String telefono;

    @Column(name = "contact_list")
    @Convert(converter = ContactListAttributeConverter.class)
    private ContactList contactList;

    public static class ContactCardDTO {
        public String nombre;
        public String apellido;
        public String email;
        public String telefono;
    }

    public ContactCard() {
        contactList = new ContactList();
    }

    public ContactCardDTO toDTO() {
        ContactCardDTO dto = new ContactCardDTO();
        dto.nombre = this.nombre;
        dto.apellido = this.apellido;
        dto.email = this.email;
        dto.telefono = this.telefono;
        return dto;
    }

    public void notificarTodoRegistro(Mascota.MascotaDTO dto) {
        contactList.notificarTodosRegistro(dto, toDTO());
    }

    public void notificarTodosMascotaPerdida(Mascota.MascotaDTO mascota_dto) {
        contactList.notificarTodosMascotaPerdida(mascota_dto, toDTO());
    }

    public void notificarTodosAdopcion(Mascota.MascotaDTO mascota_dto) {
        contactList.notificarTodosAdopcion(mascota_dto, toDTO());
    }

    public void notificarTodosRecomendacionesAdopcion(List<PublicacionOfertaAdopcion> ofertas) {
        contactList.notificarTodosRecomendacionesAdopcion(toDTO(), ofertas);
    }
    //
    // Setters y getters
    //

    public ContactList getContactList() {
        return contactList;
    }

    public void setContactList(ContactList contactList) {
        this.contactList = contactList;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<FormaNotificacion<?>> getFormasNotificacion() {
        return contactList.getFormasNotificacion();
    }

    public void agregarFormaNotificacion(FormaNotificacion<?> nueva) {
        this.contactList.agregarFormaNotificacion(nueva);
    }
}
