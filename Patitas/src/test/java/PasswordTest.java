import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.passwordUtilities.HashPassword;
import utils.passwordUtilities.PasswordVerifier;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PasswordTest {
    private final String[] claves = {
            "buenas","chaaaaau","cLaVe!!mUYSeGUra5434","12345","corto",
            "oTrA!!cLaVe%$%$mUy!!SeGuRa!!","password","12345","zzzzz","bueeeeeno",
            "cLaVe","cLaVeLoCa","holabcdefgh","cLaVe!!cAsI!!abcdeMuY!sEgUrA%$"
    };

    // Testea que el hasheo funcione correctamente y lo timea
    @Test
    public void testHashing() {
        long durTotal = 0;
        // Verifica con correctPassword()
        for (String clave : claves) {
            String hash = HashPassword.hashPassword(clave);

            long startTime = System.nanoTime();
            boolean isCorrect = HashPassword.correctPassword(clave,hash);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime)/1000000; //ms
            durTotal += duration;

            // HASH @ XY ms (clave)
            System.out.println(hash + " @ " + duration + " ms (" + clave + ")");

            Assertions.assertTrue(isCorrect); //todas deben pasar esto
        }
        float durProm = durTotal/(float)(claves.length);
        System.out.println(
                "\nDuración promedio chequeo hash: "
                        + durProm
                        + " ms (" + claves.length + " iters)\n\n"
        );
    }

    // Testea la verificación de fortaleza de una clave dada
    @Test
    public void testClaveValida() {
        long durTotal = 0;
        int inseguras = 0;
        // Verifica con isSecurePassword()
        for (String clave : this.claves) {
            long startTime = System.nanoTime();
            boolean isSecure = PasswordVerifier.isSecurePassword(clave);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime); //nanosegundos
            float durationMs = duration/1000000F;
            durTotal += duration;

            // Clave @ XY ns (segura/no segura)
            String formato = "%-"
                    // ????? java pls
                    +Arrays.stream(claves).collect(Collectors.summarizingInt(String::length)).getMax()
                    +"s @ %10d ns = %2.2e ms (%s)\n";
            System.out.format(
                    formato,
                    clave, duration, durationMs, (isSecure?" segura ":"insegura"));
            if (!isSecure) inseguras++;
        }
        float durProm = durTotal / (float)(1000000*claves.length);
        System.out.println(
                "\nDuración promedio verificación de seguridad: "
                        + durProm
                        + " ms (" + claves.length + " iters)\n\n"
        );

        assert inseguras == 10;
    }
}