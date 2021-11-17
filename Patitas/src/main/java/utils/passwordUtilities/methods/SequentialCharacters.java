package utils.passwordUtilities.methods;

import java.util.regex.Pattern;

public class SequentialCharacters implements VerifyMethod {
    @Override
    public boolean verify(String password) {
        // Regex para encontrar patrones de caracteres que se repiten 4 o mas veces
        Pattern pattern = Pattern.compile("([a-zA-Z0-9])\\1\\1+");

        // Si se encuentra algun patron
        if(pattern.matcher(password).find())
            return false;

        // Chequeo de caracteres secuenciales
        int cnt = 0;

        for(int i = 0; i < password.length() - 1; i++) {
            char actual = password.charAt(i);
            char next = password.charAt(i + 1);

            if(Math.abs(actual - next) == 1) {
                cnt++;

                // Si hay 4 caracteres secuenciales, retornamos false
                if(cnt == 4)
                    return false;
            } else
                cnt = 0;
        }

        // Si llegamos aca es porque esta todo ok
        return true;
    }
}
