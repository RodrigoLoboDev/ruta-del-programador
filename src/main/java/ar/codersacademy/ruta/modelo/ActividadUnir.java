package ar.codersacademy.ruta.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Actividad UNIR_FLECHAS en consola: unir cada elemento numerado con su par, rotulado con letras y mezclado. */
public class ActividadUnir extends ActividadInteractiva {
    private final List<String> izquierda;
    private final List<String> derecha;
    private final List<Integer> mostrado; // mostrado.get(l) = índice del par cuyo correspondiente se ve en la letra l

    /** @param pares cada par es {elemento, su correspondiente}; los correspondientes se muestran mezclados */
    public ActividadUnir(int id, String consigna, int orden, String pista, List<List<String>> pares)
            throws ContenidoInvalidoException {
        this(id, consigna, orden, pista, pares, new Random());
    }

    /** @param azar fuente del mezclado; con una semilla fija el orden mostrado es repetible (pruebas) */
    public ActividadUnir(int id, String consigna, int orden, String pista, List<List<String>> pares, Random azar)
            throws ContenidoInvalidoException {
        super(id, consigna, orden, pista);
        if (pares == null || pares.size() < 2) {
            throw new ContenidoInvalidoException("La actividad \"" + consigna + "\" necesita al menos dos pares");
        }
        List<String> izq = new ArrayList<>();
        List<String> der = new ArrayList<>();
        for (List<String> par : pares) {
            if (par == null || par.size() != 2) {
                throw new ContenidoInvalidoException("Cada par de \"" + consigna + "\" debe tener dos elementos");
            }
            izq.add(par.get(0));
            der.add(par.get(1));
        }
        this.izquierda = List.copyOf(izq);
        this.derecha = List.copyOf(der);
        List<Integer> posiciones = new ArrayList<>();
        List<Integer> yaResuelto = new ArrayList<>();
        for (int i = 0; i < izquierda.size(); i++) {
            posiciones.add(i);
            yaResuelto.add(i);
        }
        do {
            Collections.shuffle(posiciones, azar);
        } while (posiciones.equals(yaResuelto)); // no mostrar cada par enfrentado a su correspondiente
        this.mostrado = List.copyOf(posiciones);
    }

    @Override
    public String getInstrucciones() {
        return "Uní cada número con su letra, separando los pares con comas (ej: 1-B,2-A).";
    }

    @Override
    public List<String> getElementos() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < izquierda.size(); i++) {
            lineas.add((i + 1) + ") " + izquierda.get(i));
        }
        for (int l = 0; l < derecha.size(); l++) {
            lineas.add((char) ('A' + l) + ") " + derecha.get(mostrado.get(l)));
        }
        return lineas;
    }

    @Override
    public boolean validar(String respuesta) {
        if (respuesta == null) {
            return false;
        }
        String[] uniones = respuesta.split(",");
        if (uniones.length != izquierda.size()) {
            return false;
        }
        boolean[] unido = new boolean[izquierda.size()];
        for (String union : uniones) {
            String[] lados = union.trim().split("-");
            if (lados.length != 2 || lados[1].trim().length() != 1) {
                return false;
            }
            int numero;
            try {
                numero = Integer.parseInt(lados[0].trim());
            } catch (NumberFormatException e) {
                return false;
            }
            int letra = Character.toUpperCase(lados[1].trim().charAt(0)) - 'A';
            if (numero < 1 || numero > izquierda.size() || letra < 0 || letra >= derecha.size()) {
                return false;
            }
            int indiceIzq = numero - 1;
            if (unido[indiceIzq] || mostrado.get(letra) != indiceIzq) {
                return false;
            }
            unido[indiceIzq] = true;
        }
        return true;
    }
}
