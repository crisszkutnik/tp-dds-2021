package controller.Repositorios.Persona;

import controller.Repositorios.GenericRepo;
import domain.Usuario.Persona;

interface GenericRepoPersona {
    Persona getSingleFromRepo(String identifier);
}
