package domain.Hogares;

import domain.Mascota.Mascota;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import domain.MascotaPerdida.MascotaPerdida;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "hogares")
public class Hogar {
    @Column
    @Id
    private String id;

    @Column
    private String nombre;

    @Column
    private String direccion;

    @Column
    private Double latitud;

    @Column
    private Double longitud;

    @Column
    private String telefono;

    @Column
    private Integer capacidad;

    @Column
    private Integer lugaresDisponibles;

    @Column
    private Boolean patio;

    @Transient
    private List<String> caracteristicas;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<TipoAnimal, Boolean> admisiones;

    // debug
    public void print() {
        System.out.println("------------------------------------");
        String format = "Hogar <<%s>>\n" +
                "ID: %s\nDireccion: %s\nCoords: %f;%f\nTelefono: %s\n" +
                "Capacidad: %d\nLugares: %d\nPatio: %b\n";
        System.out.format(
                format,
                this.nombre, this.id, this.direccion, this.latitud, this.longitud,
                this.telefono, this.capacidad, this.lugaresDisponibles, this.patio
        );
        System.out.println("Caracteristicas:");
        for (String caracteristica : caracteristicas) {
            System.out.println("\t"+caracteristica);
        }
        System.out.println("Admisiones:");
        for (Map.Entry<TipoAnimal, Boolean> entrada : admisiones.entrySet()) {
            System.out.format("\t%s :: %b\n", entrada.getKey().toString(), entrada.getValue());
        }
    }

    private Boolean aceptaTamanio(TamanioMascota t) {
        if (this.patio)
            return t.equals(TamanioMascota.MEDIANA) || t.equals(TamanioMascota.GRANDE);
        return t.equals(TamanioMascota.CHICA);
    }

    private Boolean mascotaCumpleCaracteristicas(Mascota m) {
        // logica: verdadero si la mascota cumple alguna caracteristica de las del hogar (si el hogar pide alguna)
        if (this.caracteristicas.size()==0) return true;
        for (String caracteristica : caracteristicas) {
            if (m.tieneCaracteristica(caracteristica)) return true;
        }
        return false;
    }

    public Double metrosAPunto(Double lat, Double lon) {
        // No tenemos la coordenada en el eje z, por eso los resultados pueden ser
        // un poquito distintos a los que se pueden obtener midiendo en Google Maps
        return org.apache.lucene.util.SloppyMath.haversinMeters(
                this.latitud, this.longitud,
                lat, lon
        );
    }

    public Double metrosAHogar(Hogar h) {
        return this.metrosAPunto(h.getLatitud(), h.getLongitud());
    }

    public boolean hogarApto(MascotaPerdida mp) {
        Mascota m = mp.getMascota();

        if(m == null)
            return hogarAptoSegunCondiciones(mp.getTipo(), mp.getTamanio());
        else
            return hogarApto(m);
    }

    public boolean hogarApto(Mascota m) {
        return hogarAptoSegunCondiciones(m.getTipoAnimal(), m.getTamanio());
    }

    private boolean hogarAptoSegunCondiciones(TipoAnimal tipo, TamanioMascota tamanio) {
        return admisiones.get(tipo) && lugaresDisponibles > 0 && aceptaTamanio(tamanio);
    }
    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getLugaresDisponibles() {
        return lugaresDisponibles;
    }

    public void setLugaresDisponibles(Integer lugaresDisponibles) {
        this.lugaresDisponibles = lugaresDisponibles;
    }

    public Boolean getPatio() {
        return patio;
    }

    public void setPatio(Boolean patio) {
        this.patio = patio;
    }

    public List<String> getCaracteristicas() { return caracteristicas; }

    public void setCaracteristicas(List<String> cars) { this.caracteristicas = cars; }

    public Map<TipoAnimal, Boolean> getAdmisiones() {
        return admisiones;
    }

    public void setAdmisiones(Map<TipoAnimal, Boolean> admisiones) {
        this.admisiones = admisiones;
    }
}
