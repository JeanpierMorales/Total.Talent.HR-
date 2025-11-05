package Modelo.Repository;

import java.util.Date;
import java.util.List;

// Interfaz para el repositorio de logs de auditoría
// Define las operaciones para registrar acciones del sistema
public interface LogRepository {

    // Método para registrar una acción en el log
    void registrarAccion(String usuario, String accion, String descripcion);

    // Método para registrar login exitoso
    void registrarLoginExitoso(String usuario);

    // Método para registrar login fallido
    void registrarLoginFallido(String usuario, String razon);

    // Método para registrar creación de registro
    void registrarCreacion(String usuario, String tabla, int idRegistro);

    // Método para registrar actualización de registro
    void registrarActualizacion(String usuario, String tabla, int idRegistro);

    // Método para registrar eliminación de registro
    void registrarEliminacion(String usuario, String tabla, int idRegistro);

    // Método para obtener logs por usuario
    List<String> obtenerLogsPorUsuario(String usuario);

    // Método para obtener logs por fecha
    List<String> obtenerLogsPorFecha(Date fecha);

    // Método para obtener todos los logs
    List<String> obtenerTodosLosLogs();
}
