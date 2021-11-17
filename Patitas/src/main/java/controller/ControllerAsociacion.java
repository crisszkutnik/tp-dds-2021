package controller;

import controller.Repositorios.Asociaciones.SQLRepoAsociaciones;
import domain.Asociacion.Asociacion;

import java.util.List;

public class ControllerAsociacion {
    //private static final ListRepoAsociaciones repositorio = new ListRepoAsociaciones();
    private static final SQLRepoAsociaciones repositorio = new SQLRepoAsociaciones();

    public static boolean existeAsociacion(String nombre) {
        return repositorio.getAsociacionByName(nombre) != null;
    }

    public static Asociacion getAsociacionByName(String name) {
        return repositorio.getAsociacionByName(name);
    }

    public static Asociacion getById(int id) {
        return repositorio.getById(id);
    }

    public static List<Asociacion> getAll() {
        return repositorio.getAll();
    }

    public static Asociacion agregarAsociacion(String asociacion) {
        if (existeAsociacion(asociacion))
            return null;

        Asociacion nueva = new Asociacion();
        nueva.setNombre(asociacion);
        repositorio.persist(nueva);
        return nueva;
    }
}
