package ar.codersacademy.ruta.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Punto único de conexión JDBC con la base MySQL local (localhost:3306). */
public final class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/ruta_programador";
    private static final String USUARIO = "root";
    private static final String CLAVE = "root";

    private ConexionBD() {}

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
