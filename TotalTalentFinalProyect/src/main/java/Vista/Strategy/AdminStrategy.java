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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Estrategia específica para el rol de Administrador
// Implementa funcionalidades completas de gestión del sistema
public class AdminStrategy implements DashboardStrategy {

    // Colores específicos del administrador (azules)
    @Override
    public Color getColorFondo() {
        return new Color(240, 248, 255);
    } // Alice Blue

    @Override
    public Color getColorBoton() {
        return new Color(173, 216, 230);
    } // Light Blue

    @Override
    public Color getColorBotonHover() {
        return new Color(135, 206, 250);
    } // Sky Blue

    @Override
    public Color getColorTexto() {
        return new Color(25, 25, 112);
    } // Midnight Blue

    @Override
    public Color getColorNavbar() {
        return new Color(200, 230, 255);
    } // Light Blue for navbar

    @Override
    public String getTituloVentana() {
        return "Dashboard Administrador - Total Talent HR";
    }

    @Override
    public String getMensajeBienvenida() {
        return "Panel Principal - Administrador";
    }

    @Override
    public String getMensajeInfo() {
        return "Selecciona una opción del menú lateral para gestionar el sistema.";
    }

    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        JButton btnGestionarUsuarios = crearBotonNavbar("Gestionar Usuarios", vista);
        JButton btnGestionarEmpleados = crearBotonNavbar("Gestionar Empleados", vista);
        JButton btnGestionarContratos = crearBotonNavbar("Gestionar Contratos", vista);
        JButton btnVerLogs = crearBotonNavbar("Ver Logs del Sistema", vista);
        JButton btnGenerarReportes = crearBotonNavbar("Generar Reportes", vista);
        JButton btnVerEstadisticas = crearBotonNavbar("Ver Estadísticas", vista);

        panelIzquierdo.add(btnGestionarUsuarios);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnGestionarEmpleados);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnGestionarContratos);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnVerLogs);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnGenerarReportes);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnVerEstadisticas);

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
            case "Gestionar Usuarios":
                mostrarPanelGestionUsuarios(panelContenido, vista, controlador);
                break;
            case "Gestionar Empleados":
                mostrarPanelGestionEmpleados(panelContenido, vista, controlador);
                break;
            case "Gestionar Contratos":
                mostrarPanelGestionContratos(panelContenido, vista, controlador);
                break;
            case "Ver Logs del Sistema":
                mostrarPanelLogs(panelContenido, vista, controlador);
                break;
            case "Generar Reportes":
                mostrarPanelReportes(panelContenido, vista, controlador);
                break;
            case "Ver Estadísticas":
                mostrarPanelEstadisticas(panelContenido, vista, controlador);
                break;
            default:
                vista.mostrarPanelPrincipalPublico();
        }
    }

    private void mostrarPanelGestionUsuarios(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelGestion = new JPanel();
        panelGestion.setBackground(getColorFondo());
        panelGestion.setLayout(new BoxLayout(panelGestion, BoxLayout.Y_AXIS));
        panelGestion.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(getColorFondo());

        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnEditar = new JButton("Editar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        JButton btnRefrescar = new JButton("Refrescar");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);
        estilizarBoton(btnRefrescar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarUsuario(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarUsuario(controlador, vista));
        btnEliminar.addActionListener(e -> eliminarUsuario(controlador, vista));
        btnRefrescar.addActionListener(e -> mostrarPanelGestionUsuarios(panelContenido, vista, controlador));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);

        // Tabla de usuarios
        DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre Usuario", "Rol", "Empleado Vinculado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        vista.setTablaSeleccionada(tablaUsuarios);

        // Cargar datos ordenados por ID
        try {
            List<Usuario> usuarios = controlador.obtenerTodosUsuarios();
            usuarios = usuarios.stream().sorted(Comparator.comparing(Usuario::getIdUsuario)).collect(Collectors.toList());
            for (Usuario usuario : usuarios) {
                String empleadoInfo = usuario.getEmpleado() != null
                        ? usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellidos()
                        : "Sin vincular";
                modeloTabla.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getRol(),
                    empleadoInfo
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);

        panelGestion.add(lblTitulo);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(panelBotones);
        panelGestion.add(Box.createVerticalStrut(20));
        panelGestion.add(scrollPane);

        panelContenido.add(panelGestion, BorderLayout.CENTER);
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

    private void mostrarDialogoAgregarUsuario(TotalTalentControlador controlador, DashboardVista vista) {
        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Agregar Usuario", true);
        dialogo.setLayout(new BoxLayout(dialogo.getContentPane(), BoxLayout.Y_AXIS));
        dialogo.setSize(450, 250);
        dialogo.setLocationRelativeTo(vista);

        JTextField txtNombreUsuario = new JTextField(20);
        JPasswordField txtContrasena = new JPasswordField(20);
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "RECLUTADOR", "GERENTE", "EMPLEADO"});

        // Seleccionar empleado existente
        JComboBox<String> cbEmpleados = new JComboBox<>();
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            for (Empleado emp : empleados) {
                cbEmpleados.addItem(emp.getIdEmpleado() + " - " + emp.getNombre() + " " + emp.getApellidos());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialogo, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear paneles para cada fila con alineación izquierda-derecha
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombre.add(new JLabel("Nombre del nuevo usuario:"));
        panelNombre.add(txtNombreUsuario);

        JPanel panelContrasena = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelContrasena.add(new JLabel("Contraseña para el Nuevo usuario:"));
        panelContrasena.add(txtContrasena);

        JPanel panelRol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRol.add(new JLabel("Rol para el Nuevo usuario:"));
        panelRol.add(cbRol);

        JPanel panelEmpleado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEmpleado.add(new JLabel("Empleado para el Nuevo usuario:"));
        panelEmpleado.add(cbEmpleados);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            String nombreUsuario = txtNombreUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String rol = (String) cbRol.getSelectedItem();
            String empleadoSeleccionado = (String) cbEmpleados.getSelectedItem();

            if (!Validaciones.validarNoVacio(nombreUsuario) || !Validaciones.validarNoVacio(contrasena)
                    || !Validaciones.validarNoVacio(rol) || empleadoSeleccionado == null) {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos marcados con * son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Extraer ID del empleado del string seleccionado
                int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);
                Empleado empleado = controlador.buscarEmpleadoPorId(idEmpleado);

                controlador.agregarUsuario(nombreUsuario, contrasena, rol);
                // Vincular usuario al empleado existente
                Usuario usuario = controlador.buscarUsuarioPorNombre(nombreUsuario);
                if (usuario != null && empleado != null) {
                    usuario.setEmpleado(empleado);
                    controlador.actualizarUsuario(usuario);
                }
                JOptionPane.showMessageDialog(dialogo, "Usuario agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
                // Refrescar panel
                vista.mostrarPanel("Gestionar Usuarios");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al agregar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(panelNombre);
        dialogo.add(panelContrasena);
        dialogo.add(panelRol);
        dialogo.add(panelEmpleado);
        dialogo.add(panelBotones);

        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarUsuario(TotalTalentControlador controlador, DashboardVista vista) {
        JTable tablaUsuarios = vista.getTablaSeleccionada();
        // Obtener el usuario seleccionado de la tabla
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        int idUsuario = (Integer) modelo.getValueAt(selectedRow, 0);
        String nombreUsuarioActual = (String) modelo.getValueAt(selectedRow, 1);
        String rolActual = (String) modelo.getValueAt(selectedRow, 2);

        Usuario usuario = controlador.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            JOptionPane.showMessageDialog(vista, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Editar Usuario", true);
        dialogo.setLayout(new BoxLayout(dialogo.getContentPane(), BoxLayout.Y_AXIS));
        dialogo.setSize(450, 300);
        dialogo.setLocationRelativeTo(vista);

        JTextField txtNombreUsuario = new JTextField(nombreUsuarioActual, 20);
        JPasswordField txtContrasena = new JPasswordField(20);
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "RECLUTADOR", "GERENTE", "EMPLEADO"});
        cbRol.setSelectedItem(rolActual);

        // Seleccionar empleado existente
        JComboBox<String> cbEmpleados = new JComboBox<>();
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            for (Empleado emp : empleados) {
                cbEmpleados.addItem(emp.getIdEmpleado() + " - " + emp.getNombre() + " " + emp.getApellidos());
            }
            // Seleccionar empleado actual si existe
            if (usuario.getEmpleado() != null) {
                cbEmpleados.setSelectedItem(usuario.getEmpleado().getIdEmpleado() + " - " + usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellidos());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialogo, "Error al cargar empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear paneles para cada fila
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombre.add(new JLabel("Nombre del usuario:"));
        panelNombre.add(txtNombreUsuario);

        JPanel panelContrasena = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelContrasena.add(new JLabel("Nueva Contraseña (dejar vacío para no cambiar):"));
        panelContrasena.add(txtContrasena);

        JPanel panelRol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRol.add(new JLabel("Rol del usuario:"));
        panelRol.add(cbRol);

        JPanel panelEmpleado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEmpleado.add(new JLabel("Empleado vinculado:"));
        panelEmpleado.add(cbEmpleados);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            String nombreUsuario = txtNombreUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String rol = (String) cbRol.getSelectedItem();
            String empleadoSeleccionado = (String) cbEmpleados.getSelectedItem();

            if (!Validaciones.validarNoVacio(nombreUsuario) || !Validaciones.validarNoVacio(rol) || empleadoSeleccionado == null) {
                JOptionPane.showMessageDialog(dialogo, "Nombre de usuario, rol y empleado son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Extraer ID del empleado del string seleccionado
                int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);
                Empleado empleado = controlador.buscarEmpleadoPorId(idEmpleado);

                Usuario usuarioActualizado = controlador.buscarUsuarioPorId(idUsuario);
                usuarioActualizado.setNombreUsuario(nombreUsuario);
                if (!contrasena.isEmpty()) {
                    usuarioActualizado.setContrasena(contrasena);
                }
                usuarioActualizado.getEmpleado().setRol(Rol.valueOf(rol));
                usuarioActualizado.setEmpleado(empleado);

                controlador.actualizarUsuario(usuarioActualizado);

                JOptionPane.showMessageDialog(dialogo, "Usuario actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
                vista.mostrarPanel("Gestionar Usuarios");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(panelNombre);
        dialogo.add(panelContrasena);
        dialogo.add(panelRol);
        dialogo.add(panelEmpleado);
        dialogo.add(panelBotones);

        dialogo.setVisible(true);
    }

    private void eliminarUsuario(TotalTalentControlador controlador, DashboardVista vista) {
        JTable tablaUsuarios = vista.getTablaSeleccionada();
        // Obtener el usuario seleccionado de la tabla
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        int idUsuario = (Integer) modelo.getValueAt(selectedRow, 0);
        String nombreUsuario = (String) modelo.getValueAt(selectedRow, 1);

        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro de eliminar el usuario '" + nombreUsuario + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminarUsuario(idUsuario);
                JOptionPane.showMessageDialog(vista, "Usuario eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.mostrarPanel("Gestionar Usuarios");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        JButton btnEliminar = new JButton("Eliminar Empleado");
        JButton btnBuscar = new JButton("Buscar Empleado");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);
        estilizarBoton(btnBuscar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarEmpleado(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarEmpleado(controlador, vista));
        btnEliminar.addActionListener(e -> eliminarEmpleado(controlador, vista));
        btnBuscar.addActionListener(e -> mostrarDialogoBuscarEmpleado(controlador, vista));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
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

        // Cargar datos ordenados por ID
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            empleados = empleados.stream().sorted(Comparator.comparing(Empleado::getIdEmpleado)).collect(Collectors.toList());
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
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "RECLUTADOR", "GERENTE", "EMPLEADO"});

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
        dialogo.add(new JLabel("Rol:*"));
        dialogo.add(cbRol);
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
                empleado.setRol(Rol.valueOf((String) cbRol.getSelectedItem()));

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
        JTable tablaEmpleados = vista.getTablaSeleccionada();
        if (tablaEmpleados == null) {
            JOptionPane.showMessageDialog(vista, "No hay tabla de empleados seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = tablaEmpleados.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un empleado de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
        int idEmpleado = (Integer) modelo.getValueAt(selectedRow, 0);

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
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "RECLUTADOR", "GERENTE", "EMPLEADO"});
        cbRol.setSelectedItem(empleado.getRol().toString());

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
        dialogo.add(new JLabel("Rol:*"));
        dialogo.add(cbRol);
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
                empleado.setRol(Rol.valueOf((String) cbRol.getSelectedItem()));

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
    }

    private void eliminarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JTable tablaEmpleados = vista.getTablaSeleccionada();
        if (tablaEmpleados == null) {
            JOptionPane.showMessageDialog(vista, "No hay tabla de empleados seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = tablaEmpleados.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un empleado de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
        int idEmpleado = (Integer) modelo.getValueAt(selectedRow, 0);
        String nombreEmpleado = (String) modelo.getValueAt(selectedRow, 1) + " " + (String) modelo.getValueAt(selectedRow, 2);

        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro de eliminar el empleado '" + nombreEmpleado + "'?\nEsta acción también eliminará cualquier usuario asociado.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Verificar si el empleado tiene un usuario asociado y eliminarlo primero
                List<Usuario> usuarios = controlador.obtenerTodosUsuarios();
                for (Usuario usuario : usuarios) {
                    if (usuario.getEmpleado() != null && usuario.getEmpleado().getIdEmpleado() == idEmpleado) {
                        controlador.eliminarUsuario(usuario.getIdUsuario());
                        break;
                    }
                }

                controlador.eliminarEmpleado(idEmpleado);
                JOptionPane.showMessageDialog(vista, "Empleado eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.mostrarPanel("Gestionar Empleados");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoBuscarEmpleado(TotalTalentControlador controlador, DashboardVista vista) {
        JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(vista), "Buscar Empleado", true);
        dialogo.setLayout(new GridLayout(4, 2, 10, 10));
        dialogo.setSize(400, 200);
        dialogo.setLocationRelativeTo(vista);

        JTextField txtNombre = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtDni = new JTextField();

        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(txtNombre);
        dialogo.add(new JLabel("Apellidos:"));
        dialogo.add(txtApellidos);
        dialogo.add(new JLabel("DNI:"));
        dialogo.add(txtDni);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnCancelar = new JButton("Cancelar");

        btnBuscar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String dni = txtDni.getText().trim();

            if (nombre.isEmpty() && apellidos.isEmpty() && dni.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese al menos un criterio de búsqueda", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                List<Empleado> empleados = controlador.obtenerTodosEmpleados();
                List<Empleado> resultados = new java.util.ArrayList<>();

                for (Empleado emp : empleados) {
                    boolean coincide = true;

                    if (!nombre.isEmpty() && !emp.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                        coincide = false;
                    }
                    if (!apellidos.isEmpty() && !emp.getApellidos().toLowerCase().contains(apellidos.toLowerCase())) {
                        coincide = false;
                    }
                    if (!dni.isEmpty() && !emp.getDni().equals(dni)) {
                        coincide = false;
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
        JButton btnEliminar = new JButton("Eliminar Contrato");
        JButton btnBuscar = new JButton("Buscar Contrato");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);
        estilizarBoton(btnBuscar);

        btnAgregar.addActionListener(e -> mostrarDialogoAgregarContrato(controlador, vista));
        btnEditar.addActionListener(e -> mostrarDialogoEditarContrato(controlador, vista));
        btnEliminar.addActionListener(e -> eliminarContrato(controlador, vista));
        btnBuscar.addActionListener(e -> mostrarDialogoBuscarContrato(controlador, vista));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
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

        // Cargar datos ordenados por ID
        try {
            List<Contrato> contratos = controlador.obtenerTodosContratos();
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

    private void mostrarDialogoAgregarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de agregar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoEditarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de editar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de eliminar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoBuscarContrato(TotalTalentControlador controlador, DashboardVista vista) {
        JOptionPane.showMessageDialog(vista, "Funcionalidad de buscar contrato en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPanelLogs(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelLogs = new JPanel();
        panelLogs.setBackground(getColorFondo());
        panelLogs.setLayout(new BoxLayout(panelLogs, BoxLayout.Y_AXIS));
        panelLogs.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("LOGS DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo para buscar logs por usuario
        JPanel panelBuscar = new JPanel(new FlowLayout());
        panelBuscar.setBackground(getColorFondo());
        JTextField txtUsuario = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar Logs");
        estilizarBoton(btnBuscar);

        panelBuscar.add(new JLabel("Usuario:"));
        panelBuscar.add(txtUsuario);
        panelBuscar.add(btnBuscar);

        // Área de texto para mostrar logs
        JTextArea txtLogs = new JTextArea(20, 50);
        txtLogs.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtLogs);

        btnBuscar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            if (!usuario.isEmpty()) {
                try {
                    List<String> logs = controlador.obtenerLogsPorUsuario(usuario);
                    txtLogs.setText("");
                    for (String log : logs) {
                        txtLogs.append(log + "\n");
                    }
                } catch (Exception ex) {
                    txtLogs.setText("Error al obtener logs: " + ex.getMessage());
                }
            }
        });

        panelLogs.add(lblTitulo);
        panelLogs.add(Box.createVerticalStrut(20));
        panelLogs.add(panelBuscar);
        panelLogs.add(Box.createVerticalStrut(20));
        panelLogs.add(scrollPane);

        panelContenido.add(panelLogs, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelReportes(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelReportes = new JPanel();
        panelReportes.setBackground(getColorFondo());
        panelReportes.setLayout(new BoxLayout(panelReportes, BoxLayout.Y_AXIS));
        panelReportes.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("GENERACIÓN DE REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(getColorFondo());

        JButton btnReporteEmpleados = new JButton("Generar Reporte Empleados");
        JButton btnReporteContratos = new JButton("Generar Reporte Contratos");

        estilizarBoton(btnReporteEmpleados);
        estilizarBoton(btnReporteContratos);

        JTextArea txtReporte = new JTextArea(25, 60);
        txtReporte.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtReporte);

        btnReporteEmpleados.addActionListener(e -> {
            try {
                String reporte = controlador.generarReporteEmpleados();
                txtReporte.setText(reporte);
            } catch (Exception ex) {
                txtReporte.setText("Error al generar reporte: " + ex.getMessage());
            }
        });

        btnReporteContratos.addActionListener(e -> {
            try {
                String reporte = controlador.generarReporteContratos();
                txtReporte.setText(reporte);
            } catch (Exception ex) {
                txtReporte.setText("Error al generar reporte: " + ex.getMessage());
            }
        });

        panelBotones.add(btnReporteEmpleados);
        panelBotones.add(btnReporteContratos);

        panelReportes.add(lblTitulo);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(panelBotones);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(scrollPane);

        panelContenido.add(panelReportes, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelEstadisticas(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setBackground(getColorFondo());
        panelEstadisticas.setLayout(new BoxLayout(panelEstadisticas, BoxLayout.Y_AXIS));
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("ESTADÍSTICAS DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txtEstadisticas = new JTextArea(20, 50);
        txtEstadisticas.setEditable(false);

        // Calcular estadísticas
        try {
            List<Empleado> empleados = controlador.obtenerTodosEmpleados();
            List<Contrato> contratos = controlador.obtenerTodosContratos();
            List<Usuario> usuarios = controlador.obtenerTodosUsuarios();

            StringBuilder stats = new StringBuilder();
            stats.append("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");
            stats.append("Total de empleados: ").append(empleados.size()).append("\n");
            stats.append("Total de contratos: ").append(contratos.size()).append("\n");
            stats.append("Total de usuarios: ").append(usuarios.size()).append("\n\n");

            // Estadísticas por rol
            long administradores = usuarios.stream().filter(u -> u.getRol() == Rol.ADMINISTRADOR).count();
            long reclutadores = usuarios.stream().filter(u -> u.getRol() == Rol.RECLUTADOR).count();
            long gerentes = usuarios.stream().filter(u -> u.getRol() == Rol.GERENTE).count();
            long empleadosRol = usuarios.stream().filter(u -> u.getRol() == Rol.EMPLEADO).count();

            stats.append("Distribución por roles:\n");
            stats.append("  Administradores: ").append(administradores).append("\n");
            stats.append("  Reclutadores: ").append(reclutadores).append("\n");
            stats.append("  Gerentes: ").append(gerentes).append("\n");
            stats.append("  Empleados: ").append(empleadosRol).append("\n");

            txtEstadisticas.setText(stats.toString());
        } catch (Exception e) {
            txtEstadisticas.setText("Error al calcular estadísticas: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(txtEstadisticas);

        panelEstadisticas.add(lblTitulo);
        panelEstadisticas.add(Box.createVerticalStrut(20));
        panelEstadisticas.add(scrollPane);

        panelContenido.add(panelEstadisticas, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
