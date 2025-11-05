package Modelo;


public class Empleado {

    // Atributos de la clase Empleado 
    private int idEmpleado;
    private String nombre;
    private String apellidos;
    private int edad;
    private String dni;
    private String numero;
    private String correo;
    private String direccion;
    private String gradoInstruccion;
    private String carrera;
    private String comentarios;
    // Rol del empleado dentro del sistema
    private Rol rol; 

    // Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGradoInstruccion() {
        return gradoInstruccion;
    }

    public void setGradoInstruccion(String gradoInstruccion) {
        this.gradoInstruccion = gradoInstruccion;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    // Métodos de acceso para el rol 
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Métodos para que el empleado actualice información personal con direccion, correo o numero
    public void actualizarDireccion(String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        this.direccion = nuevaDireccion;
        this.correo = nuevoCorreo;
        this.numero = nuevoNumero;
    }

    // Método para obtener los datos del empleado 
    public String obtenerDatosEmpleado() {
        return "ID: " + this.idEmpleado + "\n"
                + "Nombre: " + this.nombre + "\n"
                + "Apellidos: " + this.apellidos + "\n"
                + "Edad: " + this.edad + "\n"
                + "DNI: " + this.dni + "\n"
                + "Número: " + this.numero + "\n"
                + "Correo: " + this.correo + "\n"
                + "Dirección: " + this.direccion + "\n"
                + "Grado de Instrucción: " + this.gradoInstruccion + "\n"
                + "Carrera: " + this.carrera + "\n"
                + "Comentarios: " + this.comentarios + "\n"
                + "Rol: " + this.rol;
    }
    
    public void validarDatos() {
        // Lógica de validación del empleado
    }

}

