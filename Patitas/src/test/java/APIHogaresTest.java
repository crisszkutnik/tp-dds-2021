import controller.ControllerAdmin;
import controller.ControllerHogares;
import domain.Hogares.Hogar;
import domain.Mascota.Mascota;
import domain.Mascota.Sexo;
import domain.Mascota.TamanioMascota;
import domain.Mascota.TipoAnimal;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class APIHogaresTest {
    @Test
    public void testDistanciaHogares() {
        Hogar medrano = new Hogar(); // UTN Medrano
        Hogar campus = new Hogar(); // UTN Campus
        Hogar casaRosada = new Hogar(); // Casa Rosada

        medrano.setLatitud(-34.5984145);
        medrano.setLongitud(-58.4200156);

        campus.setLatitud(-34.6592726);
        campus.setLongitud(-58.4695279);

        casaRosada.setLatitud(-34.6080512);
        casaRosada.setLongitud(-58.3724665);

        System.out.format("Distancia de UTN Medrano a UTN Campus: %f metros\n\n", medrano.metrosAHogar(campus));
        System.out.format("Distancia de UTN Medrano a Casa Rosada: %f metros\n\n", medrano.metrosAHogar(casaRosada));
    }

    @Test
    public void testHogaresAptosParaSara() {
        ControllerAdmin.agregarCaracteristicaPosible("Manso");

        Mascota sara = new Mascota();
        sara.setTamanio(TamanioMascota.CHICA);
        sara.setApodo("Sarita");
        sara.setAnioNacimiento(2013);
        sara.setSexo(Sexo.HEMBRA);
        sara.setTipoAnimal(TipoAnimal.PERRO);
        sara.agregarCaracteristica("Manso");

        List<Hogar> hogaresAptosParaSara = ControllerHogares.hogaresAdecuados(sara);
        assert hogaresAptosParaSara != null;

        for (Hogar h : hogaresAptosParaSara)
            h.print();

        assert hogaresAptosParaSara.stream().allMatch(
                hogar ->
                    !hogar.getPatio()
                    && hogar.getCaracteristicas().stream().anyMatch(c -> c.equals("Manso")) || hogar.getCaracteristicas().size() == 0
                    && hogar.getAdmisiones().get(TipoAnimal.PERRO)
        );
    }
}
