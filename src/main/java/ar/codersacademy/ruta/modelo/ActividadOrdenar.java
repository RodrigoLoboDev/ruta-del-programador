package ar.codersacademy.ruta.modelo;

import java.util.ArrayList;
import java.util.List;

/** Actividad ARRASTRAR_SOLTAR en consola: ordenar los elementos que se muestran invertidos. */
public class ActividadOrdenar extends ActividadInteractiva {
    private final List<String> elementos;
    private final List<Integer> ordenCorrecto;

    /** @param ordenCorrecto posiciones (desde 0) de {@code elementos} en el orden en que deben quedar */
    public ActividadOrdenar(int id, String consigna, int orden, String pista,
                            List<String> elementos, List<Integer> ordenCorrecto) throws ContenidoInvalidoException {
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
    }

    @Override
    public String getInstrucciones() {
        return "Escribí los números de los pasos en el orden correcto, separados por comas (ej: 2,1,3).";
    }

    /** Se muestran en orden inverso al de la lista original, con su número. */
    @Override
    public List<String> getElementos() {
        List<String> lineas = new ArrayList<>();
        for (int pos = 0; pos < elementos.size(); pos++) {
            lineas.add((pos + 1) + ") " + elementos.get(elementos.size() - 1 - pos));
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
            int mostrado;
            try {
                mostrado = Integer.parseInt(partes[k].trim());
            } catch (NumberFormatException e) {
                return false;
            }
            if (mostrado < 1 || mostrado > elementos.size()) {
                return false;
            }
            int indiceOriginal = elementos.size() - mostrado;
            if (indiceOriginal != ordenCorrecto.get(k)) {
                return false;
            }
        }
        return true;
    }
}
