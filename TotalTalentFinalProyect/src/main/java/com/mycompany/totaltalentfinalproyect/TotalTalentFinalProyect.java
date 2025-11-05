
package com.mycompany.totaltalentfinalproyect;

import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.DatabaseConnection;
import Modelo.Factory.ContratoFactory;
import Modelo.Repository.ContratoMysqlRepository;
import Modelo.Repository.EmpleadoMysqlRepository;
import Modelo.Repository.LogArchivoRepository;
import Modelo.Repository.UsuarioMysqlRepository;
import Modelo.Rol;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Omar Morales Silva
 */
public class TotalTalentFinalProyect {

    public static void main(String[] args) {
        /*
        Empleado emp = new Empleado();
        emp.setNombre("Omar Morales");
        emp.setDni("234566");
        emp.setRol(Rol.ADMINISTRADOR);
        System.out.println(emp.obtenerDatosEmpleado());

        System.out.println("--------------------------------");
        Contrato contrato = new ContratoFactory().crearContrato("planilla");
        contrato.setEmpleado(emp);
        contrato.setSalarioBase(34.5);
        System.out.println(contrato.mostrarDetalle());

        Contrato contratoFinal = new ContratoBuilder(contrato).conBonificacion(50).build();

        System.out.println(contratoFinal.mostrarDetalle());
        System.out.println("Sueldo Calculado: "+ contratoFinal.calcularSueldo());
        System.out.println("Señor: " + contratoFinal.getEmpleado().getNombre()+ ", Usted tiene una bonificación de: " + contratoFinal.getBonificacion());
        
        Usuario us = new Usuario();
        us.setActivo(true);
        us.setEmpleado(emp);
        System.out.println(us.toString());*/
        
        
         Connection connection = null;

        try {
            // Obtener conexión a la base de datos
            connection = DatabaseConnection.getConnection();
            System.out.println("Conexión a la base de datos exitosa!");

            // Inicializar repositorios
            EmpleadoMysqlRepository empleadoRepo = new EmpleadoMysqlRepository(connection);
            ContratoMysqlRepository contratoRepo = new ContratoMysqlRepository(connection);
            UsuarioMysqlRepository usuarioRepo = new UsuarioMysqlRepository(connection);
            LogArchivoRepository logRepo = new LogArchivoRepository();

            // Ejemplo 1: Crear y guardar un empleado
            System.out.println("\n--- Creando empleado de prueba ---");
            Empleado empleado = new Empleado();
            empleado.setNombre("Frabrizzio");
            empleado.setApellidos("Vega Espinoza");
            empleado.setEdad(23);
            empleado.setDni("74757877");
            empleado.setNumero("987655441");
            empleado.setCorreo("Fabz.vega@gmail.com");
            empleado.setDireccion("Av principal, Callao");
            empleado.setGradoInstruccion("Universitario");
            empleado.setCarrera("Ingeniería de Software");
            empleado.setComentarios(" Nueva contraración del trabajador ");
            empleado.setRol(Rol.RECLUTADOR);

            empleadoRepo.guardar(empleado);
            System.out.println("Empleado guardado con ID: " + empleado.getIdEmpleado());

            // Registrar la creación en logs
            logRepo.registrarCreacion("admin", "empleado", empleado.getIdEmpleado());

            // Ejemplo 2: Buscar empleado por ID
            System.out.println("\n--- Buscando empleado por ID ---");
            Empleado empleadoEncontrado = empleadoRepo.buscarPorId(empleado.getIdEmpleado());
            if (empleadoEncontrado != null) {
                System.out.println("Empleado encontrado: " + empleadoEncontrado.getNombre() + " " + empleadoEncontrado.getApellidos());
            }

            // Ejemplo 3: Crear y guardar un contrato
            System.out.println("\n--- Creando contrato de prueba ---");
            Contrato contrato = ContratoFactory.crearContrato("PLANILLA");
            contrato.setEmpleado(empleado);
            contrato.setFechaInicio(new Date());
            contrato.setFechaFin(null); // Contrato indefinido
            contrato.setSalarioBase(2500.0);
            contrato.setBonificacion(300.0);
            contrato.setDescuentoAFP(50.0);

            contratoRepo.guardar(contrato);
            System.out.println("Contrato guardado con ID: " + contrato.getIdContrato());

            // Registrar la creación en logs
            logRepo.registrarCreacion("admin", "contrato", contrato.getIdContrato());

            // Ejemplo 4: Obtener todos los empleados
            System.out.println("\n--- Lista de todos los empleados ---");
            List<Empleado> empleados = empleadoRepo.obtenerTodos();
            for (Empleado emp : empleados) {
                System.out.println("- " + emp.getNombre() + " " + emp.getApellidos() + " (Rol: " + emp.getRol() + ")");
            }

            // Ejemplo 5: Obtener contratos por empleado
            System.out.println("\n--- Contratos del empleado ---");
            List<Contrato> contratos = contratoRepo.buscarPorEmpleado(empleado.getIdEmpleado());
            for (Contrato cont : contratos) {
                System.out.println("- Tipo: " + cont.getTipoContrato() + ", Salario: " + cont.getSalarioBase());
            }

            // Ejemplo 6: Registrar un login exitoso
            System.out.println("\n--- Registrando login exitoso ---");
            logRepo.registrarLoginExitoso("admin");

            // Ejemplo 7: Obtener logs por usuario
            System.out.println("\n--- Logs del usuario 'admin' ---");
            List<String> logs = logRepo.obtenerLogsPorUsuario("admin");
            for (String log : logs) {
                System.out.println(log);
            }

            System.out.println("\n--- Pruebas completadas exitosamente ---");

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar conexión
            if (connection != null) {
                DatabaseConnection.closeConnection(connection);
                System.out.println("Conexión cerrada.");
            }
        }
            
    }
}
