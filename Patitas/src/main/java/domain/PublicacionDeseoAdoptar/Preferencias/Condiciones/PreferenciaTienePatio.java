package domain.PublicacionDeseoAdoptar.Preferencias.Condiciones;

import domain.Mascota.Mascota;
import domain.Mascota.TamanioMascota;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;

public class PreferenciaTienePatio implements GenericCondicion {
    public boolean puedeAdoptar(Preferencias p, Mascota mascota) {
        if (mascota.getTamanio() == TamanioMascota.CHICA)
            return true;
        else
            return p.getTienePatio();
    }
}
