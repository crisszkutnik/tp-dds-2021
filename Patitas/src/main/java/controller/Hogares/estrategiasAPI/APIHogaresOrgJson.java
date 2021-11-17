package controller.Hogares.estrategiasAPI;

import controller.Hogares.InterfazAPIHogares;
import domain.Hogares.Hogar;
import domain.Mascota.TipoAnimal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class APIHogaresOrgJson implements InterfazAPIHogares {

    // offset = nro. de pagina, minimo = 1
    // no la hago private porque no se si usaremos esto o la getAPIHogaresAll()
    // retorna JSONObject para que getAPIHogaresAll acceda al campo 'total'
    @Override
    public List<Hogar> getAPIHogaresOffset(Integer offset) throws IOException {
        // endpoint
        URL url = new URL("https://api.refugiosdds.com.ar/api/hogares?offset="+offset);

        // headers
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        con.setRequestProperty("Authorization","Bearer yv7FiiDALkWwgHSiqqu4rHMCwYiwNrkB5dzzxrlKH6QQJBlaHue8Fw2cTHiJ");

        // hace el request
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
            // desde aca
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // hasta aca, solamente para leer la rta. en un string

//            System.out.println(response.toString()); // resultado raw

            // Parsear el JSON, a lo Java
            String res = response.toString();
            JSONObject resJSON = new JSONObject(res);
            JSONArray hogaresJSON = resJSON.getJSONArray("hogares");

            return JSONArrayToHogarList(hogaresJSON);
        }
        else {
            System.out.println(
                    "Fallo en el GET a la API en ControllerHogares.getAPIHogaresSinglePage(offset: "+offset+")"
            );
            throw new IOException();
        }
    }

    // "hogares": [...] a List<>
    // public por lo de getAPIHogaresSinglePage
    public static List<Hogar> JSONArrayToHogarList(JSONArray obj) {
        List<Hogar> listaHogares = new ArrayList<>();
        for (int i=0; i<obj.length(); i++) {
            listaHogares.add(JSONToHogar(obj.getJSONObject(i)));
        }
        return listaHogares;
    }

    // {"id":..., "nombre":..., ...} a Hogar
    private static Hogar JSONToHogar(JSONObject obj) {
        // no apto para pythonistas
        Hogar hogar = new Hogar();
        hogar.setId(obj.getString("id"));
        hogar.setNombre(obj.getString("nombre"));

        JSONObject ubicacion = obj.getJSONObject("ubicacion");
        hogar.setDireccion(ubicacion.getString("direccion"));
        hogar.setLatitud(ubicacion.getDouble("lat"));
        hogar.setLongitud(ubicacion.getDouble("long"));
        hogar.setTelefono(obj.getString("telefono"));
        hogar.setCapacidad(obj.getInt("capacidad"));
        hogar.setLugaresDisponibles(obj.getInt("lugares_disponibles"));
        hogar.setPatio(obj.getBoolean("patio"));

        // java moment, para leer List<String> desde JSON
        List<String> caracteristicas = new ArrayList<>();
        JSONArray JSONArrayCaracteristicas = obj.getJSONArray("caracteristicas");
        for (int i=0; i<JSONArrayCaracteristicas.length(); i++) {
            caracteristicas.add(JSONArrayCaracteristicas.getString(i));
        }
        hogar.setCaracteristicas(caracteristicas);

        // java moment x 2, para leer Map desde JSON
        Map<TipoAnimal, Boolean> admisiones = new HashMap<>();
        JSONObject JSONAdmisiones = obj.getJSONObject("admisiones");
        Iterator<String> admisionesKeys = JSONAdmisiones.keys();
        while (admisionesKeys.hasNext()) {
            String key = admisionesKeys.next();
            admisiones.put(
                    TipoAnimal.valueOf(key.substring(0,key.length()-1).toUpperCase(Locale.ROOT)),
                    JSONAdmisiones.getBoolean(key)
            );
        }
        hogar.setAdmisiones(admisiones);

        return hogar;
    }
}
