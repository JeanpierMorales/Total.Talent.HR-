package Vista.Strategy;

import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Rol;
import Modelo.Usuario;
import Modelo.Factory.ContratoFactory;
import Utilidades.Validaciones;
import Vista.DashboardVista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        vista.setTablaSeleccionada(tablaEmpleados);

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

        // Filtro por tipo de contrato
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(getColorFondo());
        panelFiltro.add(new JLabel("Filtrar por tipo:"));
        JComboBox<String> cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Parcial", "Planilla", "Locación"});
        panelFiltro.add(cbFiltroTipo);

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
        vista.setTablaSeleccionada(tablaContratos);

        // Función para cargar contratos con filtro
        Runnable cargarContratos = () -> {
            modeloTabla.setRowCount(0);
            try {
                List<Contrato> contratos = controlador.obtenerTodosContratos();
                String filtroTipo = (String) cbFiltroTipo.getSelectedItem();
                if (!"Todos".equals(filtroTipo)) {
                    contratos = contratos.stream()
                            .filter(c -> filtroTipo.equals(c.getTipoContrato()))
                            .collect(Collectors.toList());
                }
                contratos = contratos.stream().sorted(Comparator.comparing(Contrato::getIdContrato)).collect(Collectors.toList());
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
        };

        // Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(getColorFondo());

        JButton btnAgregar = new JButton("Agregar Contrato");
        JButton btnEditar = new JButton("Editar Contrato");
        JButton btnEliminar = new JButton("Eliminar Contrato");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarContrato(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarContrato(controlador, vista));
        btnEliminar.addActionListener(e -> eliminarContrato(controlador, vista));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Panel de búsqueda
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSearch.setBackground(getColorFondo());
        JComboBox<String> cbCriterioBusqueda = new JComboBox<>(new String[]{"ID", "Nombre Empleado", "Tipo Contrato"});
        JTextField txtValorBusqueda = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Refrescar");
        estilizarBoton(btnBuscar);
        estilizarBoton(btnRefrescar);

        btnBuscar.addActionListener(e -> {
            String criterio = (String) cbCriterioBusqueda.getSelectedItem();
            String valor = txtValorBusqueda.getText().trim();
            modeloTabla.setRowCount(0);
            try {
                List<Contrato> contratos = controlador.obtenerTodosContratos();
                String filtroTipo = (String) cbFiltroTipo.getSelectedItem();
                if (!"Todos".equals(filtroTipo)) {
                    contratos = contratos.stream()
                            .filter(c -> filtroTipo.equals(c.getTipoContrato()))
                            .collect(Collectors.toList());
                }
                for (Contrato cont : contratos) {
                    boolean coincide = false;
                    if (valor.isEmpty()) {
                        coincide = true;
                    } else {
                        switch (criterio) {
                            case "ID":
                                try {
                                    int idBuscado = Integer.parseInt(valor);
                                    if (cont.getIdContrato() == idBuscado) {
                                        coincide = true;
                                    }
                                } catch (NumberFormatException ex) {
                                    // ID no válido
                                }
                                break;
                            case "Nombre Empleado":
                                if (cont.getEmpleado() != null
                                        && (cont.getEmpleado().getNombre().toLowerCase().contains(valor.toLowerCase())
                                        || cont.getEmpleado().getApellidos().toLowerCase().contains(valor.toLowerCase()))) {
                                    coincide = true;
                                }
                                break;
                            case "Tipo Contrato":
                                if (cont.getTipoContrato().toLowerCase().contains(valor.toLowerCase())) {
                                    coincide = true;
                                }
                                break;
                        }
                    }
                    if (coincide) {
                        modeloTabla.addRow(new Object[]{
                            cont.getIdContrato(),
                            cont.getTipoContrato(),
                            cont.getEmpleado() != null ? cont.getEmpleado().getNombre() + " " + cont.getEmpleado().getApellidos() : "Sin asignar",
                            cont.getSalarioBase(),
                            cont.getFechaInicio()
                        });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al buscar contratos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRefrescar.addActionListener(e -> {
            txtValorBusqueda.setText("");
            cbCriterioBusqueda.setSelectedIndex(0);
            cargarContratos.run();
        });

        panelSearch.add(new JLabel("Buscar por:"));
        panelSearch.add(cbCriterioBusqueda);
        panelSearch.add(txtValorBusqueda);
        panelSearch.add(btnBuscar);
        panelSearch.add(btnRefrescar);

        // Cargar inicial
        cargarContratos.run();

        // Listener para filtro
        cbFiltroTipo.addActionListener(e -> cargarContratos.run());

        JScrollPane scrollPane = new JScrollPane(tablaContratos);

        panelGestion.add(lblTitulo);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(panelFiltro);
        panelGestion.add(Box.createVerticalStrut(10));
        panelGestion.add(panelSearch);
        panelGestion.add(Box.createVerticalStrut(10));
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
        JTable tabla = vista.getTablaSeleccionada();
        if (tabla == null || tabla.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un empleado de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int filaSeleccionada = tabla.getSelectedRow();
        int idEmpleado = (Integer) tabla.getValueAt(filaSeleccionada, 0);

        try {
            Empleado empleado = controlador.buscarEmpleadoPorId(idEmpleado);
            if (empleado == null) {
                JOptionPane.showMessageDialog(vista, "Empleado no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Editar Empleado", true);
            dialogo.setLayout(new GridLayout(12, 2, 10, 10));
            dialogo.setSize(500, 500);
            dialogo.setLocationRelativeTo(vista);

            JTextField txtNombre = new JTextField(empleado.getNombre());
            JTextField txtApellidos = new JTextField(empleado.getApellidos());
            JTextField txtEdad = new JTextField(String.valueOf(empleado.getEdad()));
            JTextField txtDni = new JTextField(empleado.getDni());
            JTextField txtNumero = new JTextField(empleado.getNumero());
            JTextField txtCorreo = new JTextField(empleado.getCorreo());
            JTextField txtDireccion = new JTextField(empleado.getDireccion());
            JTextField txtGradoInstruccion = new JTextField(empleado.getGradoInstruccion());
            JTextField txtCarrera = new JTextField(empleado.getCarrera());
            JTextArea txtComentarios = new JTextArea(empleado.getComentarios(), 3, 20);

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

                    // Validaciones
                    if (!Validaciones.validarNoVacio(empleado.getNombre()) || !Validaciones.validarNoVacio(empleado.getApellidos())
                            || !Validaciones.validarDNI(empleado.getDni()) || !Validaciones.validarEmail(empleado.getCorreo())
                            || !Validaciones.validarTelefono(empleado.getNumero()) || !Validaciones.validarNoVacio(empleado.getDireccion())) {
                        JOptionPane.showMessageDialog(dialogo, "Campos obligatorios (*) no válidos o incompletos", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    controlador.actualizarEmpleado(empleado);
                    JOptionPane.showMessageDialog(dialogo, "Empleado actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                    vista.mostrarPanel("Gestionar Empleados");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, "Error al actualizar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnCancelar.addActionListener(e -> dialogo.dispose());

            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);

            dialogo.add(new JLabel()); // Espacio vacío
            dialogo.add(panelBotones);

            dialogo.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoBuscarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Buscar Empleado", true);
        dialogo.setLayout(new GridLayout(4, 2, 10, 10));
        dialogo.setSize(400, 200);
        dialogo.setLocationRelativeTo(vista);

        JComboBox<String> cbCriterio = new JComboBox<>(new String[]{"Nombre", "Apellidos", "DNI"});
        JTextField txtValor = new JTextField();

        dialogo.add(new JLabel("Buscar empleado por:"));
        dialogo.add(cbCriterio);
        dialogo.add(new JLabel("Ingresa el dato acá:"));
        dialogo.add(txtValor);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnCancelar = new JButton("Cancelar");

        btnBuscar.addActionListener(e -> {
            String criterio = (String) cbCriterio.getSelectedItem();
            String valor = txtValor.getText().trim();

            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese un valor para buscar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                List<Empleado> empleados = controlador.obtenerTodosEmpleados();
                List<Empleado> resultados = new java.util.ArrayList<>();

                for (Empleado emp : empleados) {
                    boolean coincide = false;

                    switch (criterio) {
                        case "Nombre":
                            if (emp.getNombre().toLowerCase().contains(valor.toLowerCase())) {
                                coincide = true;
                            }
                            break;
                        case "Apellidos":
                            if (emp.getApellidos().toLowerCase().contains(valor.toLowerCase())) {
                                coincide = true;
                            }
                            break;
                        case "DNI":
                            if (emp.getDni().equals(valor)) {
                                coincide = true;
                            }
                            break;
                    }

                    if (coincide) {
                        resultados.add(emp);
                    }
                }

                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "No se encontraron empleados con los criterios especificados", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Mostrar resultados en un nuevo diálogo
                    StringBuilder sb = new StringBuilder();
                    sb.append("Empleados encontrados:\n\n");
                    for (Empleado emp : resultados) {
                        sb.append("ID: ").append(emp.getIdEmpleado()).append("\n");
                        sb.append("Nombre: ").append(emp.getNombre()).append(" ").append(emp.getApellidos()).append("\n");
                        sb.append("DNI: ").append(emp.getDni()).append("\n");
                        sb.append("Rol: ").append(emp.getRol()).append("\n\n");
                    }

                    JTextArea txtResultados = new JTextArea(sb.toString());
                    txtResultados.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(txtResultados);
                    scrollPane.setPreferredSize(new Dimension(300, 200));

                    JOptionPane.showMessageDialog(dialogo, scrollPane, "Resultados de búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al buscar empleados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnCancelar);

        dialogo.add(new JLabel()); // Espacio vacío
        dialogo.add(panelBotones);

        dialogo.setVisible(true);
    }

    private void mostrarDialogoAgregarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Agregar Contrato", true);
        dialogo.setLayout(new GridLayout(10, 2, 10, 10));
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(vista);

        JComboBox<String> cbTipoContrato = new JComboBox<>(new String[]{"Planilla", "Locacion", "Parcial"});
        JComboBox<String> cbEmpleado = new JComboBox<>();
        JTextField txtSalarioBase = new JTextField();
        JTextField txtFechaInicio = new JTextField();
        JTextField txtFechaFin = new JTextField();
        JTextField txtBonificacion = new JTextField();
        JTextField txtDescuentoAFP = new JTextField();

        // Cargar empleados en el combo box
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            for (Empleado emp : empleados) {
                cbEmpleado.addItem(emp.getIdEmpleado() + " - " + emp.getNombre() + " " + emp.getApellidos());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialogo, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dialogo.add(new JLabel("Tipo de Contrato:*"));
        dialogo.add(cbTipoContrato);
        dialogo.add(new JLabel("Empleado:*"));
        dialogo.add(cbEmpleado);
        dialogo.add(new JLabel("Salario Base:*"));
        dialogo.add(txtSalarioBase);
        dialogo.add(new JLabel("Fecha Inicio (YYYY-MM-DD):*"));
        dialogo.add(txtFechaInicio);
        dialogo.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        dialogo.add(txtFechaFin);
        dialogo.add(new JLabel("Bonificación:"));
        dialogo.add(txtBonificacion);
        dialogo.add(new JLabel("Descuento AFP:"));
        dialogo.add(txtDescuentoAFP);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            try {
                String tipoSeleccionado = (String) cbTipoContrato.getSelectedItem();
                String empleadoSeleccionado = (String) cbEmpleado.getSelectedItem();

                if (tipoSeleccionado == null || empleadoSeleccionado == null
                        || txtSalarioBase.getText().trim().isEmpty() || txtFechaInicio.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Campos obligatorios (*) incompletos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Extraer ID del empleado del string seleccionado
                int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);

                Contrato contrato = ContratoFactory.crearContrato(tipoSeleccionado.toLowerCase());
                contrato.setTipoContrato(tipoSeleccionado);
                contrato.setEmpleado(controlador.buscarEmpleadoPorId(idEmpleado));
                contrato.setSalarioBase(Double.parseDouble(txtSalarioBase.getText().trim()));

                // Parsear fechas
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                contrato.setFechaInicio(sdf.parse(txtFechaInicio.getText().trim()));
                if (!txtFechaFin.getText().trim().isEmpty()) {
                    contrato.setFechaFin(sdf.parse(txtFechaFin.getText().trim()));
                }

                if (!txtBonificacion.getText().trim().isEmpty()) {
                    contrato.setBonificacion(Double.parseDouble(txtBonificacion.getText().trim()));
                }
                if (!txtDescuentoAFP.getText().trim().isEmpty()) {
                    contrato.setDescuentoAFP(Double.parseDouble(txtDescuentoAFP.getText().trim()));
                }

                controlador.guardarContrato(contrato);
                JOptionPane.showMessageDialog(dialogo, "Contrato agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
                vista.mostrarPanel("Gestionar Contratos");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al agregar contrato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void mostrarDialogoEditarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JTable tabla = vista.getTablaSeleccionada();
        if (tabla == null || tabla.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un contrato de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int filaSeleccionada = tabla.getSelectedRow();
        int idContrato = (Integer) tabla.getValueAt(filaSeleccionada, 0);

        try {
            Contrato contrato = controlador.buscarContratoPorId(idContrato);
            if (contrato == null) {
                JOptionPane.showMessageDialog(vista, "Contrato no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Editar Contrato", true);
            dialogo.setLayout(new GridLayout(10, 2, 10, 10));
            dialogo.setSize(500, 400);
            dialogo.setLocationRelativeTo(vista);

            JComboBox<String> cbTipoContrato = new JComboBox<>(new String[]{"Planilla", "Locacion", "Parcial"});
            cbTipoContrato.setSelectedItem(contrato.getTipoContrato());

            JComboBox<String> cbEmpleado = new JComboBox<>();
            JTextField txtSalarioBase = new JTextField(String.valueOf(contrato.getSalarioBase()));
            JTextField txtFechaInicio = new JTextField(contrato.getFechaInicio() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(contrato.getFechaInicio()) : "");
            JTextField txtFechaFin = new JTextField(contrato.getFechaFin() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(contrato.getFechaFin()) : "");
            JTextField txtBonificacion = new JTextField(String.valueOf(contrato.getBonificacion()));
            JTextField txtDescuentoAFP = new JTextField(String.valueOf(contrato.getDescuentoAFP()));

            // Cargar empleados en el combo box
            try {
                List<Empleado> empleados = controlador.obtenerTodosEmpleados();
                for (Empleado emp : empleados) {
                    cbEmpleado.addItem(emp.getIdEmpleado() + " - " + emp.getNombre() + " " + emp.getApellidos());
                    if (contrato.getEmpleado() != null && emp.getIdEmpleado() == contrato.getEmpleado().getIdEmpleado()) {
                        cbEmpleado.setSelectedItem(emp.getIdEmpleado() + " - " + emp.getNombre() + " " + emp.getApellidos());
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialogo, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dialogo.add(new JLabel("Tipo de Contrato:*"));
            dialogo.add(cbTipoContrato);
            dialogo.add(new JLabel("Empleado:*"));
            dialogo.add(cbEmpleado);
            dialogo.add(new JLabel("Salario Base:*"));
            dialogo.add(txtSalarioBase);
            dialogo.add(new JLabel("Fecha Inicio (YYYY-MM-DD):*"));
            dialogo.add(txtFechaInicio);
            dialogo.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
            dialogo.add(txtFechaFin);
            dialogo.add(new JLabel("Bonificación:"));
            dialogo.add(txtBonificacion);
            dialogo.add(new JLabel("Descuento AFP:"));
            dialogo.add(txtDescuentoAFP);

            JButton btnGuardar = new JButton("Guardar");
            JButton btnCancelar = new JButton("Cancelar");

            btnGuardar.addActionListener(e -> {
                try {
                    String tipoSeleccionado = (String) cbTipoContrato.getSelectedItem();
                    String empleadoSeleccionado = (String) cbEmpleado.getSelectedItem();

                    if (tipoSeleccionado == null || empleadoSeleccionado == null
                            || txtSalarioBase.getText().trim().isEmpty() || txtFechaInicio.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(dialogo, "Campos obligatorios (*) incompletos", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Extraer ID del empleado del string seleccionado
                    int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);

                    contrato.setTipoContrato(tipoSeleccionado);
                    contrato.setEmpleado(controlador.buscarEmpleadoPorId(idEmpleado));
                    contrato.setSalarioBase(Double.parseDouble(txtSalarioBase.getText().trim()));

                    // Parsear fechas
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    contrato.setFechaInicio(sdf.parse(txtFechaInicio.getText().trim()));
                    if (!txtFechaFin.getText().trim().isEmpty()) {
                        contrato.setFechaFin(sdf.parse(txtFechaFin.getText().trim()));
                    } else {
                        contrato.setFechaFin(null);
                    }

                    if (!txtBonificacion.getText().trim().isEmpty()) {
                        contrato.setBonificacion(Double.parseDouble(txtBonificacion.getText().trim()));
                    } else {
                        contrato.setBonificacion(0.0);
                    }
                    if (!txtDescuentoAFP.getText().trim().isEmpty()) {
                        contrato.setDescuentoAFP(Double.parseDouble(txtDescuentoAFP.getText().trim()));
                    } else {
                        contrato.setDescuentoAFP(0.0);
                    }

                    controlador.actualizarContrato(contrato);
                    JOptionPane.showMessageDialog(dialogo, "Contrato actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                    vista.mostrarPanel("Gestionar Contratos");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, "Error al actualizar contrato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnCancelar.addActionListener(e -> dialogo.dispose());

            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);

            dialogo.add(new JLabel()); // Espacio vacío
            dialogo.add(panelBotones);

            dialogo.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar contrato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JTable tabla = vista.getTablaSeleccionada();
        if (tabla == null || tabla.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un contrato para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idContrato = (Integer) tabla.getValueAt(tabla.getSelectedRow(), 0);
        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro de que desea eliminar este contrato? Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminarContrato(idContrato);
                JOptionPane.showMessageDialog(vista, "Contrato eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.mostrarPanel("Gestionar Contratos");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar contrato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
