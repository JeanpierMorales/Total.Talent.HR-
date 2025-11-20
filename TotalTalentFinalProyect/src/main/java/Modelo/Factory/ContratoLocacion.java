package Modelo.Factory;

import Modelo.Contrato;

// --- Clase ContratoLocacion (Producto Concreto) ---
// Esta es la tercera subclase de Contrato (herencia).
// Representa un contrato por "Locación de Servicios" (ej. por proyectos).
// Es el tercer "producto" que el ContratoFactory puede crear.
public class ContratoLocacion extends Contrato {

    // Atributos específicos para el contrato de locación de servicios.
    private int numeroProyectos;
    private float montoPorProyecto;

    // Constructor para la clase ContratoLocacion.
    public ContratoLocacion() {
        // Llama al método heredado para auto-asignarse su tipo.
        this.setTipoContrato("Locación");
    }

    // Métodos getter y setter para los atributos específicos.
    public int getNumeroProyectos() {
        return numeroProyectos;
    }

    public void setNumeroProyectos(int numeroProyectos) {
        this.numeroProyectos = numeroProyectos;
    }

    public float getMontoPorProyecto() {
        return montoPorProyecto;
    }

    public void setMontoPorProyecto(float montoPorProyecto) {
        this.montoPorProyecto = montoPorProyecto;
    }

    // --- Implementación del Método Abstracto ---
    // Sobrescribe el método abstracto calcularSueldo heredado de Contrato.
    // Implementa la lógica de cálculo específica para un contrato por locación.
    @Override
    public float calcularSueldo() {
        // La fórmula es (proyectos * monto_por_proyecto) + bonificación.
        // Al igual que ContratoParcial, no utiliza salario base ni AFP.
        return (numeroProyectos * montoPorProyecto) + getBonificacion();
    }

}