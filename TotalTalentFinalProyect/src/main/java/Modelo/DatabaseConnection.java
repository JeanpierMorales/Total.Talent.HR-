package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// --- Gestor de Conexión a la Base de Datos ---
// Esta clase centraliza la lógica para conectarse a la base de datos MySQL.
// Su propósito es proporcionar un punto único para obtener y cerrar conexiones,
// facilitando el mantenimiento (si la URL, usuario o pass cambian, solo se edita aquí).
public class DatabaseConnection {

    // Constantes para la configuración de la conexión.
    // URL de la base de datos (usando el driver JDBC de MySQL, localhost, puerto 3306 y BD total_talent_db).
    private static final String URL = "jdbc:mysql://localhost:3306/total_talent_db";
    // Usuario por defecto en XAMPP.
    private static final String USER = "root"; 
    // Contraseña vacía por defecto en XAMPP.
    private static final String PASSWORD = ""; 

    // Método estático para obtener una nueva conexión a la base de datos.
    // Es llamado por el constructor de la clase Facade para inicializar los repositorios.
    public static Connection getConnection() throws SQLException {
        try {
            // Carga el driver JDBC de MySQL en memoria.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Intenta establecer y devolver la conexión usando las credenciales.
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Lanza una excepción si no se encuentra la librería (el .jar) del driver.
            throw new SQLException("Driver JDBC de MySQL no encontrado: " + e.getMessage());
        }
    }

    // Método estático para cerrar una conexión de forma segura.
    // Es llamado por el método cerrarConexion() del Facade.
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Imprime un error en la consola si falla el cierre.
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}