import controller.ControllerAdmin;
import controller.ControllerAsociacion;
import controller.ControllerPersona;
import controller.ControllerVoluntarios;
import controller.ControllerUsuario;
import domain.Asociacion.Asociacion;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Persona;
import domain.Usuario.TipoDocumento;
import domain.Usuario.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VoluntariosTest {
    @Test
    public void testCrearVoluntario() {
        Persona.PersonaDTO dto = new Persona.PersonaDTO();
        dto.apellido = "Sucutric";
        dto.nombre = "Pepe";
        dto.cuil = "123";
        dto.numeroDoc = "456";
        dto.tipoDocumento = TipoDocumento.DNI;
        Persona p = ControllerPersona.crearPersona(dto);
        Asociacion a = ControllerAsociacion.agregarAsociacion("Asociacion Copada");
        Usuario u = ControllerUsuario.crearUsuario("pepeuser", "abc", p);
        ControllerVoluntarios.setUsuarioVoluntario(u.getUsername(), a);
        Assertions.assertSame(u.getAutorizacion(), NivelAutorizacion.VOLUNTARIO);
    }

    @Test
    public void testCrearAsociacionSuelto() {
        String name = "Copa2";
        ControllerAsociacion.agregarAsociacion(name);
        Assertions.assertNotNull(ControllerAsociacion.getAsociacionByName(name));
    }

    @Test
    public void testCrearAsociacionAdmin() {
        Persona.PersonaDTO dto = new Persona.PersonaDTO();
        dto.apellido = "Sucutric";
        dto.nombre = "Pepe";
        dto.cuil = "9876123";
        dto.numeroDoc = "456";
        dto.tipoDocumento = TipoDocumento.DNI;
        Persona p = ControllerPersona.crearPersona(dto);
        Usuario u = ControllerUsuario.crearUsuario("userpepe", "abc", p);

        ControllerAdmin.crearAsociacion("Aso", "userpepe");

        Assertions.assertEquals(u.getAsociacion().getNombre(), "Aso");
        Assertions.assertTrue(ControllerAsociacion.existeAsociacion("Aso"));
    }

    @Test
    public void testVoluntarioCreaVoluntarios() {
        // Creamos el usuario Guido
        Persona.PersonaDTO dto1 = new Persona.PersonaDTO();
        dto1.apellido = "Dipietro";
        dto1.nombre = "Guido";
        dto1.cuil = "12332131";
        dto1.numeroDoc = "456";
        dto1.tipoDocumento = TipoDocumento.DNI;
        Persona p1 = ControllerPersona.crearPersona(dto1);
        Usuario u1 = ControllerUsuario.crearUsuario("gedos", "abc", p1);

        // Hacemos que Guido sea voluntario
        ControllerAdmin.crearAsociacion("Copa2", "gedos");

        // Creamos el usuario Cristobal
        Persona.PersonaDTO dto2 = new Persona.PersonaDTO();
        dto2.apellido = "Szkutnik";
        dto2.nombre = "Cristobal";
        dto2.cuil = "876432423";
        dto2.numeroDoc = "456";
        dto2.tipoDocumento = TipoDocumento.DNI;
        Persona p2 = ControllerPersona.crearPersona(dto2);
        Usuario u2 = ControllerUsuario.crearUsuario("cristobalszk2", "abc", p2);

        u1.crearVoluntarioEnAsociacionPropia(u2);

        Assertions.assertEquals(u1.getAutorizacion(), NivelAutorizacion.VOLUNTARIO);
        Assertions.assertEquals(u2.getAsociacion(), u1.getAsociacion());
    }
}
