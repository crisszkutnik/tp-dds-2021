package scheduler.jobs;

import controller.ControllerOfertaAdopcion;
import domain.MascotaPerdida.MascotaPerdida;
import domain.PublicacionDeseoAdoptar.Preferencias.Preferencias;
import domain.PublicacionDeseoAdoptar.PublicacionDeseoAdoptar;
import domain.PublicacionOfertaAdopcion.PublicacionOfertaAdopcion;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

public class GenerarRecomendacionesJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<PublicacionDeseoAdoptar> adopcion = ControllerOfertaAdopcion.getAllDeseoAdoptar();

        for(PublicacionDeseoAdoptar p : adopcion) {
            List<PublicacionOfertaAdopcion> posibles = ControllerOfertaAdopcion.getOfertasAdopcionByParams(p.getPref());
            System.out.println("Posibles para la PublicacionDeseoAdoptar " + p.getId());
            posibles.forEach(t -> System.out.println(t.getMascota().getNombre() + " " + t.getMascota().getId()));
            if(posibles.size() > 0)
                p.getContactCard().forEach(c -> c.notificarTodosRecomendacionesAdopcion(posibles));
        }
    }
}
