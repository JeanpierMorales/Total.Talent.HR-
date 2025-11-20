package Vista.Strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import Controlador.TotalTalentControlador;
import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Usuario;
import Utilidades.Validaciones;
import Vista.DashboardVista;

// Estrategia específica para el rol de Empleado
// Implementa funcionalidades de visualización e edición de datos personales
public class EmpleadoStrategy implements DashboardStrategy {

    // Colores específicos del empleado (grises)
    @Override
    public Color getColorFondo() {
        return new Color(248, 248, 248);
    } // Very Light Gray

    @Override
    public Color getColorBoton() {
        return new Color(169, 169, 169);
    } // Dark Gray

    @Override
    public Color getColorBotonHover() {
        return new Color(105, 105, 105);
    } // Dim Gray

    @Override
    public Color getColorTexto() {
        return new Color(47, 79, 79);
    } // Dark Slate Gray

    @Override
    public Color getColorNavbar() {
        return new Color(220, 220, 220);
    } // Gainsboro

    @Override
    public String getTituloVentana() {
        return "Dashboard Empleado - Total Talent HR";
    }

    @Override
    public String getMensajeBienvenida() {
        return "Panel Principal - Empleado";
    }

    @Override
    public String getMensajeInfo() {
        return "Selecciona una opción del menú lateral para gestionar tus datos personales.";
    }

    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        JButton btnVerDatosPersonales = crearBotonNavbar("Ver Datos Personales", vista);
        JButton btnEditarDatosPersonales = crearBotonNavbar("Editar Datos Personales", vista);
        // JButton btnVerContratos = crearBotonNavbar("Ver Mis Contratos", vista);

        panelIzquierdo.add(btnVerDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnEditarDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        // panelIzquierdo.add(btnVerContratos);

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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBotonHover());
            }

            @Override
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
            case "Ver Datos Personales":
                mostrarPanelDatosPersonales(panelContenido, vista, controlador);
                break;
            case "Editar Datos Personales":
                mostrarPanelEditarDatosPersonales(panelContenido, vista, controlador);
                break;
            // case "Ver Mis Contratos":
            // mostrarPanelMisContratos(panelContenido, vista, controlador);
            // break;
            default:
                vista.mostrarPanelPrincipalPublico();
        }
    }

    private void mostrarPanelDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(getColorFondo());
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Usuario usuarioActual = controlador.getUsuarioActual();
        if (usuarioActual == null || usuarioActual.getEmpleado() == null) {
            JOptionPane.showMessageDialog(vista, "No se encontraron datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener datos frescos del empleado desde el controlador
        Empleado empleado = controlador.buscarEmpleadoPorId(usuarioActual.getEmpleado().getIdEmpleado());
        if (empleado == null) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar los datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sección Información Personal
        JPanel panelPersonal = crearSeccionPanel("Información Personal", getColorFondo());
        panelPersonal.setLayout(new java.awt.GridLayout(5, 2, 10, 5));
        agregarCampoGrid(panelPersonal, "Nombres:", empleado.getNombre());
        agregarCampoGrid(panelPersonal, "Apellidos:", empleado.getApellidos());
        agregarCampoGrid(panelPersonal, "DNI:", empleado.getDni());
        agregarCampoGrid(panelPersonal, "Edad:", String.valueOf(empleado.getEdad()));
        agregarCampoGrid(panelPersonal, "Teléfono:", empleado.getNumero());
        agregarCampoGrid(panelPersonal, "Correo:", empleado.getCorreo());
        agregarCampoGrid(panelPersonal, "Dirección:", empleado.getDireccion());
        agregarCampoGrid(panelPersonal, "Grado Instrucción:", empleado.getGradoInstruccion());
        agregarCampoGrid(panelPersonal, "Carrera:", empleado.getCarrera());
        agregarCampoGrid(panelPersonal, "Comentarios:", empleado.getComentarios());

        // Sección Información Laboral
        JPanel panelLaboral = crearSeccionPanel("Información Laboral", getColorFondo());
        panelLaboral.setLayout(new java.awt.GridLayout(4, 2, 10, 5));
        Contrato contrato = empleado.getContrato();
        if (contrato != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            agregarCampoGrid(panelLaboral, "Tipo Contrato:", contrato.getTipoContrato());
            agregarCampoGrid(panelLaboral, "Salario Base:", "S/ " + String.format("%.2f", contrato.getSalarioBase()));
            agregarCampoGrid(panelLaboral, "Fecha Inicio:", contrato.getFechaInicio() != null ? dateFormat.format(contrato.getFechaInicio()) : "N/A");
            agregarCampoGrid(panelLaboral, "Fecha Fin:", contrato.getFechaFin() != null ? dateFormat.format(contrato.getFechaFin()) : "Indefinido");
            agregarCampoGrid(panelLaboral, "Bonificación:", "S/ " + String.format("%.2f", contrato.getBonificacion()));
            agregarCampoGrid(panelLaboral, "Descuento AFP:", "S/ " + String.format("%.2f", contrato.getDescuentoAFP()));
            agregarCampoGrid(panelLaboral, "Sueldo Neto:", "S/ " + String.format("%.2f", contrato.calcularSueldo()));
        } else {
            agregarCampoGrid(panelLaboral, "Tipo Contrato:", "No asignado");
            agregarCampoGrid(panelLaboral, "Salario Base:", "S/ 0.00");
            agregarCampoGrid(panelLaboral, "Fecha Inicio:", "N/A");
            agregarCampoGrid(panelLaboral, "Fecha Fin:", "N/A");
            agregarCampoGrid(panelLaboral, "Bonificación:", "S/ 0.00");
            agregarCampoGrid(panelLaboral, "Descuento AFP:", "S/ 0.00");
            agregarCampoGrid(panelLaboral, "Sueldo Neto:", "S/ 0.00");
        }

        // Sección Información de Usuario
        JPanel panelUsuario = crearSeccionPanel("Información de Usuario", getColorFondo());
        panelUsuario.setLayout(new java.awt.GridLayout(2, 2, 10, 5));
        agregarCampoGrid(panelUsuario, "Usuario:", usuarioActual.getNombreUsuario());
        JPanel panelPassword = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPassword.setBackground(getColorFondo());
        panelPassword.add(new JLabel("Contraseña:"));
        JTextField txtPassword = new JTextField("********");
        txtPassword.setEditable(false);
        txtPassword.setPreferredSize(new Dimension(120, 25));
        panelPassword.add(txtPassword);
        JButton btnVerPassword = new JButton("Mostrar");
        btnVerPassword.addActionListener(e -> txtPassword.setText(usuarioActual.getContrasena()));
        panelPassword.add(btnVerPassword);
        panelUsuario.add(new JLabel(""));
        panelUsuario.add(panelPassword);

        panelPrincipal.add(panelPersonal);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelLaboral);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelUsuario);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenido.add(scrollPane, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    /* private void agregarCampo(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.setBackground(getColorFondo());
        fila.add(new JLabel(etiqueta));
        JTextField campo = new JTextField(valor != null ? valor : "");
        campo.setEditable(false);
        campo.setPreferredSize(new Dimension(200, 25));
        fila.add(campo);
        panel.add(fila);
    }*/
    private JPanel crearSeccionPanel(String titulo, Color colorFondo) {
        JPanel panelSeccion = new JPanel();
        panelSeccion.setBackground(colorFondo);
        panelSeccion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                titulo,
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                getColorTexto()
        ));
        panelSeccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        titulo,
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        getColorTexto()
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panelSeccion;
    }

    private void agregarCampoGrid(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        JTextField campo = new JTextField(valor != null ? valor : "");
        campo.setEditable(false);
        campo.setPreferredSize(new Dimension(150, 25));
        panel.add(campo);
    }

    private void mostrarPanelEditarDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        if (panelContenido == null || vista == null) {
            return;
        }

        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(getColorFondo());
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título principal
        JLabel lblTitulo = new JLabel("EDITAR DATOS PERSONALES", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;

        if (empleado == null) {
            JOptionPane.showMessageDialog(vista, "No se encontraron datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sección Datos Editables
        JPanel panelEditable = crearSeccionPanel("Datos Editables", getColorFondo());
        panelEditable.setLayout(new java.awt.GridLayout(4, 3, 10, 5));

        JTextField txtDireccion = new JTextField(empleado.getDireccion() != null ? empleado.getDireccion() : "");
        JTextField txtCorreo = new JTextField(empleado.getCorreo() != null ? empleado.getCorreo() : "");
        JTextField txtTelefono = new JTextField(empleado.getNumero() != null ? empleado.getNumero() : "");
        JTextField txtGradoInstruccion = new JTextField(empleado.getGradoInstruccion() != null ? empleado.getGradoInstruccion() : "");

        JCheckBox chkDireccion = new JCheckBox("Actualizar");
        JCheckBox chkCorreo = new JCheckBox("Actualizar");
        JCheckBox chkTelefono = new JCheckBox("Actualizar");
        JCheckBox chkGradoInstruccion = new JCheckBox("Actualizar");

        // Inicialmente deshabilitar los campos de texto
        txtDireccion.setEditable(false);
        txtCorreo.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoInstruccion.setEditable(false);

        // Agregar listeners a los checkboxes para habilitar/deshabilitar campos
        chkDireccion.addActionListener(e -> txtDireccion.setEditable(chkDireccion.isSelected()));
        chkCorreo.addActionListener(e -> txtCorreo.setEditable(chkCorreo.isSelected()));
        chkTelefono.addActionListener(e -> txtTelefono.setEditable(chkTelefono.isSelected()));
        chkGradoInstruccion.addActionListener(e -> txtGradoInstruccion.setEditable(chkGradoInstruccion.isSelected()));

        panelEditable.add(new JLabel("Dirección:"));
        panelEditable.add(txtDireccion);
        panelEditable.add(chkDireccion);
        panelEditable.add(new JLabel("Correo:"));
        panelEditable.add(txtCorreo);
        panelEditable.add(chkCorreo);
        panelEditable.add(new JLabel("Teléfono:"));
        panelEditable.add(txtTelefono);
        panelEditable.add(chkTelefono);
        panelEditable.add(new JLabel("Grado Instrucción:"));
        panelEditable.add(txtGradoInstruccion);
        panelEditable.add(chkGradoInstruccion);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(getColorFondo());

        JButton btnGuardar = new JButton("Guardar Cambios");
        estilizarBoton(btnGuardar);
        btnGuardar.setPreferredSize(new Dimension(150, 35));

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnCancelar);
        btnCancelar.setPreferredSize(new Dimension(120, 35));

        btnGuardar.addActionListener(e -> {
            try {
                boolean actualizado = false;

                // Actualizar dirección si está seleccionada
                if (chkDireccion.isSelected()) {
                    String nuevaDireccion = txtDireccion.getText().trim();
                    if (!Validaciones.validarNoVacio(nuevaDireccion)) {
                        JOptionPane.showMessageDialog(vista, "La dirección no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setDireccion(nuevaDireccion);
                    actualizado = true;
                }

                // Actualizar correo si está seleccionado
                if (chkCorreo.isSelected()) {
                    String nuevoCorreo = txtCorreo.getText().trim();
                    if (!Validaciones.validarEmail(nuevoCorreo)) {
                        JOptionPane.showMessageDialog(vista, "Correo electrónico inválido", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setCorreo(nuevoCorreo);
                    actualizado = true;
                }

                // Actualizar teléfono si está seleccionado
                if (chkTelefono.isSelected()) {
                    String nuevoTelefono = txtTelefono.getText().trim();
                    if (!Validaciones.validarTelefono(nuevoTelefono)) {
                        JOptionPane.showMessageDialog(vista, "Teléfono inválido (debe tener 9 dígitos empezando con 9)", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setNumero(nuevoTelefono);
                    actualizado = true;
                }

                // Actualizar grado de instrucción si está seleccionado
                if (chkGradoInstruccion.isSelected()) {
                    String nuevoGradoInstruccion = txtGradoInstruccion.getText().trim();
                    if (!Validaciones.validarNoVacio(nuevoGradoInstruccion)) {
                        JOptionPane.showMessageDialog(vista, "El grado de instrucción no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setGradoInstruccion(nuevoGradoInstruccion);
                    actualizado = true;
                }

                if (!actualizado) {
                    JOptionPane.showMessageDialog(vista, "Seleccione al menos un campo para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                controlador.actualizarEmpleado(empleado);
                JOptionPane.showMessageDialog(vista, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.mostrarPanel("Ver Datos Personales");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al actualizar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> vista.mostrarPanel("Ver Datos Personales"));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelEditable);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelBotones);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenido.add(scrollPane, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    /*
    private void mostrarPanelMisContratos(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        if (panelContenido == null || vista == null) {
            return;
        }

        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(getColorFondo());
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Obtener empleado actual
        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;

        if (empleado != null) {
            try {
                List<Contrato> contratos = controlador.buscarContratosPorEmpleado(empleado.getIdEmpleado());

                if (contratos.isEmpty()) {
                    JPanel panelSinContratos = new JPanel();
                    panelSinContratos.setBackground(getColorFondo());
                    panelSinContratos.add(new JLabel("No tienes contratos registrados."));
                    panelPrincipal.add(panelSinContratos);
                } else {
                    // Mostrar cada contrato en una sección separada
                    for (int i = 0; i < contratos.size(); i++) {
                        Contrato contrato = contratos.get(i);
                        JPanel panelContrato = crearSeccionPanel("Contrato #" + (i + 1), getColorFondo());
                        panelContrato.setLayout(new java.awt.GridLayout(8, 2, 10, 5));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        agregarCampoGrid(panelContrato, "ID Contrato:", String.valueOf(contrato.getIdContrato()));
                        agregarCampoGrid(panelContrato, "Tipo:", contrato.getTipoContrato());
                        agregarCampoGrid(panelContrato, "Fecha Inicio:", contrato.getFechaInicio() != null ? dateFormat.format(contrato.getFechaInicio()) : "N/A");
                        agregarCampoGrid(panelContrato, "Fecha Fin:", contrato.getFechaFin() != null ? dateFormat.format(contrato.getFechaFin()) : "Indefinido");
                        agregarCampoGrid(panelContrato, "Salario Base:", "S/ " + String.format("%.2f", contrato.getSalarioBase()));
                        agregarCampoGrid(panelContrato, "Bonificación:", "S/ " + String.format("%.2f", contrato.getBonificacion()));
                        agregarCampoGrid(panelContrato, "Descuento AFP:", "S/ " + String.format("%.2f", contrato.getDescuentoAFP()));
                        agregarCampoGrid(panelContrato, "Sueldo Neto:", "S/ " + String.format("%.2f", contrato.calcularSueldo()));

                        panelPrincipal.add(panelContrato);
                        if (i < contratos.size() - 1) {
                            panelPrincipal.add(Box.createVerticalStrut(15));
                        }
                    }
                }
            } catch (Exception e) {
                JPanel panelError = new JPanel();
                panelError.setBackground(getColorFondo());
                panelError.add(new JLabel("Error al cargar contratos: " + e.getMessage()));
                panelPrincipal.add(panelError);
            }
        } else {
            JPanel panelError = new JPanel();
            panelError.setBackground(getColorFondo());
            panelError.add(new JLabel("No se encontraron datos del empleado."));
            panelPrincipal.add(panelError);
        }

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenido.add(scrollPane, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    
     */
    public void estilizarBoton(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(getColorBotonHover());
    }
}
