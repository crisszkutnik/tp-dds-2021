package controller.Repositorios.Mascota;

import domain.Mascota.Mascota;
import domain.Usuario.Persona;

import java.util.List;

public interface GenericRepoMascota {
    List<Mascota> getMascotasByDuenio(Persona duenio);
    Mascota getMascotaByID(int id);
}
