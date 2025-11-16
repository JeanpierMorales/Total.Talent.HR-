package Modelo.Facade;

import Modelo.DatabaseConnection;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Repository.*;
import Modelo.Rol;
import Modelo.Usuario;
import Utilidades.Validaciones;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// Clase Facade que centraliza las operaciones del sistema
// Proporciona una interfaz unificada para acceder a las funcionalidades del sistema
public class Facade {

    // Repositorios para acceder a los datos
    private EmpleadoRepository empleadoRepo;
    private UsuarioRepository usuarioRepo;
    private ContratoRepository contratoRepo;
    private LogRepository logRepo;
    private Connection connection;

    // Usuario actualmente logueado en el sistema
    private Usuario usuarioActual;

    // Constructor que inicializa la conexión y los repositorios
    public Facade() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.empleadoRepo = new EmpleadoMysqlRepository(connection);
            this.usuarioRepo = new UsuarioMysqlRepository(connection);
            this.contratoRepo = new ContratoMysqlRepository(connection);
            this.logRepo = new LogArchivoRepository();
        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando Facade: " + e.getMessage());
        }
    }

    // Método para autenticar un usuario en el sistema
    public boolean login(String nombreUsuario, String contrasena) {
        try {
            Usuario usuario = usuarioRepo.verificarCredenciales(nombreUsuario, contrasena);
            if (usuario != null && usuario.isActivo()) {
                this.usuarioActual = usuario;
                logRepo.registrarLoginExitoso(nombreUsuario);
                return true;
            } else {
                logRepo.registrarLoginFallido(nombreUsuario, "Credenciales inválidas");
                return false;
            }
        } catch (Exception e) {
            logRepo.registrarLoginFallido(nombreUsuario, "Error del sistema: " + e.getMessage());
            return false;
        }
    }

    // Método para cerrar sesión
    public void logout() {
        if (usuarioActual != null) {
            logRepo.registrarAccion(usuarioActual.getNombreUsuario(), "LOGOUT", "Cierre de sesión");
            this.usuarioActual = null;
        }
    }

    // Método para obtener el usuario actualmente logueado
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Método para verificar si hay un usuario logueado
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }

    // Método para verificar permisos según el rol del usuario actual
    public boolean tienePermiso(String permiso) {
        if (usuarioActual == null) {
            return false;
        }

        Rol rol = usuarioActual.getRol();
        switch (permiso.toLowerCase()) {
            case "admin":
                return rol == Rol.ADMINISTRADOR;
            case "reclutador":
                return rol == Rol.RECLUTADOR || rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            case "gerente":
                return rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            case "empleado":
                return rol == Rol.EMPLEADO || rol == Rol.RECLUTADOR || rol == Rol.GERENTE || rol == Rol.ADMINISTRADOR;
            default:
                return false;
        }
    }

    // ===== OPERACIONES DE EMPLEADOS =====
    // Método para guardar un empleado (solo administradores y reclutadores)
    public void guardarEmpleado(Empleado empleado) {
        verificarPermiso("reclutador");
        // Validaciones de campos
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
        // Validar que el DNI no exista
        if (empleadoRepo.existeDni(empleado.getDni())) {
            throw new RuntimeException("Ya existe un empleado con el DNI: " + empleado.getDni());
        }
        empleadoRepo.guardar(empleado);
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "empleado", empleado.getIdEmpleado());
    }

    // Método para actualizar un empleado
    public void actualizarEmpleado(Empleado empleado) {
        // Permitir que empleados actualicen sus propios datos, o reclutadores/administradores actualicen cualquier empleado
        if (usuarioActual.getRol() == Rol.EMPLEADO) {
            // Si es empleado, verificar que solo actualice sus propios datos
            if (usuarioActual.getEmpleado() == null || usuarioActual.getEmpleado().getIdEmpleado() != empleado.getIdEmpleado()) {
                throw new RuntimeException("Los empleados solo pueden actualizar sus propios datos");
            }
        } else {
            // Para otros roles, verificar permisos de reclutador
            verificarPermiso("reclutador");
        }
        empleadoRepo.actualizar(empleado);
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "empleado", empleado.getIdEmpleado());
    }

    // Método para eliminar un empleado (solo administradores)
    public void eliminarEmpleado(int idEmpleado) {
        verificarPermiso("admin");
        empleadoRepo.eliminar(idEmpleado);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "empleado", idEmpleado);
    }

    // Método para buscar empleado por ID
    public Empleado buscarEmpleadoPorId(int idEmpleado) {
        verificarPermiso("empleado");
        return empleadoRepo.buscarPorId(idEmpleado);
    }

    // Método para buscar empleado por DNI
    public Empleado buscarEmpleadoPorDni(String dni) {
        verificarPermiso("reclutador");
        return empleadoRepo.buscarPorDni(dni);
    }

    // Método para obtener todos los empleados
    public List<Empleado> obtenerTodosEmpleados() {
        verificarPermiso("reclutador");
        return empleadoRepo.obtenerTodos();
    }

    // Método para buscar empleados por nombre
    public List<Empleado> buscarEmpleadosPorNombre(String nombre) {
        verificarPermiso("reclutador");
        return empleadoRepo.buscarPorNombre(nombre);
    }

    // Método para buscar empleados por rol
    public List<Empleado> buscarEmpleadosPorRol(String rol) {
        verificarPermiso("admin");
        return empleadoRepo.buscarPorRol(rol);
    }

    // ===== OPERACIONES DE CONTRATOS =====
    // Método para guardar un contrato
    public void guardarContrato(Contrato contrato) {
        verificarPermiso("reclutador");
        contratoRepo.guardar(contrato);
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "contrato", contrato.getIdContrato());
    }

    // Método para actualizar un contrato
    public void actualizarContrato(Contrato contrato) {
        verificarPermiso("reclutador");
        contratoRepo.actualizar(contrato);
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "contrato", contrato.getIdContrato());
    }

    // Método para eliminar un contrato
    public void eliminarContrato(int idContrato) {
        verificarPermiso("admin");
        contratoRepo.eliminar(idContrato);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "contrato", idContrato);
    }

    // Método para buscar contrato por ID
    public Contrato buscarContratoPorId(int idContrato) {
        verificarPermiso("empleado");
        return contratoRepo.buscarPorId(idContrato);
    }

    // Método para buscar contratos por empleado
    public List<Contrato> buscarContratosPorEmpleado(int idEmpleado) {
        verificarPermiso("reclutador");
        return contratoRepo.buscarPorEmpleado(idEmpleado);
    }

    // Método para obtener todos los contratos
    public List<Contrato> obtenerTodosContratos() {
        verificarPermiso("reclutador");
        return contratoRepo.obtenerTodos();
    }

    // Método para buscar contratos por tipo
    public List<Contrato> buscarContratosPorTipo(String tipoContrato) {
        verificarPermiso("gerente");
        return contratoRepo.buscarPorTipo(tipoContrato);
    }

    // Método para buscar contratos activos
    public List<Contrato> buscarContratosActivos() {
        verificarPermiso("gerente");
        return contratoRepo.buscarContratosActivos();
    }

    // Método para buscar contratos por rango de fechas
    public List<Contrato> buscarContratosPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        verificarPermiso("gerente");
        return contratoRepo.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    // ===== OPERACIONES DE USUARIOS =====
    // Método para guardar un usuario (solo administradores)
    public void guardarUsuario(Usuario usuario) {
        verificarPermiso("admin");
        // Validaciones de campos
        if (!Validaciones.validarNoVacio(usuario.getNombreUsuario()) || !Validaciones.validarLongitudMinima(usuario.getNombreUsuario(), 3)) {
            throw new RuntimeException("Nombre de usuario inválido: debe tener al menos 3 caracteres");
        }
        if (!Validaciones.validarNoVacio(usuario.getContrasena()) || !Validaciones.validarLongitudMinima(usuario.getContrasena(), 6)) {
            throw new RuntimeException("Contraseña inválida: debe tener al menos 6 caracteres");
        }
        // Validar que el nombre de usuario no exista
        if (usuarioRepo.existeNombreUsuario(usuario.getNombreUsuario())) {
            throw new RuntimeException("Ya existe un usuario con el nombre: " + usuario.getNombreUsuario());
        }
        usuarioRepo.guardar(usuario);
        logRepo.registrarCreacion(usuarioActual.getNombreUsuario(), "usuario", usuario.getIdUsuario());
    }

    // Método para actualizar un usuario
    public void actualizarUsuario(Usuario usuario) {
        verificarPermiso("admin");
        usuarioRepo.actualizar(usuario);
        logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "usuario", usuario.getIdUsuario());
    }

    // Método para eliminar un usuario
    public void eliminarUsuario(int idUsuario) {
        verificarPermiso("admin");
        usuarioRepo.eliminar(idUsuario);
        logRepo.registrarEliminacion(usuarioActual.getNombreUsuario(), "usuario", idUsuario);
    }

    // Método para buscar usuario por ID
    public Usuario buscarUsuarioPorId(int idUsuario) {
        verificarPermiso("admin");
        return usuarioRepo.buscarPorId(idUsuario);
    }

    // Método para buscar usuario por nombre de usuario
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        verificarPermiso("admin");
        return usuarioRepo.buscarPorNombreUsuario(nombreUsuario);
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        verificarPermiso("admin");
        return usuarioRepo.obtenerTodos();
    }

    // ===== OPERACIONES DE REPORTES =====
    // Método para generar reporte de empleados (solo gerentes)
    public String generarReporteEmpleados() {
        verificarPermiso("gerente");
        List<Empleado> empleados = empleadoRepo.obtenerTodos();
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE EMPLEADOS ===\n");
        reporte.append("Total de empleados: ").append(empleados.size()).append("\n\n");

        for (Empleado emp : empleados) {
            reporte.append("ID: ").append(emp.getIdEmpleado()).append("\n");
            reporte.append("Nombre: ").append(emp.getNombre()).append(" ").append(emp.getApellidos()).append("\n");
            reporte.append("DNI: ").append(emp.getDni()).append("\n");
            reporte.append("Rol: ").append(emp.getRol()).append("\n");
            reporte.append("------------------------\n");
        }

        logRepo.registrarAccion(usuarioActual.getNombreUsuario(), "REPORTE", "Generación de reporte de empleados");
        return reporte.toString();
    }

    // Método para generar reporte de contratos (solo gerentes)
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
    // Método para que un empleado actualice sus propios datos
    public void empleadoActualizarDatos(int idEmpleado, String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        verificarPermiso("empleado");
        // Verificar que el empleado solo pueda actualizar sus propios datos
        if (usuarioActual.getEmpleado() != null && usuarioActual.getEmpleado().getIdEmpleado() == idEmpleado) {
            Empleado empleado = empleadoRepo.buscarPorId(idEmpleado);
            if (empleado != null) {
                empleado.actualizarDireccion(nuevaDireccion, nuevoCorreo, nuevoNumero);
                empleadoRepo.actualizar(empleado);
                logRepo.registrarActualizacion(usuarioActual.getNombreUsuario(), "empleado", idEmpleado);
            } else {
                throw new RuntimeException("Empleado no encontrado");
            }
        } else {
            throw new RuntimeException("No tiene permisos para actualizar estos datos");
        }
    }

    // Método para que un empleado vea sus propios datos
    public Empleado empleadoVerMisDatos() {
        verificarPermiso("empleado");
        if (usuarioActual.getEmpleado() != null) {
            return empleadoRepo.buscarPorId(usuarioActual.getEmpleado().getIdEmpleado());
        } else {
            throw new RuntimeException("No hay empleado asociado al usuario");
        }
    }

    // Método auxiliar para verificar permisos
    private void verificarPermiso(String permiso) {
        if (!tienePermiso(permiso)) {
            throw new RuntimeException("No tiene permisos para realizar esta acción");
        }
    }
    // Método para obtener logs por usuario (solo administradores)

    public List<String> obtenerLogsPorUsuario(String nombreUsuario) {
        verificarPermiso("admin");
        return logRepo.obtenerLogsPorUsuario(nombreUsuario);
    }

    // Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
        DatabaseConnection.closeConnection(connection);
    }
}
