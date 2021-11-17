package domain.Excepciones;

public class YaExisteAsociacionException extends Exception {
    public YaExisteAsociacionException(String nombre) {
        super("Ya existe la asociacion <<" + nombre + ">>");
    }
}
