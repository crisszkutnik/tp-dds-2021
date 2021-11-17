package controller.Repositorios;

import java.util.ArrayList;
import java.util.List;

public abstract class ListRepo<T> implements GenericRepo<T> {
    protected final List<T> repositorio;

    public ListRepo() {
        repositorio = new ArrayList<>();
    }

    public void persist(T data) {
        // Si ya esta, no lo agregamos
        if(repositorio.contains(data))
            return;

        repositorio.add(data);
    }
}
