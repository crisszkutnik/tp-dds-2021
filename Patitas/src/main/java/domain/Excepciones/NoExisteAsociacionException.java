package domain.Excepciones;

public class NoExisteAsociacionException extends Exception {
    public NoExisteAsociacionException(String asociacion) {
        super("No existe la Asociacion <<" + asociacion + ">>");
    }
}
