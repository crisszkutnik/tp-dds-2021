package utils.passwordUtilities.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MostUsed implements VerifyMethod {
    @Override
    public boolean verify(String password) {
        try {
            // Agarramos el primer caracter y buscamos el archivo
            //TODO: implementar interfaz para poder cambiar el modo de lectura?
            int charNum = password.charAt(0);
            File file = new File("src/main/resources/utils.passwordUtilities/" + charNum + ".txt");
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine())
                if(scanner.nextLine().equals(password))
                    return false;

            return true;
        } catch(FileNotFoundException e) {
            // No se encontro el archivo, es decir, la password arranca con otro caracter
            return true;
        }
    }
}
