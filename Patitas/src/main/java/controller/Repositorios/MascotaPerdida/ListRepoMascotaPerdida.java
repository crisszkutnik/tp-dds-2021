package controller.Repositorios.MascotaPerdida;

import controller.Repositorios.ListRepo;
import domain.MascotaPerdida.MascotaPerdida;

import java.util.List;
import java.util.stream.Collectors;

public class ListRepoMascotaPerdida extends ListRepo<MascotaPerdida> implements GenericRepoMascotaPerdida {
    public List<MascotaPerdida> getPendientes(int offset, int cantidad) {
        return repositorio.stream().filter(n -> !n.isAprobada()).skip(offset).limit(cantidad).collect(Collectors.toList());
    }
}
