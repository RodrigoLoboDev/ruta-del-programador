package ar.codersacademy.ruta.modelo;

import java.util.List;

/** Misión de un Mundo, dirigida a una franja etaria (CU02). Su contenido se carga aparte (CU03 a CU05). */
public class Mision {
    public static final int PREGUNTAS_POR_CUESTIONARIO = 3;
    public static final int MIN_ACTIVIDADES = 3;
    public static final int MAX_ACTIVIDADES = 5;

    private final int id;
    private final String mundo;
    private final int ordenMundo;
    private final String titulo;
    private final int orden;
    private String leccion;
    private List<Pregunta> preguntas = List.of();
    private List<ActividadInteractiva> actividades = List.of();

    public Mision(int id, String mundo, int ordenMundo, String titulo, int orden) {
        this.id = id;
        this.mundo = mundo;
        this.ordenMundo = ordenMundo;
        this.titulo = titulo;
        this.orden = orden;
    }

    /** Asigna lección, cuestionario y actividades; rechaza una Misión que no cumpla las reglas del diseño. */
    public void setContenido(String leccion, List<Pregunta> preguntas, List<ActividadInteractiva> actividades)
            throws ContenidoInvalidoException {
        if (leccion == null || leccion.isBlank()) {
            throw new ContenidoInvalidoException("La Misión \"" + titulo + "\" no tiene lección");
        }
        if (preguntas == null || preguntas.size() != PREGUNTAS_POR_CUESTIONARIO) {
            throw new ContenidoInvalidoException("La Misión \"" + titulo + "\" necesita exactamente "
                    + PREGUNTAS_POR_CUESTIONARIO + " preguntas");
        }
        if (actividades == null || actividades.size() < MIN_ACTIVIDADES || actividades.size() > MAX_ACTIVIDADES) {
            throw new ContenidoInvalidoException("La Misión \"" + titulo + "\" necesita entre "
                    + MIN_ACTIVIDADES + " y " + MAX_ACTIVIDADES + " actividades");
        }
        this.leccion = leccion;
        this.preguntas = List.copyOf(preguntas);
        this.actividades = List.copyOf(actividades);
    }

    public int getId() { return id; }
    public String getMundo() { return mundo; }
    public int getOrdenMundo() { return ordenMundo; }
    public String getTitulo() { return titulo; }
    public int getOrden() { return orden; }
    public String getLeccion() { return leccion; }
    public List<Pregunta> getPreguntas() { return preguntas; }
    public List<ActividadInteractiva> getActividades() { return actividades; }

    @Override
    public String toString() {
        return "Mundo " + ordenMundo + " " + mundo + " - Misión " + orden + ": " + titulo;
    }
}
