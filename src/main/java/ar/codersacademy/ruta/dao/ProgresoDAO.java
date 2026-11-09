package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.PersistenciaException;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.util.List;

/** Contrato de acceso al progreso de los alumnos (CU04 a CU09). */
public interface ProgresoDAO {
    /** Registra la nota del cuestionario (RF07); rendirlo de nuevo conserva la mejor nota. */
    void registrarCuestionario(int idAlumno, int idMision, int correctas) throws PersistenciaException;

    /** Nota del cuestionario del alumno en la Misión; null si todavía no lo rindió (CU04). */
    Integer obtenerResultadoCuestionario(int idAlumno, int idMision) throws PersistenciaException;

    /** Registra un intento de actividad (RF07); requiere que el alumno ya tenga progreso en la Misión. */
    void registrarActividad(int idAlumno, int idMision, int idActividad, boolean correcta) throws PersistenciaException;

    /** Ids de las actividades de la Misión que el alumno ya resolvió bien (CU05). */
    List<Integer> actividadesCorrectas(int idAlumno, int idMision) throws PersistenciaException;

    /** Marca la Misión como completada al terminar sus actividades (RF07). */
    void marcarCompletada(int idAlumno, int idMision) throws PersistenciaException;

    /** Resumen del avance de todos los alumnos, sin orden garantizado (RF09). */
    List<ResumenProgreso> resumenPorAlumno() throws PersistenciaException;

    /** Resumen del avance de un alumno (CU08); null si no existe o está dado de baja. */
    ResumenProgreso resumenDe(int idAlumno) throws PersistenciaException;
}
