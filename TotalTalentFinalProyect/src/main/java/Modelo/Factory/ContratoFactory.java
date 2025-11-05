package Modelo.Factory;

import Modelo.Contrato;

public class ContratoFactory {

    // Método estático para crear instancias de contratos según el tipo proporcionado 
    // Se pasa como  parametro una cadena que indica el tipo de contrato
    public static Contrato crearContrato(String tipo) {
        switch (tipo.toLowerCase()) {
            case "parcial" -> { // Si el tipo es "Parcial", se crea y devuelve una instancia de ContratoParcial
                return new ContratoParcial();
            }
            case "planilla" -> { // Si el tipo es "Planilla", se crea y devuelve una instancia de ContratoPlanilla
                return new ContratoPlanilla();
            }
            case "locación" -> { // Si el tipo es "Locación", se crea y devuelve una instancia de ContratoLocacion
                return new ContratoLocacion();
            }
            // Si el tipo no coincide con ninguno de los casos anteriores, se lanza una excepción
            default ->
                throw new IllegalArgumentException("Tipo de contrato no válido: " + tipo);
        }
    }
}
