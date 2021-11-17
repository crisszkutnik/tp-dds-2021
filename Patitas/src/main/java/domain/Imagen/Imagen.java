package domain.Imagen;

import domain.Mascota.Mascota;
import domain.MascotaPerdida.MascotaPerdida;
import utils.GlobalConfig;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table
public class Imagen {
    @ManyToOne
    @JoinColumn(name = "id_mascota", referencedColumnName = "id")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_mascota_perdida", referencedColumnName = "id")
    private MascotaPerdida mascotaPerdida;

    @Column
    @Id
    private String nombre;
    private static NormalizadorImagen normalizador = new Graphics2DResizer();

    public void setNombre(String nueva) {
        nombre = nueva;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getPath() {
        return GlobalConfig.getImgPath() + "/" + nombre;
    }

    public void normalizar() throws IOException {
        normalizador.normalizarImagen(this.getPath());
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        if(mascotaPerdida != null)
            return;

        this.mascota = mascota;
    }

    public MascotaPerdida getMascotaPerdida() {
        return mascotaPerdida;
    }

    public void setMascotaPerdida(MascotaPerdida mascotaPerdida) {
        if(mascota != null)
            return;

        this.mascotaPerdida = mascotaPerdida;
    }

    public static void setNormalizador(NormalizadorImagen nuevo) {
        normalizador = nuevo;
    }
}
