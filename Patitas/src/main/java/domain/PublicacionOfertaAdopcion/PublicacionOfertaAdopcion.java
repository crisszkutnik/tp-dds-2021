package domain.PublicacionOfertaAdopcion;

import converters.LocalDateConverter;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.PersistenteID;
import domain.Preguntas.Respuesta;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicacion_oferta_adopcion")
public class PublicacionOfertaAdopcion extends PersistenteID {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asociacion", referencedColumnName = "id")
    private Asociacion asociacion;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    private Mascota mascota;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "publicacion", cascade = {CascadeType.ALL})
    private final List<Respuesta> respuestas;

    @Column
    private boolean vigente;

    public PublicacionOfertaAdopcion() {
        respuestas = new ArrayList<>();
    }

    public void agregarRespuesta(Respuesta rta) {
        respuestas.add(rta);
    }

    public void agregarListRespuesta(List<Respuesta> rtas) {
        respuestas.addAll(rtas);
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }
}
