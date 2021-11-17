package domain.Preguntas;

import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.PersistenteID;

import javax.persistence.*;

@Entity
@Table
public class Respuesta extends PersistenteID {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pregunta", referencedColumnName = "id")
    private Pregunta pregunta;

    @Column
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    private PublicacionOfertaAdopcion publicacion;

    public PublicacionOfertaAdopcion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionOfertaAdopcion publicacion) {
        this.publicacion = publicacion;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
