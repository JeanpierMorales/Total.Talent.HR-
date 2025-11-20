package Modelo.Factory;

import Modelo.Contrato;

// --- Clase ContratoParcial (Producto Concreto) ---
// Esta es otra subclase de Contrato (herencia).
// Representa un contrato de tipo "Parcial" o por horas.
// Es otro "producto" que el ContratoFactory puede crear.
public class ContratoParcial extends Contrato {

    // Atributos específicos para el contrato parcial.
    private int horasTrabajadas;
    private float pagoPorHora;

    // Constructor para la clase ContratoParcial.
    public ContratoParcial() {
        // Llama al método heredado para auto-asignarse su tipo.
        this.setTipoContrato("Parcial");
    }

    // Métodos getter y setter para los atributos específicos.
    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public float getPagoPorHora() {
        return pagoPorHora;
    }

    public void setPagoPorHora(float pagoPorHora) {
        this.pagoPorHora = pagoPorHora;
    }

    // --- Implementación del Método Abstracto ---
    // Sobrescribe el método abstracto calcularSueldo heredado de Contrato.
    // Implementa la lógica de cálculo específica para un contrato parcial.
    @Override
    public float calcularSueldo() {
        // La fórmula es (horas * pago_por_hora) + bonificación.
        // Nótese que este tipo de contrato no usa getSalarioBase()
        // ni resta el descuento de AFP en su cálculo.
        return horasTrabajadas * pagoPorHora + getBonificacion();
    }

}