package controller;

import domain.Asociacion.Asociacion;
import domain.Usuario.Usuario;
import domain.Usuario.NivelAutorizacion;

public class ControllerVoluntarios {
    public static void setUsuarioVoluntario(String username, Asociacion asociacion) {
        Usuario user = ControllerUsuario.buscarUsuario(username);

        if(user == null)
            return;

        user.setAutorizacion(NivelAutorizacion.VOLUNTARIO);
        user.setAsociacion(asociacion);
        ControllerUsuario.actualizarUsuario(user);
    }

    public static boolean existeVoluntario(String username) {
        Usuario user = ControllerUsuario.buscarUsuario(username);

        if(user == null)
            return false;
        else
            return user.getAutorizacion() == NivelAutorizacion.VOLUNTARIO;
    }

    public static Usuario getVoluntarioByUsername(String username) {
        Usuario user = ControllerUsuario.buscarUsuario(username);
        return user.getAutorizacion() == NivelAutorizacion.VOLUNTARIO ? user : null;
    }
}
