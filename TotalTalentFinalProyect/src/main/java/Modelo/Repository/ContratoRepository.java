package Modelo.Repository;

import Modelo.Contrato;
import java.util.Date;
import java.util.List;

// Interfaz para el repositorio de contratos que define las operaciones CRUD para la entidad Contrato

public interface ContratoRepository {

    // Método para guardar un nuevo contrato en la base de datos
    void guardar(Contrato contrato);

    // Método para actualizar un contrato existente
    void actualizar(Contrato contrato);

    // Método para eliminar un contrato por su ID
    void eliminar(int idContrato);

    // Método para buscar un contrato por su ID
    Contrato buscarPorId(int idContrato);

    // Método para buscar contratos por ID de empleado
    List<Contrato> buscarPorEmpleado(int idEmpleado);

    // Método para buscar contratos por tipo
    List<Contrato> buscarPorTipo(String tipoContrato);

    // Método para obtener todos los contratos
    List<Contrato> obtenerTodos();

    // Método para buscar contratos activos sin fecha de final
    List<Contrato> buscarContratosActivos();

    // Método para buscar contratos por rango de fechas
    List<Contrato> buscarPorRangoFechas(Date fechaInicio, Date fechaFin);
}
