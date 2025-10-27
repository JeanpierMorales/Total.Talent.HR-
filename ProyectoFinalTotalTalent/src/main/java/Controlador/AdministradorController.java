package Controlador;


import Modelo.Contrato;
import java.sql.SQLException;
import java.util.List;
import Modelo.Facade.SistemaGestionFacade;
import Modelo.Empleado;
import Modelo.EmpleadoFactory;

// Controlador que sirve de puente entre las vistas Swing y la fachada del sistema
public class AdministradorController {

    private final SistemaGestionFacade facade;

    public AdministradorController(SistemaGestionFacade facade) {
        this.facade = facade;
    }

    public void crearEmpleado(Empleado e) throws SQLException {
        facade.registrarEmpleado(e);
    }

    // helper que usa la factory para crear empleado segun tipo
    public void crearEmpleadoDesdeFactory(String tipo, String nombre, String apellido, String correo, String password, String cargo, double salario) throws SQLException {
        Empleado e = EmpleadoFactory.crearEmpleado(tipo, nombre, apellido, correo, password, cargo, salario);
        facade.registrarEmpleado(e);
    }

    public void actualizarEmpleado(Empleado e) throws SQLException {
        facade.actualizarEmpleado(e);
    }

    public void eliminarEmpleado(int id) throws SQLException {
        facade.eliminarEmpleado(id);
    }

    public List<Empleado> listarEmpleados() throws SQLException {
        return facade.listarEmpleados();
    }

    public List<Empleado> buscarEmpleados(String texto) throws SQLException {
        return facade.buscarEmpleados(texto);
    }

    public Empleado buscarEmpleadoPorCorreo(String correo) throws SQLException {
        return facade.buscarEmpleadoPorCorreo(correo);

    }

    public Empleado buscarEmpleadoPorId(int id) throws SQLException {
        return facade.obtenerEmpleadoPorId(id);
    }

    public void registrarLoginEmpleado(int id) throws SQLException {
        facade.registrarLoginEmpleado(id);
    }

    public void crearContrato(String tipo, String duracion, double monto, String beneficios, String op1, String op2, String op3, Integer empleadoId) throws SQLException {
        facade.registrarContratoDesdeFactory(tipo, duracion, monto, beneficios, op1, op2, op3, empleadoId);
    }

    public List<Contrato> listarContratos() throws SQLException {
        return facade.listarContratos();
    }

    public void eliminarContrato(int id) throws SQLException {
        facade.eliminarContrato(id);
    }
}
