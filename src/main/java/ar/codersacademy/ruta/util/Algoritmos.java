package ar.codersacademy.ruta.util;

import java.util.Comparator;
import java.util.List;

/** Ordenamiento y búsqueda escritos a mano (sin Collections.sort ni Collections.binarySearch). */
public final class Algoritmos {
    private Algoritmos() {}

    /**
     * Ordena la lista en el lugar por inserción: cada elemento se desplaza hacia la izquierda hasta
     * encontrar su lugar dentro de la parte ya ordenada. Es estable y cuesta O(n²), suficiente para un curso.
     */
    public static <T> void ordenarPorInsercion(List<T> lista, Comparator<? super T> criterio) {
        for (int i = 1; i < lista.size(); i++) {
            T actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && criterio.compare(lista.get(j), actual) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }

    /**
     * Búsqueda binaria: descarta la mitad de la lista en cada paso, O(log n).
     * La lista debe estar ordenada con el mismo criterio que se usa para buscar.
     * @return la posición de un elemento equivalente a {@code buscado}, o -1 si no está
     */
    public static <T> int buscarBinario(List<T> ordenada, T buscado, Comparator<? super T> criterio) {
        int desde = 0;
        int hasta = ordenada.size() - 1;
        while (desde <= hasta) {
            int medio = (desde + hasta) >>> 1;
            int comparacion = criterio.compare(ordenada.get(medio), buscado);
            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                desde = medio + 1;
            } else {
                hasta = medio - 1;
            }
        }
        return -1;
    }
}
