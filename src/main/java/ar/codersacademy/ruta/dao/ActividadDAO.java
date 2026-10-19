package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAO {
    private final ObjectMapper mapper = new ObjectMapper();

    /** Actividades interactivas de una Misión, en orden, leyendo su contenido_json con Jackson (CU05). */
    public List<ActividadInteractiva> listarPorMision(int idMision) throws SQLException, ContenidoInvalidoException {
        String sql = """
            SELECT id_actividad, tipo, consigna, orden, contenido_json
            FROM actividad_interactiva
            WHERE id_mision = ?
            ORDER BY orden""";
        List<ActividadInteractiva> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idMision);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JsonNode json;
                    try {
                        json = mapper.readTree(rs.getString(5));
                    } catch (JsonProcessingException e) {
                        throw new ContenidoInvalidoException(
                                "El contenido de la actividad \"" + rs.getString(3) + "\" no es un JSON válido", e);
                    }
                    lista.add(crearActividad(rs.getString(2), rs.getInt(1), rs.getString(3), rs.getInt(4), json));
                }
            }
        }
        return lista;
    }

    /**
     * Patrón Factory: único lugar que conoce qué clase concreta corresponde a cada tipo guardado en la base.
     * El resto del sistema recibe una ActividadInteractiva y no distingue subclases.
     */
    private ActividadInteractiva crearActividad(String tipo, int id, String consigna, int orden, JsonNode json)
            throws ContenidoInvalidoException {
        String pista = json.path("pista").asText(null);
        try {
            return switch (tipo) {
                case "ARRASTRAR_SOLTAR" -> new ActividadOrdenar(id, consigna, orden, pista,
                        leer(json, "elementos", new TypeReference<List<String>>() {}),
                        leer(json, "ordenCorrecto", new TypeReference<List<Integer>>() {}));
                case "UNIR_FLECHAS" -> new ActividadUnir(id, consigna, orden, pista,
                        leer(json, "pares", new TypeReference<List<List<String>>>() {}));
                case "COMPLETAR" -> new ActividadCompletar(id, consigna, orden, pista,
                        leer(json, "texto", new TypeReference<String>() {}),
                        leer(json, "opciones", new TypeReference<List<String>>() {}),
                        leer(json, "respuesta", new TypeReference<String>() {}));
                default -> throw new ContenidoInvalidoException("Tipo de actividad desconocido: " + tipo);
            };
        } catch (IllegalArgumentException e) {
            throw new ContenidoInvalidoException("El contenido de la actividad \"" + consigna + "\" está mal formado", e);
        }
    }

    /** Convierte el campo del JSON al tipo pedido; falla si falta o tiene otra forma. */
    private <T> T leer(JsonNode json, String campo, TypeReference<T> tipo) {
        JsonNode nodo = json.get(campo);
        if (nodo == null || nodo.isNull()) {
            throw new IllegalArgumentException("falta el campo \"" + campo + "\"");
        }
        return mapper.convertValue(nodo, tipo);
    }
}
