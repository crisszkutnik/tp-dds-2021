package controller.Repositorios.MascotaPerdida;

import domain.MascotaPerdida.MascotaPerdida;

import java.util.List;

public interface GenericRepoMascotaPerdida {
    List<MascotaPerdida> getPendientes(int offset, int cantidad);
}
