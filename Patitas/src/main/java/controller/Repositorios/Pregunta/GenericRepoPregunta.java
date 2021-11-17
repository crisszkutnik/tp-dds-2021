package controller.Repositorios.Pregunta;

import db.EntityManagerHelper;
import domain.Asociacion.Asociacion;
import domain.Preguntas.Pregunta;

import javax.persistence.NoResultException;
import java.util.List;

public interface GenericRepoPregunta {
    List<Pregunta> getByAsociacion(Asociacion a);
    List<Pregunta> getPreguntasGenericas();
}
