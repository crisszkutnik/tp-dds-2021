package domain.PublicacionDeseoAdoptar.Preferencias.Condiciones;

import domain.Mascota.Mascota;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;

public interface GenericCondicion {
    boolean puedeAdoptar(Preferencias p, Mascota mascota);
}
