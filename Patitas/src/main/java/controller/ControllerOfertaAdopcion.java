package controller;

import controller.Repositorios.Pregunta.SQLRepoPregunta;
import controller.Repositorios.PublicacionDeseoAdoptar.SQLRepoPublicacionDeseoAdoptar;
import controller.Repositorios.PublicacionOfertaAdopcion.SQLRepoPublicacionOfertaAdopcion;
import db.HibernateUtil;
import domain.Asociacion.Asociacion;
// esta clase es totalmente obesa
import domain.ContactCard.ContactPerClass.ContactCardPublicacionDeseoAdoptar;
import domain.Mascota.Mascota;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Preguntas.Pregunta;
import domain.Preguntas.Respuesta;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.Usuario.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerOfertaAdopcion {
    private static final SQLRepoPregunta repoPreguntas = new SQLRepoPregunta();
    private static final SQLRepoPublicacionOfertaAdopcion repoPublicacionOfertaAdopcion = new SQLRepoPublicacionOfertaAdopcion();
    private static final SQLRepoPublicacionDeseoAdoptar repoPublicacionDeseoAdoptar = new SQLRepoPublicacionDeseoAdoptar();

    public static List<PublicacionOfertaAdopcion> getOfertasAdopcionByParams(Preferencias pref) {
        try {
            List<PublicacionOfertaAdopcion> todas = repoPublicacionOfertaAdopcion.getAll();
            return todas.stream().filter(
                    p -> pref.puedeAdoptar(p.getMascota())
            ).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<PublicacionOfertaAdopcion> getOfertasAdopcionByParams(Preferencias pref, Persona per) {
        try {
            return repoPublicacionOfertaAdopcion.getAll().stream().filter(p -> pref.puedeAdoptar(p.getMascota(), per)).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static PublicacionOfertaAdopcion getOfertaAdopcionById(int id) {
        return repoPublicacionOfertaAdopcion.getById(id);
    }

    public static PublicacionOfertaAdopcion getPublicacionOfertaAdopcionByMascota(Mascota m) {
        return repoPublicacionOfertaAdopcion.getByMascota(m);
    }

    public static void agregarPreguntaGeneral(String pregunta, List<String> opciones) {
        Pregunta p = new Pregunta();
        p.setPregunta(pregunta);
        p.agregarListaRespuestasPosibles(opciones);
        repoPreguntas.persist(p);
    }

    public static void agregarPreguntaAsociacion(String pregunta, List<String> opciones, Asociacion asociacion) {
        Pregunta p = new Pregunta();
        p.setAsociacion(asociacion);
        p.setPregunta(pregunta);
        p.agregarListaRespuestasPosibles(opciones);
        repoPreguntas.persist(p);
    }

    public static void eliminarPregunta(int id) {
        repoPreguntas.deleteById(id);
    }

    public static List<Pregunta> getPreguntasGenerales() {
        return repoPreguntas.getPreguntasGenericas();
    }

    public static List<Pregunta> getPreguntasAsociacion(Asociacion asociacion) {
         return repoPreguntas.getByAsociacion(asociacion);
    }

    public static Pregunta getPreguntaById(int id) {
        return repoPreguntas.getById(id);
    }

    public static void agregarOferta(Asociacion asociacion, Mascota mascota, List<Respuesta> respuestas) {
        PublicacionOfertaAdopcion oferta = new PublicacionOfertaAdopcion();
        oferta.setAsociacion(asociacion);
        oferta.setMascota(mascota);
        mascota.setEnAdopcion(true);
        oferta.setVigente(true);

        for(Respuesta rta : respuestas)
            rta.setPublicacion(oferta);

        oferta.agregarListRespuesta(respuestas);
        oferta.setFecha(LocalDate.now());
        //repoPublicacionOfertaAdopcion.persist(oferta);
        //ControllerMascota.updateMascota(mascota);
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.persist(oferta);
        s.merge(mascota);
        t.commit();
        s.close();
    }

    public static void removerOfertaAdopcion(PublicacionOfertaAdopcion p) {
        Mascota mas = p.getMascota();
        repoPublicacionOfertaAdopcion.remove(p);
        mas.setEnAdopcion(false);
        ControllerMascota.updateMascota(mas);
    }

    public static void removerOfertaAdopcion(Mascota mascota) throws Exception {
        PublicacionOfertaAdopcion p = repoPublicacionOfertaAdopcion.getByMascota(mascota);
        // Primero vamos a tener que remover todas las AdopcionPublicacionOfertaAdopcion
        ControllerAdopcionOfertaAdopcion.removerTodasAdopcionOfertaAdopcion(p);
        repoPublicacionOfertaAdopcion.remove(p);
        mascota.setEnAdopcion(false);
        ControllerMascota.updateMascota(mascota);
    }

    public static List<PublicacionOfertaAdopcion> getOfertasByAsociacion(Asociacion asociacion) {
        return repoPublicacionOfertaAdopcion.getByAsociacion(asociacion);
    }

    public static PublicacionDeseoAdoptar crearDeseoAdoptar(Persona persona, Preferencias p, List<ContactCardPublicacionDeseoAdoptar> contacto) {
        PublicacionDeseoAdoptar ret = new PublicacionDeseoAdoptar();
        ret.setFecha(LocalDate.now());
        ret.setPref(p);
        ret.setPersona(persona);
        ret.setVigente(true);
        p.setPublicacion(ret);
        ret.addContacto(contacto);
        contacto.forEach(c -> c.setPublicacionDeseoAdoptar(ret));
        return ret;
    }

    public static void persistirDeseoAdoptar(PublicacionDeseoAdoptar pub) {
        repoPublicacionDeseoAdoptar.persist(pub);
    }

    public static void eliminarDeseoAdoptar(PublicacionDeseoAdoptar pub) {
        repoPublicacionDeseoAdoptar.remove(pub);
    }

    public static PublicacionDeseoAdoptar getDeseoAdoptarById(int id) {
        return repoPublicacionDeseoAdoptar.getById(id);
    }

    public static List<PublicacionDeseoAdoptar> getDeseoAdoptarByPersona(Persona persona) {
        return repoPublicacionDeseoAdoptar.getByPersona(persona);
    }

    public static List<PublicacionDeseoAdoptar> getAllDeseoAdoptar() {
        return repoPublicacionDeseoAdoptar.findAll();
    }
}
