package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import java.util.List;

/** Contrato de acceso a los datos de las Misiones (CU02, CU03). */
public interface MisionDAO {
    /** Misiones disponibles para una franja etaria, ordenadas por Mundo y Misión (RF02). */
    List<Mision> listarPorFranja(int idFranja) throws PersistenciaException;

    /** Texto de la lección de una Misión; null si la Misión no existe (CU03). */
    String obtenerLeccion(int idMision) throws PersistenciaException;
}
