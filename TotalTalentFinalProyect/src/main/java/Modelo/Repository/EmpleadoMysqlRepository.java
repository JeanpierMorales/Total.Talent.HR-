package Modelo.Repository;

import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// --- Implementación del Repositorio de Empleados (MySQL) ---
// Esta clase implementa la interfaz EmpleadoRepository.
// Contiene todo el código JDBC y SQL para gestionar la tabla 'empleado'.
public class EmpleadoMysqlRepository implements EmpleadoRepository {

    // La conexión a la BD, inyectada por el Facade.
    private Connection connection;

    // Constructor que recibe la conexión.
    public EmpleadoMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo empleado.
    public void guardar(Empleado empleado) { 
        String sql = "INSERT INTO empleado (nombre, apellidos, edad, dni, numero, correo, direccion, "
                + "grado_instruccion, carrera, comentarios, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar todos los valores del objeto Empleado a la consulta SQL.
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellidos());
            stmt.setInt(3, empleado.getEdad());
            stmt.setString(4, empleado.getDni());
            stmt.setString(5, empleado.getNumero());
            stmt.setString(6, empleado.getCorreo());
            stmt.setString(7, empleado.getDireccion());
            stmt.setString(8, empleado.getGradoInstruccion());
            stmt.setString(9, empleado.getCarrera());
            stmt.setString(10, empleado.getComentarios());
            stmt.setString(11, empleado.getRol().toString()); // Convierte el enum Rol a String.
            stmt.executeUpdate();

            // Obtener el ID auto-generado.
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setIdEmpleado(rs.getInt(1)); // Asigna el nuevo ID al objeto.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para actualizar un empleado existente.
    // Nota: Este UPDATE solo actualiza ciertos campos (los que el EmpleadoStrategy permite editar).
    public void actualizar(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, numero = ?, correo = ?, "
                + "direccion = ?, grado_instruccion = ? WHERE id_empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getNumero());
            stmt.setString(4, empleado.getCorreo());
            stmt.setString(5, empleado.getDireccion());
            stmt.setString(6, empleado.getGradoInstruccion());
            stmt.setInt(7, empleado.getIdEmpleado()); // El ID va en el WHERE.
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar un empleado por su ID.
    public void eliminar(int idEmpleado) {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para buscar un empleado por su ID.
    public Empleado buscarPorId(int idEmpleado) {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Llama al método helper para construir el objeto Empleado.
                    return mapearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para buscar un empleado por su DNI.
    public Empleado buscarPorDni(String dni) {
        String sql = "SELECT * FROM empleado WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado por DNI: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para buscar empleados por rol.
    public List<Empleado> buscarPorRol(String rol) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleado WHERE rol = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rol); // El rol se pasa como String.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapearEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleados por rol: " + e.getMessage());
        }
        return empleados;
    }

    @Override
    // Método para obtener todos los empleados.
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        // Se ordenan alfabéticamente por apellidos y nombre.
        String sql = "SELECT * FROM empleado ORDER BY apellidos, nombre";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los empleados: " + e.getMessage());
        }
        return empleados;
    }

    @Override
    // Método para verificar si un DNI ya existe.
    public boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM empleado WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True si el conteo > 0.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de DNI: " + e.getMessage());
        }
        return false;
    }

    @Override
    // Método para buscar empleados por nombre o apellidos (búsqueda parcial).
    public List<Empleado> buscarPorNombre(String nombre) {
        List<Empleado> empleados = new ArrayList<>();
        // Se usa LIKE y %...% para buscar coincidencias parciales (ej. "mar" encuentra "Maria").
        String sql = "SELECT * FROM empleado WHERE nombre LIKE ? OR apellidos LIKE ? ORDER BY apellidos, nombre";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String patron = "%" + nombre + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapearEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleados por nombre: " + e.getMessage());
        }
        return empleados;
    }

    // Método auxiliar (privado) para mapear un ResultSet a un objeto Empleado.
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado(); // Crea la instancia.
        // Asigna todos los campos de la tabla 'empleado'.
        empleado.setIdEmpleado(rs.getInt("id_empleado"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setEdad(rs.getInt("edad"));
        empleado.setDni(rs.getString("dni"));
        empleado.setNumero(rs.getString("numero"));
        empleado.setCorreo(rs.getString("correo"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setGradoInstruccion(rs.getString("grado_instruccion"));
        empleado.setCarrera(rs.getString("carrera"));
        empleado.setComentarios(rs.getString("comentarios"));
        empleado.setRol(Rol.valueOf(rs.getString("rol"))); // Convierte String a enum Rol.

        // --- Relación con Contrato ---
        // Después de crear el empleado, llama a otro método para
        // buscar y adjuntar su contrato activo.
        cargarContratoActivo(empleado);

        return empleado;
    }

    // Método auxiliar (privado) para cargar el contrato activo del empleado.
    private void cargarContratoActivo(Empleado empleado) throws SQLException {
        // Esta consulta busca el contrato más reciente del empleado que esté activo
        // (fecha_fin es NULL o es mayor o igual a hoy).
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado "
                + "WHERE c.id_empleado = ? AND (c.fecha_fin IS NULL OR c.fecha_fin >= CURDATE()) "
                + "ORDER BY c.fecha_inicio DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empleado.getIdEmpleado());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapea el contrato encontrado (usando el método de abajo).
                    Contrato contrato = mapearContrato(rs);
                    // Asigna el contrato encontrado al objeto Empleado.
                    empleado.setContrato(contrato);
                }
            }
        }
    }

    // Método auxiliar (privado) para mapear un ResultSet a un objeto Contrato.
    // Este método es una copia del que está en ContratoMysqlRepository.
    // Es necesario aquí para que funcione el método cargarContratoActivo.
    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        // --- Uso del Factory ---
        // 1. Obtiene el tipo de contrato (ej. "Planilla") de la BD.
        String tipoContrato = rs.getString("tipo_contrato");
        
        // 2. Llama al ContratoFactory para que cree la instancia correcta
        //    (ej. un objeto ContratoPlanilla).
        Contrato contrato = Modelo.Factory.ContratoFactory.crearContrato(tipoContrato);

        // 3. Mapea los campos comunes.
        contrato.setIdContrato(rs.getInt("id_contrato"));
        contrato.getEmpleado().setIdEmpleado(rs.getInt("id_empleado"));
        contrato.getEmpleado().setNombre(rs.getString("nombre"));
        contrato.getEmpleado().setApellidos(rs.getString("apellidos"));
        contrato.setFechaInicio(rs.getDate("fecha_inicio"));
        contrato.setFechaFin(rs.getDate("fecha_fin"));
        contrato.setSalarioBase(rs.getBigDecimal("salario_base").floatValue());
        contrato.setBonificacion(rs.getBigDecimal("bonificacion").floatValue());
        contrato.setDescuentoAFP(rs.getBigDecimal("descuento_afp").floatValue());

        // 4. Mapea los campos específicos usando 'instanceof'.
        //    Esto es crucial para que el Factory funcione correctamente con la BD.
        if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
            Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
            cp.setHorasExtras(rs.getBigDecimal("horas_extras").floatValue());
        } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
            Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
            cp.setHorasTrabajadas(rs.getInt("horas_trabajadas"));
            cp.setPagoPorHora(rs.getBigDecimal("pago_por_hora").floatValue());
        } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
            Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
            cl.setMontoPorProyecto(rs.getBigDecimal("monto_por_proyecto").floatValue());
            cl.setNumeroProyectos(rs.getInt("numero_proyectos"));
        }

        return contrato; // Devuelve el objeto Contrato completo.
    }
}