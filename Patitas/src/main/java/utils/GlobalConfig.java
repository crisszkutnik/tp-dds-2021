package utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class GlobalConfig {
    public static JSONObject configObj;
    public static final String imgPropName = "IMAGE_PROPERTIES";
    private static boolean productionEnv;

    static public void loadConfig() {
        try {
            String jsonText = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
            configObj = new JSONObject(jsonText);
            productionEnv = !configObj.getString("ENV").equals("DEVELOPMENT");
        } catch(IOException ignored) {}
    }

    static public boolean productionEnv() {
        return productionEnv;
    }

    static private void writeImageValues(JSONObject img) {
        try {
            FileWriter fw = new FileWriter("config.json");
            configObj.put(imgPropName, img);
            fw.write(configObj.toString());
            fw.flush();
            System.out.println(configObj.toString());
        } catch(IOException ignored) {}
    }

    static public JSONObject getImageValues() {
        return configObj.getJSONObject(imgPropName);
    }

    static public String getImgDir() {
        return configObj.getString("IMG_DIR");
    }

    static public String getImgPath() {
        return configObj.getString("IMG_PATH");
    }

    static public String getQRPath() {
        return configObj.getString("QR_PATH");
    }


    static public void changeImageValue(String key, int newVal) {
        if(!(key.equals("width") || key.equals("height")))
            return;

        JSONObject imgVal = configObj.getJSONObject(imgPropName);
        imgVal.put(key, newVal);
        writeImageValues(imgVal);
    }

    static public void changeAllImageValues(int alto, int ancho) {
        JSONObject imgVal = configObj.getJSONObject(imgPropName);
        imgVal.put("height", alto);
        imgVal.put("width", ancho);
        writeImageValues(imgVal);
    }

    static public void generateDirectories() {
        Arrays.asList(
                getImgPath(),
                getQRPath()
        ).forEach(dir -> {
            new File(dir).mkdir();
        });
    }
}
