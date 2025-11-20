package Modelo;

// --- Enumeración de Roles ---
// Esta enumeración define los únicos roles de usuario permitidos en el sistema.
// Usar un 'enum' en lugar de Strings previene errores de tipeo y
// centraliza los tipos de permisos que existen.
public enum Rol { 
    
    // Tiene acceso completo a todas las funcionalidades del sistema (CRUD de usuarios,
    // empleados, contratos, reportes, logs y estadísticas).
    ADMINISTRADOR,
    
    // Puede gestionar empleados y contratos (Crear, Actualizar, Leer).
    // Usado por la ReclutadorStrategy.
    RECLUTADOR,
    
    // Puede generar reportes y ver estadísticas.
    // Usado por la GerenteStrategy.
    GERENTE,
    
    // Solo puede ver y actualizar sus propios datos personales.
    // Usado por la EmpleadoStrategy.
    EMPLEADO
}