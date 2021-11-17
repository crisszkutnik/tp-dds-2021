package controller;

import controller.Repositorios.Persona.ListRepoPersona;
import controller.Repositorios.Persona.SQLRepoPersona;
import domain.Usuario.Persona;


public class ControllerPersona {
    //private static final ListRepoPersona repositorio = new ListRepoPersona(); // Por ahora se va a guardar aca
    private static final SQLRepoPersona repositorio = new SQLRepoPersona();

    public static Persona crearPersona(Persona.PersonaDTO dto) {
        if(existePersona(dto.cuil)) {
            System.out.println("Ya existe el usuario para este CUIL");
            return null;
        }

        Persona nueva = new Persona();
        nueva.setTipoDocumento(dto.tipoDocumento);
        nueva.setCuil(dto.cuil);
        nueva.setNumeroDoc(dto.numeroDoc);
        nueva.setNombre(dto.nombre);
        nueva.setApellido(dto.apellido);
        repositorio.persist(nueva);
        return nueva;
    }

    public static Persona getPersonaOrCreate(Persona.PersonaDTO dto) {
        Persona ret = getPersonaByCUIL(dto.cuil);

        if(ret == null)
            ret = crearPersona(dto);

        return ret;
    }

    public static Persona getPersonaByCUIL(String cuil) {
        return repositorio.getSingleFromRepo(cuil);
    }

    public static boolean existePersona(String cuil) {
        return getPersonaByCUIL(cuil) != null;
    }
}