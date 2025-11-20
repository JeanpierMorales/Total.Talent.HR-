package Modelo.Repository;

import Modelo.Contrato;
import java.util.Date;
import java.util.List;

// --- Interfaz ContratoRepository ---
// Esta interfaz define el contrato (las operaciones CRUD) para la entidad Contrato.
// El Facade depende de esta interfaz para gestionar contratos.
public interface ContratoRepository {

    // Define un método para guardar un nuevo contrato.
    void guardar(Contrato contrato);

    // Define un método para actualizar un contrato existente.
    void actualizar(Contrato contrato);

    // Define un método para eliminar un contrato por su ID.
    void eliminar(int idContrato);

    // Define un método para buscar un contrato por su ID.
    Contrato buscarPorId(int idContrato);

    // Define un método para buscar todos los contratos de un empleado.
    List<Contrato> buscarPorEmpleado(int idEmpleado);

    // Define un método para buscar contratos por tipo (Planilla, Parcial, etc.).
    List<Contrato> buscarPorTipo(String tipoContrato);

    // Define un método para obtener todos los contratos.
    List<Contrato> obtenerTodos();

    // Define un método para buscar solo los contratos activos.
    List<Contrato> buscarContratosActivos();

    // Define un método para buscar contratos por un rango de fechas.
    List<Contrato> buscarPorRangoFechas(Date fechaInicio, Date fechaFin);
}