package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.PersistenciaException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de AlumnoDAO: lee los alumnos desde MySQL.
 */
public class AlumnoDAOJdbc implements AlumnoDAO {
    
    @Override
    public List<Alumno> listarActivos() throws PersistenciaException {
        String sql = "SELECT id_alumno, nombre, apellido, id_franja FROM alumno WHERE activo = TRUE ORDER BY nombre";
        List<Alumno> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Alumno(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener la lista de alumnos", e);
        }
        return lista;
    }
}
