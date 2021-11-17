package controller;

import controller.Repositorios.MascotaPerdida.SQLRepoMascotaPerdida;
import domain.ContactCard.ContactPerClass.ContactCardMascotaPerdida;
import domain.Hogares.Hogar;
import domain.MascotaPerdida.MascotaPerdida;
import domain.Usuario.Persona;

import java.util.List;

public class ControllerMascotaPerdida {
    private final static SQLRepoMascotaPerdida repositorio = new SQLRepoMascotaPerdida();
    private static void setParams(MascotaPerdida obj, MascotaPerdida.MascotaPerdidaDTO dto) {
        obj.setDescripcion(dto.descripcion);
        obj.setLongitud(dto.longitud);
        obj.setLatitud(dto.latitud);
        obj.setRescatista(dto.rescatista);
        obj.setMascota(dto.mascota);
        obj.agregarFotos(dto.fotos);

        for(ContactCardMascotaPerdida c : dto.contacto) {
            c.setMascotaPerdida(obj);
            obj.agregarContacto(c);
        }
    }

    public static MascotaPerdida generarMascotaPerdidaDesconocida(MascotaPerdida.MascotaPerdidaDTO dto) {
        MascotaPerdida obj = new MascotaPerdida();
        setParams(obj, dto);
        obj.setAprobada(false);
        return obj;
    }

    public static void persistirMascotaPerdida(MascotaPerdida p) {
        repositorio.persist(p);
    }

    public static void generarMascotaPerdidaConocida(MascotaPerdida.MascotaPerdidaDTO dto) {
        MascotaPerdida obj = new MascotaPerdida();
        setParams(obj, dto);
        obj.setMascota(dto.mascota);
        obj.setAprobada(true);
        obj.notificar();
        repositorio.persist(obj);
    }

    public static void setHogarPublicacion(MascotaPerdida m, Hogar hogar) throws Exception {
        if(!hogar.hogarApto(m))
            throw new Exception("Hogar " + hogar.getId() + " no es apto para mascota " + m.getMascota().getId());

        m.setHogar(hogar);
        repositorio.update(m);
    }

    public static List<MascotaPerdida> obtenerPendientes(int offset, int cantidad) {
        return repositorio.getPendientes(offset, cantidad);
    }

    public static List<MascotaPerdida> obtenerAprobadasSinChapita(int offset, int cantidad) {
        return repositorio.getAprobadasSinChapita(offset, cantidad);
    }

    public static MascotaPerdida getById(int id) {
        return repositorio.getById(id);
    }

    public static List<MascotaPerdida> getByPersona(Persona persona) {
        return repositorio.getByPersona(persona);
    }

    public static void aprobarPublicacion(MascotaPerdida publicacion) {
        publicacion.setAprobada(true);
        repositorio.update(publicacion);
    }
}
