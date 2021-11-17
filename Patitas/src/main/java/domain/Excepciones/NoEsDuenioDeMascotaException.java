package domain.Excepciones;

import domain.Mascota.Mascota;
import domain.Usuario.Persona;

public class NoEsDuenioDeMascotaException extends Exception {
    public NoEsDuenioDeMascotaException(Mascota mascota, Persona persona) {
        super("Persona CUIL " + persona.getCuil() + " no es el duenio de Mascota ID " + mascota.getId());
    }
}
