package Modelo.Repository;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogArchivoRepository implements LogRepository {

    // Logger para registrar errores internos de esta clase
    private static final Logger logger = Logger.getLogger(LogArchivoRepository.class.getName());

    // Carpeta donde se guardan los archivos de log
    private static final String carpetaLogs = "logs";

    // Prefijo para los nombres de archivos de log
    private static final String prefijoArchivo = "log_";

    // Extensión de los archivos de log
    private static final String extensionArchivo = ".txt";

    // Formato para la fecha y hora en los logs
    private final SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Constructor: crea la carpeta de logs si no existe
    public LogArchivoRepository() {
        try {
            Files.createDirectories(Paths.get(carpetaLogs));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creando carpeta de logs: " + e.getMessage());
        }
    }

    // Registra una acción general del usuario
    @Override
    public void registrarAccion(String usuario, String accion, String descripcion) {
        String entradaLog = String.format("[%s] USUARIO: %s | ACCION: %s | DESCRIPCION: %s",
                formatoFechaHora.format(new Date()), usuario, accion, descripcion);
        escribirEnLog(entradaLog);
    }

    // Registra un login exitoso
    @Override
    public void registrarLoginExitoso(String usuario) {
        String entradaLog = String.format("[%s] LOGIN EXITOSO - USUARIO: %s",
                formatoFechaHora.format(new Date()), usuario);
        escribirEnLog(entradaLog);
    }

    // Registra un login fallido con la razón
    @Override
    public void registrarLoginFallido(String usuario, String razon) {
        String entradaLog = String.format("[%s] LOGIN FALLIDO - USUARIO: %s | RAZON: %s",
                formatoFechaHora.format(new Date()), usuario, razon);
        escribirEnLog(entradaLog);
    }

    // Registra la creación de un registro en una tabla
    @Override
    public void registrarCreacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] CREACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Registra la actualización de un registro en una tabla
    @Override
    public void registrarActualizacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] ACTUALIZACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Registra la eliminación de un registro en una tabla
    @Override
    public void registrarEliminacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] ELIMINACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Obtiene todos los logs de un usuario específico
    @Override
    public List<String> obtenerLogsPorUsuario(String usuario) {
        List<String> logsDelUsuario = new ArrayList<>();
        try {
            List<String> todosLosLogs = leerTodosLosLogs();
            for (String log : todosLosLogs) {
                if (log.contains("USUARIO: " + usuario)) {
                    logsDelUsuario.add(log);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error obteniendo logs por usuario: " + e.getMessage());
        }
        return logsDelUsuario;
    }

    // Obtiene todos los logs de una fecha específica
    @Override
    public List<String> obtenerLogsPorFecha(Date fecha) {
        List<String> logsDeLaFecha = new ArrayList<>();
        SimpleDateFormat formatoFechaArchivo = new SimpleDateFormat("yyyy-MM-dd");
        String fechaComoTexto = formatoFechaArchivo.format(fecha);

        try {
            List<String> todosLosLogs = leerTodosLosLogs();
            for (String log : todosLosLogs) {
                if (log.contains("[" + fechaComoTexto)) {
                    logsDeLaFecha.add(log);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error obteniendo logs por fecha: " + e.getMessage());
        }
        return logsDeLaFecha;
    }

    // Obtiene todos los logs disponibles
    @Override
    public List<String> obtenerTodosLosLogs() {
        try {
            return leerTodosLosLogs();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error obteniendo todos los logs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Método privado que escribe una entrada en el archivo de log del día actual
    private void escribirEnLog(String entradaLog) {
        String nombreArchivo = carpetaLogs + File.separator
                + prefijoArchivo
                + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                + extensionArchivo;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            escritor.write(entradaLog);
            escritor.newLine();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error escribiendo en archivo de log: " + e.getMessage());
        }
    }

    // Método privado: lee todos los logs de todos los archivos
    private List<String> leerTodosLosLogs() throws IOException {
        List<String> todosLosLogs = new ArrayList<>();
        File carpetaLog = new File(carpetaLogs);

        if (!carpetaLog.exists() || !carpetaLog.isDirectory()) {
            return todosLosLogs;
        }

        File[] archivosLog = carpetaLog.listFiles((dir, nombre)
                -> nombre.startsWith(prefijoArchivo) && nombre.endsWith(extensionArchivo));

        if (archivosLog != null) {
            // Ordena archivos por fecha (más reciente primero)
            Arrays.sort(archivosLog, (f1, f2) -> f2.getName().compareTo(f1.getName()));

            for (File archivoLog : archivosLog) {
                try (BufferedReader lector = new BufferedReader(new FileReader(archivoLog))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        todosLosLogs.add(linea);
                    }
                }
            }
        }

        return todosLosLogs;
    }
}
