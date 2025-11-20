package Modelo.Factory;

import Modelo.Contrato;

// --- Clase ContratoFactory (Fábrica de Contratos) ---
// Esta clase implementa el patrón de diseño Factory.
// Su única responsabilidad es crear y devolver instancias de las subclases de Contrato
// (ContratoPlanilla, ContratoParcial, ContratoLocacion) basándose en un String.
// Esto centraliza la lógica de instanciación y oculta la complejidad de
// "cuál" clase específica crear al resto del sistema.
public class ContratoFactory {

    // Método estático para crear instancias de contratos según el tipo proporcionado.
    // Al ser 'static', puede ser llamado directamente (ej. ContratoFactory.crearContrato(...))
    // sin necesidad de instanciar un objeto ContratoFactory.
    // Es utilizado por ContratoMysqlRepository (en el método mapearContrato) y por las
    // Strategies (AdminStrategy, ReclutadorStrategy) al agregar un nuevo contrato.
    public static Contrato crearContrato(String tipo) {
        // Convertimos a minúsculas para hacer la comparación robusta (acepta "Planilla", "planilla", etc.)
        String tipoLower = tipo.toLowerCase();
        
        // El switch es el corazón de la fábrica. Decide qué objeto concreto instanciar.
        switch (tipoLower) {
            case "parcial" -> { // Si el tipo es "Parcial"
                // Crea y devuelve una nueva instancia de ContratoParcial.
                return new ContratoParcial();
            }
            case "planilla" -> { // Si el tipo es "Planilla"
                // Crea y devuelve una nueva instancia de ContratoPlanilla.
                return new ContratoPlanilla();
            }
            case "locación", "locacion" -> { // Acepta ambas escrituras para "Locación"
                // Crea y devuelve una nueva instancia de ContratoLocacion.
                return new ContratoLocacion();
            }
            // Si el tipo no coincide con ninguno, se lanza una excepción.
            // Esto previene que se creen objetos nulos o inválidos.
            default ->
                throw new IllegalArgumentException("Tipo de contrato no válido: " + tipo);
        }
    }
}