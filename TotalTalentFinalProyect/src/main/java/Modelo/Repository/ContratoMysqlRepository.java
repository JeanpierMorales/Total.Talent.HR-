package Modelo.Repository;

import Modelo.Contrato;
import Modelo.Factory.ContratoFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ContratoMysqlRepository implements ContratoRepository {

    private Connection connection;

    // Constructor que recibe la conexión a la base de datos
    public ContratoMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo contrato en la base de datos
    public void guardar(Contrato contrato) {
        // Sentencia SQL para insertar un nuevo contrato
        String sql = "INSERT INTO contrato (id_empleado, tipo_contrato, fecha_inicio, fecha_fin, salario_base, "
                + "bonificacion, descuento_afp, horas_extras, horas_trabajadas, pago_por_hora, monto_por_proyecto, numero_proyectos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Usamos PreparedStatement para prevenir SQL Injection y manejar parámetros de manera segura 
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // retorna las llaves generadas 
            stmt.setInt(1, contrato.getEmpleado().getIdEmpleado()); // ID del empleado asociado al contrato
            stmt.setString(2, contrato.getTipoContrato()); // Tipo de contrato
            stmt.setDate(3, new java.sql.Date(contrato.getFechaInicio().getTime())); // Fecha de inicio del contrato
            if (contrato.getFechaFin() != null) { // Fecha de fin del contrato, puede ser null
                stmt.setDate(4, new java.sql.Date(contrato.getFechaFin().getTime())); // Fecha de fin del contrato
            } else {
                stmt.setNull(4, Types.DATE); // Si es null, se establece como NULL en la base de datos
            }
            stmt.setBigDecimal(5, BigDecimal.valueOf(contrato.getSalarioBase())); // Salario base del contrato
            stmt.setBigDecimal(6, BigDecimal.valueOf(contrato.getBonificacion())); // Bonificación del contrato
            stmt.setBigDecimal(7, BigDecimal.valueOf(contrato.getDescuentoAFP())); // Descuento AFP del contrato solo si aplica

            // Campos específicos según el tipo de contrato, solo se van a completar dependiendo del tipo
            if (contrato instanceof Modelo.Factory.ContratoPlanilla) { // Contrato por planilla
                // Cast al tipo específico de contrato 
                Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
                stmt.setBigDecimal(8, BigDecimal.valueOf(cp.getHorasExtras())); // Horas extras trabajadas
                stmt.setBigDecimal(9, BigDecimal.ZERO); // Horas trabajadas no aplican
                stmt.setBigDecimal(10, BigDecimal.ZERO); // Pago por hora no aplica
                stmt.setBigDecimal(11, BigDecimal.ZERO); // Monto por proyecto no aplica 
                stmt.setInt(12, 0); // Número de proyectos no aplica
            } else if (contrato instanceof Modelo.Factory.ContratoParcial) { // Contrato parcial
                Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato; // Cast al tipo específico de contrato
                stmt.setBigDecimal(8, BigDecimal.ZERO); // Horas extras no aplican
                stmt.setInt(9, cp.getHorasTrabajadas()); // Horas trabajadas
                stmt.setBigDecimal(10, BigDecimal.valueOf(cp.getPagoPorHora())); // Pago por hora
                stmt.setBigDecimal(11, java.math.BigDecimal.ZERO); // Monto por proyecto no aplica
                stmt.setInt(12, 0); // Número de proyectos no aplica
            } else if (contrato instanceof Modelo.Factory.ContratoLocacion) { // Contrato de locación de servicios
                Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
                stmt.setBigDecimal(8, java.math.BigDecimal.ZERO); // Horas extras no aplican
                stmt.setInt(9, 0); // Horas trabajadas no aplican
                stmt.setBigDecimal(10, java.math.BigDecimal.ZERO); // Pago por hora no aplica
                stmt.setBigDecimal(11, BigDecimal.valueOf(cl.getMontoPorProyecto())); // Monto por proyecto
                stmt.setInt(12, cl.getNumeroProyectos()); // Número de proyectos
            }

            stmt.executeUpdate(); // Ejecutar la inserción a la base de datos

            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) { // Obtener las llaves generadas
                if (rs.next()) { // Si hay una llave generada
                    // Asignar el ID generado al objeto contrato
                    contrato.setIdContrato(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            // Manejo de errores en caso de fallo en la operación SQL
            throw new RuntimeException("Error al guardar contrato: " + e.getMessage());
        }
    }

    // Método para actualizar un contrato existente en la base de datos
    @Override
    public void actualizar(Contrato contrato) {
        // Sentencia SQL para actualizar un contrato
        String sql = "UPDATE contrato SET fecha_fin = ?, salario_base = ?, bonificacion = ?, descuento_afp = ?, "
                + "horas_extras = ?, horas_trabajadas = ?, pago_por_hora = ?, monto_por_proyecto = ?, numero_proyectos = ? "
                + "WHERE id_contrato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (contrato.getFechaFin() != null) { // Fecha de fin del contrato puede ser null
                stmt.setDate(1, new java.sql.Date(contrato.getFechaFin().getTime())); // Fecha de fin del contrato
            } else {
                stmt.setNull(1, Types.DATE); // Si es null, se establece como NULL en la base de datos
            }
            stmt.setBigDecimal(2, BigDecimal.valueOf(contrato.getSalarioBase())); // Salario base del contrato
            stmt.setBigDecimal(3, BigDecimal.valueOf(contrato.getBonificacion())); // Bonificación del contrato
            stmt.setBigDecimal(4, BigDecimal.valueOf(contrato.getDescuentoAFP())); // Descuento AFP del contrato

            // Campos específicos según el tipo de contrato, al igual que el anterior solo se completan dependiendo del tipo
            //  lo que no corresponda al tipo será obviado colocando Zero
            if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
                Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
                stmt.setBigDecimal(5, BigDecimal.valueOf(cp.getHorasExtras()));
                stmt.setInt(6, 0);
                stmt.setBigDecimal(7, java.math.BigDecimal.ZERO);
                stmt.setBigDecimal(8, java.math.BigDecimal.ZERO);
                stmt.setInt(9, 0);
            } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
                Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
                stmt.setBigDecimal(5, java.math.BigDecimal.ZERO);
                stmt.setInt(6, cp.getHorasTrabajadas());
                stmt.setBigDecimal(7, BigDecimal.valueOf(cp.getPagoPorHora()));
                stmt.setBigDecimal(8, java.math.BigDecimal.ZERO);
                stmt.setInt(9, 0);
            } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
                Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
                stmt.setBigDecimal(5, java.math.BigDecimal.ZERO);
                stmt.setInt(6, 0);
                stmt.setBigDecimal(7, java.math.BigDecimal.ZERO);
                stmt.setBigDecimal(8, BigDecimal.valueOf(cl.getMontoPorProyecto()));
                stmt.setInt(9, cl.getNumeroProyectos());
            }
            stmt.setInt(10, contrato.getIdContrato());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contrato: " + e.getMessage());
        }
    }

    // Método para eliminar un contrato que existe a través del ID
    @Override
    public void eliminar(int idContrato) {
        // Sentencia para eliminar un contraro de la base de datos con el uso de su ID
        String sql = "DELETE FROM contrato WHERE id_contrato = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idContrato); // Settea el Id del contraro
            stmt.executeUpdate(); //  Ejecuta la sentencia 
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contrato: " + e.getMessage());
        }
    }

    // Método para buscar un contrato con el uso de su ID
    @Override
    public Contrato buscarPorId(int idContrato) {

        // Preparación de la sentencua SQL para ejecutar la busqueda 
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.id_contrato = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { // Prueba la sentencia para llevarla a la base de datos
            stmt.setInt(1, idContrato); // Ingresa el Id del contrato a buscar
            try (ResultSet rs = stmt.executeQuery()) { // Ejecuta la sentencia y obtiene el resultado
                if (rs.next()) {   // Si hay un resultado, mapea el resultado a un objeto Contrato 
                    return mapearContrato(rs); // Retorna el contrato mapeado
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contrato por ID: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar contratos por ID de empleado
    @Override
    public List<Contrato> buscarPorEmpleado(int idEmpleado) {

        List<Contrato> contratos = new ArrayList<>(); // Lista para almacenar los contratos encontrados
        // Sentencia SQl para buscar empleado por Id
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.id_empleado = ? ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado); // Settea el Id del empleado a buscar
            try (ResultSet rs = stmt.executeQuery()) { // Ejecuta la sentencia y obtiene el resultado
                while (rs.next()) { // next es para recorrer los resultados
                    // Mapea cada fila del resultado a un objeto Contrato y lo agrega a la lista
                    contratos.add(mapearContrato(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos por empleado: " + e.getMessage());
        }
        return contratos; // Devuelve la lista de contratos encontrados
    }

    // Método para buscar contratos por tipo de contrato
    @Override
    public List<Contrato> buscarPorTipo(String tipoContrato) {
        List<Contrato> contratos = new ArrayList<>(); // Lista para almacenar los contratos encontrados
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.tipo_contrato = ? ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoContrato); // Settea el tipo de contrato a buscar
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(mapearContrato(rs)); // Mapea cada fila del resultado a un objeto Contrato y lo agrega a la lista
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos por tipo: " + e.getMessage());
        }
        return contratos;
    }

    // Método para obtener todos los contratos en la base de datos
    @Override
    public List<Contrato> obtenerTodos() {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contratos.add(mapearContrato(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los contratos: " + e.getMessage());
        }
        return contratos;
    }

    // Método para buscar contratos activos (sin fecha de fin o con fecha de fin futura)
    @Override
    public List<Contrato> buscarContratosActivos() {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado "
                + "WHERE c.fecha_fin IS NULL OR c.fecha_fin >= CURDATE() ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contratos.add(mapearContrato(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos activos: " + e.getMessage());
        }
        return contratos;
    }

    // Método para buscar contratos dentro de un rango de fechas  
    @Override
    public List<Contrato> buscarPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado "
                + "WHERE c.fecha_inicio BETWEEN ? AND ? ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(mapearContrato(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos por rango de fechas: " + e.getMessage());
        }
        return contratos;
    }

    // Método auxiliar para mapear ResultSet a objeto Contrato
    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        String tipoContrato = rs.getString("tipo_contrato");
        Contrato contrato = ContratoFactory.crearContrato(tipoContrato);

        contrato.setIdContrato(rs.getInt("id_contrato"));
        contrato.getEmpleado().setIdEmpleado(rs.getInt("id_empleado"));
        contrato.setFechaInicio(rs.getDate("fecha_inicio"));
        contrato.setFechaFin(rs.getDate("fecha_fin"));
        contrato.setSalarioBase(rs.getBigDecimal("salario_base").doubleValue());
        contrato.setBonificacion(rs.getBigDecimal("bonificacion").doubleValue());
        contrato.setDescuentoAFP(rs.getBigDecimal("descuento_afp").doubleValue());

        // Mapear campos específicos según el tipo
        if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
            Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
            cp.setHorasExtras(rs.getBigDecimal("horas_extras").doubleValue());
        } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
            Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
            cp.setHorasTrabajadas(rs.getInt("horas_trabajadas"));
            cp.setPagoPorHora(rs.getBigDecimal("pago_por_hora").doubleValue());
        } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
            Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
            cl.setMontoPorProyecto(rs.getBigDecimal("monto_por_proyecto").doubleValue());
            cl.setNumeroProyectos(rs.getInt("numero_proyectos"));
        }

        return contrato;
    }
}
