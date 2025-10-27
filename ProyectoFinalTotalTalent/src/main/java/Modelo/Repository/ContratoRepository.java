package Modelo.Repository;

import Modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Repositorio para contratos con operaciones basicas
public class ContratoRepository {

    private final Connection conn;

    public ContratoRepository(Connection conn) {
        this.conn = conn;
    }

    public void crear(Contrato c) throws SQLException {
        String sql = "INSERT INTO contrato(tipo, duracion, monto, beneficios, campo_opcional_1, campo_opcional_2, campo_opcional_3, empleado_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getDuracion());
            ps.setDouble(3, c.getMonto());
            ps.setString(4, c.getBeneficios());
            ps.setString(5, c.getCampoOpcional1());
            ps.setString(6, c.getCampoOpcional2());
            ps.setString(7, c.getCampoOpcional3());
            if (c.getEmpleadoId() != null) {
                ps.setInt(8, c.getEmpleadoId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Contrato c) throws SQLException {
        String sql = "UPDATE contrato SET tipo=?, duracion=?, monto=?, beneficios=?, campo_opcional_1=?, campo_opcional_2=?, campo_opcional_3=?, empleado_id=? WHERE id_contrato = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getTipo());
            ps.setString(2, c.getDuracion());
            ps.setDouble(3, c.getMonto());
            ps.setString(4, c.getBeneficios());
            ps.setString(5, c.getCampoOpcional1());
            ps.setString(6, c.getCampoOpcional2());
            ps.setString(7, c.getCampoOpcional3());
            if (c.getEmpleadoId() != null) {
                ps.setInt(8, c.getEmpleadoId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setInt(9, c.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM contrato WHERE id_contrato = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Contrato buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM contrato WHERE id_contrato = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearContrato(rs);
                }
            }
        }
        return null;
    }

    public List<Contrato> listarTodos() throws SQLException {
        String sql = "SELECT * FROM contrato ORDER BY id_contrato";
        List<Contrato> lista = new ArrayList<>();
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearContrato(rs));
            }
        }
        return lista;
    }

    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Contrato c;
        switch (tipo.toLowerCase()) {
            case "planilla":
                c = new ContratoPlanilla();
                break;
            case "locacion":
                c = new ContratoLocacion();
                break;
            case "parcial":
                c = new ContratoParcial();
                break;
            default:
                c = new ContratoPlanilla();
                break;
        }
        c.setId(rs.getInt("id_contrato"));
        c.setTipo(tipo);
        c.setDuracion(rs.getString("duracion"));
        c.setMonto(rs.getDouble("monto"));
        c.setBeneficios(rs.getString("beneficios"));
        c.setCampoOpcional1(rs.getString("campo_opcional_1"));
        c.setCampoOpcional2(rs.getString("campo_opcional_2"));
        c.setCampoOpcional3(rs.getString("campo_opcional_3"));
        int empId = rs.getInt("empleado_id");
        if (!rs.wasNull()) {
            c.setEmpleadoId(empId);
        }
        return c;
    }
}
