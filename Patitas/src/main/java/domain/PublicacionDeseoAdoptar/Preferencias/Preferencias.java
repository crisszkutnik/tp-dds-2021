package domain.PublicacionDeseoAdoptar.Preferencias;

import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TipoAnimal;
import domain.PublicacionDeseoAdoptar.Preferencias.Condiciones.GenericCondicion;
import domain.PublicacionDeseoAdoptar.Preferencias.Condiciones.PreferenciaSexo;
import domain.PublicacionDeseoAdoptar.Preferencias.Condiciones.PreferenciaTienePatio;
import domain.PublicacionDeseoAdoptar.Preferencias.Condiciones.PreferenciaTipoAnimal;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;

import javax.persistence.*;

@Entity
@Table(name = "preferencias")
public class Preferencias {
    @Column(name = "tiene_patio")
    private boolean tienePatio;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_animal")
    private TipoAnimal tipoAnimal;

    @Id
    @Column
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "publicacion_id")
    private PublicacionDeseoAdoptar publicacion;

    @Transient
    private static final GenericCondicion[] condiciones = {
            new PreferenciaSexo(),
            new PreferenciaTienePatio(),
            new PreferenciaTipoAnimal()
    };

    public boolean puedeAdoptar(Mascota mascota, Persona p) {
        if(mascota.getDuenio().equals(p))
            return false;

        for(GenericCondicion c : condiciones)
            if(!c.puedeAdoptar(this, mascota))
                return false;

        return true;
    }

    public boolean puedeAdoptar(Mascota mascota) {
        // Una persona no puede adoptar una Mascota de la que es duenio
        // (Chequeo solo valido si se evalua pref por una publicacion deseo adoptar)
        if (publicacion != null && publicacion.getPersona().equals(mascota.getDuenio()))
            return false;

        for (GenericCondicion c : condiciones) {
            if (!c.puedeAdoptar(this, mascota))
                return false;
        }
        return true;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PublicacionDeseoAdoptar getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDeseoAdoptar publicacion) {
        this.publicacion = publicacion;
    }

    public boolean getTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(boolean tienePatio) {
        this.tienePatio = tienePatio;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public TipoAnimal getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(TipoAnimal tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }
}
