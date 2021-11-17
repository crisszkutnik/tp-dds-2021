package domain.Excepciones;

public class NoExisteVoluntarioException extends Exception {
    public NoExisteVoluntarioException(String username) {
        super("No existe el voluntario <<" + username + ">>");
    }
}
