package utils.passwordUtilities;

import utils.passwordUtilities.methods.*;

import java.util.Arrays;
import java.util.List;

public class PasswordVerifier {
    // Se fija si la password esta en la lista de las 10000 mas usadas
    private static final List<VerifyMethod> methods = Arrays.asList(
            new PasswordLength(),
            new SequentialCharacters(),
            new MostUsed()
    );

    public static boolean isSecurePassword(String password) {
        for(VerifyMethod method : methods) {
            if(!method.verify(password))
                return false;
        }
        return true;
    }
}