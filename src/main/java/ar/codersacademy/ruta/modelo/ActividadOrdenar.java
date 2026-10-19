package ar.codersacademy.ruta.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Actividad ARRASTRAR_SOLTAR en consola: ordenar los elementos que se muestran mezclados al azar. */
public class ActividadOrdenar extends ActividadInteractiva {
    private final List<String> elementos;
    private final List<Integer> ordenCorrecto;
    private final List<Integer> mostrado; // mostrado.get(p) = posición original del elemento que se ve en el lugar p

    /** @param ordenCorrecto posiciones (desde 0) de {@code elementos} en el orden en que deben quedar */
    public ActividadOrdenar(int id, String consigna, int orden, String pista,
                            List<String> elementos, List<Integer> ordenCorrecto) throws ContenidoInvalidoException {
        this(id, consigna, orden, pista, elementos, ordenCorrecto, new Random());
    }

    /** @param azar fuente del mezclado; con una semilla fija el orden mostrado es repetible (pruebas) */
    public ActividadOrdenar(int id, String consigna, int orden, String pista,
                            List<String> elementos, List<Integer> ordenCorrecto, Random azar)
            throws ContenidoInvalidoException {
        super(id, consigna, orden, pista);
        if (elementos == null || elementos.size() < 2 || ordenCorrecto == null
                || ordenCorrecto.size() != elementos.size()) {
            throw new ContenidoInvalidoException("La actividad \"" + consigna + "\" necesita elementos y un orden correcto del mismo tamaño");
        }
        for (int i = 0; i < elementos.size(); i++) {
            if (!ordenCorrecto.contains(i)) {
                throw new ContenidoInvalidoException("El orden correcto de \"" + consigna + "\" no es una permutación");
            }
        }
        this.elementos = List.copyOf(elementos);
        this.ordenCorrecto = List.copyOf(ordenCorrecto);
        List<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < elementos.size(); i++) {
            posiciones.add(i);
        }
        do {
            Collections.shuffle(posiciones, azar);
        } while (posiciones.equals(this.ordenCorrecto)); // no mostrar el orden ya resuelto
        this.mostrado = List.copyOf(posiciones);
    }

    @Override
    public String getInstrucciones() {
        return "Escribí los números de los pasos en el orden correcto, separados por comas (ej: 2,1,3).";
    }

    /** Se muestran mezclados (el mismo orden durante toda la vida del objeto), con su número. */
    @Override
    public List<String> getElementos() {
        List<String> lineas = new ArrayList<>();
        for (int pos = 0; pos < elementos.size(); pos++) {
            lineas.add((pos + 1) + ") " + elementos.get(mostrado.get(pos)));
        }
        return lineas;
    }

    @Override
    public boolean validar(String respuesta) {
        if (respuesta == null) {
            return false;
        }
        String[] partes = respuesta.split(",");
        if (partes.length != elementos.size()) {
            return false;
        }
        for (int k = 0; k < partes.length; k++) {
            int posicion;
            try {
                posicion = Integer.parseInt(partes[k].trim());
            } catch (NumberFormatException e) {
                return false;
            }
            if (posicion < 1 || posicion > elementos.size()) {
                return false;
            }
            if (!mostrado.get(posicion - 1).equals(ordenCorrecto.get(k))) {
                return false;
            }
        }
        return true;
    }
}
