package domain.Excepciones;

public class YaExisteVoluntarioException extends Exception {
    public YaExisteVoluntarioException(String username) {
        super("Ya existe el Voluntario <<" + username + ">>");
    }
}
