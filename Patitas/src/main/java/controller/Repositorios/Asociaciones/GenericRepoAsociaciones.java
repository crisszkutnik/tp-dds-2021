package controller.Repositorios.Asociaciones;

import domain.Asociacion.Asociacion;

interface GenericRepoAsociaciones {
    Asociacion getAsociacionByName(String name);
}
