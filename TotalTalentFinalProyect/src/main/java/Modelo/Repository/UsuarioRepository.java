package Modelo.Repository;

import Modelo.Usuario;
import java.util.List;

// Interfaz para el repositorio de usuarios que nos define las operaciones CRUD para la entidad Usuario

public interface UsuarioRepository {

    // Método para guardar un nuevo usuario en la base de datos
    void guardar(Usuario usuario);

    // Método para actualizar un usuario existente
    void actualizar(Usuario usuario);

    // Método para eliminar un usuario por su ID
    void eliminar(int idUsuario);

    // Método para buscar un usuario por su ID
    Usuario buscarPorId(int idUsuario);

    // Método para buscar un usuario por su nombre de usuario
    Usuario buscarPorNombreUsuario(String nombreUsuario);

    // Método para obtener todos los usuarios
    List<Usuario> obtenerTodos();

    // Método para verificar si un nombre de usuario ya existe
    boolean existeNombreUsuario(String nombreUsuario);

    // Método para verificar credenciales de login
    Usuario verificarCredenciales(String nombreUsuario, String contrasena);
}
