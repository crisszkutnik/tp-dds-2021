import com.google.zxing.WriterException;
import controller.ControllerAdmin;
import controller.ControllerMascotaPerdida;
import controller.ControllerPersona;
import controller.ControllerMascota;
import controller.Mascota.QRGenerator;
import controller.Mascota.ZXingQRGenerator;
import domain.ContactCard.ContactCard;
import domain.ContactCard.contactMethods.Email.Methods.SendgridEmail;
import domain.Imagen.Graphics2DResizer;
import domain.Imagen.Imagen;
import domain.Imagen.NormalizadorImagen;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.GlobalConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MascotasTest {
    @Test
    public void testLaBestiaNoPuedeTenerCaracteristicaBestial() {
        ControllerAdmin.agregarCaracteristicaPosible("Brutal");
        ControllerAdmin.agregarCaracteristicaPosible("Canibal");

        Mascota laBestia = new Mascota();
        laBestia.setSexo(Sexo.HEMBRA);
        laBestia.setApodo("LA BESTIA");
        laBestia.setTamanio(TamanioMascota.GRANDE); // porque no existe GIGANTE
        laBestia.setTipoAnimal(TipoAnimal.GATO); // plot twist
        laBestia.setAnioNacimiento(1980);
        laBestia.agregarCaracteristica("Brutal");
        laBestia.agregarCaracteristica("Bestial");
        laBestia.agregarCaracteristica("Canibal");

        assert laBestia.getCaracteristicas().size() == 2;
        assert !laBestia.tieneCaracteristica("Bestial");
    }

    @Test
    public void testAgregarImagenConResize() {
        // Cosas que se van a inicializar en config mas adelante!
        JSONObject imgProps = GlobalConfig.configObj.getJSONObject("IMAGE_PROPERTIES");
        final Integer WIDTH = imgProps.getInt("width");
        final Integer HEIGHT = imgProps.getInt("height");
        NormalizadorImagen norm = new Graphics2DResizer();
        Imagen.setNormalizador(norm);

        // Resto del test
        final String path = "src/test/resources/perro.jpg";
        Mascota patitas = new Mascota();
        try {
            patitas.agregarImagen(path);
        }
        catch (IOException e) {
            Assertions.fail("No se pudo abrir la imagen en " + path);
        }
        assert patitas.getImagenes().size() == 1;

        // A ver si anduvo?!?!
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            Integer width = bimg.getWidth();
            Integer height = bimg.getHeight();

            assert width.equals(WIDTH) && height.equals(HEIGHT);
        }
        catch (IOException e) {
            Assertions.fail("Por alguna razon ahora no se puede abrir mas la imagen o.O");
        }
    }

    @Test
    public void testGenerarQR() {
        QRGenerator generadorQR = new ZXingQRGenerator();
        try {
            generadorQR.generarQR("Soy un QR de prueba que generé en un test","src/test/resources/QR.PNG");
        }
        catch (IOException e1) {
            Assertions.fail("IOException en generar QR de prueba.");
        }
        catch (WriterException e) {
            Assertions.fail("WriterException en generar QR de prueba.");
        }
    }

    @Test
    public void testGenerarMascotaPerdidaConChapita() {
        // Genero el dueño de Garfield que no me acuerdo quien es
        Persona.PersonaDTO dto_duenio = new Persona.PersonaDTO();
        dto_duenio.nombre = "No me ";
        dto_duenio.apellido = "Acuerdo";
        dto_duenio.tipoDocumento = TipoDocumento.DNI;
        dto_duenio.numeroDoc = "14";
        dto_duenio.cuil = "20-14-4";
        Persona duenio_garfield = ControllerPersona.crearPersona(dto_duenio);

        // Contact card
        ContactCard contacto = new ContactCard();
        contacto.setApellido("Acuerdo");
        contacto.setNombre("Nome");
        contacto.setTelefono("+5414271166666666");
        contacto.setEmail("dipietroguido@gmail.com");
        contacto.agregarFormaNotificacion(new SendgridEmail());

        // Genero a Garfield
        Mascota.MascotaDTO garfield_dto = new Mascota.MascotaDTO();
        garfield_dto.nombre = "Garry Field";
        garfield_dto.apodo = "Garfield";
        garfield_dto.duenio = duenio_garfield;
        //garfield_dto.contacto = Collections.singletonList(contacto);

        Mascota garfield = ControllerMascota.persistirDTOMascota(garfield_dto);

        // Se pierde Garfield, un rescatista avisa
        Persona.PersonaDTO rescatista_dto = new Persona.PersonaDTO();
        rescatista_dto.nombre = "Pedro";
        rescatista_dto.apellido = "Polentas";
        rescatista_dto.cuil = "65432";
        rescatista_dto.numeroDoc = "1";
        rescatista_dto.tipoDocumento = TipoDocumento.PASAPORTE;
        Persona rescatista = ControllerPersona.crearPersona(rescatista_dto);

        MascotaPerdida.MascotaPerdidaDTO garfield_perdido = new MascotaPerdida.MascotaPerdidaDTO();
        garfield_perdido.mascota = garfield;
        garfield_perdido.descripcion = "Es un gato naranja que habla";
        garfield_perdido.latitud = -35.0;
        garfield_perdido.longitud = -54.0;
        garfield_perdido.rescatista = rescatista;

        ControllerMascotaPerdida.generarMascotaPerdidaConocida(garfield_perdido);
        Assertions.assertEquals(1, ControllerMascotaPerdida.obtenerPendientes(0, 5).size());
    }

    @Test
    public void testGenerarMascotaPerdidaSinChapita() {
        //Genero a Pluto
        Mascota.MascotaDTO pluto_dto = new Mascota.MascotaDTO();
        pluto_dto.nombre = "Pluto";
        pluto_dto.apodo = "Pluto";
        pluto_dto.tamanio = TamanioMascota.GRANDE;
        pluto_dto.sexo = Sexo.MACHO;
        pluto_dto.anioNacimiento = 1910;
        pluto_dto.tipoAnimal = TipoAnimal.PERRO;
        //pluto_dto.duenio = NULL;
        //pluto_dto.contacto = NULL;

        Mascota pluto = ControllerMascota.persistirDTOMascota(pluto_dto);

        //Se pierde Pluto, rescatista da aviso
        Persona rescatista = new Persona();
        rescatista.setNombre("Maria");
        rescatista.setNombre("Flores");
        rescatista.setCuil("12345");
        rescatista.setNumeroDoc("6");
        rescatista.setTipoDocumento(TipoDocumento.PASAPORTE);

        //Formulario
        MascotaPerdida.MascotaPerdidaDTO pluto_perdido = new MascotaPerdida.MascotaPerdidaDTO();
        pluto_perdido.mascota = pluto;
        pluto_perdido.descripcion = "Es un perro amarillo";
        pluto_perdido.latitud = -38.0;
        pluto_perdido.longitud = -50.0;
        pluto_perdido.rescatista = rescatista;

        ControllerMascotaPerdida.generarMascotaPerdidaDesconocida(pluto_perdido);
        List<MascotaPerdida> perdidas = ControllerMascotaPerdida.obtenerPendientes(0, 5);
        MascotaPerdida pub_pluto_perdido = perdidas.stream().filter(
                p -> p.getMascota().equals(pluto)
        ).findFirst().orElse(null);

        Assertions.assertNotNull(pub_pluto_perdido);

        ControllerMascotaPerdida.aprobarPublicacion(pub_pluto_perdido);

        Assertions.assertEquals(1, perdidas.size());
    }
}