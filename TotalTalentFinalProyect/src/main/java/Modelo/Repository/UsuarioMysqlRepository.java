package Modelo.Repository;
// Implementación MySQL del repositorio de usuarios

import Modelo.Empleado;
import Modelo.Rol;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioMysqlRepository implements UsuarioRepository {

    private Connection connection;

    // Constructor que recibe la conexión a la base de datos
    public UsuarioMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo usuario en la base de datos
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena, id_empleado, activo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar valores a los parámetros del PreparedStatement
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setInt(3, usuario.getEmpleado().getIdEmpleado());
            stmt.setBoolean(4, usuario.isActivo());
            stmt.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para actualizar un usuario existente
    public void actualizar(Usuario usuario) {
        // Verificar que el usuario tenga un ID asignado
        String sql = "UPDATE usuario SET nombre_usuario = ?, contrasena = ?, activo = ? WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setBoolean(3, usuario.isActivo());
            stmt.setInt(4, usuario.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar un usuario por su ID
    public void eliminar(int idUsuario) {
        // Verificar que el ID del usuario sea válido
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario); // Asignar el ID del usuario a eliminar
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para buscar un usuario por su ID
    public Usuario buscarPorId(int idUsuario) {
        // Verificar que el ID del usuario sea válido
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado WHERE u.id_usuario = ?";
        // Preparar la consulta SQL
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Asignar el ID del usuario a buscar
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            // Manejar excepciones de SQL
            throw new RuntimeException("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    //  Método para buscar un usuario por su nombre de usuario
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        // Verificar que el nombre de usuario no sea nulo o vacío
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado WHERE u.nombre_usuario = ?";
        // Preparar la consulta SQL
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario); // Asignar el nombre de usuario a buscar
            try (ResultSet rs = stmt.executeQuery()) { // Ejecutar la consulta
                if (rs.next()) { // Si se encuentra un resultado
                    return mapearUsuario(rs); // Mapear el resultado a un objeto Usuario
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por nombre: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        // Lista para almacenar los usuarios obtenidos
        List<Usuario> usuarios = new ArrayList<>();

        // Consulta SQL para obtener todos los usuarios
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            // Iterar sobre los resultados y mapear cada fila a un objeto Usuario
            while (rs.next()) {
                // Agregar el usuario mapeado a la lista
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return usuarios; // Retornar la lista de usuarios
    }

    @Override
    // Método para verificar si un nombre de usuario ya existe
    public boolean existeNombreUsuario(String nombreUsuario) {
        // Consulta SQL para contar usuarios con el nombre dado
        String sql = "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ?";
        // Preparar y ejecutar la consulta
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario); // Asignar el nombre de usuario a verificar
            try (ResultSet rs = stmt.executeQuery()) { // Ejecutar la consulta
                if (rs.next()) { // Si hay un resultado
                    return rs.getInt(1) > 0; // Retornar true si el conteo es mayor a 0
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de nombre de usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    // Método para verificar credenciales de login
    public Usuario verificarCredenciales(String nombreUsuario, String contrasena) {
        // Buscar el usuario por su nombre de usuario
        Usuario usuario = buscarPorNombreUsuario(nombreUsuario);
        // Verificar si el usuario existe y si la contraseña coincide
        if (usuario != null && usuario.verificarCredenciales(contrasena)) {
            return usuario; // Retornar el usuario si las credenciales son correctas
        }
        return null;
    }

    // Método auxiliar para mapear ResultSet a objeto Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        //  Crear un nuevo objeto Usuario y asignar sus propiedades
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setActivo(rs.getBoolean("activo"));

        // Mapear empleado si existe
        int idEmpleado = rs.getInt("id_empleado");
        if (!rs.wasNull()) { // Verificar si el id_empleado no es nulo
            Empleado empleado = new Empleado(); // Crear un nuevo objeto Empleado
            empleado.setIdEmpleado(idEmpleado); // Asignar el ID del empleado
            empleado.setNombre(rs.getString("nombre"));
            empleado.setApellidos(rs.getString("apellidos"));
            empleado.setRol(Rol.valueOf(rs.getString("rol"))); // Asignar el rol del empleado
            usuario.setEmpleado(empleado); // Asignar el empleado al usuario
        }

        return usuario; //  Retornar el objeto Usuario mapeado
    }
}
