package ar.codersacademy.ruta.modelo;

import java.util.List;

/**
 * Actividad interactiva de una Misión (CU05). Cada tipo decide qué mostrar y cómo interpretar la respuesta;
 * quien la usa (vista, controlador) trabaja siempre contra esta clase, sin conocer las subclases.
 */
public abstract class ActividadInteractiva {
    private static final String PISTA_POR_DEFECTO = "Releé la consigna y probá de nuevo.";

    private final int id;
    private final String consigna;
    private final int orden;
    private final String pista;

    protected ActividadInteractiva(int id, String consigna, int orden, String pista) {
        if (consigna == null || consigna.isBlank()) {
            throw new IllegalArgumentException("La actividad necesita una consigna");
        }
        this.id = id;
        this.consigna = consigna;
        this.orden = orden;
        this.pista = (pista == null || pista.isBlank()) ? PISTA_POR_DEFECTO : pista;
    }

    /** Texto que explica cómo responder en este tipo de actividad. */
    public abstract String getInstrucciones();

    /** Líneas a mostrar al alumno, ya rotuladas (cada tipo arma las suyas). */
    public abstract List<String> getElementos();

    /** Indica si la respuesta tipeada por el alumno es correcta; una respuesta mal formada es incorrecta. */
    public abstract boolean validar(String respuesta);

    public int getId() { return id; }
    public String getConsigna() { return consigna; }
    public int getOrden() { return orden; }

    /** Pista que se muestra tras una respuesta incorrecta (CU07). */
    public String getPista() { return pista; }

    @Override
    public String toString() {
        return consigna;
    }
}
