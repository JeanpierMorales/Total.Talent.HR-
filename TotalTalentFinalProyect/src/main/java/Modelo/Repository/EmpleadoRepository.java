package Modelo.Repository;

import Modelo.Empleado;
import java.util.List;

// Interfaz para el repositorio de usuarios que nos define las operaciones CRUD para la entidad Empleado


public interface EmpleadoRepository {

    // Método para guardar un nuevo empleado en la base de datos
    void guardar(Empleado empleado);

    // Método para actualizar un empleado existente
    void actualizar(Empleado empleado);

    // Método para eliminar un empleado por su ID
    void eliminar(int idEmpleado);

    // Método para buscar un empleado por su ID
    Empleado buscarPorId(int idEmpleado);

    // Método para buscar empleados por DNI
    Empleado buscarPorDni(String dni);

    // Método para buscar empleados por rol
    List<Empleado> buscarPorRol(String rol);

    // Método para obtener todos los empleados
    List<Empleado> obtenerTodos();

    // Método para verificar si un DNI ya existe
    boolean existeDni(String dni);

    // Método para buscar empleados por nombre haciendo una busqueda parcial
    List<Empleado> buscarPorNombre(String nombre);
}
