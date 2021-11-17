import controller.ControllerAsociacion;
import controller.ControllerOfertaAdopcion;
import controller.ControllerPersona;
import controller.ControllerMascota;
import controller.ControllerUsuario;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.Preguntas.Pregunta;
import domain.Preguntas.Respuesta;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import domain.Usuario.Usuario;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdopcionesTest {
    @Test
    @Order(1)
    public void preguntasTest() {
        Asociacion as = ControllerAsociacion.agregarAsociacion("Mandibulas Fuertes");
        ControllerOfertaAdopcion.agregarPreguntaAsociacion(
                "Que onda?",
                new ArrayList<>(),
                as
        );
        ControllerOfertaAdopcion.agregarPreguntaAsociacion(
                "Que onda 2?",
                Arrays.asList("Res1", "Res2"),
                as
        );
        ControllerOfertaAdopcion.agregarPreguntaGeneral(
                "Preg general",
                new ArrayList<>()
        );

        List<Pregunta> pregsGenerales = ControllerOfertaAdopcion.getPreguntasGenerales();
        Assertions.assertTrue(
                pregsGenerales.stream().anyMatch(p -> p.getPregunta().equals("Preg general"))
        );
        List<Pregunta> pregsAsoc = ControllerOfertaAdopcion.getPreguntasAsociacion(as);
        Assertions.assertTrue(
                pregsAsoc.stream().anyMatch(p -> p.getPregunta().equals("Que onda?"))
        );
        Assertions.assertTrue(
                pregsAsoc.stream().anyMatch(p -> p.getPregunta().equals("Que onda 2?"))
        );
    }

    @Test
    @Order(2)
    public void darEnAdopcionUnaMascotaAjenaTest() {
        // Creamos a Chando y su usuario
        Persona.PersonaDTO dto = new Persona.PersonaDTO();
        dto.nombre = "Chandemonium";
        dto.apellido = "Schillingers";
        dto.cuil = "142749";
        dto.numeroDoc = "666";
        dto.tipoDocumento = TipoDocumento.PASAPORTE;

        Persona chando = ControllerPersona.crearPersona(dto);
        Usuario changos13 = ControllerUsuario.crearUsuario("changos13", "abc", chando);

        // Creamos a Pasta y a su mascota
        Persona.PersonaDTO pastaDTO = new Persona.PersonaDTO();
        pastaDTO.nombre = "Pastrafórico";
        pastaDTO.apellido = "Plutarquianksy";
        pastaDTO.cuil = "737373";
        pastaDTO.numeroDoc = "1";
        pastaDTO.tipoDocumento = TipoDocumento.CEDULA;

        Persona pasta = ControllerPersona.crearPersona(pastaDTO);

        Mascota.MascotaDTO tortugaPerro = new Mascota.MascotaDTO();
        tortugaPerro.nombre = "Tortuperro";
        tortugaPerro.anioNacimiento = 1984;
        tortugaPerro.duenio = pasta;
        Mascota tortuperro = ControllerMascota.persistirDTOMascota(tortugaPerro);

        ControllerAsociacion.agregarAsociacion("Vivero de Tortugas Perro");
        Asociacion rara = ControllerAsociacion.getAsociacionByName("Vivero de Tortugas Perro");

        ControllerUsuario.darMascotaEnAdopcion(changos13, tortuperro, rara, new ArrayList<>());

        // Asserts
        // odio lo verbose que es este lenguaje del demonio
        List<PublicacionOfertaAdopcion> ofertas_asociacion_rara = ControllerOfertaAdopcion.getOfertasByAsociacion(rara);
        Assertions.assertEquals(ofertas_asociacion_rara.size(), 0);
    }

    @Test
    @Order(3)
    public void variasOfertasDeAdopcionTest() {
        Asociacion as = ControllerAsociacion.getAsociacionByName("Mandibulas fuertes");
        List<Pregunta> pregsGenerales = ControllerOfertaAdopcion.getPreguntasGenerales();
        List<Pregunta> pregsAsoc = ControllerOfertaAdopcion.getPreguntasAsociacion(as);
        List<Respuesta> rtas = new ArrayList<>();

        Respuesta rta = new Respuesta();
        rta.setPregunta(pregsGenerales.get(0));
        rta.setRespuesta("Respuesta generica");
        rtas.add(rta);

        rta = new Respuesta();
        rta.setPregunta(pregsAsoc.get(0));
        rta.setRespuesta("Res1");
        rtas.add(rta);

        rta = new Respuesta();
        rta.setPregunta(pregsAsoc.get(1));
        rta.setRespuesta("Res2");
        rtas.add(rta);

        Mascota mascota = ControllerMascota.getMascotaByID(1);
        ControllerOfertaAdopcion.agregarOferta(as, mascota, rtas);
    }

    @Test
    @Order(4)
    public void publicacionQuieroAdoptarTest() {
        PublicacionDeseoAdoptar pub = new PublicacionDeseoAdoptar();
        pub.setVigente(true);
        pub.setPersona(ControllerPersona.getPersonaByCUIL("737373"));
        Preferencias pref = new Preferencias();
        pub.setPref(pref);
        pref.setPublicacion(pub);
        ControllerOfertaAdopcion.persistirDeseoAdoptar(pub);
    }

    @Test
    public void recomendacionesAdopcionTest() {
        // TODO
    }
}
