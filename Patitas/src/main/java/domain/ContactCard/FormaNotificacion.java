package domain.ContactCard;

import domain.Mascota.Mascota;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;

import java.io.IOException;
import java.util.List;

public interface FormaNotificacion<T> {
    void contactarGenerico(ContactCard.ContactCardDTO datosUsuario, T metadata) throws IOException;
    void contactarMascotaPerdida(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException;
    void contactarAdopcion(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException;
    void contactarRegistro(ContactCard.ContactCardDTO datosUsuario, Mascota.MascotaDTO mascota) throws IOException;
    void contactarRecomendacionesSemanales(ContactCard.ContactCardDTO datosUsuario, List<PublicacionOfertaAdopcion> ofertas) throws IOException;
}
