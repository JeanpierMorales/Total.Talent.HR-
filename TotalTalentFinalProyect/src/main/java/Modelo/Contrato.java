package Modelo;


import java.util.Date;

// --- Clase Abstracta Contrato ---
// Esta es la clase base para los diferentes tipos de contratos (Planilla, Parcial, Locacion).
// Utiliza el patrón de diseño Factory, donde esta es la clase "Producto" abstracta.
// Define todos los atributos y métodos comunes que un contrato debe tener.
public abstract class Contrato {

    // Atributos comunes a todos los tipos de contrato
    private int idContrato;
    private String tipoContrato; // "Planilla", "Parcial", "Locacion"
    
    // Relación: Un Contrato pertenece a un Empleado.
    private Empleado empleado;
    
    private Date fechaInicio;
    private Date fechaFin; // Puede ser null si es indefinido
    private float salarioBase;

    // Atributos opcionales (usados por el Builder)
    private float bonificacion;
    private float descuentoAFP;

    public Contrato() {
        // Constructor por defecto.
        // Se inicializa un Empleado vacío para evitar NullPointerException
        // al ser llamado por el ContratoMysqlRepository (mapearContrato).
        this.empleado = new Empleado();
    }

    // --- Métodos de acceso Getters y Setters ---

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

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public void setBonificacion(float bonificacion) {
        this.bonificacion = bonificacion;
    }

    public void setDescuentoAFP(float descuentoAFP) {
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

    public float getSalarioBase() {
        return salarioBase;
    }

    public float getBonificacion() {
        return bonificacion;
    }

    public float getDescuentoAFP() {
        return descuentoAFP;
    }

    // --- Métodos Adicionales ---

    // Método abstracto para calcular el salario.
    // "abstract" obliga a que las clases hijas (ContratoPlanilla, ContratoParcial, etc.)
    // implementen su propia lógica de cálculo de sueldo.
    public abstract float calcularSueldo();

    // Método para mostrar detalles del contrato (usado en reportes o consola).
    public String mostrarDetalle() {
        return "Contrato " + tipoContrato + " de " + (empleado != null ? empleado.getNombre() : "Sin asignar");
    }

}