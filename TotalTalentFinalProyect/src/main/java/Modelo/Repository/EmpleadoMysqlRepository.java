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

public class EmpleadoMysqlRepository implements EmpleadoRepository {

    private Connection connection;

    // Constructor que recibe la conexión a la base de datos
    public EmpleadoMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo empleado
    public void guardar(Empleado empleado) { // 
        String sql = "INSERT INTO empleado (nombre, apellidos, edad, dni, numero, correo, direccion, "
                + "grado_instruccion, carrera, comentarios, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar los valores del empleado al PreparedStatement
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
            stmt.setString(11, empleado.getRol().toString());
            stmt.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setIdEmpleado(rs.getInt(1)); // Asignar el ID generado al objeto empleado
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para actualizar un empleado existente
    public void actualizar(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, numero = ?, correo = ?, "
                + "direccion = ?, grado_instruccion = ? WHERE id_empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Asignar los valores del empleado al PreparedStatement
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getNumero());
            stmt.setString(4, empleado.getCorreo());
            stmt.setString(5, empleado.getDireccion());
            stmt.setString(6, empleado.getGradoInstruccion());
            stmt.setInt(7, empleado.getIdEmpleado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar un empleado por su ID
    public void eliminar(int idEmpleado) {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Asignar el ID del empleado al PreparedStatement
            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado: " + e.getMessage());
        }
    }

    @Override
    // Método para buscar un empleado por su ID
    public Empleado buscarPorId(int idEmpleado) {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        // Utilizar try-with-resources para manejar la conexión y el PreparedStatement
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para buscar un empleado por su DNI
    public Empleado buscarPorDni(String dni) {
        // Utilizar try-with-resources para manejar la conexión y el PreparedStatement
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

    
    /// Método para buscar empleados por rol
    public List<Empleado> buscarPorRol(String rol) {
        List<Empleado> empleados = new ArrayList<>(); // Inicializar la lista de empleados
        String sql = "SELECT * FROM empleado WHERE rol = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rol);
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterar sobre el ResultSet y mapear cada fila a un objeto Empleado
                while (rs.next()) {
                    empleados.add(mapearEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleados por rol: " + e.getMessage());
        }
        // Retornar la lista de empleados encontrados
        return empleados;
    }

    @Override
    // Método para obtener todos los empleados ordenados por apellidos y nombre
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        // Consulta SQL para obtener todos los empleados ordenados por apellidos y nombre
        String sql = "SELECT * FROM empleado ORDER BY apellidos, nombre";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Mapear cada fila del ResultSet a un objeto Empleado y agregarlo a la lista
                empleados.add(mapearEmpleado(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los empleados: " + e.getMessage());
        }
        return empleados; // retornar la lista de empleados
    }

    @Override
    // Método para verificar si un DNI ya existe en la base de datos
    public boolean existeDni(String dni) {
        // Consulta SQL para contar cuántos empleados tienen el mismo DNI
        String sql = "SELECT COUNT(*) FROM empleado WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Asignar el DNI al PreparedStatement
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de DNI: " + e.getMessage());
        }
        return false;
    }

    @Override
    // Método para buscar empleados por nombre o apellidos
    public List<Empleado> buscarPorNombre(String nombre) {
        // Inicializar la lista de empleados
        List<Empleado> empleados = new ArrayList<>();
        // Consulta SQL para buscar empleados por nombre o apellidos
        String sql = "SELECT * FROM empleado WHERE nombre LIKE ? OR apellidos LIKE ? ORDER BY apellidos, nombre";
        // Utilizar try-with-resources para manejar la conexión y el PreparedStatement
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

    // Método auxiliar para mapear ResultSet a objeto Empleado
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado(); // Crear una nueva instancia de Empleado
        empleado.setIdEmpleado(rs.getInt("id_empleado")); // Asignar el ID del empleado
        empleado.setNombre(rs.getString("nombre")); // Asignar el nombre del empleado
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setEdad(rs.getInt("edad"));
        empleado.setDni(rs.getString("dni"));
        empleado.setNumero(rs.getString("numero"));
        empleado.setCorreo(rs.getString("correo"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setGradoInstruccion(rs.getString("grado_instruccion"));
        empleado.setCarrera(rs.getString("carrera"));
        empleado.setComentarios(rs.getString("comentarios"));
        empleado.setRol(Rol.valueOf(rs.getString("rol")));

        // Cargar el contrato activo del empleado
        cargarContratoActivo(empleado);

        return empleado;
    }

    // Método auxiliar para cargar el contrato activo del empleado
    private void cargarContratoActivo(Empleado empleado) throws SQLException {
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado "
                + "WHERE c.id_empleado = ? AND (c.fecha_fin IS NULL OR c.fecha_fin >= CURDATE()) "
                + "ORDER BY c.fecha_inicio DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empleado.getIdEmpleado());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapear el contrato usando el método auxiliar del ContratoRepository
                    Contrato contrato = mapearContrato(rs);
                    empleado.setContrato(contrato);
                }
            }
        }
    }

    // Método auxiliar para mapear ResultSet a objeto Contrato (copiado de ContratoMysqlRepository)
    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        Modelo.Factory.ContratoFactory factory = new Modelo.Factory.ContratoFactory();
        String tipoContrato = rs.getString("tipo_contrato");
        Contrato contrato = factory.crearContrato(tipoContrato);

        contrato.setIdContrato(rs.getInt("id_contrato"));
        contrato.getEmpleado().setIdEmpleado(rs.getInt("id_empleado"));
        contrato.getEmpleado().setNombre(rs.getString("nombre"));
        contrato.getEmpleado().setApellidos(rs.getString("apellidos"));
        contrato.setFechaInicio(rs.getDate("fecha_inicio"));
        contrato.setFechaFin(rs.getDate("fecha_fin"));
        contrato.setSalarioBase(rs.getBigDecimal("salario_base").doubleValue());
        contrato.setBonificacion(rs.getBigDecimal("bonificacion").doubleValue());
        contrato.setDescuentoAFP(rs.getBigDecimal("descuento_afp").doubleValue());

        // Mapear campos específicos según el tipo
        if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
            Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
            cp.setHorasExtras(rs.getBigDecimal("horas_extras").doubleValue());
        } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
            Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
            cp.setHorasTrabajadas(rs.getInt("horas_trabajadas"));
            cp.setPagoPorHora(rs.getBigDecimal("pago_por_hora").doubleValue());
        } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
            Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
            cl.setMontoPorProyecto(rs.getBigDecimal("monto_por_proyecto").doubleValue());
            cl.setNumeroProyectos(rs.getInt("numero_proyectos"));
        }

        return contrato;
    }
}
