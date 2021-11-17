package controller.Repositorios.Asociaciones;

import controller.Repositorios.ListRepo;
import domain.Asociacion.Asociacion;

public class ListRepoAsociaciones extends ListRepo<Asociacion> implements GenericRepoAsociaciones {
    public Asociacion getAsociacionByName(String name) {
        return repositorio.stream().filter(n -> name.equals(n.getNombre())).findFirst().orElse(null);
    }
}
