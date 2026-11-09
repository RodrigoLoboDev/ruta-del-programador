package ar.codersacademy.ruta.modelo;

/**
 * Se lanza cuando falla el acceso a la base de datos MySQL
 * (conexión, consulta o actualización). Envuelve la SQLException
 * original como causa, para que las capas de arriba no dependan de JDBC.
 */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
