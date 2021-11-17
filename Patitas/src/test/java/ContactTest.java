//import domain.ContactCard.ListFormaNotificacionConverter;


public class ContactTest {
    /*@Test
    public void testEnvioWhatsApp() {
        final String telefono = "";
        if (telefono.length() == 0) return;

        FormaNotificacion<?> contactoWpp = new TwilioWhatsApp();
        ContactCard usuario = new ContactCard();
        usuario.setTelefono(telefono);

        Mascota perrito = new Mascota();
        perrito.setNombre("Rufus-lander");

        try {
            contactoWpp.contactarMascotaPerdida(usuario.toDTO(), perrito.toDTO());
        }
        catch (IOException e) {
            System.out.println("Error enviando warap");
            assert 1==0;
        }
    }

    @Test
    public void testContactoEmail() {
        FormaNotificacion<?> email = new SendgridEmail();
        ContactCard contacto = new ContactCard();
        contacto.setEmail("lacoleraesmuymala@gmail.com");

        Mascota gato = new Mascota();
        gato.setNombre("Pelos pelados");

        try {
            email.contactarMascotaPerdida(contacto.toDTO(), gato.toDTO());
        }
        catch (IOException e) {
            fail("Error enviando e-mail");
        }
    }

    @Test
    public void testEnvioSMS() {
        final String telefono = "";
        if (telefono.length() == 0) return;

        FormaNotificacion<?> contactoSMS = new TwilioSMS();
        ContactCard usuario = new ContactCard();
        usuario.setTelefono(telefono);

        Mascota perrito = new Mascota();
        perrito.setNombre("bueno parece que anda esto");

        try {
            contactoSMS.contactarMascotaPerdida(usuario.toDTO(), perrito.toDTO());
        }
        catch (IOException e) {
            System.out.println("Error enviando warap");
            assert 1==0;
        }
    }*/

    /*@Test
    public void testFormaNotificacionConverterToString() {
        List<FormaNotificacion<?>> lista = Arrays.asList(
                new SendgridEmail(),
                new TwilioSMS(),
                new TwilioWhatsApp()
        );

        ListFormaNotificacionConverter converter = new ListFormaNotificacionConverter();
        String str_lista = converter.convertToDatabaseColumn(lista);

        assert str_lista.equals(
                "domain.ContactCard.contactMethods.Email.Methods.SendgridEmail,"+
                "domain.ContactCard.contactMethods.SMS.Methods.TwilioSMS,"+
                "domain.ContactCard.contactMethods.WhatsApp.Methods.TwilioWhatsApp,"
        );
    }

    @Test
    public void testFormaNotificacionConverterToList() {
        String str_lista =
                "domain.ContactCard.contactMethods.Email.Methods.SendgridEmail," +
                        "domain.ContactCard.contactMethods.SMS.Methods.TwilioSMS," +
                        "domain.ContactCard.contactMethods.WhatsApp.Methods.TwilioWhatsApp,";
        List<String> str_lista_split = Arrays.asList(str_lista.split(","));

        ListFormaNotificacionConverter converter = new ListFormaNotificacionConverter();
        List<FormaNotificacion<?>> lista = converter.convertToEntityAttribute(str_lista);

        for (int i = 0; i < str_lista_split.size(); i++) {
            assert str_lista_split.get(i).equals(
                    lista.get(i).getClass().getName()
            );
        }
    }*/
}
