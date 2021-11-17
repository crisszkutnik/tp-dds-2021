package domain.PublicacionDeseoAdoptar.Preferencias.Condiciones;

import domain.Mascota.Mascota;
import domain.Mascota.TipoAnimal;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;

public class PreferenciaTipoAnimal implements GenericCondicion {
    public boolean puedeAdoptar(Preferencias p, Mascota mascota) {
        TipoAnimal tipoAnimal = p.getTipoAnimal();
        if (tipoAnimal == null) // No hay preferencia
            return true;
        else
            return tipoAnimal == mascota.getTipoAnimal();
    }
}
