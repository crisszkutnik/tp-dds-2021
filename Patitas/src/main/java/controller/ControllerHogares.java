package controller;

import controller.Hogares.InterfazAPIHogares;
import controller.Hogares.SQLRepoHogares;
import controller.Hogares.estrategiasAPI.APIHogaresOrgJson;
import domain.Hogares.Hogar;
import domain.Mascota.Mascota;
import domain.MascotaPerdida.MascotaPerdida;

import java.util.*;
import java.util.stream.Collectors;

public class ControllerHogares {
    protected static InterfazAPIHogares estrategiaAPI = new APIHogaresOrgJson();
    private static final SQLRepoHogares repositorio = new SQLRepoHogares();

    private static List<Hogar> getHogaresByOffset(int offset) {
        try {
            return estrategiaAPI.getAPIHogaresOffset(offset);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        actualizarHogares();
    }

    public static void actualizarHogares() {
        List<Hogar> hogares = getHogaresByOffset(1);
        int i = 2;

        while(hogares != null) {
            for(Hogar h : hogares)
                repositorio.persist(h);
            hogares = getHogaresByOffset(i);
            i++;
        }
    }

    public static List<Hogar> getAllHogares() {
        return repositorio.findAll();
    }

    public static Hogar getById(String id) {
        return repositorio.getHogarById(id);
    }

    // todos los hogares que aceptan a la mascota dada
    public static List<Hogar> hogaresAdecuados(Mascota mascota) {
        return getAllHogares().stream().filter(h -> h.hogarApto(mascota)).collect(Collectors.toList());
    }

    public static List<Hogar> hogaresAdecuados(MascotaPerdida mp) {
        return getAllHogares().stream().filter(h -> h.hogarApto(mp)).collect(Collectors.toList());
    }
}
