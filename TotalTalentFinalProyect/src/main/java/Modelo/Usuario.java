package Modelo;

/**
 * Esta clase representa un usuario dentro de nuestro sistema. Lo usamo para
 * manejar las credenciales de autenticación y referencia al empleado que le
 * toque. Todos los empleados tienen un usuario para acceder al sistema, pero
 * con permisos limitados según el rol que le dimos.
 */
public class Usuario {

    // Atributos de la clase Usuario
    private int idUsuario; // Identificador único del usuario
    private String nombreUsuario; // Nombre de usuario para login que va a ser único
    private String contrasena; // Contraseña encriptada para el login
    private Empleado empleado; // Referencia al empleado correspondiente
    private boolean activo; // Estado del usuario ya sea activo o inactivo

    // Constructor por defecto
    public Usuario() {
        this.activo = true; // Por defecto, usuarios nuevos están activos
    }

    // Constructor con parámetros principales
    public Usuario(String nombreUsuario, String contrasena, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleado = empleado;
        this.activo = true;
    }

    // Métodos de acceos Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Obtiene el rol del empleado asociado a este usuario
    public Rol getRol() {
        return empleado != null ? empleado.getRol() : null;
    }

    //  VErifica que las credenciales del usuario sean correctas
    public boolean verificarCredenciales(String contrasenaIngresada) {
        return this.activo && this.contrasena != null
                && this.contrasena.equals(contrasenaIngresada);
    }

    // Muestra la información del usuario por consola para probar la clase 
    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nombreUsuario='" + nombreUsuario + '\''
                + ", rol=" + getRol()
                + ", activo=" + activo
                + '}';
    }
}
