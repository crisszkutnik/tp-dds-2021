package domain.Mascota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.Repositorios.Caracteristicas.RepositorioCaracteristicas;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascota;
import domain.PersistenteID;
import domain.Usuario.Persona;
import domain.Imagen.Imagen;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table
public class Mascota extends PersistenteID {
    @Column
    private String nombre;

    @Column
    private String apodo;

    @Column(name = "anio_nacimiento")
    private int anioNacimiento;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_animal")
    private TipoAnimal tipoAnimal;

    @Enumerated(EnumType.ORDINAL)
    private Sexo sexo;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mascota", cascade = {CascadeType.ALL})
    private List<ContactCardMascota> contacto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuil_duenio", referencedColumnName = "cuil")
    private Persona duenio;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mascota", cascade = {CascadeType.ALL})
    private final List<Imagen> imagenes;

    @Column(name = "en_adopcion")
    private boolean enAdopcion;

    @ElementCollection
    private final List<String> caracteristicas;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tamanio_mascota")
    private TamanioMascota tamanio;

    @Column
    private String descripcion;

    public Mascota() {
        contacto = new ArrayList<>();
        imagenes = new ArrayList<>();
        caracteristicas = new ArrayList<>();
    }

    public void notificarDuenioMascotaPerdida() {
        for(ContactCardMascota card : this.contacto)
            card.getContact().notificarTodosMascotaPerdida(toDTO());
    }

    public void notificarDuenioAdopcion() {
        for (ContactCardMascota card : this.contacto)
            card.getContact().notificarTodosAdopcion(toDTO());
    }

    public boolean tieneCaracteristica(String nombre) {
        return caracteristicas.stream().anyMatch(nombre::equals);
    }

    public void agregarCaracteristica(String nombre) {
        if(!tieneCaracteristica(nombre) && RepositorioCaracteristicas.existePosibilidad(nombre))
            caracteristicas.add(nombre);
    }

    public void agregarContactos(List<ContactCardMascota> l) {
        contacto.addAll(l);
    }

    public void setContacto(List<ContactCardMascota> contacto) {
        this.contacto = contacto;
    }

    public void agregarImagen(String name) throws IOException {
        Imagen img = new Imagen();
        img.setNombre(name);
        img.setMascota(this);
        img.normalizar();
        this.imagenes.add(img);
    }

    public static class MascotaDTO {
        public String nombre;
        public String apodo;
        public int anioNacimiento;
        public TipoAnimal tipoAnimal;
        public Sexo sexo;
        public List<ContactCardMascota> contacto;
        public Persona duenio;
        public List<Imagen> imagenes;
        public List<String> caracteristicas;
        public TamanioMascota tamanio;
        public String descripcion;
        public boolean enAdopcion;

        public MascotaDTO() {
            imagenes = new ArrayList<>();
            contacto = new ArrayList<>();
            caracteristicas = new ArrayList<>();
        }
    }

    public MascotaDTO toDTO() {
        MascotaDTO dto = new MascotaDTO();
        dto.nombre = this.nombre;
        dto.apodo = this.apodo;
        dto.anioNacimiento = this.anioNacimiento;
        dto.tipoAnimal = this.tipoAnimal;
        dto.sexo = this.sexo;
        dto.contacto = this.contacto;
        dto.duenio = this.duenio;
        dto.imagenes = this.imagenes;
        dto.caracteristicas = this.caracteristicas;
        dto.tamanio = this.tamanio;
        return dto;
    }

    //
    // Setters y getters
    //


    public boolean isEnAdopcion() {
        return enAdopcion;
    }

    public void setEnAdopcion(boolean enAdopcion) {
        this.enAdopcion = enAdopcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public TipoAnimal getTipoAnimal() {
        return tipoAnimal;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public List<ContactCardMascota> getContacto() {
        return contacto;
    }

    public List<ContactCard> getContactCard() {
        List<ContactCard> r = new ArrayList<>();
        for(ContactCardMascota m : contacto)
            r.add(m.getContact());
        return r;
    }

    public Persona getDuenio() {
        return duenio;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public void setTipoAnimal(TipoAnimal tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void agregarContacto(ContactCardMascota ctc) {
        contacto.add(ctc);
    }

    public void setDuenio(Persona duenio) {
        this.duenio = duenio;
    }

    public void agregarImagen(Imagen img) {
        imagenes.add(img);
    }

    public List<String> getCaracteristicas() {
        return this.caracteristicas;
    }

    public TamanioMascota getTamanio() {
        return tamanio;
    }

    public void setTamanio(TamanioMascota tamanio) {
        this.tamanio = tamanio;
    }
}
