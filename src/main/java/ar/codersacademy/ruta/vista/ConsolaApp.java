package ar.codersacademy.ruta.vista;

import ar.codersacademy.ruta.controlador.RutaController;
import ar.codersacademy.ruta.modelo.Alumno;
import ar.codersacademy.ruta.modelo.Mision;
import ar.codersacademy.ruta.modelo.ResumenProgreso;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola del prototipo operacional.
 * En el TP3/TP4 se reemplaza por pantallas JavaFX sin cambiar el controlador ni los DAO.
 */
public class ConsolaApp {
    public static void main(String[] args) {
        RutaController ctrl = new RutaController();
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("=== Ruta del Programador - Prototipo ===");
            List<Alumno> alumnos = ctrl.alumnos();
            for (int i = 0; i < alumnos.size(); i++) {
                Alumno a = alumnos.get(i);
                System.out.printf("%d) %s %s%n", i + 1, a.nombre(), a.apellido());
            }
            System.out.print("Seleccioná tu perfil: ");
            Alumno alumno = alumnos.get(Integer.parseInt(in.nextLine().trim()) - 1);

            List<Mision> misiones = ctrl.misionesDe(alumno);
            System.out.println("\nMisiones disponibles para " + alumno.nombre() + ":");
            for (int i = 0; i < misiones.size(); i++) {
                Mision m = misiones.get(i);
                System.out.printf("%d) Mundo %d %s - Misión %d: %s%n", i + 1, m.ordenMundo(), m.mundo(), m.orden(), m.titulo());
            }
            System.out.print("Elegí una Misión: ");
            Mision mision = misiones.get(Integer.parseInt(in.nextLine().trim()) - 1);

            System.out.print("¿Cuántas respuestas correctas tuviste en el cuestionario (0 a 3)? ");
            int correctas = Integer.parseInt(in.nextLine().trim());
            boolean completada = ctrl.registrarCuestionario(alumno, mision, correctas);
            System.out.println(completada ? "¡Misión completada!" : "Casi: repasá la lección y volvé a intentarlo.");

            System.out.println("\n--- Progreso de los alumnos (vista del docente) ---");
            for (ResumenProgreso r : ctrl.progresoGeneral()) {
                System.out.printf("%-20s misiones completadas: %d | promedio cuestionario: %s%n",
                        r.alumno(), r.misionesCompletadas(),
                        r.promedioCuestionario() == null ? "-" : String.format("%.2f", r.promedioCuestionario()));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
