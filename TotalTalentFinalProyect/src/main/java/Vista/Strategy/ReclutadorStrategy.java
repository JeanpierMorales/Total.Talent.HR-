package Vista.Strategy;


import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Rol;
import Modelo.Usuario;
import Utilidades.Validaciones;
import Vista.DashboardVista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Estrategia específica para el rol de Reclutador
// Implementa funcionalidades de gestión de empleados y contratos
public class ReclutadorStrategy implements DashboardStrategy {

    // Colores específicos del reclutador (verdes)
    @Override
    public Color getColorFondo() {
        return new Color(240, 255, 240);
    } // Honeydew

    @Override
    public Color getColorBoton() {
        return new Color(144, 238, 144);
    } // Light Green

    @Override
    public Color getColorBotonHover() {
        return new Color(50, 205, 50);
    } // Lime Green

    @Override
    public Color getColorTexto() {
        return new Color(0, 100, 0);
    } // Dark Green

    @Override
    public Color getColorNavbar() {
        return new Color(200, 255, 200);
    } // Light Green for navbar

    @Override
    public String getTituloVentana() {
        return "Dashboard Reclutador - Total Talent HR";
    }

    @Override
    public String getMensajeBienvenida() {
        return "Panel Principal - Reclutador";
    }

    @Override
    public String getMensajeInfo() {
        return "Selecciona una opción del menú lateral para gestionar empleados y contratos.";
    }

    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        JButton btnGestionarEmpleados = crearBotonNavbar("Gestionar Empleados", vista);
        JButton btnGestionarContratos = crearBotonNavbar("Gestionar Contratos", vista);
        JButton btnBuscarCandidatos = crearBotonNavbar("Buscar Candidatos", vista);

        panelIzquierdo.add(btnGestionarEmpleados);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnGestionarContratos);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnBuscarCandidatos);

        return panelIzquierdo;
    }

    private JButton crearBotonNavbar(String texto, DashboardVista vista) {
        JButton boton = new JButton(texto);
        boton.setBackground(getColorBoton());
        boton.setForeground(getColorTexto());
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setMaximumSize(new Dimension(180, 40));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBotonHover());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBoton());
            }
        });
        boton.addActionListener(e -> vista.mostrarPanel(texto));
        return boton;
    }

    @Override
    public void mostrarPanel(String panelNombre, JPanel panelContenido, Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        TotalTalentControlador controlador = vista.getControlador();

        switch (panelNombre) {
            case "Gestionar Empleados":
                mostrarPanelGestionEmpleados(panelContenido, vista, controlador);
                break;
            case "Gestionar Contratos":
                mostrarPanelGestionContratos(panelContenido, vista, controlador);
                break;
            case "Buscar Candidatos":
                mostrarPanelBuscarCandidatos(panelContenido, vista, controlador);
                break;
            default:
                vista.mostrarPanelPrincipalPublico();
        }
    }

    private void mostrarPanelGestionEmpleados(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelGestion = new JPanel();
        panelGestion.setBackground(getColorFondo());
        panelGestion.setLayout(new BoxLayout(panelGestion, BoxLayout.Y_AXIS));
        panelGestion.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("GESTIÓN DE EMPLEADOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(getColorFondo());

        JButton btnAgregar = new JButton("Agregar Empleado");
        JButton btnEditar = new JButton("Editar Empleado");
        JButton btnBuscar = new JButton("Buscar Empleado");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnBuscar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarEmpleado(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarEmpleado(controlador, vista));
        btnBuscar.addActionListener(e -> mostrarDialogoBuscarEmpleado(controlador, vista));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnBuscar);

        // Tabla de empleados
        DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellidos", "DNI", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEmpleados.getTableHeader().setReorderingAllowed(false);

        // Cargar datos
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            for (Empleado emp : empleados) {
                modeloTabla.addRow(new Object[]{
                    emp.getIdEmpleado(),
                    emp.getNombre(),
                    emp.getApellidos(),
                    emp.getDni(),
                    emp.getRol()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);

        panelGestion.add(lblTitulo);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(panelBotones);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(scrollPane);

        panelContenido.add(panelGestion, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelGestionContratos(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelGestion = new JPanel();
        panelGestion.setBackground(getColorFondo());
        panelGestion.setLayout(new BoxLayout(panelGestion, BoxLayout.Y_AXIS));
        panelGestion.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("GESTIÓN DE CONTRATOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(getColorFondo());

        JButton btnAgregar = new JButton("Agregar Contrato");
        JButton btnEditar = new JButton("Editar Contrato");
        JButton btnBuscar = new JButton("Buscar Contrato");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnBuscar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarContrato(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarContrato(controlador, vista));
        btnBuscar.addActionListener(e -> mostrarDialogoBuscarContrato(controlador, vista));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnBuscar);

        // Tabla de contratos
        DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Empleado", "Salario Base", "Fecha Inicio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaContratos = new JTable(modeloTabla);
        tablaContratos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaContratos.getTableHeader().setReorderingAllowed(false);

        // Cargar datos
        try {
            List<Contrato> contratos = controlador.obtenerTodosContratos();
            for (Contrato cont : contratos) {
                modeloTabla.addRow(new Object[]{
                    cont.getIdContrato(),
                    cont.getTipoContrato(),
                    cont.getEmpleado() != null ? cont.getEmpleado().getNombre() + " " + cont.getEmpleado().getApellidos() : "Sin asignar",
                    cont.getSalarioBase(),
                    cont.getFechaInicio()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar contratos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(tablaContratos);

        panelGestion.add(lblTitulo);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(panelBotones);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(scrollPane);

        panelContenido.add(panelGestion, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelBuscarCandidatos(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBackground(getColorFondo());
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("BÚSQUEDA DE CANDIDATOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de búsqueda
        JPanel panelBuscar = new JPanel(new FlowLayout());
        panelBuscar.setBackground(getColorFondo());

        JTextField txtBusqueda = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JComboBox<String> cbCriterio = new JComboBox<>(new String[]{"Nombre", "Habilidades", "Experiencia"});

        estilizarBoton(btnBuscar);

        panelBuscar.add(new JLabel("Criterio:"));
        panelBuscar.add(cbCriterio);
        panelBuscar.add(new JLabel("Término:"));
        panelBuscar.add(txtBusqueda);
        panelBuscar.add(btnBuscar);

        // Área de resultados
        JTextArea txtResultados = new JTextArea(20, 50);
        txtResultados.setEditable(false);
        txtResultados.setText("Funcionalidad de búsqueda de candidatos externa próximamente.\n\n"
                + "Aquí se mostrarían candidatos de bases de datos externas,\n"
                + "portales de empleo, redes sociales, etc.");
        JScrollPane scrollPane = new JScrollPane(txtResultados);

        btnBuscar.addActionListener(e -> {
            String criterio = (String) cbCriterio.getSelectedItem();
            String termino = txtBusqueda.getText().trim();
            if (!termino.isEmpty()) {
                txtResultados.setText("Buscando candidatos por " + criterio.toLowerCase() + ": " + termino + "\n\n"
                        + "Resultados simulados:\n"
                        + "- Candidato 1: Juan Pérez - Desarrollador Java\n"
                        + "- Candidato 2: María García - Analista de RRHH\n"
                        + "- Candidato 3: Carlos López - Gerente de Proyecto\n\n"
                        + "Nota: Esta funcionalidad requiere integración con APIs externas.");
            }
        });

        panelBusqueda.add(lblTitulo);
        panelBusqueda.add(Box.createVerticalStrut(20));
        panelBusqueda.add(panelBuscar);
        panelBusqueda.add(Box.createVerticalStrut(20));
        panelBusqueda.add(scrollPane);

        panelContenido.add(panelBusqueda, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(getColorBoton());
        boton.setForeground(getColorTexto());
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBotonHover());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBoton());
            }
        });
    }

    private void mostrarDialogoAgregarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Agregar Empleado", true);
        dialogo.setLayout(new GridLayout(12, 2, 10, 10));
        dialogo.setSize(500, 500);
        dialogo.setLocationRelativeTo(vista);

        JTextField txtNombre = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtEdad = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtNumero = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtGradoInstruccion = new JTextField();
        JTextField txtCarrera = new JTextField();
        JTextArea txtComentarios = new JTextArea(3, 20);
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"EMPLEADO"}); // Solo puede crear empleados

        dialogo.add(new JLabel("Nombre:*"));
        dialogo.add(txtNombre);
        dialogo.add(new JLabel("Apellidos:*"));
        dialogo.add(txtApellidos);
        dialogo.add(new JLabel("Edad:*"));
        dialogo.add(txtEdad);
        dialogo.add(new JLabel("DNI:*"));
        dialogo.add(txtDni);
        dialogo.add(new JLabel("Número:*"));
        dialogo.add(txtNumero);
        dialogo.add(new JLabel("Correo:*"));
        dialogo.add(txtCorreo);
        dialogo.add(new JLabel("Dirección:*"));
        dialogo.add(txtDireccion);
        dialogo.add(new JLabel("Grado Instrucción:"));
        dialogo.add(txtGradoInstruccion);
        dialogo.add(new JLabel("Carrera:"));
        dialogo.add(txtCarrera);
        dialogo.add(new JLabel("Comentarios:"));
        dialogo.add(new JScrollPane(txtComentarios));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            try {
                Empleado empleado = new Empleado();
                empleado.setNombre(txtNombre.getText().trim());
                empleado.setApellidos(txtApellidos.getText().trim());
                empleado.setEdad(Integer.parseInt(txtEdad.getText().trim()));
                empleado.setDni(txtDni.getText().trim());
                empleado.setNumero(txtNumero.getText().trim());
                empleado.setCorreo(txtCorreo.getText().trim());
                empleado.setDireccion(txtDireccion.getText().trim());
                empleado.setGradoInstruccion(txtGradoInstruccion.getText().trim());
                empleado.setCarrera(txtCarrera.getText().trim());
                empleado.setComentarios(txtComentarios.getText().trim());
                empleado.setRol(Rol.EMPLEADO);

                // Validaciones
                if (!Validaciones.validarNoVacio(empleado.getNombre()) || !Validaciones.validarNoVacio(empleado.getApellidos())
                        || !Validaciones.validarDNI(empleado.getDni()) || !Validaciones.validarEmail(empleado.getCorreo())
                        || !Validaciones.validarTelefono(empleado.getNumero()) || !Validaciones.validarNoVacio(empleado.getDireccion())) {
                    JOptionPane.showMessageDialog(dialogo, "Campos obligatorios (*) no válidos o incompletos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controlador.guardarEmpleado(empleado);
                JOptionPane.showMessageDialog(dialogo, "Empleado agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
                vista.mostrarPanel("Gestionar Empleados");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al agregar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(new JLabel()); // Espacio vacío
        dialogo.add(panelBotones);

        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de editar empleado en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoBuscarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de buscar empleado en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoAgregarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de agregar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoEditarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de editar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoBuscarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de buscar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
