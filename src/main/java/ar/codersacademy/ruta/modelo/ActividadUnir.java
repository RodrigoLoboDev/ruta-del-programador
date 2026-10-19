package ar.codersacademy.ruta.modelo;

import java.util.ArrayList;
import java.util.List;

/** Actividad UNIR_FLECHAS en consola: unir cada elemento numerado con su par rotulado con letras. */
public class ActividadUnir extends ActividadInteractiva {
    private final List<String> izquierda;
    private final List<String> derecha;

    /** @param pares cada par es {elemento, su correspondiente}; el correspondiente se muestra en orden inverso */
    public ActividadUnir(int id, String consigna, int orden, String pista, List<List<String>> pares)
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
        for (int j = 0; j < derecha.size(); j++) {
            lineas.add((char) ('A' + j) + ") " + derecha.get(derecha.size() - 1 - j));
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
            int indiceDer = derecha.size() - 1 - letra;
            if (unido[indiceIzq] || indiceIzq != indiceDer) {
                return false;
            }
            unido[indiceIzq] = true;
        }
        return true;
    }
}
