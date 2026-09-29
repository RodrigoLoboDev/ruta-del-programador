package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgresoDAO {
    /** Registra o actualiza el progreso de un alumno en una Misión (RF07). */
    public void registrarCuestionario(int idAlumno, int idMision, int correctas, boolean completada) throws SQLException {
        String sql = """
            INSERT INTO progreso (id_alumno, id_mision, resultado_cuestionario, completada, fecha_finalizacion)
            VALUES (?, ?, ?, ?, IF(?, NOW(), NULL))
            ON DUPLICATE KEY UPDATE resultado_cuestionario = VALUES(resultado_cuestionario),
                                    completada = VALUES(completada),
                                    fecha_finalizacion = VALUES(fecha_finalizacion)""";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMision);
            ps.setInt(3, correctas);
            ps.setBoolean(4, completada);
            ps.setBoolean(5, completada);
            ps.executeUpdate();
        }
    }

    /** Resumen del avance de todos los alumnos (RF09). */
    public List<ResumenProgreso> resumenPorAlumno() throws SQLException {
        String sql = """
            SELECT CONCAT(a.nombre, ' ', a.apellido),
                   COUNT(CASE WHEN pr.completada THEN 1 END),
                   AVG(pr.resultado_cuestionario)
            FROM alumno a LEFT JOIN progreso pr ON pr.id_alumno = a.id_alumno
            WHERE a.activo = TRUE
            GROUP BY a.id_alumno, a.nombre, a.apellido
            ORDER BY 2 DESC""";
        List<ResumenProgreso> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double prom = rs.getDouble(3);
                lista.add(new ResumenProgreso(rs.getString(1), rs.getInt(2), rs.wasNull() ? null : prom));
            }
        }
        return lista;
    }
}
