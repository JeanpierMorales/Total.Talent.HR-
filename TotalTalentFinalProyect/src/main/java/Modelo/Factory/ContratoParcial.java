package Modelo.Factory;

import Modelo.Contrato;

public class ContratoParcial extends Contrato {

    // Atributos específicos para el contrato parcial con horas trabajadas y su pago por hora
    private int horasTrabajadas;
    private double pagoPorHora;

    // Constructor para la clase ContratoParcial
    public ContratoParcial() {
        this.setTipoContrato("Parcial");
    }

    // Métodos getter y setter para horasTrabajadas y pagoPorHora
    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getPagoPorHora() {
        return pagoPorHora;
    }

    public void setPagoPorHora(double pagoPorHora) {
        this.pagoPorHora = pagoPorHora;
    }

    // Método para calcular el salario mensual basado en las horas trabajadas y el pago por hora
    @Override
    public double calcularSueldo() {
        return horasTrabajadas * pagoPorHora + getBonificacion(); // No se le quita AFP
    }

}
