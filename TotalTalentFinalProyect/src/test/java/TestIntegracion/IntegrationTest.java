package TestIntegracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Modelo.Contrato;
import Modelo.Factory.ContratoFactory;
import Modelo.Factory.ContratoLocacion;
import Modelo.Factory.ContratoParcial;
import Modelo.Factory.ContratoPlanilla;

/**
 * Clase de pruebas de integración para el patrón Factory de contratos. Se
 * prueban la creación de contratos y sus cálculos de sueldo.
 *
 * @author Omar Morales Silva
 */
public class IntegrationTest {

    /**
     * Prueba de integración para crear un contrato Parcial y calcular su
     * sueldo.
     */
    @Test
    public void testCrearContratoParcial() {
        // Crear contrato usando el factory
        Contrato contrato = ContratoFactory.crearContrato("parcial");

        // Verificar que es instancia de ContratoParcial
        assertTrue(contrato instanceof ContratoParcial, "Debería crear una instancia de ContratoParcial");

        // Configurar atributos específicos
        ContratoParcial parcial = (ContratoParcial) contrato;
        parcial.setHorasTrabajadas(160);
        parcial.setPagoPorHora(10.0);
        parcial.setBonificacion(50.0);

        // Calcular sueldo
        double sueldo = contrato.calcularSueldo();

        // Verificar cálculo: 160 * 10 + 50 = 1650
        assertEquals(1650.0, sueldo, "El sueldo calculado debería ser correcto");
    }

    /**
     * Prueba de integración para crear un contrato Planilla y calcular su
     * sueldo.
     */
    @Test
    public void testCrearContratoPlanilla() {
        // Crear contrato usando el factory
        Contrato contrato = ContratoFactory.crearContrato("planilla");

        // Verificar que es instancia de ContratoPlanilla
        assertTrue(contrato instanceof ContratoPlanilla, "Debería crear una instancia de ContratoPlanilla");

        // Configurar atributos
        ContratoPlanilla planilla = (ContratoPlanilla) contrato;
        planilla.setSalarioBase(1000.0);
        planilla.setBonificacion(100.0);
        planilla.setDescuentoAFP(50.0);
        planilla.setHorasExtras(10.0);

        // Calcular sueldo: 1000 + 100 + (10*15) - 50 = 1200
        double sueldo = contrato.calcularSueldo();
        assertEquals(1200.0, sueldo, "El sueldo calculado debería ser correcto");
    }

    /**
     * Prueba de integración para crear un contrato Locación y calcular su
     * sueldo.
     */
    @Test
    public void testCrearContratoLocacion() {
        // Crear contrato usando el factory
        Contrato contrato = ContratoFactory.crearContrato("locacion");

        // Verificar que es instancia de ContratoLocacion
        assertTrue(contrato instanceof ContratoLocacion, "Debería crear una instancia de ContratoLocacion");

        // Configurar atributos específicos
        ContratoLocacion locacion = (ContratoLocacion) contrato;
        locacion.setNumeroProyectos(5);
        locacion.setMontoPorProyecto(200.0);
        locacion.setBonificacion(100.0);

        // Calcular sueldo: 5 * 200 + 100 = 1100
        double sueldo = contrato.calcularSueldo();
        assertEquals(1100.0, sueldo, "El sueldo calculado debería ser correcto");
    }
}
