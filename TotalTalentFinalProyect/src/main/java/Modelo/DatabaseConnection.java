package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Constantes para la configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/total_talent_db";
    private static final String USER = "root"; // Usuario por defecto en XAMPP
    private static final String PASSWORD = ""; // Contraseña vacía por defecto en XAMPP

    // buscamos obtener la conexión a la base de datos con el uso de excepciones
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC de MySQL no encontrado: " + e.getMessage());
        }
    }

    // Cerramos la conexión a la base de datos de forma segura tras su uso 
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
