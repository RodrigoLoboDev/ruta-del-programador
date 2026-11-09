package ar.codersacademy.ruta.modelo;

import java.time.LocalDateTime;

/**
 * Avance de un alumno en una Misión que ya empezó (fila de la exportación del progreso).
 * La nota y la fecha son null mientras el alumno no rindió el cuestionario o no terminó la Misión.
 */
public record DetalleMision(String mision, Integer notaCuestionario, boolean completada,
                            LocalDateTime fechaFinalizacion, int actividadesCorrectas, int actividadesTotales) {}
