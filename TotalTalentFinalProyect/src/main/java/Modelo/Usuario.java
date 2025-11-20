package Modelo;

/**
 * Esta clase representa un usuario dentro de nuestro sistema. Lo usamo para
 * manejar las credenciales de autenticación y referencia al empleado que le
 * toque. Todos los empleados tienen un usuario para acceder al sistema, pero
 * con permisos limitados según el rol que le dimos.
 */
// --- Entidad Usuario ---
// Esta clase (POJO) maneja la autenticación y el acceso al sistema.
// Es la entidad que "inicia sesión". Está directamente vinculada a un Empleado.
public class Usuario {

    // Atributos de la clase Usuario
    private int idUsuario; // Identificador único del usuario
    private String nombreUsuario; // Nombre de usuario para login (debe ser único)
    private String contrasena; // Contraseña (idealmente debería guardarse encriptada)
    
    // Relación clave: Un Usuario "es" un Empleado.
    // El Empleado asociado contiene el Rol y los datos personales.
    private Empleado empleado; 
    
    private boolean activo; // Estado del usuario (para habilitar o deshabilitar cuentas)

    // Constructor por defecto
    public Usuario() {
        this.activo = true; // Por defecto, los usuarios nuevos están activos.
    }

    // Constructor con parámetros principales
    public Usuario(String nombreUsuario, String contrasena, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleado = empleado;
        this.activo = true;
    }

    // --- Métodos de acceso Getters y Setters ---
    
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

    // --- Lógica de Negocio ---

    // Obtiene el rol del empleado asociado a este usuario.
    // Esto es un ejemplo de "delegación": el Usuario no tiene rol,
    // le pregunta a su Empleado cuál es su rol.
    // Usado por el Facade (tienePermiso) y las Vistas (Strategies) para
    // determinar qué puede hacer el usuario.
    public Rol getRol() {
        return empleado != null ? empleado.getRol() : null;
    }

    // Verifica que las credenciales del usuario sean correctas.
    // Compara la contraseña ingresada con la almacenada.
    // Es llamado por el UsuarioMysqlRepository y el Facade (login).
    public boolean verificarCredenciales(String contrasenaIngresada) {
        return this.activo && this.contrasena != null
                && this.contrasena.equals(contrasenaIngresada);
    }

    // Muestra la información del usuario por consola (para pruebas y logs).
    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nombreUsuario='" + nombreUsuario + '\''
                + ", rol=" + getRol() // Llama al método getRol()
                + ", activo=" + activo
                + '}';
    }
}