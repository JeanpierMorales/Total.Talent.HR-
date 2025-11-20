package TestUnitarios;

import static org.junit.jupiter.api.Assertions.assertFalse; // Importa assertFalse para pruebas negativas
import static org.junit.jupiter.api.Assertions.assertTrue; // Importa assertTrue para pruebas positivas
import org.junit.jupiter.api.Test; // Importa la anotación @Test

import Utilidades.Validaciones; // Importa la clase Validaciones que contiene los métodos a probar

/**
 * Clase de pruebas unitarias para la clase Validaciones. Se prueban todos los
 * métodos de validación con casos válidos e inválidos.
 *
 * @author Omar Morales Silva
 */
public class UnityTest {

    /**
     * Prueba para validarDNI: DNI válido (8 dígitos) e inválido.
     */
    @Test
    public void testValidarDNI() {
        // Caso válido
        assertTrue(Validaciones.validarDNI("12345678"), "DNI válido debería ser aceptado");

        // Caso inválido: menos de 8 dígitos
        assertFalse(Validaciones.validarDNI("1234567"), "DNI con menos de 8 dígitos debería ser rechazado");
    }

    /**
     * Prueba para validarEmail: Email válido e inválido.
     */
    @Test
    public void testValidarEmail() {
        // Caso válido
        assertTrue(Validaciones.validarEmail("omar@gmail.com"), "Email válido debería ser aceptado");

        // Caso inválido: sin dominio
        assertFalse(Validaciones.validarEmail("rosa@"), "Email sin dominio debería ser rechazado");

        // Caso inválido: dominio inválido
        assertFalse(Validaciones.validarEmail("@example"), "Email con dominio inválido debería ser rechazado");

        // Caso inválido: nulo
        assertFalse(Validaciones.validarEmail(null), "Email nulo debería ser rechazado");
    }

    /**
     * Prueba para validarTelefono: Teléfono válido (9 dígitos empezando con 9)
     * e inválido.
     */
    @Test
    public void testValidarTelefono() {
        // Caso válido
        assertTrue(Validaciones.validarTelefono("912345678"), "Teléfono válido debería ser aceptado");

        // Caso inválido: no empieza con 9
        assertFalse(Validaciones.validarTelefono("812345678"), "Teléfono que no empieza con 9 debería ser rechazado");

        // Caso inválido: menos de 9 dígitos
        assertFalse(Validaciones.validarTelefono("91234567"), "Teléfono con menos de 9 dígitos debería ser rechazado");

        // Caso inválido: más de 9 dígitos
        assertFalse(Validaciones.validarTelefono("9123456789"), "Teléfono con más de 9 dígitos debería ser rechazado");

        // Caso inválido: contiene letras
        assertFalse(Validaciones.validarTelefono("91234567a"), "Teléfono con letras debería ser rechazado");

        // Caso inválido: nulo
        assertFalse(Validaciones.validarTelefono(null), "Teléfono nulo debería ser rechazado");
    }

    /**
     * Prueba para validarNoVacio: Cadena no vacía y vacía.
     */
    @Test
    public void testValidarNoVacio() {
        // Caso válido
        assertTrue(Validaciones.validarNoVacio("Hola"), "Cadena no vacía debería ser aceptada");

        // Caso inválido: cadena vacía
        assertFalse(Validaciones.validarNoVacio(""), "Cadena vacía debería ser rechazada");
        // Caso inválido: cadena con solo espacios
        assertFalse(Validaciones.validarNoVacio("   "), "Cadena con solo espacios debería ser rechazada");

        // Caso inválido: nulo
        assertFalse(Validaciones.validarNoVacio(null), "Cadena nula debería ser rechazada");
    }

    /**
     * Prueba para validarLongitudMinima: Cadena con longitud suficiente e
     * insuficiente. Se asume que para contraseñas, una longitud mínima de 8 es
     * válida.
     */
    @Test
    public void testValidarLongitudMinima() {
        // Caso válido: longitud suficiente
        assertTrue(Validaciones.validarLongitudMinima("contraseña", 8), "Cadena con longitud suficiente debería ser aceptada");

        // Caso inválido: longitud insuficiente
        assertFalse(Validaciones.validarLongitudMinima("corta", 8), "Cadena con longitud insuficiente debería ser rechazada");

        // Caso inválido: nulo
        assertFalse(Validaciones.validarLongitudMinima(null, 8), "Cadena nula debería ser rechazada");
    }
}
