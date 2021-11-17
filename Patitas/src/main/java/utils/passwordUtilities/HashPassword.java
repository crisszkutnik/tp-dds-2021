package utils.passwordUtilities;

//import org.mindrot.BCrypt;
import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean correctPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
}
