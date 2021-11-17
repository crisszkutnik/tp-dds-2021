package controller.Repositorios.Caracteristicas;

import java.util.ArrayList;
import java.util.List;

public class RepositorioCaracteristicas {
    private static List<String> posibles  = new ArrayList<>();

    public static boolean existePosibilidad(String nombre) {
        return posibles.stream().anyMatch(nombre::equals);
    }

    public static void agregarPosibilidad(String nombre) {
        if(!existePosibilidad(nombre))
            posibles.add(nombre);
    }

    public static void removerPosibilidad(String nombre) {
        posibles.removeIf(nombre::equals);
    }
}
