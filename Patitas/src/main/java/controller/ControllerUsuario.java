package controller;

import controller.ControllerOfertaAdopcion;
import controller.Repositorios.Usuario.SQLRepoUsuario;
import domain.Asociacion.Asociacion;
import domain.Mascota.Mascota;
import domain.Preguntas.Respuesta;
import domain.Usuario.NivelAutorizacion;
import domain.Usuario.Persona;
import domain.Usuario.Usuario;

import java.util.List;

public class ControllerUsuario {
    private static final SQLRepoUsuario repositorio = new SQLRepoUsuario();

    private static boolean existeUsername(String candidate) {
        return repositorio.fetchByUsername(candidate) != null;
    }

    public static void actualizarUsuario(Usuario usuario) {
        repositorio.update(usuario);
    }

    public static Usuario crearUsuario(String username, String passwordHash, Persona persona) {
        if(existeUsername(username))
            return null;

        Usuario nuevo = new Usuario();
        nuevo.setUsername(username);
        nuevo.setPasswordHash(passwordHash);
        nuevo.setPersona(persona);
        nuevo.setAutorizacion(NivelAutorizacion.USUARIO);
        repositorio.persist(nuevo);
        return nuevo;
    }

    public static Usuario buscarUsuario(String username) {
        return repositorio.fetchByUsername(username);
    }

    public static void hacerVoluntario(Usuario user, Asociacion as) {
        user.setAsociacion(as);
        if(user.getAutorizacion() == NivelAutorizacion.USUARIO)
            user.setAutorizacion(NivelAutorizacion.VOLUNTARIO);
    }

    public static void darMascotaEnAdopcion(Usuario usuario, Mascota mascota, Asociacion asociacion, List<Respuesta> respuestas) {
        // No podes dar en adopcion una mascota que no es tuya!
        // TODO: tirar una excepcion, agregar alguna forma de señalar que esto falló?
        if (mascota.getDuenio() != usuario.getPersona())
            return;

        ControllerOfertaAdopcion.agregarOferta(asociacion, mascota, respuestas);
    }
}
