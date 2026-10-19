package ar.codersacademy.ruta.controlador;

import ar.codersacademy.ruta.dao.ActividadDAO;
import ar.codersacademy.ruta.dao.AlumnoDAO;
import ar.codersacademy.ruta.dao.MisionDAO;
import ar.codersacademy.ruta.dao.PreguntaDAO;
import ar.codersacademy.ruta.dao.ProgresoDAO;
import ar.codersacademy.ruta.modelo.ActividadInteractiva;
import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.ContenidoInvalidoException;
import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.Pregunta;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/** Controlador: coordina la vista con los DAO y aplica las reglas de negocio. */
public class RutaController {
    public static final int PREGUNTAS_POR_CUESTIONARIO = Mision.PREGUNTAS_POR_CUESTIONARIO;
    public static final int MINIMO_PARA_APROBAR = 2;
    public static final String PALABRA_SALIR = "salir";

    /** Resultado de rendir el cuestionario (CU04). */
    public record ResultadoCuestionario(int correctas, boolean aprobado) {}

    /** Qué pasó con la respuesta a una actividad (CU05, CU07). */
    public enum ResultadoRespuesta { CORRECTA, INCORRECTA, MISION_COMPLETADA, SALIO }

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final MisionDAO misionDAO = new MisionDAO();
    private final PreguntaDAO preguntaDAO = new PreguntaDAO();
    private final ActividadDAO actividadDAO = new ActividadDAO();
    private final ProgresoDAO progresoDAO = new ProgresoDAO();

    // Sesión de actividades en curso (CU05): se arma en iniciarActividades y se vacía al terminar o salir.
    private final Queue<ActividadInteractiva> pendientes = new ArrayDeque<>();
    private Alumno alumnoEnCurso;
    private Mision misionEnCurso;

    public List<Alumno> alumnos() throws SQLException { return alumnoDAO.listarActivos(); }

    public List<Mision> misionesDe(Alumno alumno) throws SQLException {
        return misionDAO.listarPorFranja(alumno.getIdFranja());
    }

    /** Carga lección, preguntas y actividades de la Misión si todavía no están (CU03). */
    public void cargarContenido(Mision mision) throws SQLException, ContenidoInvalidoException {
        if (mision.getLeccion() != null) {
            return;
        }
        mision.setContenido(misionDAO.obtenerLeccion(mision.getId()),
                preguntaDAO.listarPorMision(mision.getId()),
                actividadDAO.listarPorMision(mision.getId()));
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

    /**
     * Corrige el cuestionario y registra la nota (CU04).
     * @param opcionesElegidas una opción (posición desde 0) por pregunta, en el orden de las preguntas
     */
    public ResultadoCuestionario rendirCuestionario(Alumno alumno, Mision mision, List<Integer> opcionesElegidas)
            throws SQLException, ContenidoInvalidoException {
        cargarContenido(mision);
        List<Pregunta> preguntas = mision.getPreguntas();
        if (opcionesElegidas.size() != preguntas.size()) {
            throw new IllegalArgumentException("Se esperaba una respuesta por pregunta");
        }
        int correctas = 0;
        for (int i = 0; i < preguntas.size(); i++) {
            if (preguntas.get(i).esCorrecta(opcionesElegidas.get(i))) {
                correctas++;
            }
        }
        return new ResultadoCuestionario(correctas, registrarCuestionario(alumno, mision, correctas));
    }

    /** Las actividades solo se habilitan con el cuestionario de la Misión aprobado (CU05). */
    public boolean cuestionarioAprobado(Alumno alumno, Mision mision) throws SQLException {
        Integer nota = progresoDAO.obtenerResultadoCuestionario(alumno.getId(), mision.getId());
        return nota != null && apruebaCuestionario(nota);
    }

    /**
     * Arma la cola con las actividades que el alumno todavía no resolvió bien (CU05).
     * @return cuántas quedan; si ninguna, la Misión se da por completada
     * @throws IllegalStateException si el cuestionario no está aprobado
     */
    public int iniciarActividades(Alumno alumno, Mision mision) throws SQLException, ContenidoInvalidoException {
        exigirCuestionarioAprobado(alumno, mision);
        cargarContenido(mision);
        Set<Integer> resueltas = new HashSet<>(progresoDAO.actividadesCorrectas(alumno.getId(), mision.getId()));
        pendientes.clear();
        for (ActividadInteractiva actividad : mision.getActividades()) {
            if (!resueltas.contains(actividad.getId())) {
                pendientes.add(actividad);
            }
        }
        alumnoEnCurso = alumno;
        misionEnCurso = mision;
        if (pendientes.isEmpty()) {
            progresoDAO.marcarCompletada(alumno.getId(), mision.getId());
        }
        return pendientes.size();
    }

    /** Actividad que toca resolver ahora, o null si no quedan. */
    public ActividadInteractiva actividadActual() {
        return pendientes.peek();
    }

    /**
     * Evalúa la respuesta a la actividad actual y registra el intento (CU05, CU06).
     * Si es incorrecta la actividad vuelve al final de la cola; si escribe "salir" se corta la sesión
     * (lo ya resuelto queda guardado); al vaciarse la cola la Misión se completa.
     */
    public ResultadoRespuesta responder(String respuesta) throws SQLException {
        if (pendientes.isEmpty()) {
            throw new IllegalStateException("No hay una actividad en curso");
        }
        if (respuesta.trim().equalsIgnoreCase(PALABRA_SALIR)) {
            pendientes.clear();
            return ResultadoRespuesta.SALIO;
        }
        exigirCuestionarioAprobado(alumnoEnCurso, misionEnCurso);
        ActividadInteractiva actividad = pendientes.poll();
        boolean correcta = actividad.validar(respuesta);
        progresoDAO.registrarActividad(alumnoEnCurso.getId(), misionEnCurso.getId(), actividad.getId(), correcta);
        if (!correcta) {
            pendientes.add(actividad);
            return ResultadoRespuesta.INCORRECTA;
        }
        if (pendientes.isEmpty()) {
            progresoDAO.marcarCompletada(alumnoEnCurso.getId(), misionEnCurso.getId());
            return ResultadoRespuesta.MISION_COMPLETADA;
        }
        return ResultadoRespuesta.CORRECTA;
    }

    private void exigirCuestionarioAprobado(Alumno alumno, Mision mision) throws SQLException {
        if (!cuestionarioAprobado(alumno, mision)) {
            throw new IllegalStateException("Hay que aprobar el cuestionario antes de hacer las actividades");
        }
    }

    public List<ResumenProgreso> progresoGeneral() throws SQLException { return progresoDAO.resumenPorAlumno(); }

    /** Progreso propio del alumno (CU08). */
    public ResumenProgreso progresoDe(Alumno alumno) throws SQLException { return progresoDAO.resumenDe(alumno.getId()); }
}
