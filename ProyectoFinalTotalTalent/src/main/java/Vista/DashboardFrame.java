package Vista;

import Modelo.*;
import Modelo.Facade.*;
import Controlador.*;
import Utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final Connection conn;
    private final AdministradorController controller;
    private final Empleado administrador;

    private JTable tblEmpleados;
    private JTable tblContratos;
    private DefaultTableModel modelEmpleados;
    private DefaultTableModel modelContratos;
    private JTextField txtBuscar;

    public DashboardFrame(Connection conn, Empleado administrador) {
        this.conn = conn;
        this.administrador = administrador;
        SistemaGestionFacade facade = new SistemaGestionFacade(conn);
        this.controller = new AdministradorController(facade);

        setTitle("Total Talent HR - Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        cargarDatos();

        // Cerrar conexión al salir
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        });
    }

    /**
     * Inicializa los componentes gráficos del Dashboard
     */
    private void initComponents() {
        // ===== Colores y fuentes =====
        Color primaryColor = new Color(52, 152, 219);       // Azul principal
        Color secondaryColor = new Color(46, 204, 113);     // Verde para agregar
        Color dangerColor = new Color(231, 76, 60);         // Rojo para eliminar
        Color lightGray = new Color(245, 245, 245);         // Fondo claro
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);

        // ===== Root panel =====
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(Color.WHITE);

        // ===== Top panel =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        JLabel lblBienvenida = new JLabel("Bienvenido " + administrador.getNombre());
        lblBienvenida.setFont(titleFont);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.add(lblBienvenida, BorderLayout.NORTH);

        // ===== Panel Empleados =====
        JPanel empleadosPanel = new JPanel(new BorderLayout(10, 10));
        empleadosPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor, 2, true), "Empleados de Total Talent"));
        empleadosPanel.setBackground(lightGray);

        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        busquedaPanel.setBackground(lightGray);
        txtBuscar = new JTextField(30);
        txtBuscar.setFont(mainFont);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Refrescar");

        // Estilo botones
        btnBuscar.setBackground(primaryColor);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnBuscar.setFont(mainFont);

        btnRefrescar.setBackground(secondaryColor);
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnRefrescar.setFont(mainFont);

        busquedaPanel.add(new JLabel("Buscar:"));
        busquedaPanel.add(txtBuscar);
        busquedaPanel.add(btnBuscar);
        busquedaPanel.add(btnRefrescar);

        // Tabla empleados
        modelEmpleados = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido", "Correo", "Cargo", "Salario"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados = new JTable(modelEmpleados);
        tblEmpleados.setFont(mainFont);
        tblEmpleados.setRowHeight(26);
        tblEmpleados.setFillsViewportHeight(true);
        tblEmpleados.setShowGrid(true);
        tblEmpleados.setGridColor(Color.LIGHT_GRAY);
        tblEmpleados.setSelectionBackground(primaryColor);
        tblEmpleados.setSelectionForeground(Color.WHITE);

        JScrollPane spEmpleados = new JScrollPane(tblEmpleados);

        empleadosPanel.add(busquedaPanel, BorderLayout.NORTH);
        empleadosPanel.add(spEmpleados, BorderLayout.CENTER);

        // Panel de acciones
        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        accionesPanel.setBackground(lightGray);
        JButton btnAgregarEmpleado = new JButton("Agregar Empleado");
        JButton btnEditarEmpleado = new JButton("Editar Empleado");
        JButton btnEliminarEmpleado = new JButton("Eliminar Empleado");

        // Colores botones según funcionalidad
        btnAgregarEmpleado.setBackground(secondaryColor);
        btnAgregarEmpleado.setForeground(Color.WHITE);
        btnAgregarEmpleado.setFocusPainted(false);
        btnAgregarEmpleado.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnEditarEmpleado.setBackground(primaryColor);
        btnEditarEmpleado.setForeground(Color.WHITE);
        btnEditarEmpleado.setFocusPainted(false);
        btnEditarEmpleado.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnEliminarEmpleado.setBackground(dangerColor);
        btnEliminarEmpleado.setForeground(Color.WHITE);
        btnEliminarEmpleado.setFocusPainted(false);
        btnEliminarEmpleado.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        accionesPanel.add(btnAgregarEmpleado);
        accionesPanel.add(btnEditarEmpleado);
        accionesPanel.add(btnEliminarEmpleado);
        empleadosPanel.add(accionesPanel, BorderLayout.SOUTH);

        // ===== Panel Contratos =====
        modelContratos = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Duración", "Monto", "EmpleadoID", "EmpleadoNombre"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblContratos = new JTable(modelContratos);
        tblContratos.setFont(mainFont);
        tblContratos.setRowHeight(26);
        tblContratos.setFillsViewportHeight(true);
        tblContratos.setShowGrid(true);
        tblContratos.setGridColor(Color.LIGHT_GRAY);
        tblContratos.setSelectionBackground(secondaryColor);
        tblContratos.setSelectionForeground(Color.WHITE);
        JScrollPane spContratos = new JScrollPane(tblContratos);

        JPanel contratosPanel = new JPanel(new BorderLayout(10, 10));
        contratosPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor, 2, true), "Contratos"));
        contratosPanel.setBackground(lightGray);
        contratosPanel.add(spContratos, BorderLayout.CENTER);

        JPanel contratoBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        contratoBtns.setBackground(lightGray);
        JButton btnAgregarContrato = new JButton("Agregar Contrato");
        JButton btnEliminarContrato = new JButton("Eliminar Contrato");

        btnAgregarContrato.setBackground(secondaryColor);
        btnAgregarContrato.setForeground(Color.WHITE);
        btnAgregarContrato.setFocusPainted(false);
        btnAgregarContrato.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnEliminarContrato.setBackground(dangerColor);
        btnEliminarContrato.setForeground(Color.WHITE);
        btnEliminarContrato.setFocusPainted(false);
        btnEliminarContrato.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        contratoBtns.add(btnAgregarContrato);
        contratoBtns.add(btnEliminarContrato);
        contratosPanel.add(contratoBtns, BorderLayout.SOUTH);

        // ===== División vertical =====
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, empleadosPanel, contratosPanel);
        split.setDividerLocation(360);
        split.setBorder(null);

        root.add(top, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        add(root);

        // ===== Permisos y listeners =====
        boolean esAdmin = administrador.getCargo() != null
                && administrador.getCargo().equalsIgnoreCase("Administrador");
        boolean esReclutador = administrador.getCargo() != null
                && administrador.getCargo().equalsIgnoreCase("Reclutador");

        btnEditarEmpleado.setEnabled(esAdmin);
        btnEliminarEmpleado.setEnabled(esAdmin);
        btnAgregarContrato.setEnabled(esAdmin);
        btnEliminarContrato.setEnabled(esAdmin);
        btnAgregarEmpleado.setEnabled(esAdmin || esReclutador);

        btnBuscar.addActionListener(e -> onBuscar());
        btnRefrescar.addActionListener(e -> cargarDatos());
        btnAgregarEmpleado.addActionListener(this::onAgregarEmpleado);
        btnEditarEmpleado.addActionListener(this::onEditarEmpleado);
        btnEliminarEmpleado.addActionListener(this::onEliminarEmpleado);
        btnAgregarContrato.addActionListener(this::onAgregarContrato);
        btnEliminarContrato.addActionListener(this::onEliminarContrato);
    }

    private void cargarDatos() {
        try {
            modelEmpleados.setRowCount(0);
            modelContratos.setRowCount(0);
            List<Empleado> empleados = controller.listarEmpleados();
            for (Empleado e : empleados) {
                modelEmpleados.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getApellido(),
                    e.getCorreo(), e.getCargo(), e.getSalario()
                });
            }

            List<Contrato> contratos = controller.listarContratos();
            for (Contrato c : contratos) {
                String nombreEmp = "";
                if (c.getEmpleadoId() != null) {
                    Empleado emp = empleados.stream()
                            .filter(x -> x.getId().equals(c.getEmpleadoId()))
                            .findFirst()
                            .orElse(null);
                    if (emp != null) {
                        nombreEmp = emp.getNombre() + " " + emp.getApellido();
                    }
                }
                modelContratos.addRow(new Object[]{
                    c.getId(), c.getTipo(), c.getDuracion(),
                    c.getMonto(), c.getEmpleadoId(), nombreEmp
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage());
        }
    }

    private void onBuscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarDatos();
            return;
        }
        try {
            List<Empleado> lista = controller.buscarEmpleados(texto);
            modelEmpleados.setRowCount(0);
            for (Empleado e : lista) {
                modelEmpleados.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getApellido(),
                    e.getCorreo(), e.getCargo(), e.getSalario()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + ex.getMessage());
        }
    }

    private void onAgregarEmpleado(ActionEvent ev) {
        JTextField nombre = new JTextField();
        JTextField apellido = new JTextField();
        JTextField correo = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField cargo = new JTextField();
        JTextField salario = new JTextField();

        Object[] message = {
            "Nombre", nombre,
            "Apellido", apellido,
            "Correo (termina en .com)", correo,
            "Password (mín 8)", password,
            "Cargo", cargo,
            "Salario", salario
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuevo Empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String em = correo.getText().trim();
                String pw = new String(password.getPassword());

                if (!ValidationUtils.isValidEmail(em)) {
                    JOptionPane.showMessageDialog(this, "Correo inválido. Debe terminar en .com");
                    return;
                }
                if (!ValidationUtils.isValidPassword(pw)) {
                    JOptionPane.showMessageDialog(this, "Contraseña inválida. Mínimo 8 caracteres");
                    return;
                }

                boolean permitido = administrador.getCargo().equalsIgnoreCase("Administrador")
                        || administrador.getCargo().equalsIgnoreCase("Reclutador");
                if (!permitido) {
                    JOptionPane.showMessageDialog(this, "No tiene permiso para agregar empleados");
                    return;
                }

                controller.crearEmpleadoDesdeFactory(
                        cargo.getText(),
                        nombre.getText(),
                        apellido.getText(),
                        em,
                        pw,
                        cargo.getText(),
                        Double.parseDouble(salario.getText())
                );
                cargarDatos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar empleado: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Salario inválido. Ingrese un número válido.");
            }
        }
    }

    private void onEditarEmpleado(ActionEvent ev) {
        int fila = tblEmpleados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para editar");
            return;
        }

        int idEmpleado = (int) modelEmpleados.getValueAt(fila, 0);
        JTextField nombre = new JTextField((String) modelEmpleados.getValueAt(fila, 1));
        JTextField apellido = new JTextField((String) modelEmpleados.getValueAt(fila, 2));
        JTextField correo = new JTextField((String) modelEmpleados.getValueAt(fila, 3));
        JTextField cargo = new JTextField((String) modelEmpleados.getValueAt(fila, 4));
        JTextField salario = new JTextField(modelEmpleados.getValueAt(fila, 5).toString());

        // Agregamos campo opcional para cambiar contraseña
        JPasswordField password = new JPasswordField();

        Object[] message = {
            "Nombre", nombre,
            "Apellido", apellido,
            "Correo", correo,
            "Cargo", cargo,
            "Salario", salario,
            "Nueva contraseña (opcional)", password
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (!ValidationUtils.isValidEmail(correo.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Correo inválido. Debe terminar en .com");
                    return;
                }

                // Recuperar empleado actual de la BD para mantener su password si no se cambia
                Empleado actual = controller.buscarEmpleadoPorId(idEmpleado);
                String nuevaPass = new String(password.getPassword()).trim();
                if (nuevaPass.isEmpty()) {
                    nuevaPass = actual.getPassword(); // mantiene la actual
                }
 
                Empleado e = new Empleado(
                        nombre.getText(),
                        apellido.getText(),
                        correo.getText(),
                        cargo.getText(),
                        Double.parseDouble(salario.getText())
                );
                e.setId(idEmpleado);

                controller.actualizarEmpleado(e);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Salario inválido. Ingrese un número válido.");
            }
        }
    }

    private void onEliminarEmpleado(ActionEvent ev) {
        int fila = tblEmpleados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar");
            return;
        }

        int idEmpleado = (int) modelEmpleados.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarEmpleado(idEmpleado);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar empleado: " + ex.getMessage());
            }
        }
    }

    private void onAgregarContrato(ActionEvent ev) {
        int fila = tblEmpleados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para crear contrato");
            return;
        }

        int idEmpleado = (int) modelEmpleados.getValueAt(fila, 0);
        JTextField tipo = new JTextField();
        JTextField duracion = new JTextField();
        JTextField monto = new JTextField();
        JTextField beneficios = new JTextField();

        Object[] message = {
            "Tipo de Contrato (planilla, locacion, parcial)", tipo,
            "Duración (ej. 6 meses)", duracion,
            "Monto (ej. 3500.00)", monto,
            "Beneficios adicionales (opcional)", beneficios
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuevo Contrato", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String tipoStr = tipo.getText().trim().toLowerCase();
                controller.crearContrato(
                        tipoStr,
                        duracion.getText(),
                        Double.parseDouble(monto.getText()),
                        beneficios.getText(),
                        null, null, null,
                        idEmpleado
                );
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Contrato creado correctamente");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear contrato: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Monto inválido. Ingrese un número válido.");
            }
        }
    }

    private void onEliminarContrato(ActionEvent ev) {
        int fila = tblContratos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contrato para eliminar");
            return;
        }

        int idContrato = (int) modelContratos.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este contrato?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarContrato(idContrato);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Contrato eliminado correctamente");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar contrato: " + ex.getMessage());
            }
        }
    }
}
