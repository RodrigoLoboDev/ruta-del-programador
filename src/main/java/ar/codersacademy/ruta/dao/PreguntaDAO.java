package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.ContenidoInvalidoException;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import ar.codersacademy.ruta.modelo.Pregunta;
import java.util.List;

/** Contrato de acceso a las preguntas de los cuestionarios (CU04). */
public interface PreguntaDAO {
    /** Preguntas del cuestionario de una Misión con sus opciones, en orden (CU04). */
    List<Pregunta> listarPorMision(int idMision) throws PersistenciaException, ContenidoInvalidoException;
}
