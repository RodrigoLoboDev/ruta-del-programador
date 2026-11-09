package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementación JDBC de MisionDAO: lee y escribe en MySQL. */
public class MisionDAOJdbc implements MisionDAO {
    /** Misiones disponibles para una franja etaria, ordenadas por Mundo y Misión (RF02). */
    @Override
    public List<Mision> listarPorFranja(int idFranja) throws PersistenciaException {
        String sql = """
            SELECT mi.id_mision, m.nombre, m.orden, mi.titulo, mi.orden
            FROM mision mi JOIN mundo m ON m.id_mundo = mi.id_mundo
            WHERE mi.id_franja = ?
            ORDER BY m.orden, mi.orden""";
        List<Mision> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idFranja);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Mision(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5)));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron obtener las Misiones", e);
        }
        return lista;
    }

    /** Texto de la lección de una Misión; null si la Misión no existe (CU03). */
    @Override
    public String obtenerLeccion(int idMision) throws PersistenciaException {
        String sql = "SELECT leccion FROM mision WHERE id_mision = ?";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idMision);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener la lección de la Misión", e);
        }
    }
}
