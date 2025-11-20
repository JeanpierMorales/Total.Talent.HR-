package Modelo;

// --- Entidad Empleado ---
// Esta clase (POJO o Bean) representa la entidad "Empleado".
// Contiene toda la información personal y laboral de una persona en la empresa.
// Es una clase fundamental, ya que es la base sobre la que operan
// los Contratos y los Usuarios.
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
    
    // Atributo que almacena el rol del empleado, usando la enumeración Rol.
    private Rol rol;
    
    // Atributo para el contrato actual o más relevante del empleado.
    // Esta es una relación de agregación: un Empleado "tiene un" Contrato.
    private Contrato contrato;

    // --- Getters y Setters ---
    // Métodos de acceso para todos los atributos.
    
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

    // Métodos de acceso para el rol.
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Método específico para que el empleado actualice su información personal.
    // Es llamado por el Facade (empleadoActualizarDatos) cuando un Empleado
    // edita sus propios datos desde la EmpleadoStrategy.
    public void actualizarDireccion(String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        this.direccion = nuevaDireccion;
        this.correo = nuevoCorreo;
        this.numero = nuevoNumero;
    }

    // Método para obtener un resumen de los datos del empleado (usado en reportes o consola).
    public String obtenerDatosEmpleado() {
        return "ID: " + this.idEmpleado + "\n"
                + "Nombre: " + this.nombre + "\n"
                + "Apellidos: " + this.apellidos + "\n"
                + "Edad: " + this.edad + "\n"
                + "DNI: " + this.dni + "\n"
                + "Número: " + this.numero + "\n"
                + "Correo: " + this.correo + "\n"
                + "Dirección: " + this.direccion + "\n"
                + "Grado de Instrucción: " + this.getGradoInstruccion() + "\n"
                + "Carrera: " + this.getCarrera() + "\n"
                + "Comentarios: " + this.getComentarios() + "\n"
                + "Rol: " + this.rol;
    }

    public void validarDatos() {
        // Lógica de validación (actualmente manejada en el Facade y Validaciones).
    }

    // Getter y Setter para la relación con Contrato.
    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}