import controller.ControllerHogares;
import controller.ControllerMascota;
import controller.Repositorios.Asociaciones.SQLRepoAsociaciones;
import controller.Repositorios.Mascota.SQLRepoMascota;
import controller.Repositorios.MascotaPerdida.SQLRepoMascotaPerdida;
import controller.Repositorios.Persona.SQLRepoPersona;
import controller.Repositorios.Pregunta.SQLRepoPregunta;
import controller.Repositorios.PublicacionDeseoAdoptar.SQLRepoPublicacionDeseoAdoptar;
import controller.Repositorios.PublicacionOfertaAdopcion.SQLRepoPublicacionOfertaAdopcion;
import controller.Repositorios.Usuario.SQLRepoUsuario;
import domain.Asociacion.Asociacion;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascota;
import domain.ContactCard.ContactPerClass.ContactCardMascotaPerdida;
import domain.ContactCard.contactMethods.Email.EmailContactMethod;
import domain.ContactCard.contactMethods.SMS.SMSContactMethod;
import domain.ContactCard.contactMethods.WhatsApp.WhatsAppContactMethod;
import domain.Imagen.Imagen;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import domain.Preguntas.Pregunta;
import domain.Preguntas.Respuesta;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import domain.Usuario.Usuario;
import org.junit.jupiter.api.*;
//import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
//import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import utils.passwordUtilities.HashPassword;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBTest {
    /*@Test
    @Order(1)
    public void existeContexto() {
        Assertions.assertNotNull(entityManager());
    }

    @Test
    @Order(2)
    public void existeContextoConTransaccion() throws Exception {
        withTransaction(() -> {});
    }*/

    @Test
    public void populateHogaresTable() {
        ControllerHogares.actualizarHogares();
    }

    @Test
    @Order(3)
    public void creaGuardaRecuperaPersona() {
        Persona persona = new Persona();
        persona.setNombre("Cristobal");
        persona.setApellido("Szkutnik");
        persona.setCuil("20433148925");
        persona.setNumeroDoc("43314892");
        persona.setTipoDocumento(TipoDocumento.DNI);
        SQLRepoPersona repo = new SQLRepoPersona();
        repo.persist(persona);
        Persona persona2 = repo.getSingleFromRepo("20433148925");
        Assertions.assertEquals("Cristobal", persona2.getNombre());
    }

    @Test
    @Order(4)
    public void creaAsociacionRecuperaAsociacion() {
        SQLRepoAsociaciones repo = new SQLRepoAsociaciones();
        Asociacion asociacion = new Asociacion();
        asociacion.setNombre("Patitas");
        repo.persist(asociacion);
        Asociacion otra = repo.getAsociacionByName("Patitas");
        Assertions.assertEquals(otra.getNombre(), "Patitas");
    }

    @Test
    @Order(5)
    public void creaRecuperaUsuario() {
        SQLRepoUsuario repoUser = new SQLRepoUsuario();
        SQLRepoPersona repoPersona = new SQLRepoPersona();

        // Recuperamos la persona
        Persona persona = repoPersona.getSingleFromRepo("20433148925");

        // Creamos el usuario y lo persistimos
        Usuario nuevo = new Usuario();
        nuevo.setUsername("cristobalszk");
        nuevo.setAutorizacion(NivelAutorizacion.ADMIN);
        String passwordHash = HashPassword.hashPassword("prueba123");
        nuevo.setPasswordHash(passwordHash);
        nuevo.setPersona(persona);
        repoUser.persist(nuevo);

        // Recupera usuario
        Usuario user = repoUser.fetchByUsername("cristobalszk");
        Assertions.assertEquals(user.getUsername(), "cristobalszk");
        Assertions.assertEquals(user.getPersona().getNombre(), "Cristobal");
        Assertions.assertEquals(user.getPersona().getApellido(), "Szkutnik");
    }

    @Test
    @Order(6)
    public void agregaRecuperaAsociacionUsuario() {
        SQLRepoUsuario repoUser = new SQLRepoUsuario();
        SQLRepoAsociaciones repoAsc = new SQLRepoAsociaciones();
        Usuario user = repoUser.fetchByUsername("cristobalszk");
        Asociacion asociacion = repoAsc.getAsociacionByName("Patitas");
        user.setAsociacion(asociacion);

        // Guardamos el usuario con los cambios
        repoUser.persist(user);

        Usuario user2 = repoUser.fetchByUsername("cristobalszk");
        Assertions.assertEquals(user2.getAsociacion().getNombre(), "Patitas");
    }

    @Test
    @Order(7)
    public void crearMascota() {
        Persona per = new SQLRepoPersona().getSingleFromRepo("20433148925");
        Mascota.MascotaDTO dto = new Mascota.MascotaDTO();
        dto.nombre = "Pantufla";
        dto.apodo = "Chaucha";
        dto.duenio = per;
        dto.anioNacimiento = 1900;
        dto.tipoAnimal = TipoAnimal.PERRO;
        dto.sexo = Sexo.MACHO;
        dto.tamanio = TamanioMascota.GRANDE;
        Mascota mas = ControllerMascota.crearMascota(dto);

        Imagen img = new Imagen();
        img.setNombre("test1.jpg");
        img.setMascota(mas);
        mas.agregarImagen(img);
        img = new Imagen();
        img.setNombre("test2.jpg");
        img.setMascota(mas);
        mas.agregarImagen(img);

        ControllerMascota.persistirObjMascota(mas);
    }

    @Test
    @Order(8)
    public void creaContactCards() {
        Mascota mascota = new SQLRepoMascota().getMascotaByID(1);
        ContactCard card_1 = new ContactCard();
        card_1.setNombre("Julian");
        card_1.setApellido("Delasparedes");
        card_1.setTelefono("+14274973");
        card_1.setEmail("jdelasparedes@frba.utn.edu.ar");
        card_1.agregarFormaNotificacion(new EmailContactMethod());
        card_1.agregarFormaNotificacion(new SMSContactMethod());

        ContactCard card_2 = new ContactCard();
        card_2.setNombre("Mauricio");
        card_2.setApellido("Anteca");
        card_2.setTelefono("+111111");
        card_2.setEmail("manteca@frba.utn.edu.ar");
        card_2.agregarFormaNotificacion(new WhatsAppContactMethod());
        card_2.agregarFormaNotificacion(new EmailContactMethod());

        ContactCardMascota cm = new ContactCardMascota();
        cm.setContact(card_1);
        cm.setMascota(mascota);
        mascota.agregarContacto(cm);
        cm = new ContactCardMascota();
        cm.setContact(card_2);
        cm.setMascota(mascota);
        mascota.agregarContacto(cm);
        new SQLRepoMascota().persist(mascota);
    }

    @Test
    @Order(9)
    public void recuperaContactCards() {
        Mascota mascota = new SQLRepoMascota().getMascotaByID(1);
        List<ContactCard> lista = mascota.getContactCard();
        Assertions.assertEquals(lista.size(), 2);
        ContactCard c = lista.get(0);
        Assertions.assertEquals(c.getContactList().getFormasNotificacion().size(), 2);
        System.out.println(c.getContactList().getStr());
        c = lista.get(1);
        Assertions.assertEquals(c.getContactList().getFormasNotificacion().size(), 2);
        System.out.println(c.getContactList().getStr());
    }

    @Test
    @Order(10)
    public void persisteMascotaPerdida() {
        Mascota mascota = new SQLRepoMascota().getMascotaByID(1);
        Persona per = new SQLRepoPersona().getSingleFromRepo("20433148925");

        MascotaPerdida m = new MascotaPerdida();

        m.setAprobada(false);
        m.setMascota(mascota);
        m.setDescripcion("Se perdio porque nacio en el año 1900");
        m.setLatitud(-14.27);
        m.setLongitud(27.49);
        m.setRescatista(per);

        ContactCard cc = new ContactCard();
        cc.setNombre("Carlos");
        cc.setApellido("Argueso");
        cc.setEmail("a@a.a");
        cc.setTelefono("123");
        ContactCardMascotaPerdida c = new ContactCardMascotaPerdida();
        c.setContact(cc);
        c.setMascotaPerdida(m);
        m.agregarContacto(c);

        Imagen i3 = new Imagen();
        i3.setMascotaPerdida(m);
        i3.setNombre("i3.jpg");
        m.agregarFoto(i3);

        new SQLRepoMascotaPerdida().persist(m);
    }

    @Test
    @Order(11)
    public void recuperaMascotaPerdida() {
        Mascota mascota = new SQLRepoMascota().getMascotaByID(1);
        Persona per = new SQLRepoPersona().getSingleFromRepo("20433148925");
        List<MascotaPerdida> pendientes = new SQLRepoMascotaPerdida().getPendientes(0, 1);
        Assertions.assertEquals(1, pendientes.size());

        MascotaPerdida m = pendientes.get(0);

        Assertions.assertFalse(m.isAprobada());
        Assertions.assertEquals("Se perdio porque nacio en el año 1900", m.getDescripcion());
        Assertions.assertEquals(-14.27, m.getLatitud());
        Assertions.assertEquals(27.49, m.getLongitud());
        Assertions.assertEquals(mascota, m.getMascota());
        Assertions.assertEquals(per, m.getRescatista());
        System.out.println(m.getFotos().get(0).getNombre() + " " + m.getFotos().get(0).getMascotaPerdida().getId());
        Assertions.assertEquals(m.getFotos().get(0).getNombre(), "i3.jpg");

        List<ContactCard> c = m.getContactCard();
        Assertions.assertEquals(c.size(), 1);
        Assertions.assertEquals("Carlos", c.get(0).getNombre());
    }

    @Test
    @Order(12)
    public void persisteRecuperaPreguntaAsociacion() {
        Asociacion a = new SQLRepoAsociaciones().getAsociacionByName("Patitas");
        SQLRepoPregunta repoPreg = new SQLRepoPregunta();
        Pregunta p = new Pregunta();
        p.setAsociacion(a);
        p.setPregunta("Pare de sufrir?");
        p.agregarRespuestaPosible("Si");
        p.agregarRespuestaPosible("No");
        repoPreg.persist(p);

        p = new Pregunta();
        p.setAsociacion(a);
        p.setPregunta("Por que quiere dejar de sufrir?");
        repoPreg.persist(p);

        p = new Pregunta();
        p.setPregunta("Esta es generica");
        repoPreg.persist(p);

        // Intentamos recuperar las preguntas

        List<Pregunta> preguntas = repoPreg.getByAsociacion(a);
        Assertions.assertEquals(preguntas.size(), 2);
        preguntas = repoPreg.getPreguntasGenericas();

        // Hay una pregunta anterior que se genera en AdopcionesTest.preguntasTest
        Assertions.assertEquals(preguntas.size(), 2);
    }

    @Test
    @Order(13)
    public void creaPublicacionOfertaAdopcion() {
        SQLRepoPublicacionOfertaAdopcion repo = new SQLRepoPublicacionOfertaAdopcion();
        SQLRepoPregunta repoPreg = new SQLRepoPregunta();
        Mascota m = new SQLRepoMascota().getMascotaByID(1);
        Asociacion a = new SQLRepoAsociaciones().getAsociacionByName("Patitas");
        PublicacionOfertaAdopcion p = new PublicacionOfertaAdopcion();
        p.setAsociacion(a);
        p.setMascota(m);
        p.setVigente(true);
        p.setFecha(LocalDate.of(2000, 10, 14));
        List<Pregunta> preguntasAsoc = repoPreg.getByAsociacion(a);
        List<Pregunta> preguntaGenerica = repoPreg.getPreguntasGenericas();

        Respuesta r1 = new Respuesta();
        r1.setPregunta(preguntasAsoc.get(0));
        r1.setRespuesta("Si");
        r1.setPublicacion(p);

        Respuesta r2 = new Respuesta();
        r2.setPregunta(preguntasAsoc.get(1));
        r2.setRespuesta("No");
        r2.setPublicacion(p);

        Respuesta r3 = new Respuesta();
        r3.setPregunta(preguntaGenerica.get(0));
        r3.setRespuesta("No se tal vez");
        r3.setPublicacion(p);

        p.agregarListRespuesta(Arrays.asList(r1, r2, r3));
        repo.persist(p);
    }

    @Test
    @Order(14)
    public void creaPublicacionDeseoAdoptar() {
        SQLRepoPublicacionDeseoAdoptar repo = new SQLRepoPublicacionDeseoAdoptar();
        PublicacionDeseoAdoptar p = new PublicacionDeseoAdoptar();
        p.setVigente(true);
        p.setPersona(new SQLRepoPersona().getSingleFromRepo("20433148925"));
        Preferencias pref = p.getPref();
        pref.setPublicacion(p);
        pref.setSexo(Sexo.MACHO);
        pref.setTienePatio(false);
        pref.setTipoAnimal(TipoAnimal.GATO);
        repo.persist(p);
    }
}
