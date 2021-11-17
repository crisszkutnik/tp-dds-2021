package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import scheduler.jobs.GenerarRecomendacionesJob;

public class TareasQuartz {
    private static final SchedulerFactory sf = new StdSchedulerFactory();

    public static void scheduleAll() {
        try {
            Scheduler scheduler = sf.getScheduler();

            scheduleRecomendacionesSemanales();

            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    ///

    private static void scheduleRecomendacionesSemanales() {
        try {
            Scheduler scheduler = sf.getScheduler();
            JobDetail job = JobBuilder.newJob(GenerarRecomendacionesJob.class)
                    .withIdentity("GenerarRecomendacionesJob")
                    .build();

            Trigger viernes_al_mediodia = TriggerBuilder.newTrigger()
                    .withIdentity("Todos los viernes al mediodia")
                    .startNow()
                    .withSchedule(
                            CronScheduleBuilder.weeklyOnDayAndHourAndMinute(
                                    DateBuilder.FRIDAY,12,0
                            )
                    )
                    .build();

            scheduler.scheduleJob(job, viernes_al_mediodia);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
