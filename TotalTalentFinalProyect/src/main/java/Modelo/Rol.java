
package Modelo;


public enum Rol { // la clase va a definir los diferentes roles de usuario en el sistema Total Talent
    /// Tiene acceso completo a todas las funcionalidades del sistema, incluyendo CRUD completo de empleados,
    /// contratos, usuarios y reportes
    
    ADMINISTRADOR,
    
    /// Puede crear contratos con empleados correspondientes,
    ///gestionar empleados y ver información básica
    RECLUTADOR,
    
    ///Gerente: Puede generar reportes a partir de los datos del sistema, ver
    ///métricas y estadísticas
    GERENTE,
    
    /// Solo puede ver y actualizar sus propios datos personales
    EMPLEADO
}
