package controller;

import domain.Asociacion.Asociacion;
import controller.Repositorios.Caracteristicas.RepositorioCaracteristicas;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Usuario;

import java.util.List;

public class ControllerAdmin {
    public static Usuario setUsuarioAdmin(String username) {
        Usuario user = ControllerUsuario.buscarUsuario(username);

        if(user == null)
            return null;

        user.setAutorizacion(NivelAutorizacion.ADMIN);
        ControllerUsuario.actualizarUsuario(user);
        return user;
    }

    public static Usuario removeUsuarioAdmin(String username) {
        Usuario user = ControllerUsuario.buscarUsuario(username);

        if(user == null || user.getAutorizacion() != NivelAutorizacion.ADMIN)
            return null;

        // Si no tiene asociacion es porque no es un voluntario
        user.setAutorizacion(user.getAsociacion() == null ? NivelAutorizacion.USUARIO : NivelAutorizacion.VOLUNTARIO);
        ControllerUsuario.actualizarUsuario(user);
        return user;
    }

    public static void crearAsociacion(String asociacion, String username) {
        if (ControllerUsuario.buscarUsuario(username) != null) {
            Asociacion a = ControllerAsociacion.agregarAsociacion(asociacion);
            ControllerVoluntarios.setUsuarioVoluntario(username, a);
        }
    }

    public static void agregarCaracteristicaPosible(String car) {
        RepositorioCaracteristicas.agregarPosibilidad(car);
    }

    public static void agregarPreguntaGeneral(String pregunta, List<String> opciones) {
        ControllerOfertaAdopcion.agregarPreguntaGeneral(pregunta, opciones);
    }
}
