package Modelo.Repository;

import Modelo.Empleado;
import java.util.List;

// --- Interfaz EmpleadoRepository ---
// Esta interfaz define el contrato (las operaciones CRUD) para la entidad Empleado.
// El Facade depende de esta interfaz para gestionar empleados.
public interface EmpleadoRepository {

    // Define un método para guardar un nuevo empleado en la base de datos.
    void guardar(Empleado empleado);

    // Define un método para actualizar un empleado existente.
    void actualizar(Empleado empleado);

    // Define un método para eliminar un empleado por su ID.
    void eliminar(int idEmpleado);

    // Define un método para buscar un empleado por su ID.
    Empleado buscarPorId(int idEmpleado);

    // Define un método para buscar un empleado por su DNI.
    Empleado buscarPorDni(String dni);

    // Define un método para buscar empleados por su rol.
    List<Empleado> buscarPorRol(String rol);

    // Define un método para obtener todos los empleados.
    List<Empleado> obtenerTodos();

    // Define un método para verificar si un DNI ya existe (para validación).
    boolean existeDni(String dni);

    // Define un método para buscar empleados por nombre (búsqueda parcial).
    // Usado en las vistas de Admin y Reclutador.
    List<Empleado> buscarPorNombre(String nombre);
}