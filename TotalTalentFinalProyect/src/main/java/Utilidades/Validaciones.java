package Utilidades;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para validaciones comunes en el sistema Total Talent HR.
 */
public class Validaciones {

    // Patrón para validar DNI (8 dígitos)
    private static final Pattern PATRON_DNI = Pattern.compile("\\d{8}");

    // Patrón para validar email
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Patrón para validar teléfono (9 dígitos, empezando con 9)
    private static final Pattern PATRON_TELEFONO = Pattern.compile("9\\d{8}");

    /**
     * Valida si el DNI tiene exactamente 8 dígitos.
     * @param dni El DNI a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarDNI(String dni) {
        if (dni == null) return false;
        return PATRON_DNI.matcher(dni.trim()).matches();
    }

    /**
     * Valida si el email tiene un formato correcto.
     * @param email El email a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarEmail(String email) {
        if (email == null) return false;
        return PATRON_EMAIL.matcher(email.trim()).matches();
    }

    /**
     * Valida si el teléfono tiene 9 dígitos y empieza con 9.
     * @param telefono El teléfono a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        return PATRON_TELEFONO.matcher(telefono.trim()).matches();
    }

    /**
     * Valida si una cadena no está vacía o nula.
     * @param cadena La cadena a validar.
     * @return true si no es nula y no está vacía, false en caso contrario.
     */
    public static boolean validarNoVacio(String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }

    /**
     * Valida si una cadena tiene una longitud mínima.
     * @param cadena La cadena a validar.
     * @param longitudMinima La longitud mínima requerida.
     * @return true si cumple, false en caso contrario.
     */
    public static boolean validarLongitudMinima(String cadena, int longitudMinima) {
        return cadena != null && cadena.trim().length() >= longitudMinima;
    }
}
