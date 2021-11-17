package domain.Usuario;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table
public class Persona {
    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(name = "numero_doc")
    private String numeroDoc;

    @Column
    @Id
    private String cuil;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc")
    private TipoDocumento tipoDocumento;

    public static class PersonaDTO {
        public String nombre;
        public String apellido;
        public String numeroDoc;
        public String cuil;
        public TipoDocumento tipoDocumento;
    }

    public boolean equals(Persona a) {
        return this.cuil.equals(a.getCuil());
    }

    public PersonaDTO toDTO() {
        PersonaDTO dto = new PersonaDTO();
        dto.nombre = this.nombre;
        dto.apellido = this.apellido;
        dto.numeroDoc = this.numeroDoc;
        dto.cuil = this.cuil;
        dto.tipoDocumento = this.tipoDocumento;
        return dto;
    }

    //
    // Setters y getters
    //

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public String getCuil() {
        return cuil;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
