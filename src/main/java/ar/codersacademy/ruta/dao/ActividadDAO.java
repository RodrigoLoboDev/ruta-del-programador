package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.ActividadInteractiva;
import ar.codersacademy.ruta.modelo.ContenidoInvalidoException;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import java.util.List;

/** Contrato de acceso a las actividades interactivas de las Misiones (CU05). */
public interface ActividadDAO {
    /** Actividades interactivas de una Misión, en orden (CU05). */
    List<ActividadInteractiva> listarPorMision(int idMision) throws PersistenciaException, ContenidoInvalidoException;
}
