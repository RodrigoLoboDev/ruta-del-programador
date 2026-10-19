package ar.codersacademy.ruta.modelo;

import java.util.ArrayList;
import java.util.List;

/** Actividad COMPLETAR: completar el espacio en blanco de un texto eligiendo entre opciones. */
public class ActividadCompletar extends ActividadInteractiva {
    private final String texto;
    private final List<String> opciones;
    private final String respuestaCorrecta;

    public ActividadCompletar(int id, String consigna, int orden, String pista,
                              String texto, List<String> opciones, String respuestaCorrecta)
            throws ContenidoInvalidoException {
        super(id, consigna, orden, pista);
        if (texto == null || texto.isBlank() || opciones == null || opciones.size() < 2
                || respuestaCorrecta == null || !opciones.contains(respuestaCorrecta)) {
            throw new ContenidoInvalidoException("La actividad \"" + consigna
                    + "\" necesita un texto, al menos dos opciones y una respuesta que esté entre ellas");
        }
        this.texto = texto;
        this.opciones = List.copyOf(opciones);
        this.respuestaCorrecta = respuestaCorrecta;
    }

    @Override
    public String getInstrucciones() {
        return texto + "\nEscribí el número de la opción que completa el espacio.";
    }

    @Override
    public List<String> getElementos() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < opciones.size(); i++) {
            lineas.add((i + 1) + ") " + opciones.get(i));
        }
        return lineas;
    }

    @Override
    public boolean validar(String respuesta) {
        int numero;
        try {
            numero = Integer.parseInt(respuesta.trim());
        } catch (NumberFormatException e) {
            return false;
        }

        if (numero < 1 || numero > opciones.size()) {
            return false;
        }

        return opciones.get(numero - 1).equals(respuestaCorrecta);
    }
}
