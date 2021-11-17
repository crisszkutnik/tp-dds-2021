package domain.Excepciones;

public class CrearAsociacionException extends Exception {
    public CrearAsociacionException(String asociacion, String causa) {
        super("Ocurrió un error creando la Asociacion <<" + asociacion + ">> (" + causa + ")");
    }
}
