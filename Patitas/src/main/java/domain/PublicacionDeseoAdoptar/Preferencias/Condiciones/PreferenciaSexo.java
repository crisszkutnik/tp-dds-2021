package domain.PublicacionDeseoAdoptar.Preferencias.Condiciones;

import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;

public class PreferenciaSexo implements GenericCondicion {
    public boolean puedeAdoptar(Preferencias p, Mascota mascota) {
        Sexo sexo = p.getSexo();
        if (sexo == null) // No hay preferencia
            return true;
        else
            return sexo == mascota.getSexo();
    }
}
