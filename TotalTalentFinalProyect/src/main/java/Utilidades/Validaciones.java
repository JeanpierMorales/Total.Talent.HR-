package Utilidades;

import java.util.regex.Pattern; // Usamos la clase Pattern para compilar expresiones regulares.

// --- Clase de Utilidades para Validaciones ---
// Esta es una clase 'helper' (de ayuda) que centraliza todas las reglas
// de validación de datos comunes del sistema (DNI, email, teléfono, etc.).
// Al tener métodos estáticos (static), otras clases (como el Facade o las Vistas)
// pueden usarlos directamente sin necesidad de crear una instancia de Validaciones.
// (Ej: Validaciones.validarDNI("...")).
public class Validaciones {

    // --- Expresiones Regulares (Patrones) ---
    // Se definen como 'private static final' porque son constantes
    // que se compilan una sola vez y se reutilizan.

    // Patrón para validar DNI: Acepta exactamente 8 dígitos numéricos.
    // \\d significa "dígito" y {8} significa "exactamente 8 veces".
    private static final Pattern PATRON_DNI = Pattern.compile("\\d{8}");

    // Patrón para validar email: Una expresión regular estándar para formatos de email.
    // Busca [usuario]@[dominio].[extension]
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Patrón para validar teléfono: Acepta un '9' al inicio, seguido de 8 dígitos más.
    // (Total 9 dígitos, empezando obligatoriamente con 9).
    private static final Pattern PATRON_TELEFONO = Pattern.compile("9\\d{8}");

    /**
     * Valida si el DNI tiene exactamente 8 dígitos.
     * Es llamado por el Facade (en guardarEmpleado) y por las Strategies (AdminStrategy).
     * @param dni El DNI a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarDNI(String dni) {
        // Primero, comprueba si el string es nulo para evitar un NullPointerException.
        if (dni == null) return false;
        // .trim() elimina espacios en blanco al inicio o al final.
        // .matcher(dni).matches() compara el DNI contra el patrón PATRON_DNI.
        return PATRON_DNI.matcher(dni.trim()).matches();
    }

    /**
     * Valida si el email tiene un formato correcto.
     * Es llamado por el Facade (en guardarEmpleado) y por las Strategies.
     * @param email El email a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarEmail(String email) {
        // Comprueba si es nulo.
        if (email == null) return false;
        // Compara el email (sin espacios) contra el patrón PATRON_EMAIL.
        return PATRON_EMAIL.matcher(email.trim()).matches();
    }

    /**
     * Valida si el teléfono tiene 9 dígitos y empieza con 9.
     * Es llamado por el Facade (en guardarEmpleado) y por las Strategies.
     * @param telefono El teléfono a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarTelefono(String telefono) {
        // Comprueba si es nulo.
        if (telefono == null) return false;
        // Compara el teléfono (sin espacios) contra el patrón PATRON_TELEFONO.
        return PATRON_TELEFONO.matcher(telefono.trim()).matches();
    }

    /**
     * Valida si una cadena no está vacía o nula.
     * Este es un método de validación crucial usado en casi todas las entradas de texto.
     * Es llamado por el Facade (guardarEmpleado, guardarUsuario) y las Strategies.
     * @param cadena La cadena a validar.
     * @return true si no es nula y no está vacía (ni siquiera solo espacios).
     */
    public static boolean validarNoVacio(String cadena) {
        // 1. Comprueba que no sea nula.
        // 2. Comprueba que, después de quitarle los espacios (trim), no esté vacía (isEmpty).
        return cadena != null && !cadena.trim().isEmpty();
    }

    /**
     * Valida si una cadena tiene una longitud mínima.
     * Usado principalmente por el Facade para validar la longitud de
     * nombres de usuario y contraseñas al crear un Usuario.
     * @param cadena La cadena a validar.
     * @param longitudMinima La longitud mínima requerida.
     * @return true si cumple, false en caso contrario.
     */
    public static boolean validarLongitudMinima(String cadena, int longitudMinima) {
        // 1. Comprueba que no sea nula.
        // 2. Comprueba que la longitud (sin espacios) sea mayor o igual a la mínima.
        return cadena != null && cadena.trim().length() >= longitudMinima;
    }
}