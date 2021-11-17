package controller.Mascota;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRGenerator {
    void generarQR(String data, String path) throws WriterException, IOException;
}
