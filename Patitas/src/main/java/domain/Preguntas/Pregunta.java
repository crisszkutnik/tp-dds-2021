package domain.Preguntas;

import domain.Asociacion.Asociacion;
import domain.PersistenteID;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
public class Pregunta extends PersistenteID {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asociacion", referencedColumnName = "id")
    private Asociacion asociacion;

    @Column
    private String pregunta;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(
            name = "pregunta_respuestas_posibles",
            joinColumns = @JoinColumn(name = "pregunta_id", referencedColumnName = "id")
    )
    @Column(name = "respuesta_posible")
    private final List<String> respuestasPosibles;

    public Pregunta() {
        respuestasPosibles = new ArrayList<>();
    }

    private boolean existeRespuestaPosible(String str) {
        return respuestasPosibles.stream().anyMatch(n -> n.equals(str));
    }

    public boolean respuestaPosible(String rta) {
        if(respuestasPosibles.size() == 0) // Si esta vacio, se supone que se acepta cualquier respuesta
            return true;

        return respuestasPosibles.stream().anyMatch(n -> n.equals(rta));
    }

    public void agregarListaRespuestasPosibles(List<String> rtas) {
        if(rtas.stream().anyMatch(this::existeRespuestaPosible))
            return;

        respuestasPosibles.addAll(rtas);
    }

    public void agregarRespuestaPosible(String rta) {
        if(existeRespuestaPosible(rta))
            return;

        respuestasPosibles.add(rta);
    }

    public List<String> getRespuestasPosibles() {
        return respuestasPosibles;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
