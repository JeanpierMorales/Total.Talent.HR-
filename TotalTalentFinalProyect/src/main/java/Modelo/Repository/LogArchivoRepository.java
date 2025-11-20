package Modelo.Repository;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// --- Implementación del Repositorio de Logs (Archivos) ---
// Esta clase implementa la interfaz LogRepository.
// Es la implementación concreta que cumple el contrato guardando los logs
// directamente en archivos de texto (.txt) en el disco duro, en lugar de
// una base de datos.
public class LogArchivoRepository implements LogRepository {

    // Logger de Java para registrar errores internos de esta propia clase.
    private static final Logger logger = Logger.getLogger(LogArchivoRepository.class.getName());

    // Carpeta donde se guardarán los archivos de log.
    private static final String carpetaLogs = "logs";
    // Prefijo para los nombres de los archivos.
    private static final String prefijoArchivo = "log_";
    // Extensión de los archivos.
    private static final String extensionArchivo = ".txt";

    // Formato estándar para la fecha y hora que aparecerá en cada línea del log.
    private final SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Constructor: se ejecuta cuando el Facade crea esta instancia.
    // Su trabajo es asegurarse de que la carpeta "logs" exista.
    public LogArchivoRepository() {
        try {
            // Intenta crear el directorio "logs" (si ya existe, no hace nada).
            Files.createDirectories(Paths.get(carpetaLogs));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creando carpeta de logs: " + e.getMessage());
        }
    }

    // Implementación del método de la interfaz.
    // Formatea el string y lo pasa al método privado escribirEnLog.
    @Override
    public void registrarAccion(String usuario, String accion, String descripcion) {
        String entradaLog = String.format("[%s] USUARIO: %s | ACCION: %s | DESCRIPCION: %s",
                formatoFechaHora.format(new Date()), usuario, accion, descripcion);
        escribirEnLog(entradaLog);
    }

    // Implementación del método de la interfaz.
    @Override
    public void registrarLoginExitoso(String usuario) {
        String entradaLog = String.format("[%s] LOGIN EXITOSO - USUARIO: %s",
                formatoFechaHora.format(new Date()), usuario);
        escribirEnLog(entradaLog);
    }

    // Implementación del método de la interfaz.
    @Override
    public void registrarLoginFallido(String usuario, String razon) {
        String entradaLog = String.format("[%s] LOGIN FALLIDO - USUARIO: %s | RAZON: %s",
                formatoFechaHora.format(new Date()), usuario, razon);
        escribirEnLog(entradaLog);
    }

    // Implementación del método de la interfaz.
    @Override
    public void registrarCreacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] CREACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Implementación del método de la interfaz.
    @Override
    public void registrarActualizacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] ACTUALIZACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Implementación del método de la interfaz.
    @Override
    public void registrarEliminacion(String usuario, String tabla, int idRegistro) {
        String entradaLog = String.format("[%s] ELIMINACION - USUARIO: %s | TABLA: %s | ID: %d",
                formatoFechaHora.format(new Date()), usuario, tabla, idRegistro);
        escribirEnLog(entradaLog);
    }

    // Obtiene todos los logs de un usuario específico.
    @Override
    public List<String> obtenerLogsPorUsuario(String usuario) {
        List<String> logsDelUsuario = new ArrayList<>();
        try {
            // Llama al método privado que lee *todos* los logs.
            List<String> todosLosLogs = leerTodosLosLogs();
            // Filtra la lista en memoria.
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

    // Obtiene todos los logs de una fecha específica.
    @Override
    public List<String> obtenerLogsPorFecha(Date fecha) {
        List<String> logsDeLaFecha = new ArrayList<>();
        SimpleDateFormat formatoFechaArchivo = new SimpleDateFormat("yyyy-MM-dd");
        String fechaComoTexto = formatoFechaArchivo.format(fecha);

        try {
            List<String> todosLosLogs = leerTodosLosLogs();
            // Filtra la lista buscando el texto de la fecha.
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

    // Obtiene todos los logs disponibles.
    @Override
    public List<String> obtenerTodosLosLogs() {
        try {
            return leerTodosLosLogs(); // Devuelve la lista completa.
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error obteniendo todos los logs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Método privado que escribe una entrada en el archivo de log del día actual.
    private void escribirEnLog(String entradaLog) {
        // Genera el nombre del archivo basado en la fecha actual.
        // Ej: "logs/log_2025-11-17.txt"
        String nombreArchivo = carpetaLogs + File.separator
                + prefijoArchivo
                + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                + extensionArchivo;

        // Usa un try-with-resources para asegurar que el BufferedWriter se cierre.
        // FileWriter(nombreArchivo, true) abre el archivo en modo "append" (agregar al final).
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            escritor.write(entradaLog); // Escribe la línea de log.
            escritor.newLine(); // Añade un salto de línea.
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error escribiendo en archivo de log: " + e.getMessage());
        }
    }

    // Método privado: lee todos los logs de todos los archivos.
    private List<String> leerTodosLosLogs() throws IOException {
        List<String> todosLosLogs = new ArrayList<>();
        File carpetaLog = new File(carpetaLogs);

        if (!carpetaLog.exists() || !carpetaLog.isDirectory()) {
            return todosLosLogs;
        }

        // Filtra los archivos en la carpeta para obtener solo los .txt que empiezan con "log_".
        File[] archivosLog = carpetaLog.listFiles((dir, nombre)
                -> nombre.startsWith(prefijoArchivo) && nombre.endsWith(extensionArchivo));

        if (archivosLog != null) {
            // Ordena los archivos por nombre en orden descendente (los más recientes primero).
            Arrays.sort(archivosLog, (f1, f2) -> f2.getName().compareTo(f1.getName()));

            // Itera sobre cada archivo de log encontrado.
            for (File archivoLog : archivosLog) {
                // Usa try-with-resources para el BufferedReader.
                try (BufferedReader lector = new BufferedReader(new FileReader(archivoLog))) {
                    String linea;
                    // Lee cada línea del archivo y la añade a la lista.
                    while ((linea = lector.readLine()) != null) {
                        todosLosLogs.add(linea);
                    }
                }
            }
        }

        return todosLosLogs;
    }
}