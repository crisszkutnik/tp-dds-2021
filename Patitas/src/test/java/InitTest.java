import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.GlobalConfig;

public class InitTest {
    @Test
    public void loadConfig() {
        GlobalConfig.loadConfig();
        JSONObject imgProp = GlobalConfig.configObj.getJSONObject("IMAGE_PROPERTIES");
        int w = imgProp.getInt("width");
        int h =  imgProp.getInt("height");
        System.out.println(w + " - " +h);
        GlobalConfig.changeImageValue("width", 750);
        imgProp = GlobalConfig.configObj.getJSONObject("IMAGE_PROPERTIES");
        Assertions.assertEquals(imgProp.getInt("width"), 750);
    }
}
