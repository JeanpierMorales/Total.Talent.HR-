package Modelo.Repository;

import Modelo.Empleado;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

    // Conexión a la base de datos
    private final Connection conn;

    // Constructor: recibe la conexión al crear el repositorio
    public EmpleadoRepository(Connection conn) {
        this.conn = conn;
    }

    //Crea un nuevo registro de empleado en la base de datos.
    public void crear(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleado(nombre, apellido, correo, password, cargo, salario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getCorreo());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getCargo());
            ps.setDouble(6, e.getSalario());
            ps.executeUpdate();

            // Obtener el ID autogenerado por la base de datos
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
        }
    }

    // REtorna un empleado por su ID
    public Empleado obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Crea un nuevo objeto Empleado a partir de los datos obtenidos
                    return new Empleado(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("password"),
                            rs.getDouble("salario")
                    );
                }
            }
        }
        return null;
    }

    // Actualiza los datos de un empleado existente.
    public void actualizar(Empleado e) throws SQLException {
        String sql = "UPDATE empleado SET nombre=?, apellido=?, correo=?, password=?, cargo=?, salario=? WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getCorreo());
            ps.setString(4, e.getPassword()); 
            ps.setString(5, e.getCargo());
            ps.setDouble(6, e.getSalario());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        }
    }

    //ELIMINA un empleado por su ID.
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Busca un empleado por su ID usando un método auxiliar para mapear los datos.
    public Empleado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        }
        return null;
    }

    // Busca empleado por correo 
    public Empleado buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        }
        return null;
    }

    // Lista los empleados de la base de datos
    public List<Empleado> listarTodos() throws SQLException {
        String sql = "SELECT * FROM empleado ORDER BY id_empleado";
        List<Empleado> lista = new ArrayList<>();
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearEmpleado(rs));
            }
        }
        return lista;
    }

    // BUSCA empleados por texto (nombre, apellido o correo).
    public List<Empleado> buscarPorTexto(String texto) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE nombre LIKE ? OR apellido LIKE ? OR correo LIKE ? ORDER BY id_empleado";
        List<Empleado> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + texto + "%"; // Patrón de búsqueda
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEmpleado(rs));
                }
            }
        }
        return lista;
    }

    // Registra la fecha y hora del último inicio de sesión del empleado
    public void registrarLogin(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleado SET last_login = ? WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            ps.setInt(2, idEmpleado);
            ps.executeUpdate();
        }
    }

    /**
     * Método privado que mapea un ResultSet a un objeto Empleado.
     * Traduce los datos de la base de datos a un objeto Java.
     */
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setId(rs.getInt("id_empleado"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setCorreo(rs.getString("correo"));
        e.setPassword(rs.getString("password"));
        e.setCargo(rs.getString("cargo"));
        e.setSalario(rs.getDouble("salario"));

        // Fechas opcionales
        Timestamp tIngreso = rs.getTimestamp("fecha_ingreso");
        if (tIngreso != null) {
            e.setFechaIngreso(tIngreso.toLocalDateTime());
        }

        Timestamp tMod = rs.getTimestamp("ultima_modificacion");
        if (tMod != null) {
            e.setUltimaModificacion(tMod.toLocalDateTime());
        }

        Timestamp tLogin = rs.getTimestamp("last_login");
        if (tLogin != null) {
            e.setLastLogin(tLogin.toLocalDateTime());
        }

        return e;
    }
}
