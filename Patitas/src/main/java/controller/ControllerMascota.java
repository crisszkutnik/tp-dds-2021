package controller;

import controller.Mascota.QRGenerator;
import controller.Mascota.ZXingQRGenerator;
import controller.Repositorios.Mascota.SQLRepoMascota;
import domain.ContactCard.ContactPerClass.ContactCardMascota;
import domain.Imagen.Imagen;
import domain.Mascota.Mascota;
import domain.Usuario.Persona;
import jdk.nashorn.internal.objects.Global;
import utils.GlobalConfig;

import java.util.List;

public class ControllerMascota {
    private static final SQLRepoMascota repositorio = new SQLRepoMascota();
    private static final QRGenerator qrGenerator = new ZXingQRGenerator();

    public static Mascota crearMascota(Mascota.MascotaDTO dto) {
        Mascota nueva = new Mascota();
        nueva.setNombre(dto.nombre);
        nueva.setAnioNacimiento(dto.anioNacimiento);
        nueva.setApodo(dto.apodo);
        nueva.setDuenio(dto.duenio);
        nueva.setSexo(dto.sexo);
        nueva.setTamanio(dto.tamanio);
        nueva.setTipoAnimal(dto.tipoAnimal);
        nueva.setDescripcion(dto.descripcion);
        nueva.setEnAdopcion(dto.enAdopcion);

        for(ContactCardMascota card : dto.contacto)
            nueva.agregarContacto(card);

        for(Imagen img : dto.imagenes)
            nueva.agregarImagen(img);

        for(String car : dto.caracteristicas)
            nueva.agregarCaracteristica(car);

        return nueva;
    }

    public static void generarQR(Mascota mascota) {
        try {
            String id = Integer.toString(mascota.getId());
            String url;
            String route = "encontreMascota/conChapita/";
            if(GlobalConfig.productionEnv())
                url = "https://tp-dds-2021.herokuapp.com/" + route + id;
            else
                url = "http://localhost:4567/" + route + id;
            String path = GlobalConfig.getQRPath() + "/" + id + ".PNG";
            qrGenerator.generarQR(url, path);
        } catch(Exception e) {
            System.out.println("ERR: Creando QR");
            e.printStackTrace();
        }
    }

    public static void persistirObjMascota(Mascota mas) {
        repositorio.persist(mas);
    }

    public static Mascota persistirDTOMascota(Mascota.MascotaDTO dto) {
        Mascota nueva = crearMascota(dto);

        repositorio.persist(nueva);
        return nueva;
    }

    public static void updateMascota(Mascota mas) {
        repositorio.update(mas);
    }

    public static Mascota getMascotaByID(int id) {
        return repositorio.getMascotaByID(id);
    }

    public static List<Mascota> getByDuenio(Persona duenio) {
        return repositorio.getMascotasByDuenio(duenio);
    }
}
