package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase utilitaria para obtener conexiones JDBC
public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3305/total_talent_hr?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
