package ar.codersacademy.ruta.controlador;

import ar.codersacademy.ruta.dao.AlumnoDAO;
import ar.codersacademy.ruta.dao.MisionDAO;
import ar.codersacademy.ruta.dao.ProgresoDAO;
import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.sql.SQLException;
import java.util.List;

/** Controlador: coordina la vista con los DAO y aplica las reglas de negocio. */
public class RutaController {
    public static final int PREGUNTAS_POR_CUESTIONARIO = 3;
    public static final int MINIMO_PARA_APROBAR = 2;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final MisionDAO misionDAO = new MisionDAO();
    private final ProgresoDAO progresoDAO = new ProgresoDAO();

    public List<Alumno> alumnos() throws SQLException { return alumnoDAO.listarActivos(); }

    public List<Mision> misionesDe(Alumno alumno) throws SQLException {
        return misionDAO.listarPorFranja(alumno.getIdFranja());
    }

    /** Regla de negocio: el cuestionario se aprueba con al menos 2 de 3 respuestas correctas. */
    public static boolean apruebaCuestionario(int correctas) {
        if (correctas < 0 || correctas > PREGUNTAS_POR_CUESTIONARIO) {
            throw new IllegalArgumentException("Cantidad de respuestas correctas fuera de rango: " + correctas);
        }
        return correctas >= MINIMO_PARA_APROBAR;
    }

    /** Registra la nota; devuelve si aprobó (aprobar habilita las actividades, no completa la Misión). */
    public boolean registrarCuestionario(Alumno alumno, Mision mision, int correctas) throws SQLException {
        boolean aprobado = apruebaCuestionario(correctas);
        progresoDAO.registrarCuestionario(alumno.getId(), mision.getId(), correctas);
        return aprobado;
    }

    public List<ResumenProgreso> progresoGeneral() throws SQLException { return progresoDAO.resumenPorAlumno(); }
}
