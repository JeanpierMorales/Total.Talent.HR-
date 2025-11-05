package Modelo.Factory;

import Modelo.Contrato;



public class ContratoPlanilla extends Contrato {

    // Atributo de horas extra para el contrato de planilla
    private double horasExtras;

    // Constructor de la clase ContratoPlanilla
    public double getHorasExtras() {
        return horasExtras;
    }
    
    public ContratoPlanilla(){
        this.setTipoContrato("Planilla");
    }

    // Métodos getter y setter de la clase
    public void setHorasExtras(double horasExtras) {
        this.horasExtras = horasExtras;
    }

    @Override
    public double calcularSueldo() {
        // Calcula el total de sueldo aplicando las horas extras y también los descuentos pro afp
        double total = getSalarioBase() + getBonificacion();
        total += (horasExtras * 15);
        total -= getDescuentoAFP();
        return total;
    }

}
