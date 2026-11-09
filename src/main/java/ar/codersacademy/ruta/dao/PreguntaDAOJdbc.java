package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.ContenidoInvalidoException;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import ar.codersacademy.ruta.modelo.Pregunta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementación JDBC de PreguntaDAO: lee y escribe en MySQL. */
public class PreguntaDAOJdbc implements PreguntaDAO {
    /** Preguntas del cuestionario de una Misión con sus opciones, en orden (CU04). */
    @Override
    public List<Pregunta> listarPorMision(int idMision) throws PersistenciaException, ContenidoInvalidoException {
        String sql = """
            SELECT p.id_pregunta, p.enunciado, o.texto, o.es_correcta
            FROM pregunta p JOIN opcion_pregunta o ON o.id_pregunta = p.id_pregunta
            WHERE p.id_mision = ?
            ORDER BY p.orden, o.id_opcion""";
        List<Pregunta> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idMision);
            try (ResultSet rs = ps.executeQuery()) {
                int idActual = -1;
                String enunciado = null;
                List<String> opciones = new ArrayList<>();
                int correcta = -1;
                while (rs.next()) {
                    if (rs.getInt(1) != idActual) {
                        if (idActual != -1) {
                            lista.add(crearPregunta(idActual, enunciado, opciones, correcta));
                        }
                        idActual = rs.getInt(1);
                        enunciado = rs.getString(2);
                        opciones = new ArrayList<>();
                        correcta = -1;
                    }
                    if (rs.getBoolean(4)) {
                        correcta = opciones.size();
                    }
                    opciones.add(rs.getString(3));
                }
                if (idActual != -1) {
                    lista.add(crearPregunta(idActual, enunciado, opciones, correcta));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el cuestionario de la Misión", e);
        }
        return lista;
    }

    private Pregunta crearPregunta(int id, String enunciado, List<String> opciones, int correcta)
            throws ContenidoInvalidoException {
        try {
            return new Pregunta(id, enunciado, opciones, correcta);
        } catch (IllegalArgumentException e) {
            throw new ContenidoInvalidoException("La pregunta \"" + enunciado + "\" está mal cargada: " + e.getMessage(), e);
        }
    }
}
