package Modelo.Facade;

import Modelo.DatabaseConnection;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Repository.ContratoMysqlRepository;
import Modelo.Repository.ContratoRepository;
import Modelo.Repository.EmpleadoMysqlRepository;
import Modelo.Repository.EmpleadoRepository;
import Modelo.Repository.LogArchivoRepository;
import Modelo.Repository.LogRepository;
import Modelo.Repository.UsuarioMysqlRepository;
import Modelo.Repository.UsuarioRepository;
import Modelo.Rol;
import Modelo.Usuario;
import Utilidades.Validaciones; // Importamos la clase de utilidades para validar datos.
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// --- Clase Facade (Fachada) ---
// Este es el único punto de entrada al paquete Modelo desde el Controlador.
// Aplica el patrón de diseño Facade: simplifica una interfaz compleja.
// En lugar de que el Controlador tenga que instanciar y gestionar todos
// los repositorios (UsuarioRepository, EmpleadoRepository, etc.),
// simplemente llama a métodos de esta clase (ej. facade.login, facade.guardarEmpleado).
public class Facade {

    // --- Repositorios ---
    // Atributos para cada repositorio que manejará el acceso a datos.
    private EmpleadoRepository empleadoRepo;
    private UsuarioRepository usuarioRepo;
    private ContratoRepository contratoRepo;
    private LogRepository logRepo; // Repositorio para la bitácora (logs).
    private Connection connection; // Objeto de conexión a la BD.

    // Usuario que ha iniciado sesión en el sistema.
    private Usuario usuarioActual;

    // Constructor del Facade.
    // Se encarga de inicializar la conexión a la base de datos y
    // crear las instancias de todos los repositorios, inyectándoles la conexión.
    public Facade() {
        try {
            // Pide la conexión a la clase estática DatabaseConnection.
            this.connection = DatabaseConnection.getConnection();
            
            // Instancia los repositorios concretos (MySQL) y les pasa la conexión.
            this.empleadoRepo = new EmpleadoMysqlRepository(connection);
            this.usuarioRepo = new UsuarioMysqlRepository(connection);
            this.contratoRepo = new ContratoMysqlRepository(connection);
            
            // Instancia el repositorio de logs (que escribe en archivos).
            this.logRepo = new LogArchivoRepository();
        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando Facade: " + e.getMessage());
        }
    }

    // Método para autenticar un usuario. Es llamado por el Controlador.
    public boolean login(String nombreUsuario, String contrasena) {
        try {
            // Llama al repositorio de usuarios para verificar las credenciales.
            Usuario usuario = usuarioRepo.verificarCredenciales(nombreUsuario, contrasena);
            
            // Si el usuario existe y está activo...
            if (usuario != null && usuario.isActivo()) {
                this.usuarioActual = usuario; // Almacena al usuario en la sesión del Facade.
                logRepo.registrarLoginExitoso(nombreUsuario); // Registra el éxito en el log.
                return true;
            } else {
                logRepo.registrarLoginFallido(nombreUsuario, "Credenciales inválidas"); // Registra el fallo.
                return false;
            }
        } catch (Exception e) {
            logRepo.registrarLoginFallido(nombreUsuario, "Error del sistema: " + e.getMessage());
            return false;
        }
    }

    // Método para cerrar la sesión.
    public void logout() {
        if (usuarioActual != null) {
            // Registra la acción de logout en el log.
            logRepo.registrarAccion(usuarioActual.getNombreUsuario(), "LOGOUT", "Cierre de sesión");
            this.usuarioActual = null; // Elimina al usuario de la sesión actual.
        }
    }

    // Devuelve el usuario que está actualmente logueado.
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Verifica si hay una sesión activa.
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }

    // --- Verificación de Permisos ---
    // Método interno para centralizar la lógica de permisos.
    // Comprueba si el usuarioActual tiene el rol necesario para una acción.
    public boolean tienePermiso(String permiso) {
        if (usuarioActual == null) {
            return false;
        }

        Rol rol = usuarioActual.getRol(); // Obtiene el rol del usuario (ADMINISTRADOR, GERENTE, etc.).
        switch (permiso.toLowerCase()) {
            case "admin":
                return rol == Rol.ADMINISTRADOR;
            case "reclutador":
                // Reclutador, Gerente y Admin pueden hacer acciones de reclutador.
                return rol == Rol.RECLUTADOR || rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            case "gerente":
                // Gerente y Admin pueden hacer acciones de gerente.
                return rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            case "empleado":
                // Todos los roles son, en esencia, empleados.
                return rol == Rol.EMPLEADO || rol == Rol.RECLUTADOR || rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            default:
                return false;
        }
    }

    // ===== OPERACIONES DE EMPLEADOS =====
    // Los siguientes métodos son llamados por el Controlador.
    // El Facade primero verifica los permisos y luego llama al repositorio correspondiente.

    // Guarda un nuevo empleado.
    public void guardarEmpleado(Empleado empleado) {
        verificarPermiso("reclutador"); // Solo Reclutador o superior pueden guardar.
        
        // --- Validaciones de negocio ---
        // Usa la clase Validaciones para asegurar la integridad de los datos.
        if (!Validaciones.validarDNI(empleado.getDni())) {
            throw new RuntimeException("DNI inválido: debe tener 8 dígitos");
        }
        if (!Validaciones.validarEmail(empleado.getCorreo())) {
            throw new RuntimeException("Email inválido");
        }
        if (!Validaciones.validarTelefono(empleado.getNumero())) {
            throw new RuntimeException("Teléfono inválido: debe tener 9 dígitos empezando con 9");
        }
        if (!Validaciones.validarNoVacio(empleado.getNombre()) || !Validaciones.validarNoVacio(empleado.getApellidos())) {
            throw new RuntimeException("Nombre y apellidos son obligatorios");
        }
        // Verifica que el DNI no esté duplicado usando el repositorio.
        if (empleadoRepo.existeDni(empleado.getDni())) {
            throw new RuntimeException("Ya existe un empleado con el DNI: " + empleado.getDni());
        }
        
        // Si todo es válido, llama al repositorio para guardar.
        empleadoRepo.guardar(empleado);
        
        // Registra la creación en el log.
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "empleado", empleado.getIdEmpleado());
    }

    // Actualiza un empleado existente.
    public void actualizarEmpleado(Empleado empleado) {
        // Regla especial: un Empleado puede actualizar sus propios datos.
        if (usuarioActual.getRol() == Rol.EMPLEADO) {
            if (usuarioActual.getEmpleado() == null || usuarioActual.getEmpleado().getIdEmpleado() != empleado.getIdEmpleado()) {
                throw new RuntimeException("Los empleados solo pueden actualizar sus propios datos");
            }
        } else {
            // Para otros roles, se requiere permiso de "reclutador".
            verificarPermiso("reclutador");
        }
        
        // Llama al repositorio para actualizar.
        empleadoRepo.actualizar(empleado);
        
        // Registra la actualización en el log.
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "empleado", empleado.getIdEmpleado());
    }

    // Elimina un empleado (solo Admin).
    public void eliminarEmpleado(int idEmpleado) {
        verificarPermiso("admin"); // Solo el Administrador puede eliminar.
        empleadoRepo.eliminar(idEmpleado);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "empleado", idEmpleado);
    }

    // Busca un empleado por ID (permiso básico "empleado").
    public Empleado buscarEmpleadoPorId(int idEmpleado) {
        verificarPermiso("empleado");
        return empleadoRepo.buscarPorId(idEmpleado);
    }

    // Busca un empleado por DNI (permiso "reclutador").
    public Empleado buscarEmpleadoPorDni(String dni) {
        verificarPermiso("reclutador");
        return empleadoRepo.buscarPorDni(dni);
    }

    // Obtiene todos los empleados (permiso "reclutador").
    public List<Empleado> obtenerTodosEmpleados() {
        verificarPermiso("reclutador");
        return empleadoRepo.obtenerTodos();
    }

    // Busca empleados por nombre (permiso "reclutador").
    public List<Empleado> buscarEmpleadosPorNombre(String nombre) {
        verificarPermiso("reclutador");
        return empleadoRepo.buscarPorNombre(nombre);
    }

    // Busca empleados por rol (permiso "admin").
    public List<Empleado> buscarEmpleadosPorRol(String rol) {
        verificarPermiso("admin");
        return empleadoRepo.buscarPorRol(rol);
    }

    // ===== OPERACIONES DE CONTRATOS =====

    // Guarda un nuevo contrato.
    public void guardarContrato(Contrato contrato) {
        verificarPermiso("reclutador");
        contratoRepo.guardar(contrato);
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "contrato", contrato.getIdContrato());
    }

    // Actualiza un contrato.
    public void actualizarContrato(Contrato contrato) {
        verificarPermiso("reclutador");
        contratoRepo.actualizar(contrato);
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "contrato", contrato.getIdContrato());
    }

    // Elimina un contrato (solo Admin).
    public void eliminarContrato(int idContrato) {
        verificarPermiso("admin");
        contratoRepo.eliminar(idContrato);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "contrato", idContrato);
    }

    // Busca un contrato por ID.
    public Contrato buscarContratoPorId(int idContrato) {
        verificarPermiso("empleado"); // Permiso básico.
        return contratoRepo.buscarPorId(idContrato);
    }

    // Busca contratos por ID de empleado.
    public List<Contrato> buscarContratosPorEmpleado(int idEmpleado) {
        verificarPermiso("reclutador");
        return contratoRepo.buscarPorEmpleado(idEmpleado);
    }

    // Obtiene todos los contratos.
    public List<Contrato> obtenerTodosContratos() {
        verificarPermiso("reclutador");
        return contratoRepo.obtenerTodos();
    }

    // Busca contratos por tipo (permiso "gerente").
    public List<Contrato> buscarContratosPorTipo(String tipoContrato) {
        verificarPermiso("gerente");
        return contratoRepo.buscarPorTipo(tipoContrato);
    }

    // Busca contratos activos (permiso "gerente").
    public List<Contrato> buscarContratosActivos() {
        verificarPermiso("gerente");
        return contratoRepo.buscarContratosActivos();
    }

    // Busca contratos por rango de fechas (permiso "gerente").
    public List<Contrato> buscarContratosPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        verificarPermiso("gerente");
        return contratoRepo.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    // ===== OPERACIONES DE USUARIOS =====

    // Guarda un nuevo usuario (solo Admin).
    public void guardarUsuario(Usuario usuario) {
        verificarPermiso("admin");
        
        // --- Validaciones de negocio ---
        if (!Validaciones.validarNoVacio(usuario.getNombreUsuario()) || !Validaciones.validarLongitudMinima(usuario.getNombreUsuario(), 3)) {
            throw new RuntimeException("Nombre de usuario inválido: debe tener al menos 3 caracteres");
        }
        if (!Validaciones.validarNoVacio(usuario.getContrasena()) || !Validaciones.validarLongitudMinima(usuario.getContrasena(), 6)) {
            throw new RuntimeException("Contraseña inválida: debe tener al menos 6 caracteres");
        }
        // Verifica que el nombre de usuario no esté duplicado.
        if (usuarioRepo.existeNombreUsuario(usuario.getNombreUsuario())) {
            throw new RuntimeException("Ya existe un usuario con el nombre: " + usuario.getNombreUsuario());
        }
        
        usuarioRepo.guardar(usuario);
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "usuario", usuario.getIdUsuario());
    }

    // Actualiza un usuario (solo Admin).
    public void actualizarUsuario(Usuario usuario) {
        verificarPermiso("admin");
        usuarioRepo.actualizar(usuario);
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "usuario", usuario.getIdUsuario());
    }

    // Elimina un usuario (solo Admin).
    public void eliminarUsuario(int idUsuario) {
        verificarPermiso("admin");
        usuarioRepo.eliminar(idUsuario);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "usuario", idUsuario);
    }

    // Busca un usuario por ID (solo Admin).
    public Usuario buscarUsuarioPorId(int idUsuario) {
        verificarPermiso("admin");
        return usuarioRepo.buscarPorId(idUsuario);
    }

    // Busca un usuario por nombre (solo Admin).
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        verificarPermiso("admin");
        return usuarioRepo.buscarPorNombreUsuario(nombreUsuario);
    }

    // Obtiene todos los usuarios (solo Admin).
    public List<Usuario> obtenerTodosUsuarios() {
        verificarPermiso("admin");
        return usuarioRepo.obtenerTodos();
    }

    // ===== OPERACIONES DE REPORTES =====

    // Genera un reporte de empleados (permiso "gerente").
    public String generarReporteEmpleados() {
        verificarPermiso("gerente");
        List<Empleado> empleados = empleadoRepo.obtenerTodos();
        StringBuilder reporte = new StringBuilder(); // Usamos StringBuilder para eficiencia.
        reporte.append("=== REPORTE DE EMPLEADOS ===\n");
        reporte.append("Total de empleados: ").append(empleados.size()).append("\n\n");

        for (Empleado emp : empleados) {
            reporte.append("ID: ").append(emp.getIdEmpleado()).append("\n");
            reporte.append("Nombre: ").append(emp.getNombre()).append(" ").append(emp.getApellidos()).append("\n");
            reporte.append("DNI: ").append(emp.getDni()).append("\n");
            reporte.append("Rol: ").append(emp.getRol()).append("\n");
            reporte.append("------------------------\n");
        }

        // Registra la acción en el log.
        logRepo.registrarAccion(usuarioActual.getNombreUsuario(), "REPORTE", "Generación de reporte de empleados");
        return reporte.toString();
    }

    // Genera un reporte de contratos (permiso "gerente").
    public String generarReporteContratos() {
        verificarPermiso("gerente");
        List<Contrato> contratos = contratoRepo.obtenerTodos();
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE CONTRATOS ===\n");
        reporte.append("Total de contratos: ").append(contratos.size()).append("\n\n");

        for (Contrato cont : contratos) {
            reporte.append("ID: ").append(cont.getIdContrato()).append("\n");
            reporte.append("Tipo: ").append(cont.getTipoContrato()).append("\n");
            reporte.append("Empleado: ").append(cont.getEmpleado().getNombre()).append(" ").append(cont.getEmpleado().getApellidos()).append("\n");
            reporte.append("Salario Base: ").append(cont.getSalarioBase()).append("\n");
            reporte.append("Fecha Inicio: ").append(cont.getFechaInicio()).append("\n");
            reporte.append("Fecha Fin: ").append(cont.getFechaFin() != null ? cont.getFechaFin() : "Indefinido").append("\n");
            reporte.append("------------------------\n");
        }

        logRepo.registrarAccion(usuarioActual.getNombreUsuario(), "REPORTE", "Generación de reporte de contratos");
        return reporte.toString();
    }

    // ===== MÉTODOS DE EMPLEADOS PARA ACTUALIZAR SUS DATOS =====

    // Método específico para que el rol Empleado actualice sus datos.
    public void empleadoActualizarDatos(int idEmpleado, String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        verificarPermiso("empleado"); // Requiere permiso base.
        
        // Doble verificación: solo puede modificar sus propios datos.
        if (usuarioActual.getEmpleado() != null && usuarioActual.getEmpleado().getIdEmpleado() == idEmpleado) {
            Empleado empleado = empleadoRepo.buscarPorId(idEmpleado);
            if (empleado != null) {
                // Llama a un método específico en la clase Empleado.
                empleado.actualizarDireccion(nuevaDireccion, nuevoCorreo, nuevoNumero);
                empleadoRepo.actualizar(empleado); // Persiste los cambios.
                logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "empleado", idEmpleado);
            } else {
                throw new RuntimeException("Empleado no encontrado");
            }
        } else {
            throw new RuntimeException("No tiene permisos para actualizar estos datos");
        }
    }

    // Método para que el rol Empleado vea sus propios datos.
    public Empleado empleadoVerMisDatos() {
        verificarPermiso("empleado");
        if (usuarioActual.getEmpleado() != null) {
            // Busca el empleado asociado a su usuario.
            return empleadoRepo.buscarPorId(usuarioActual.getEmpleado().getIdEmpleado());
        } else {
            throw new RuntimeException("No hay empleado asociado al usuario");
        }
    }

    // --- Métodos Auxiliares ---

    // Método privado para lanzar una excepción si el permiso no se cumple.
    // Esto detiene la ejecución si un usuario intenta hacer algo indebido.
    private void verificarPermiso(String permiso) {
        if (!tienePermiso(permiso)) {
            throw new RuntimeException("No tiene permisos para realizar esta acción");
        }
    }
    
    // Obtiene los logs (permiso "admin").
    public List<String> obtenerLogsPorUsuario(String nombreUsuario) {
        verificarPermiso("admin");
        return logRepo.obtenerLogsPorUsuario(nombreUsuario);
    }

    // Cierra la conexión a la base de datos.
    // Es llamado por el Controlador cuando la aplicación se cierra.
    public void cerrarConexion() {
        DatabaseConnection.closeConnection(connection);
    }
}