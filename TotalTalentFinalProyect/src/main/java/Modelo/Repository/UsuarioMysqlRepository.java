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

// --- Implementación del Repositorio de Usuarios (MySQL) ---
// Esta clase implementa la interfaz UsuarioRepository.
// Aquí es donde se define toda la lógica de SQL (JDBC) para interactuar
// con la tabla 'usuario' en la base de datos MySQL.
public class UsuarioMysqlRepository implements UsuarioRepository {

    // La conexión a la base de datos, inyectada desde el Facade.
    private Connection connection;

    // Constructor que recibe la conexión a la base de datos desde el Facade.
    public UsuarioMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo usuario en la base de datos.
    public void guardar(Usuario usuario) {
        // Sentencia SQL de inserción.
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena, id_empleado, activo) VALUES (?, ?, ?, ?)";
        // Usamos try-with-resources para que el PreparedStatement se cierre automáticamente.
        // Statement.RETURN_GENERATED_KEYS es crucial para obtener el ID auto-generado.
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar valores a los parámetros del PreparedStatement (los '?').
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setInt(3, usuario.getEmpleado().getIdEmpleado()); // Guarda la llave foránea.
            stmt.setBoolean(4, usuario.isActivo());
            stmt.executeUpdate(); // Ejecuta la inserción.

            // Bloque para obtener el ID generado.
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Asigna el ID devuelto por la BD al objeto Usuario.
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para actualizar un usuario existente.
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre_usuario = ?, contrasena = ?, activo = ? WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setBoolean(3, usuario.isActivo());
            stmt.setInt(4, usuario.getIdUsuario()); // El ID se usa en el WHERE.
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar un usuario por su ID.
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario); // Asignar el ID del usuario a eliminar.
            stmt.executeUpdate(); // Ejecuta la sentencia.
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    // Método para buscar un usuario por su ID.
    public Usuario buscarPorId(int idUsuario) {
        // Usamos LEFT JOIN para traer también los datos del empleado asociado,
        // aunque el empleado haya sido borrado (por eso LEFT JOIN y no INNER JOIN).
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado WHERE u.id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario); // Asignar el ID a buscar.
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // Si se encuentra un resultado...
                    return mapearUsuario(rs); // ...se mapea a un objeto Usuario.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null; // Devuelve null si no se encontró.
    }

    @Override
    // Método para buscar un usuario por su nombre de usuario.
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        // Misma consulta que buscarPorId, pero filtrando por nombre_usuario.
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado WHERE u.nombre_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario); // Asignar el nombre de usuario a buscar.
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs); // Mapea el resultado.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por nombre: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para obtener todos los usuarios.
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar los resultados.
        String sql = "SELECT u.*, e.nombre, e.apellidos, e.rol FROM usuario u "
                + "LEFT JOIN empleado e ON u.id_empleado = e.id_empleado";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            // Itera sobre *todos* los resultados.
            while (rs.next()) {
                // Mapea cada fila a un objeto Usuario y lo añade a la lista.
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return usuarios; // Retorna la lista de usuarios.
    }

    @Override
    // Método para verificar si un nombre de usuario ya existe.
    public boolean existeNombreUsuario(String nombreUsuario) {
        // SQL eficiente que solo cuenta las ocurrencias.
        String sql = "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Devuelve true si el conteo es mayor a 0.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de nombre de usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    // Método para verificar credenciales de login.
    public Usuario verificarCredenciales(String nombreUsuario, String contrasena) {
        // 1. Busca el usuario por su nombre.
        Usuario usuario = buscarPorNombreUsuario(nombreUsuario);
        
        // 2. Si existe, delega la verificación de la contraseña al método
        //    verificarCredenciales del *objeto Usuario*.
        //    Esto es bueno (Separación de Responsabilidades): el Repositorio busca
        //    y la Entidad (Usuario) sabe cómo validarse a sí misma.
        if (usuario != null && usuario.verificarCredenciales(contrasena)) {
            return usuario; // Retorna el usuario si las credenciales son correctas.
        }
        return null; // Devuelve null si no existe o la contraseña es incorrecta.
    }

    // Método auxiliar (privado) para mapear un ResultSet a un objeto Usuario.
    // Este es un método clave para transformar datos de la BD en objetos Java.
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        // Mapea los campos de la tabla 'usuario'.
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setActivo(rs.getBoolean("activo"));

        // Mapea los datos del empleado asociado (gracias al LEFT JOIN).
        int idEmpleado = rs.getInt("id_empleado");
        if (!rs.wasNull()) { // Verifica si el id_empleado no era NULL en la BD.
            Empleado empleado = new Empleado();
            empleado.setIdEmpleado(idEmpleado);
            empleado.setNombre(rs.getString("nombre"));
            empleado.setApellidos(rs.getString("apellidos"));
            empleado.setRol(Rol.valueOf(rs.getString("rol"))); // Convierte el String "GERENTE" al enum Rol.GERENTE.
            
            // Asigna el objeto Empleado completo al Usuario.
            usuario.setEmpleado(empleado);
        }

        return usuario; // Retorna el objeto Usuario ya construido.
    }
}