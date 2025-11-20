package Modelo.Factory;

import Modelo.Contrato;


// --- Clase ContratoPlanilla (Producto Concreto) ---
// Esta es una subclase de Contrato (herencia). Representa un tipo específico
// de contrato, en este caso, un empleado en planilla.
// Es uno de los "productos" que el ContratoFactory puede crear.
public class ContratoPlanilla extends Contrato {

    // Atributo específico para el contrato de planilla.
    private float horasExtras;

    // Métodos getter y setter para el atributo específico.
    public float getHorasExtras() {
        return horasExtras;
    }
    
    public void setHorasExtras(float horasExtras) {
        this.horasExtras = horasExtras;
    }

    // Constructor de la clase ContratoPlanilla.
    public ContratoPlanilla(){
        // Llama al método setTipoContrato (heredado de Contrato)
        // para auto-asignarse su tipo.
        this.setTipoContrato("Planilla");
    }

    // --- Implementación del Método Abstracto ---
    // Sobrescribe el método abstracto calcularSueldo heredado de Contrato.
    // Cada tipo de contrato (Planilla, Parcial, Locacion) tiene su propia
    // fórmula para calcular el sueldo.
    @Override
    public float calcularSueldo() {
        // Obtiene los valores base (heredados) usando getSalarioBase(), getBonificacion(), etc.
        float total = getSalarioBase() + getBonificacion();
        // Añade el cálculo específico de este tipo de contrato (pago de horas extras).
        total += (horasExtras * 15); // Asume un pago fijo de 15 por hora extra.
        // Aplica el descuento de AFP (solo planilla lo aplica).
        total -= getDescuentoAFP();
        return total;
    }

}