package Modelo.Repository;

import Modelo.Usuario;
import java.util.List;

// --- Interfaz UsuarioRepository ---
// Esta interfaz define el contrato (las operaciones CRUD) para la entidad Usuario.
// El Facade depende de esta interfaz para gestionar usuarios,
// sin saber si los datos vienen de MySQL, archivos, etc.
public interface UsuarioRepository {

    // Define un método para guardar un nuevo usuario en la base de datos.
    void guardar(Usuario usuario);

    // Define un método para actualizar un usuario existente.
    void actualizar(Usuario usuario);

    // Define un método para eliminar un usuario por su ID.
    void eliminar(int idUsuario);

    // Define un método para buscar un usuario por su ID.
    Usuario buscarPorId(int idUsuario);

    // Define un método para buscar un usuario por su nombre de usuario (para login).
    Usuario buscarPorNombreUsuario(String nombreUsuario);

    // Define un método para obtener todos los usuarios.
    List<Usuario> obtenerTodos();

    // Define un método para verificar si un nombre de usuario ya existe (para validación).
    boolean existeNombreUsuario(String nombreUsuario);

    // Define un método para verificar las credenciales de login.
    // Es el método clave que usará el Facade en su función de login.
    Usuario verificarCredenciales(String nombreUsuario, String contrasena);
}