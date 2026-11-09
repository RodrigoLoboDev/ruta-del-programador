package ar.codersacademy.ruta.util;

import ar.codersacademy.ruta.modelo.DetalleMision;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta el progreso de un alumno a un archivo CSV (separador ";", UTF-8 con BOM para que Excel
 * muestre bien las tildes). Los encabezados son un arreglo fijo y las filas un ArrayList de arreglos.
 * No imprime ni lee: solo arma el contenido y escribe el archivo.
 */
public final class ExportadorCSV {
    public static final String SEPARADOR = ";";
    /** Marca de orden de bytes que se escribe al inicio del archivo. */
    public static final String BOM = "\uFEFF";

    // Columnas fijas del CSV: siempre son 5, por eso un arreglo
    public static final String[] ENCABEZADOS = {
            "Misión", "Nota cuestionario", "Completada", "Fecha finalización", "Actividades correctas"
    };

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ExportadorCSV() {}

    /** Una fila (String[]) por Misión; la cantidad de Misiones es variable, por eso ArrayList. */
    public static ArrayList<String[]> armarFilas(List<DetalleMision> detalle) {
        ArrayList<String[]> filas = new ArrayList<>();
        for (DetalleMision d : detalle) {
            String[] fila = {
                    d.mision(),
                    d.notaCuestionario() == null ? "-" : String.valueOf(d.notaCuestionario()),
                    d.completada() ? "Sí" : "No",
                    d.fechaFinalizacion() == null ? "-" : d.fechaFinalizacion().format(FORMATO_FECHA),
                    d.actividadesCorrectas() + "/" + d.actividadesTotales()
            };
            filas.add(fila);
        }
        return filas;
    }

    /** Escribe el BOM, la línea de encabezados y una línea por fila, en UTF-8. */
    public static void escribir(Path destino, String[] encabezados, List<String[]> filas) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            w.write(BOM);
            escribirLinea(w, encabezados);
            for (String[] fila : filas) {
                escribirLinea(w, fila);
            }
        }
    }

    /** Escapa cada celda en un arreglo nuevo del mismo tamaño y las une con el separador. */
    private static void escribirLinea(BufferedWriter w, String[] celdas) throws IOException {
        String[] escapadas = new String[celdas.length];
        for (int i = 0; i < celdas.length; i++) {
            escapadas[i] = escapar(celdas[i]);
        }
        w.write(String.join(SEPARADOR, escapadas));
        w.newLine();
    }

    /** Si la celda tiene separador, comillas o salto de línea la encierra entre comillas y duplica las internas. */
    static String escapar(String celda) {
        if (celda == null) {
            return "";
        }
        boolean requiereComillas = celda.contains(SEPARADOR) || celda.contains("\"")
                || celda.contains("\n") || celda.contains("\r");
        return requiereComillas ? "\"" + celda.replace("\"", "\"\"") + "\"" : celda;
    }
}
