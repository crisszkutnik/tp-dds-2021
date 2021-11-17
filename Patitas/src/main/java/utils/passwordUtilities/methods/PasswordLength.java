package utils.passwordUtilities.methods;

public class PasswordLength implements VerifyMethod {
    @Override
    public boolean verify(String password) {
        return password.length() >= 8;
    }
}
