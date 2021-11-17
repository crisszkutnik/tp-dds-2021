package domain.Imagen;

import org.json.JSONObject;
import utils.GlobalConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Graphics2DResizer implements NormalizadorImagen {
    @Override
    public void normalizarImagen(String path) throws IOException {
        JSONObject imgProp = GlobalConfig.configObj.getJSONObject(GlobalConfig.imgPropName);
        int alto = imgProp.getInt("height");
        int ancho = imgProp.getInt("width");

        // Abre archivo
        File inputFile = new File(path);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Crea img de salida
        BufferedImage outputImage = new BufferedImage(
            ancho,
            alto,
            inputImage.getType()
        );

        // Redimensiona la imagen
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, ancho, alto, null);
        g2d.dispose();

        // Path de salida (el mismo)
        String formatName = path.substring(path.lastIndexOf(".") + 1);
        // Escribe archivo
        ImageIO.write(outputImage, formatName, new File(path));
    }
}
