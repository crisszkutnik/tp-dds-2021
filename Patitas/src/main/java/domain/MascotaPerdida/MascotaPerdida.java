package domain.MascotaPerdida;

import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascotaPerdida;
import domain.Hogares.Hogar;
import domain.Imagen.Imagen;
import domain.Mascota.Mascota;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import domain.PersistenteID;
import domain.Usuario.Persona;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascotas_perdidas")
public class MascotaPerdida extends PersistenteID {
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mascotaPerdida", cascade = {CascadeType.ALL})
    private final List<Imagen> fotos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuil_rescatista", referencedColumnName = "cuil")
    private Persona rescatista;

    @Column
    private String descripcion;

    @Column
    private Double latitud;

    @Column
    private Double longitud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_hogar", referencedColumnName = "id")
    private Hogar hogar;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mascotaPerdida", cascade = {CascadeType.ALL})
    private final List<ContactCardMascotaPerdida> contacto;

    @Column
    private boolean aprobada;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_animal")
    private TipoAnimal tipo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tamanio_mascota")
    private TamanioMascota tamanio;

    public MascotaPerdida() {
        this.fotos = new ArrayList<>();
        this.contacto = new ArrayList<>();
        this.mascota = null; // null es que todavia no se identifico que mascota es
        this.aprobada = false;
    }

    public void notificar() {
        if (this.mascota != null)
            this.mascota.notificarDuenioMascotaPerdida();
    }

    public Double metrosAHogar(Hogar h) {
        return h.metrosAPunto(this.latitud, this.longitud);
    }

    public static class MascotaPerdidaDTO {
        public Persona rescatista;
        public String descripcion;
        public Double latitud;
        public Double longitud;
        public Mascota mascota;
        public Hogar hogar;
        public List<Imagen> fotos;
        public List<ContactCardMascotaPerdida> contacto;
    }

    public MascotaPerdidaDTO toDTO() {
        MascotaPerdidaDTO dto = new MascotaPerdidaDTO();
        dto.rescatista = this.rescatista;
        dto.descripcion = this.descripcion;
        dto.latitud = this.latitud;
        dto.longitud = this.longitud;
        dto.mascota = this.mascota;
        dto.fotos = this.fotos;
        dto.contacto = this.contacto;
        return dto;
    }

    //
    // Setters y getters
    //

    public TipoAnimal getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    public TamanioMascota getTamanio() {
        return tamanio;
    }

    public void setTamanio(TamanioMascota tamanio) {
        this.tamanio = tamanio;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public List<Imagen> getFotos() {
        return fotos;
    }

    public void agregarFoto(Imagen foto) {
        foto.setMascotaPerdida(this);
        this.fotos.add(foto);
    }

    public void agregarFotos(List<Imagen> fotos) {
        for(Imagen i : fotos)
            i.setMascotaPerdida(this);

        this.fotos.addAll(fotos);
    }

    public Persona getRescatista() {
        return rescatista;
    }

    public void setRescatista(Persona rescatista) {
        this.rescatista = rescatista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Hogar getHogar() {
        return hogar;
    }

    public void setHogar(Hogar hogar) {
        this.hogar = hogar;
    }

    public List<ContactCardMascotaPerdida> getContacto() { return contacto; }

    public List<ContactCard> getContactCard() {
        List<ContactCard> r = new ArrayList<>();
        for(ContactCardMascotaPerdida m : contacto)
            r.add(m.getContact());
        return r;
    }

    public void agregarContacto(ContactCardMascotaPerdida nuevo) { contacto.add(nuevo); }

    public void agregarContacto(List<ContactCardMascotaPerdida> nuevas) {
        contacto.addAll(nuevas);
    }
}
