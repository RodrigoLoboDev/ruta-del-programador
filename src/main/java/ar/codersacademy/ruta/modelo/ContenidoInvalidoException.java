package ar.codersacademy.ruta.modelo;

/** Contenido de una Misión incompleto o mal formado (sin preguntas, sin actividades o JSON inválido). */
public class ContenidoInvalidoException extends Exception {
    public ContenidoInvalidoException(String mensaje) {
        super(mensaje);
    }

    public ContenidoInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
