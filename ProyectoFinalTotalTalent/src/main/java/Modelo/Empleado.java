package Modelo;

import java.time.LocalDateTime;

// Clase entidad Empleado con atributos basicos y campos de auditoria
public class Empleado {

    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private String password; 
    private String cargo;
    private double salario;
    private LocalDateTime fechaIngreso;
    private LocalDateTime ultimaModificacion;
    private LocalDateTime lastLogin;

    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String correo, String cargo, double salario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.cargo = cargo;
        this.salario = salario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(LocalDateTime ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "Empleado{id=" + id + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' + ", correo='" + correo + '\'' + ", cargo='" + cargo + '\'' + ", salario=" + salario + '}';
    }
}
