package Modelo.Factory;

import Modelo.Contrato;

public class ContratoLocacion extends Contrato {

    // Atributos específicos para el contrato de locación de servicios
    private int numeroProyectos;
    private double montoPorProyecto;

    // Constructor para la clase ContratoLocacion desde Contrato
    public ContratoLocacion() {
        this.setTipoContrato("Locación");
    }

    // métodos getter y setter para numeroProyectos y montoPorProyecto
    public int getNumeroProyectos() {
        return numeroProyectos;
    }

    public void setNumeroProyectos(int numeroProyectos) {
        this.numeroProyectos = numeroProyectos;
    }

    public double getMontoPorProyecto() {
        return montoPorProyecto;
    }

    public void setMontoPorProyecto(double montoPorProyecto) {
        this.montoPorProyecto = montoPorProyecto;
    }

    // Método para calcular el sueldo basado en el número de proyectos y el monto por proyecto
    @Override
    public double calcularSueldo() {
        return (numeroProyectos * montoPorProyecto) + getBonificacion();
    }

}
