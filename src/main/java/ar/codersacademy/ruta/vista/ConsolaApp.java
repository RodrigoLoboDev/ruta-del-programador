package ar.codersacademy.ruta.vista;

import ar.codersacademy.ruta.controlador.RutaController;
import ar.codersacademy.ruta.controlador.RutaController.ResultadoCuestionario;
import ar.codersacademy.ruta.controlador.RutaController.ResultadoRespuesta;
import ar.codersacademy.ruta.modelo.ActividadInteractiva;
import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.ContenidoInvalidoException;
import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.Pregunta;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Vista de consola del prototipo operacional: único lugar con Scanner y System.out.
 * Menú principal con los casos de uso CU01 a CU09; las reglas viven en el controlador.
 */
public class ConsolaApp {
    private static final RutaController ctrl = new RutaController();
    private static final Scanner in = new Scanner(System.in);

    private static Alumno alumno = null;
    private static Mision mision = null;

    public static void main(String[] args) {
        System.out.println("=== Ruta del Programador - Prototipo ===");
        int opcion = -1;
        try {
            while (opcion != 0) {
                mostrarMenu();
                opcion = leerEntero("Opción: ", 0, 7);
                try {
                    switch (opcion) {
                        case 1 -> seleccionarPerfil();
                        case 2 -> verMundosYMisiones();
                        case 3 -> verLeccion();
                        case 4 -> resolverCuestionario();
                        case 5 -> resolverActividades();
                        case 6 -> verProgresoPropio();
                        case 7 -> verProgresoDeAlumnos();
                        case 0 -> System.out.println("¡Hasta la próxima!");
                    }
                } catch (ContenidoInvalidoException e) {
                    System.out.println("No se puede abrir la Misión: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("Error de base de datos: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nFin de la entrada. ¡Hasta la próxima!");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú principal ---");
        System.out.println("Perfil: " + (alumno == null ? "(sin elegir)" : alumno)
                + " | Misión: " + (mision == null ? "(sin elegir)" : mision.getTitulo()));
        System.out.println("1) Seleccionar perfil");
        System.out.println("2) Ver Mundos y Misiones");
        System.out.println("3) Ver lección");
        System.out.println("4) Resolver cuestionario");
        System.out.println("5) Resolver actividades");
        System.out.println("6) Ver mi progreso");
        System.out.println("7) Ver progreso de los alumnos (docente)");
        System.out.println("0) Salir");
    }

    /** CU01. */
    private static void seleccionarPerfil() throws SQLException {
        List<Alumno> alumnos = ctrl.alumnos();
        for (int i = 0; i < alumnos.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, alumnos.get(i));
        }
        alumno = alumnos.get(leerEntero("Seleccioná tu perfil: ", 1, alumnos.size()) - 1);
        mision = null;
        System.out.println("¡Hola, " + alumno.getNombre() + "!");
    }

    /** CU02: lista las Misiones de la franja del alumno, agrupadas por Mundo, y deja elegir una. */
    private static void verMundosYMisiones() throws SQLException {
        if (!hayPerfil()) {
            return;
        }
        List<Mision> misiones = ctrl.misionesDe(alumno);
        int mundoActual = -1;
        for (int i = 0; i < misiones.size(); i++) {
            Mision m = misiones.get(i);
            if (m.getOrdenMundo() != mundoActual) {
                mundoActual = m.getOrdenMundo();
                System.out.printf("Mundo %d: %s%n", m.getOrdenMundo(), m.getMundo());
            }
            System.out.printf("  %d) Misión %d: %s%n", i + 1, m.getOrden(), m.getTitulo());
        }
        Mision elegida = misiones.get(leerEntero("Elegí una Misión: ", 1, misiones.size()) - 1);
        mision = null;
        try {
            ctrl.cargarContenido(elegida);
            mision = elegida;
            System.out.println("Misión elegida: " + mision.getTitulo());
        } catch (ContenidoInvalidoException e) {
            System.out.println("Esa Misión todavía no se puede jugar: " + e.getMessage());
        }
    }

    /** CU03. */
    private static void verLeccion() {
        if (!hayMision()) {
            return;
        }
        System.out.println("\n--- Lección: " + mision.getTitulo() + " ---");
        System.out.println(mision.getLeccion());
    }

    /** CU04: si ya está aprobado va directo a las actividades; si aprueba ahora, también. */
    private static void resolverCuestionario() throws SQLException, ContenidoInvalidoException {
        if (!hayMision()) {
            return;
        }
        if (ctrl.cuestionarioAprobado(alumno, mision)) {
            System.out.println("Ya aprobaste el cuestionario de esta Misión. ¡Vamos a las actividades!");
            resolverActividades();
            return;
        }
        System.out.println("\n--- Cuestionario: " + mision.getTitulo() + " ---");
        List<Integer> elegidas = new ArrayList<>();
        int numero = 1;
        for (Pregunta p : mision.getPreguntas()) {
            System.out.printf("%nPregunta %d: %s%n", numero++, p.getEnunciado());
            List<String> opciones = p.getOpciones();
            for (int i = 0; i < opciones.size(); i++) {
                System.out.printf("  %d) %s%n", i + 1, opciones.get(i));
            }
            elegidas.add(leerEntero("Tu respuesta: ", 1, opciones.size()) - 1);
        }
        ResultadoCuestionario r = ctrl.rendirCuestionario(alumno, mision, elegidas);
        System.out.printf("%nRespondiste bien %d de %d.%n", r.correctas(), RutaController.PREGUNTAS_POR_CUESTIONARIO);
        if (r.aprobado()) {
            System.out.println("¡Cuestionario aprobado! Se habilitan las actividades.");
            resolverActividades();
        } else {
            System.out.println("Casi: repasá la lección (opción 3) y volvé a intentarlo.");
        }
    }

    /** CU05 a CU07: presenta cada actividad igual, sin importar su tipo; la actividad interpreta la respuesta. */
    private static void resolverActividades() throws SQLException, ContenidoInvalidoException {
        if (!hayMision()) {
            return;
        }
        if (!ctrl.cuestionarioAprobado(alumno, mision)) {
            System.out.println("Primero tenés que aprobar el cuestionario (opción 4).");
            return;
        }
        int pendientes = ctrl.iniciarActividades(alumno, mision);
        if (pendientes == 0) {
            System.out.println("Ya terminaste todas las actividades de esta Misión: ¡Misión completada!");
            return;
        }
        System.out.printf("%nTenés %d actividades. Escribí \"%s\" para volver al menú: tu progreso queda guardado.%n",
                pendientes, RutaController.PALABRA_SALIR);
        ActividadInteractiva actividad;
        while ((actividad = ctrl.actividadActual()) != null) {
            System.out.println("\n--- " + actividad.getConsigna() + " ---");
            System.out.println(actividad.getInstrucciones());
            for (String linea : actividad.getElementos()) {
                System.out.println("   " + linea);
            }
            System.out.print("Tu respuesta: ");
            ResultadoRespuesta resultado = ctrl.responder(in.nextLine());
            switch (resultado) {
                case CORRECTA -> System.out.println("¡Correcto!");
                case INCORRECTA -> System.out.println("Todavía no. Pista: " + actividad.getPista()
                        + "\n(Vas a volver a intentarla más adelante.)");
                case MISION_COMPLETADA -> System.out.println("¡Correcto! ¡Misión completada!");
                case SALIO -> System.out.println("Volvés al menú. Tu progreso quedó guardado.");
            }
        }
    }

    /** CU08. */
    private static void verProgresoPropio() throws SQLException {
        if (!hayPerfil()) {
            return;
        }
        ResumenProgreso r = ctrl.progresoDe(alumno);
        System.out.println("\n--- Mi progreso ---");
        System.out.println(r == null ? "Sin datos de progreso." : formatear(r));
    }

    /** CU09. */
    private static void verProgresoDeAlumnos() throws SQLException {
        System.out.println("\n--- Progreso de los alumnos (vista del docente) ---");
        for (ResumenProgreso r : ctrl.progresoGeneral()) {
            System.out.println(formatear(r));
        }
    }

    private static String formatear(ResumenProgreso r) {
        return String.format("%-20s misiones completadas: %d | promedio cuestionario: %s",
                r.alumno(), r.misionesCompletadas(),
                r.promedioCuestionario() == null ? "-" : String.format("%.2f", r.promedioCuestionario()));
    }

    private static boolean hayPerfil() {
        if (alumno == null) {
            System.out.println("Primero elegí tu perfil (opción 1).");
        }
        return alumno != null;
    }

    private static boolean hayMision() {
        if (!hayPerfil()) {
            return false;
        }
        if (mision == null) {
            System.out.println("Primero elegí una Misión (opción 2).");
        }
        return mision != null;
    }

    /** Pide un entero entre min y max; repite si el texto no es un número o está fuera de rango. */
    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int n = in.nextInt();
                in.nextLine(); // descarta el resto de la línea
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.printf("Ingresá un número entre %d y %d.%n", min, max);
            } catch (InputMismatchException e) {
                in.nextLine(); // descarta el texto inválido que nextInt() dejó sin consumir
                System.out.println("Eso no es un número.");
            }
        }
    }
}
