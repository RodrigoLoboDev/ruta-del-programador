package ar.codersacademy.ruta.modelo;

import java.util.List;

/** Pregunta de opción múltiple del cuestionario de una Misión (CU04). */
public class Pregunta {
    private final int id;
    private final String enunciado;
    private final List<String> opciones;
    private final int indiceCorrecta;

    /** @param indiceCorrecta posición (desde 0) de la opción correcta dentro de {@code opciones} */
    public Pregunta(int id, String enunciado, List<String> opciones, int indiceCorrecta) {
        if (enunciado == null || enunciado.isBlank()) {
            throw new IllegalArgumentException("La pregunta necesita un enunciado");
        }
        if (opciones == null || opciones.size() < 2) {
            throw new IllegalArgumentException("La pregunta necesita al menos dos opciones");
        }
        if (indiceCorrecta < 0 || indiceCorrecta >= opciones.size()) {
            throw new IllegalArgumentException("Índice de opción correcta fuera de rango: " + indiceCorrecta);
        }
        this.id = id;
        this.enunciado = enunciado;
        this.opciones = List.copyOf(opciones);
        this.indiceCorrecta = indiceCorrecta;
    }

    /** @param indice posición (desde 0) de la opción elegida */
    public boolean esCorrecta(int indice) {
        return indice == indiceCorrecta;
    }

    public int getId() { return id; }
    public String getEnunciado() { return enunciado; }
    public List<String> getOpciones() { return opciones; }

    @Override
    public String toString() {
        return enunciado;
    }
}
