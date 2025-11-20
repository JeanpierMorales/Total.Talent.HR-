package Modelo.Repository;

import java.util.Date;
import java.util.List;

// --- Interfaz LogRepository ---
// Esta interfaz define el contrato para cualquier clase que quiera
// gestionar el almacenamiento y recuperación de logs de auditoría del sistema.
// El Facade depende de esta interfaz para registrar las acciones de los usuarios.
public interface LogRepository {

    // Define un método para registrar una acción genérica.
    void registrarAccion(String usuario, String accion, String descripcion);

    // Define un método para registrar un inicio de sesión exitoso.
    void registrarLoginExitoso(String usuario);

    // Define un método para registrar un inicio de sesión fallido.
    void registrarLoginFallido(String usuario, String razon);

    // Define un método para registrar la creación de un nuevo registro (ej. nuevo empleado).
    void registrarCreacion(String usuario, String tabla, int idRegistro);

    // Define un método para registrar la actualización de un registro.
    void registrarActualizacion(String usuario, String tabla, int idRegistro);

    // Define un método para registrar la eliminación de un registro.
    void registrarEliminacion(String usuario, String tabla, int idRegistro);

    // Define un método para obtener todos los logs de un usuario específico.
    // Usado por el Facade, que a su vez es llamado por la AdminStrategy.
    List<String> obtenerLogsPorUsuario(String usuario);

    // Define un método para obtener todos los logs de una fecha específica.
    List<String> obtenerLogsPorFecha(Date fecha);

    // Define un método para obtener absolutamente todos los logs del sistema.
    List<String> obtenerTodosLosLogs();
}