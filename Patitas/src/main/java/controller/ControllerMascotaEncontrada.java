package controller;

import controller.Repositorios.MascotaEncontrada.SQLRepoMascotaEncontrada;
import domain.ContactCard.ContactCard;
import domain.ContactCard.ContactPerClass.ContactCardMascotaEncontrada;
import domain.Mascota.Mascota;
import domain.MascotaEncontrada.MascotaEncontrada;
import domain.MascotaPerdida.MascotaPerdida;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerMascotaEncontrada {
    private final static SQLRepoMascotaEncontrada repositorio = new SQLRepoMascotaEncontrada();

    public static MascotaEncontrada crearMascotaEncontrada(MascotaEncontrada.MascotaEncontradaDTO dto) {
        MascotaEncontrada nueva = new MascotaEncontrada();
        nueva.setDuenio(dto.duenio);
        nueva.setPublicacion(dto.publicacion);
        nueva.addContact(dto.contacto);
        return nueva;
    }

    public static void persistirMascotaEncontrada(MascotaEncontrada p) {
        repositorio.persist(p);
    }

    public static boolean existeEncontradaParaPublicacion(MascotaPerdida p ) {
        return repositorio.existeMascotaEncontrada(p);
    }

    public static List<MascotaEncontrada> getMascotaEncontradaByMascotaPerdida(MascotaPerdida mp) {
        return repositorio.executeQueryListResult("FROM MascotaEncontrada WHERE id_mascota_perdida=" + mp.getId(), MascotaEncontrada.class);
    }

    public static class Reclamos {
        public List<MascotaEncontrada> listaReclamos;

        public List<MascotaEncontrada> getListaReclamos() {
            return this.listaReclamos;
        }
    }
    public static Reclamos reclamosParaPerdidaOrNull(MascotaPerdida p) {
        List<MascotaEncontrada> reclamos = repositorio.executeQueryListResult(
            "FROM MascotaEncontrada WHERE id_mascota_perdida='" + p.getId() + "'",
            MascotaEncontrada.class
        );

        Reclamos claims = new Reclamos();
        claims.listaReclamos = reclamos;
        return claims;
    }

    public static List<Reclamos> reclamosParaPerdidasOrNull(List<MascotaPerdida> ps) {
        return ps.stream().map(ControllerMascotaEncontrada::reclamosParaPerdidaOrNull).collect(Collectors.toList());
    }
}