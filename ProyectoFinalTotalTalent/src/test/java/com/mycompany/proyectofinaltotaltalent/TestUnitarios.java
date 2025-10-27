package com.mycompany.proyectofinaltotaltalent;

import Modelo.*;                     // Importa todas las clases del paquete Modelo (Empleado, Contrato, etc.)
import Utils.ValidationUtils;        // Importa las utilidades de validación (correo, password, etc.)
import org.junit.jupiter.api.Test;   // Importa la anotación @Test de JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa los métodos de aserción (assertTrue, assertFalse, etc.)

public class TestUnitarios {

    //  Verifica que un correo válido sea reconocido como correcto
    @Test
    void validarCorreoCorrecto() {
        // Debe devolver true porque termina en @totaltalent.com
        assertTrue(ValidationUtils.isValidEmail("omar.morales@totaltalent.com"));
    }

    //  Verifica que un correo con dominio incorrecto sea rechazado
    @Test
    void validarCorreoIncorrecto() {
        // Debe devolver false porque termina en @totaltalent.pe
        assertFalse(ValidationUtils.isValidEmail("omar.morales@totaltalent.pe"));
    }

    //  Comprueba que una contraseña con 8 caracteres sea válida
    @Test
    void validarPasswordMinimaCorrecta() {
        // Debe ser true porque tiene la longitud mínima permitida (8)
        assertTrue(ValidationUtils.isValidPassword("124p5678"));
    }

    //  Comprueba que una contraseña demasiado corta sea rechazada
    @Test
    void validarPasswordCortaIncorrecta() {
        // Debe ser false porque tiene menos de 8 caracteres
        assertFalse(ValidationUtils.isValidPassword("12345"));
    }

    //  Verifica que el salario de un empleado sea mayor que cero
    @Test
    void empleadoSalarioMayorQueCero() {
        // Se crea un empleado y se le asigna un salario positivo
        Empleado e = new Empleado();
        e.setSalario(10000);
        // Se comprueba que el salario sea mayor a 0
        assertTrue(e.getSalario() > 0);
    }

    // Comprueba que el ID del empleado se asigne correctamente al contrato
    @Test
    void contratoSeteaEmpleadoId() {
        // Se crea un contrato y se le asigna un ID de empleado
        Contrato c = new Contrato() {
        };
        c.setEmpleadoId(5);
        // Se verifica que el valor se haya guardado correctamente
        assertEquals(5, c.getEmpleadoId());
    }
}
