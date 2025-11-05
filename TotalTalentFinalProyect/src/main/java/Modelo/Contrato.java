package Modelo;


import java.util.Date;

public abstract class Contrato {

    // Atributos de la clase Contrato base para todos los tipos de contrato
    private int idContrato;
    private String tipoContrato;
    private Empleado empleado;
    private Date fechaInicio;
    private Date fechaFin;
    private double salarioBase;

    // Atributos opcionales para el builder
    private double bonificacion;
    private double descuentoAFP;

    public Contrato() {
        // Constructor por defecto para instanciarlo en el factory
    }

    // Métodos de acceso Getters y Setters
    // Setters
    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public void setBonificacion(Double bonificacion) {
        this.bonificacion = bonificacion;
    }

    public void setDescuentoAFP(Double descuentoAFP) {
        this.descuentoAFP = descuentoAFP;
    }

    // Getters
    public int getIdContrato() {
        return idContrato;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public double getDescuentoAFP() {
        return descuentoAFP;
    }

    // Métodos adicionales según sea necesario
    // Método abstracto para calcular el salario final del contrato por cada tipo 
    public abstract double calcularSueldo();

    // Método para mostrar detalles del contrato
    public String mostrarDetalle() {
        return "Contrato " + tipoContrato + " de " + (empleado != null ? empleado.getNombre() : "Sin asignar");
    }

}
