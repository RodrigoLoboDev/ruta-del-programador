package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import java.util.List;

/**
 * Contrato de acceso a los datos de los alumnos (CU01). Puede tener distintas implementaciones, como JDBC o en memoria.
 */
public interface AlumnoDAO {
    List<Alumno> listarActivos() throws PersistenciaException;
}