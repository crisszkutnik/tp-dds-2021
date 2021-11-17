package controller.Mascota;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class ZXingQRGenerator implements QRGenerator {

    @Override
    public void generarQR(String data, String path) throws WriterException, IOException {
        int height = 200;
        int width = 200;

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
                BarcodeFormat.QR_CODE, width, height
        );

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                new File(path).toPath(),
                new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE)
        );
    }
}
