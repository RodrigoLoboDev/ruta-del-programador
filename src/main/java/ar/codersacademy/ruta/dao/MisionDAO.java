package ar.codersacademy.ruta.dao;

import ar.codersacademy.ruta.modelo.Mision;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MisionDAO {
    /** Misiones disponibles para una franja etaria, ordenadas por Mundo y Misión (RF02). */
    public List<Mision> listarPorFranja(int idFranja) throws SQLException {
        String sql = """
            SELECT mi.id_mision, m.nombre, m.orden, mi.titulo, mi.orden
            FROM mision mi JOIN mundo m ON m.id_mundo = mi.id_mundo
            WHERE mi.id_franja = ?
            ORDER BY m.orden, mi.orden""";
        List<Mision> lista = new ArrayList<>();
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idFranja);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Mision(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5)));
                }
            }
        }
        return lista;
    }
}
