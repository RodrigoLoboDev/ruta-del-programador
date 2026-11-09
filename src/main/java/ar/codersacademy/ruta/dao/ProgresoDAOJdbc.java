package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.PersistenciaException;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementación JDBC de ProgresoDAO: lee y escribe en MySQL. */
public class ProgresoDAOJdbc implements ProgresoDAO {
    /**
     * Registra la nota del cuestionario (RF07). Rendirlo de nuevo conserva la mejor nota (así quien ya
     * aprobó no pierde el acceso a las actividades) y no toca completada ni fecha_finalizacion.
     */
    @Override
    public void registrarCuestionario(int idAlumno, int idMision, int correctas) throws PersistenciaException {
        String sql = """
            INSERT INTO progreso (id_alumno, id_mision, resultado_cuestionario)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE resultado_cuestionario = GREATEST(COALESCE(resultado_cuestionario, 0), VALUES(resultado_cuestionario))""";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMision);
            ps.setInt(3, correctas);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar la nota del cuestionario", e);
        }
    }

    /** Nota del cuestionario del alumno en la Misión; null si todavía no lo rindió (CU04). */
    @Override
    public Integer obtenerResultadoCuestionario(int idAlumno, int idMision) throws PersistenciaException {
        String sql = "SELECT resultado_cuestionario FROM progreso WHERE id_alumno = ? AND id_mision = ?";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMision);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                int nota = rs.getInt(1);
                return rs.wasNull() ? null : nota;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar la nota del cuestionario", e);
        }
    }

    /**
     * Registra un intento de actividad (RF07): suma un intento y, una vez correcta, la actividad
     * queda correcta. Requiere que el alumno ya tenga progreso en la Misión (cuestionario rendido).
     */
    @Override
    public void registrarActividad(int idAlumno, int idMision, int idActividad, boolean correcta) throws PersistenciaException {
        String sql = """
            INSERT INTO resultado_actividad (id_progreso, id_actividad, correcta, intentos)
            SELECT id_progreso, ?, ?, 1 FROM progreso WHERE id_alumno = ? AND id_mision = ?
            ON DUPLICATE KEY UPDATE correcta = (correcta OR VALUES(correcta)),
                                    intentos = LEAST(intentos + 1, 127),
                                    fecha = NOW()""";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idActividad);
            ps.setBoolean(2, correcta);
            ps.setInt(3, idAlumno);
            ps.setInt(4, idMision);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el intento de la actividad", e);
        }
    }

    /** Ids de las actividades de la Misión que el alumno ya resolvió bien (CU05). */
    @Override
    public List<Integer> actividadesCorrectas(int idAlumno, int idMision) throws PersistenciaException {
        String sql = """
            SELECT ra.id_actividad
            FROM resultado_actividad ra JOIN progreso pr ON pr.id_progreso = ra.id_progreso
            WHERE pr.id_alumno = ? AND pr.id_mision = ? AND ra.correcta""";
        List<Integer> ids = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMision);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar las actividades resueltas", e);
        }
        return ids;
    }

    /** Marca la Misión como completada al terminar sus actividades (RF07); conserva la fecha si ya lo estaba. */
    @Override
    public void marcarCompletada(int idAlumno, int idMision) throws PersistenciaException {
        String sql = """
            UPDATE progreso SET completada = TRUE, fecha_finalizacion = COALESCE(fecha_finalizacion, NOW())
            WHERE id_alumno = ? AND id_mision = ?""";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMision);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo marcar la Misión como completada", e);
        }
    }

    /** Resumen del avance de todos los alumnos, sin orden garantizado: el controlador los ordena (RF09). */
    @Override
    public List<ResumenProgreso> resumenPorAlumno() throws PersistenciaException {
        return resumen(null);
    }

    /** Resumen del avance de un alumno (CU08); null si no existe o está dado de baja. */
    @Override
    public ResumenProgreso resumenDe(int idAlumno) throws PersistenciaException {
        List<ResumenProgreso> lista = resumen(idAlumno);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /** Resumen de todos los alumnos activos, o solo del indicado si {@code idAlumno} no es null. */
    private List<ResumenProgreso> resumen(Integer idAlumno) throws PersistenciaException {
        String sql = """
            SELECT CONCAT(a.nombre, ' ', a.apellido),
                   COUNT(CASE WHEN pr.completada THEN 1 END),
                   AVG(pr.resultado_cuestionario)
            FROM alumno a LEFT JOIN progreso pr ON pr.id_alumno = a.id_alumno
            WHERE a.activo = TRUE AND (? IS NULL OR a.id_alumno = ?)
            GROUP BY a.id_alumno, a.nombre, a.apellido""";
        List<ResumenProgreso> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, idAlumno);
            ps.setObject(2, idAlumno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String alumno = rs.getString(1);
                    int completadas = rs.getInt(2);
                    double prom = rs.getDouble(3);
                    boolean sinCuestionarios = rs.wasNull(); // wasNull() refiere a la última columna leída
                    lista.add(new ResumenProgreso(alumno, completadas, sinCuestionarios ? null : prom));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el progreso de los alumnos", e);
        }
        return lista;
    }
}
