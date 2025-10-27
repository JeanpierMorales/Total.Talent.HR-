package Modelo;

import java.time.LocalDateTime;

/**
 * Clase base abstracta para contratos.
 */
public abstract class Contrato {

    protected Integer id;
    protected String tipo;
    protected String duracion;
    protected double monto;
    protected String beneficios;
    protected String campoOpcional1;
    protected String campoOpcional2;
    protected String campoOpcional3;
    protected Integer empleadoId;
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime ultimaModificacion;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getCampoOpcional1() {
        return campoOpcional1;
    }

    public void setCampoOpcional1(String campoOpcional1) {
        this.campoOpcional1 = campoOpcional1;
    }

    public String getCampoOpcional2() {
        return campoOpcional2;
    }

    public void setCampoOpcional2(String campoOpcional2) {
        this.campoOpcional2 = campoOpcional2;
    }

    public String getCampoOpcional3() {
        return campoOpcional3;
    }

    public void setCampoOpcional3(String campoOpcional3) {
        this.campoOpcional3 = campoOpcional3;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(LocalDateTime ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    @Override
    public String toString() {
        return "Contrato{id=" + id + ", tipo='" + tipo + '\'' + ", duracion='" + duracion + '\'' + ", monto=" + monto + '}';
    }

    // Métodos opcionales
    public void setHorarioFlexible(boolean flexible) {
        /* opcional */ }

    public void setBonificacionExtra(double bonificacion) {
        /* opcional */ }

    public void setComentarios(String comentarios) {
        /* opcional */ }
}
