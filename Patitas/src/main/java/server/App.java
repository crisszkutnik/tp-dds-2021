package server;
import scheduler.TareasQuartz;
import utils.GlobalConfig;
import utils.HandlebarsUtils.HandlebarsHelper;
import utils.ViewHelper;
import views.*;
import views.EncontreMascota.ConChapita;
import views.EncontreMascota.AprobarPublicaciones;
import views.EncontreMascota.EncontreMascota;
import views.MisMascotas.Actualizacion.ActualizarMascota;
import views.MisMascotas.Actualizacion.ActualizarMascotaPerdida;
import views.MisMascotas.InteresadosAdopcion;
import views.MisMascotas.SeleccionarHogarTransito;
import views.EncontreMascota.SinChapita;
import views.MisMascotas.DarEnAdopcion;
import views.MisMascotas.MisMascotas;
import views.OfertaAdopcion;

import static spark.Spark.*;

public class App {
    static int getHerokuPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if(processBuilder.environment().get("PORT") != null)
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        return 4567;
    }


    public static void main(String[] args) {
        //////////// INIT ////////////
        port(getHerokuPort());
        TareasQuartz.scheduleAll();

        HandlebarsHelper.init();

        GlobalConfig.loadConfig();
        GlobalConfig.generateDirectories();

        if (GlobalConfig.configObj.getString("ENV").equals("DEVELOPMENT")) {
            System.out.println("STATIC FILES: DEVELOPMENT MODE");
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            System.out.println("STATIC FILES: PRODUCTION MODE");
            staticFiles.location("/public");
        }

        //////////// RUTAS ////////////

        // Home
        before("/", ViewHelper::addRedirectAttribute);
        get("/", Home::processRequest);

        // Quiero adoptar
        before("/quieroAdoptar", ViewHelper::haltIfNotLogged);
        get("/quieroAdoptar", QuieroAdoptar::processRequest);
        post("/quieroAdoptar/registrarSolicitud", QuieroAdoptar::registrarSolicitud);

        // Mascotas perdidas

        path("/mascotasPerdidas", () -> {
            before("/*", ViewHelper::addRedirectAttribute);
            get("", MascotasPerdidas::processRequest);
            get("/encontreMiMascota/:idPublicacion", EncontreMiMascota::processRequest);
            post("/encontreMiMascota/register", EncontreMiMascota::register);
        });

        // Ofertas adopcion
        path("/ofertasAdopcion", () -> {
            before("/*", ViewHelper::haltIfNotLogged);
            before("/*", ViewHelper::addRedirectAttribute);
            get("/:idOferta", OfertaAdopcion::processRequest);
            post("/:idOferta/adoptarPublicacion", OfertaAdopcion::crearAdopcionOfertaAdopcion);
        });

        // Registrar mascota
        path("/registrarMascota", () -> {
            before("/*", ViewHelper::addRedirectAttribute);
            get("/", RegistrarMascota::processRequest);
            post("/addMascota", RegistrarMascota::addMascota);
        });

        // Mis mascotas
        path("/misMascotas", () -> {
            before("/*", ViewHelper::haltIfNotLogged);
            before("/*", ViewHelper::addRedirectAttribute);

            get("/", MisMascotas::processRequest);
            get("/seleccionarHogarTransito", SeleccionarHogarTransito::processRequest);
            get("/verQR/:id", MisMascotas::verQR);
            post("/removerAdopcion", MisMascotas::removeAdopcion);
            post("/removerMascotaPerdida", MisMascotas::removerMascotaPerdida);
            post("/setHogarTransito", SeleccionarHogarTransito::setHogarTransito);

            path("/darEnAdopcion", () -> {
                get("", DarEnAdopcion::processRequest);
                post("/add", DarEnAdopcion::addAdopcion);
            });

            path("/actualizarDatos", () -> {
                get("/actualizarMascota/:idMascota", ActualizarMascota::processRequest);
                post("/actualizarMascota/:idMascota/actualizar", ActualizarMascota::actualizarMascota);
                get("/actualizarMascotaPerdida/:idPublicacion", ActualizarMascotaPerdida::processRequest);
                post("/actualizarMascotaPerdida/:idPublicacion/actualizar", ActualizarMascotaPerdida::actualizarMascotaPerdida);
            });

            post("/cancelarOfertaAdopcion", QuieroAdoptar::darDeBajaSolicitud);
            get("/interesadosAdopcion/:idMascota", InteresadosAdopcion::processRequest);
            post("/interesadosAdopcion/:idPublicacion/transferirMascota/:idPublicacionInteresado", InteresadosAdopcion::transferirMascota);
        });

        // Encontre mascota
        path("/encontreMascota", () -> {
            before("/*", ViewHelper::addRedirectAttribute);

            get("/", EncontreMascota::renderLandingPage);

            path("/conChapita", () -> {
                get("/:idMascota", ConChapita::processRequest);
                post("/register", ConChapita::registerMascotaPerdida);
            });

            path("/sinChapita", () -> {
                get("/", SinChapita::processRequest);
                post("/register", SinChapita::registerMascotaPerdida);
            });

            path("/aprobarPublicaciones", () -> {
               before("/*", ViewHelper::haltIfNotLogged);
               before("/*", ViewHelper::haltIfNotVoluntario);
               // Si es admin no lo frena, pero igual no deberian poder aprobar publicaciones?
               get("/", AprobarPublicaciones::processRequest);
               post("/:idPublicacion", AprobarPublicaciones::aceptarPublicacion);
            });
        });

        // Panel admin / voluntario
        path("/actionPanel", () -> {
            before("/*", ViewHelper::haltIfNotLogged);
            before("/*", ViewHelper::addRedirectAttribute);
            get("/", ActionPanel::processRequest);
            path("/admin", () -> {
                before("/", ViewHelper::haltIfNotAdmin);
                post("/addAdmin", ActionPanel::addAdmin);
                post("/removeAdmin", ActionPanel::removeAdmin);
                post("/changeImgParams", ActionPanel::changeImgParams);
                post("/addAsociacion", ActionPanel::addAsociacion);
                get("/preguntaGeneralPanel", ActionPanel::modificarPreguntaGeneralPanel);
                post("/preguntaGeneralPanel/addPreguntaGeneral", ActionPanel::addPreguntaGeneral);
                post("/preguntaGeneralPanel/removerPreguntaGeneral", ActionPanel::removerPreguntaGeneral);
            });

            path("/voluntario", () -> {
               before("/", ViewHelper::haltIfNotVoluntario);
               post("/addVoluntario", ActionPanel::addVoluntario);
               post("/preguntaAsociacionPanel/removeVoluntario", ActionPanel::removeVoluntario);
               post("/preguntaAsociacionPanel/addPreguntaAsociacion", ActionPanel::addPreguntaAsociacion);
               post("/preguntaAsociacionPanel/removerPreguntaAsociacion", ActionPanel::removerPreguntaAsociacion);
               get("/preguntaAsociacionPanel", ActionPanel::modificarPreguntaAsociacionPanel);
            });
        });

        // Auth
        path("/authapi", () -> {
            path("/notlogged", () -> {
                before("/*", ViewHelper::haltIfLogged);
                get("/auth", Auth::processRequest);
                post("/register", Auth::registerRequest);
                post("/login", Auth::loginRequest);
            });

            path("/logged", () -> {
                before("/*", ViewHelper::haltIfNotLogged);
                get("/logout", Auth::logout);
            });
        });

        notFound(NotFound::processRequest);
    }
}

// tao
