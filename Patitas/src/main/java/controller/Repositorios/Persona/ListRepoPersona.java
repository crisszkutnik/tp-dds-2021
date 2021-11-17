package controller.Repositorios.Persona;

import controller.Repositorios.ListRepo;
import domain.Usuario.Persona;

public class ListRepoPersona extends ListRepo<Persona> implements GenericRepoPersona {
    public Persona getSingleFromRepo(String identifier) {
        // identifier es CUIL
        return repositorio.stream().filter(n -> identifier.equals(n.getCuil())).findFirst().orElse(null);
    }
}
