package Modelo.Facade;

import Modelo.*;
import Modelo.Repository.ContratoRepository;
import Modelo.Repository.EmpleadoRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Facade que unifica operaciones de alto nivel entre repositorios y builders.
 */
public class SistemaGestionFacade {

    private final EmpleadoRepository empleadoRepo;
    private final ContratoRepository contratoRepo;
    private final ContratoFactory factory;

    public SistemaGestionFacade(Connection conn) {
        this.empleadoRepo = new EmpleadoRepository(conn);
        this.contratoRepo = new ContratoRepository(conn);
        this.factory = new ContratoFactory();
    }

    // ===== Empleado CRUD =====
    public void registrarEmpleado(Empleado e) throws SQLException {
        empleadoRepo.crear(e);
    }

    public void actualizarEmpleado(Empleado e) throws SQLException {
        empleadoRepo.actualizar(e);
    }

    public void eliminarEmpleado(int id) throws SQLException {
        empleadoRepo.eliminar(id);
    }

    public List<Empleado> listarEmpleados() throws SQLException {
        return empleadoRepo.listarTodos();
    }

    public List<Empleado> buscarEmpleados(String texto) throws SQLException {
        return empleadoRepo.buscarPorTexto(texto);
    }

    public Empleado buscarEmpleadoPorCorreo(String correo) throws SQLException {
        return empleadoRepo.buscarPorCorreo(correo);
    }

    public void registrarLoginEmpleado(int id) throws SQLException {
        empleadoRepo.registrarLogin(id);
    }

    public Empleado obtenerEmpleadoPorId(int id) throws SQLException {
        return empleadoRepo.obtenerPorId(id);
    }

    // ===== Contratos =====
    public void registrarContratoDesdeFactory(String tipo, String duracion, double monto, String beneficios,
            String op1, String op2, String op3, Integer empleadoId) throws SQLException {
        // Crear contrato base desde la factory
        Contrato base = factory.crearContrato(tipo);

        // Construir el contrato con el builder
        ContratoConcretoBuilder builder = new ContratoConcretoBuilder(base);
        builder.setTipo(tipo);
        builder.setDuracion(duracion);
        builder.setMonto(monto);
        builder.setBeneficios(beneficios);
        builder.setCampoOpcional1(op1);
        builder.setCampoOpcional2(op2);
        builder.setCampoOpcional3(op3);
        builder.setEmpleadoId(empleadoId);

        // Obtener resultado final
        Contrato contratoFinal = builder.getResultado();

        // Guardar en la base de datos
        contratoRepo.crear(contratoFinal);
    }

    public List<Contrato> listarContratos() throws SQLException {
        return contratoRepo.listarTodos();
    }

    public void eliminarContrato(int id) throws SQLException {
        contratoRepo.eliminar(id);
    }
}
