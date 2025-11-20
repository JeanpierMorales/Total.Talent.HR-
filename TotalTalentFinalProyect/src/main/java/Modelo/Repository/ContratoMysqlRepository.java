package Modelo.Repository;

import Modelo.Contrato;
import Modelo.Factory.ContratoFactory; // Importa el Factory.
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// --- Implementación del Repositorio de Contratos (MySQL) ---
// Esta clase implementa la interfaz ContratoRepository.
// Es la clase más compleja del repositorio porque debe manejar
// las 3 subclases de Contrato (Planilla, Parcial, Locacion) al
// guardar, actualizar y leer de la base de datos.
public class ContratoMysqlRepository implements ContratoRepository {

    private Connection connection; // Conexión inyectada por el Facade.

    // Constructor que recibe la conexión.
    public ContratoMysqlRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    // Método para guardar un nuevo contrato en la base de datos.
    public void guardar(Contrato contrato) {
        // Esta sentencia SQL incluye *todas* las columnas posibles para *todos* los tipos de contrato.
        String sql = "INSERT INTO contrato (id_empleado, tipo_contrato, fecha_inicio, fecha_fin, salario_base, "
                + "bonificacion, descuento_afp, horas_extras, horas_trabajadas, pago_por_hora, monto_por_proyecto, numero_proyectos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // --- Mapeo de campos comunes ---
            stmt.setInt(1, contrato.getEmpleado().getIdEmpleado());
            stmt.setString(2, contrato.getTipoContrato());
            stmt.setDate(3, new java.sql.Date(contrato.getFechaInicio().getTime()));
            if (contrato.getFechaFin() != null) {
                stmt.setDate(4, new java.sql.Date(contrato.getFechaFin().getTime()));
            } else {
                stmt.setNull(4, Types.DATE); // Permite fechas de fin nulas (indefinidas).
            }
            stmt.setBigDecimal(5, BigDecimal.valueOf(contrato.getSalarioBase()));
            stmt.setBigDecimal(6, BigDecimal.valueOf(contrato.getBonificacion()));
            stmt.setBigDecimal(7, BigDecimal.valueOf(contrato.getDescuentoAFP()));

            // --- Mapeo de campos específicos ---
            // Aquí se usa 'instanceof' para determinar qué tipo de contrato estamos guardando
            // y rellenar las columnas correctas, dejando las demás en CERO.
            if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
                Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
                stmt.setBigDecimal(8, BigDecimal.valueOf(cp.getHorasExtras())); // Específico Planilla
                stmt.setBigDecimal(9, BigDecimal.ZERO);
                stmt.setBigDecimal(10, BigDecimal.ZERO);
                stmt.setBigDecimal(11, BigDecimal.ZERO);
                stmt.setInt(12, 0);
            } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
                Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
                stmt.setBigDecimal(8, BigDecimal.ZERO);
                stmt.setInt(9, cp.getHorasTrabajadas()); // Específico Parcial
                stmt.setBigDecimal(10, BigDecimal.valueOf(cp.getPagoPorHora())); // Específico Parcial
                stmt.setBigDecimal(11, java.math.BigDecimal.ZERO);
                stmt.setInt(12, 0);
            } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
                Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
                stmt.setBigDecimal(8, java.math.BigDecimal.ZERO);
                stmt.setInt(9, 0);
                stmt.setBigDecimal(10, java.math.BigDecimal.ZERO);
                stmt.setBigDecimal(11, BigDecimal.valueOf(cl.getMontoPorProyecto())); // Específico Locacion
                stmt.setInt(12, cl.getNumeroProyectos()); // Específico Locacion
            }

            stmt.executeUpdate(); // Ejecuta la inserción.

            // Obtener el ID auto-generado.
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    contrato.setIdContrato(rs.getInt(1)); // Asigna el ID al objeto.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar contrato: " + e.getMessage());
        }
    }

    @Override
    // Método para actualizar un contrato existente.
    public void actualizar(Contrato contrato) {
        // Similar a 'guardar', la sentencia SQL incluye todas las columnas específicas.
        String sql = "UPDATE contrato SET fecha_fin = ?, salario_base = ?, bonificacion = ?, descuento_afp = ?, "
                + "horas_extras = ?, horas_trabajadas = ?, pago_por_hora = ?, monto_por_proyecto = ?, numero_proyectos = ? "
                + "WHERE id_contrato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // --- Mapeo de campos comunes ---
            if (contrato.getFechaFin() != null) {
                stmt.setDate(1, new java.sql.Date(contrato.getFechaFin().getTime()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setBigDecimal(2, BigDecimal.valueOf(contrato.getSalarioBase()));
            stmt.setBigDecimal(3, BigDecimal.valueOf(contrato.getBonificacion()));
            stmt.setBigDecimal(4, BigDecimal.valueOf(contrato.getDescuentoAFP()));

            // --- Mapeo de campos específicos (usando 'instanceof') ---
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
            
            // ID para el WHERE.
            stmt.setInt(10, contrato.getIdContrato());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contrato: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar un contrato por su ID.
    public void eliminar(int idContrato) {
        String sql = "DELETE FROM contrato WHERE id_contrato = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idContrato); // Setea el Id del contrato.
            stmt.executeUpdate(); // Ejecuta la sentencia.
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contrato: " + e.getMessage());
        }
    }

    @Override
    // Método para buscar un contrato por su ID.
    public Contrato buscarPorId(int idContrato) {
        // Se une con 'empleado' para obtener el nombre y apellido del empleado.
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.id_contrato = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idContrato);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {   
                    // Llama al helper 'mapearContrato' para construir el objeto.
                    return mapearContrato(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contrato por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    // Método para buscar contratos por ID de empleado.
    public List<Contrato> buscarPorEmpleado(int idEmpleado) {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.id_empleado = ? ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Mapea cada contrato encontrado y lo añade a la lista.
                    contratos.add(mapearContrato(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos por empleado: " + e.getMessage());
        }
        return contratos;
    }

    @Override
    // Método para buscar contratos por tipo de contrato.
    public List<Contrato> buscarPorTipo(String tipoContrato) {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT c.*, e.nombre, e.apellidos FROM contrato c "
                + "JOIN empleado e ON c.id_empleado = e.id_empleado WHERE c.tipo_contrato = ? ORDER BY c.fecha_inicio DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoContrato); // Filtra por el string del tipo.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(mapearContrato(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contratos por tipo: " + e.getMessage());
        }
        return contratos;
    }

    @Override
    // Método para obtener todos los contratos.
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

    @Override
    // Método para buscar contratos activos.
    public List<Contrato> buscarContratosActivos() {
        List<Contrato> contratos = new ArrayList<>();
        // La lógica de "activo" es: fecha_fin ES NULA (indefinido) O la fecha_fin
        // es mayor o igual a la fecha actual (CURDATE()).
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

    @Override
    // Método para buscar contratos dentro de un rango de fechas.
    public List<Contrato> buscarPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<Contrato> contratos = new ArrayList<>();
        // Usa BETWEEN para filtrar por la fecha de inicio.
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

    // --- Método de Mapeo Clave ---
    // Este método auxiliar es crucial: convierte una fila de la BD (ResultSet)
    // en un objeto Contrato (Planilla, Parcial o Locacion).
    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        
        // 1. Lee la columna "tipo_contrato" (ej. "Planilla").
        String tipoContrato = rs.getString("tipo_contrato");
        
        // 2. Usa el ContratoFactory para crear la instancia correcta.
        //    Si tipoContrato es "Planilla", la variable 'contrato' será
        //    un objeto de tipo ContratoPlanilla.
        Contrato contrato = ContratoFactory.crearContrato(tipoContrato);

        // 3. Mapea todos los campos comunes heredados por Contrato.
        contrato.setIdContrato(rs.getInt("id_contrato"));
        contrato.getEmpleado().setIdEmpleado(rs.getInt("id_empleado"));
        contrato.getEmpleado().setNombre(rs.getString("nombre"));
        contrato.getEmpleado().setApellidos(rs.getString("apellidos"));
        contrato.setFechaInicio(rs.getDate("fecha_inicio"));
        contrato.setFechaFin(rs.getDate("fecha_fin"));
        contrato.setSalarioBase(rs.getBigDecimal("salario_base").floatValue());
        contrato.setBonificacion(rs.getBigDecimal("bonificacion").floatValue());
        contrato.setDescuentoAFP(rs.getBigDecimal("descuento_afp").floatValue());

        // 4. Mapea los campos específicos usando 'instanceof'.
        //    Esto nos permite llamar a los setters específicos de cada subclase.
        if (contrato instanceof Modelo.Factory.ContratoPlanilla) {
            // Si el factory creó un ContratoPlanilla, le asignamos las horas extras.
            Modelo.Factory.ContratoPlanilla cp = (Modelo.Factory.ContratoPlanilla) contrato;
            cp.setHorasExtras(rs.getBigDecimal("horas_extras").floatValue());
        } else if (contrato instanceof Modelo.Factory.ContratoParcial) {
            // Si creó un ContratoParcial, le asignamos horas trabajadas y pago por hora.
            Modelo.Factory.ContratoParcial cp = (Modelo.Factory.ContratoParcial) contrato;
            cp.setHorasTrabajadas(rs.getInt("horas_trabajadas"));
            cp.setPagoPorHora(rs.getBigDecimal("pago_por_hora").floatValue());
        } else if (contrato instanceof Modelo.Factory.ContratoLocacion) {
            // Si creó un ContratoLocacion, le asignamos datos del proyecto.
            Modelo.Factory.ContratoLocacion cl = (Modelo.Factory.ContratoLocacion) contrato;
            cl.setMontoPorProyecto(rs.getBigDecimal("monto_por_proyecto").floatValue());
            cl.setNumeroProyectos(rs.getInt("numero_proyectos"));
        }

        return contrato; // Devuelve el objeto contrato completo y del tipo correcto.
    }
}