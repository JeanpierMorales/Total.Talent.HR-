package Vista.Strategy;


import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Usuario;
import Utilidades.Validaciones;
import Vista.DashboardVista;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Estrategia específica para el rol de Empleado
// Implementa funcionalidades de visualización y edición de datos personales
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
        JButton btnVerContratos = crearBotonNavbar("Ver Mis Contratos", vista);

        panelIzquierdo.add(btnVerDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnEditarDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnVerContratos);

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
            case "Ver Datos Personales":
                mostrarPanelDatosPersonales(panelContenido, vista, controlador);
                break;
            case "Editar Datos Personales":
                mostrarPanelEditarDatosPersonales(panelContenido, vista, controlador);
                break;
            case "Ver Mis Contratos":
                mostrarPanelMisContratos(panelContenido, vista, controlador);
                break;
            default:
                vista.mostrarPanelPrincipalPublico();
        }
    }

    private void mostrarPanelDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(getColorFondo());
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("MIS DATOS PERSONALES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener empleado actual
        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;

        JTextArea txtDatos = new JTextArea(20, 50);
        txtDatos.setEditable(false);
        txtDatos.setFont(new Font("Monospaced", Font.PLAIN, 12));

        if (empleado != null) {
            txtDatos.setText(usuarioActual.getEmpleado().obtenerDatosEmpleado());
        } else {
            txtDatos.setText("No se encontraron datos del empleado.");
        }

        JScrollPane scrollPane = new JScrollPane(txtDatos);

        panelDatos.add(lblTitulo);
        panelDatos.add(Box.createVerticalStrut(20));
        panelDatos.add(scrollPane);

        panelContenido.add(panelDatos, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelEditarDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelEditar = new JPanel();
        panelEditar.setBackground(getColorFondo());
        panelEditar.setLayout(new BoxLayout(panelEditar, BoxLayout.Y_AXIS));
        panelEditar.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("EDITAR DATOS PERSONALES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener empleado actual
        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;

        if (empleado == null) {
            JOptionPane.showMessageDialog(vista, "No se encontraron datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Formulario de edición
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBackground(getColorFondo());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Información Editable"));

        JTextField txtNumero = new JTextField(empleado.getNumero());
        JTextField txtCorreo = new JTextField(empleado.getCorreo());
        JTextField txtDireccion = new JTextField(empleado.getDireccion());
        JTextField txtGradoInstruccion = new JTextField(empleado.getGradoInstruccion());
        JTextField txtCarrera = new JTextField(empleado.getCarrera());
        JTextArea txtComentarios = new JTextArea(empleado.getComentarios(), 3, 20);

        panelFormulario.add(new JLabel("Número:"));
        panelFormulario.add(txtNumero);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);
        panelFormulario.add(new JLabel("Grado Instrucción:"));
        panelFormulario.add(txtGradoInstruccion);
        panelFormulario.add(new JLabel("Carrera:"));
        panelFormulario.add(txtCarrera);
        panelFormulario.add(new JLabel("Comentarios:"));
        panelFormulario.add(new JScrollPane(txtComentarios));

        JButton btnGuardar = new JButton("Guardar Cambios");
        estilizarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                // Actualizar datos del empleado
                empleado.actualizarDireccion(txtDireccion.getText().trim(),
                        txtCorreo.getText().trim(),
                        txtNumero.getText().trim());
                empleado.setGradoInstruccion(txtGradoInstruccion.getText().trim());
                empleado.setCarrera(txtCarrera.getText().trim());
                empleado.setComentarios(txtComentarios.getText().trim());

                // Validaciones
                if (!Validaciones.validarEmail(empleado.getCorreo())
                        || !Validaciones.validarTelefono(empleado.getNumero())
                        || !Validaciones.validarNoVacio(empleado.getDireccion())) {
                    JOptionPane.showMessageDialog(vista, "Datos inválidos. Verifique email, teléfono y dirección", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controlador.actualizarEmpleado(empleado);
                JOptionPane.showMessageDialog(vista, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.mostrarPanel("Ver Datos Personales");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al actualizar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setBackground(getColorFondo());
        panelBoton.add(btnGuardar);

        panelEditar.add(lblTitulo);
        panelEditar.add(Box.createVerticalStrut(20));
        panelEditar.add(panelFormulario);
        panelEditar.add(Box.createVerticalStrut(20));
        panelEditar.add(panelBoton);

        panelContenido.add(panelEditar, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarPanelMisContratos(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelContratos = new JPanel();
        panelContratos.setBackground(getColorFondo());
        panelContratos.setLayout(new BoxLayout(panelContratos, BoxLayout.Y_AXIS));
        panelContratos.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("MIS CONTRATOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener empleado actual
        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;

        JTextArea txtContratos = new JTextArea(20, 50);
        txtContratos.setEditable(false);

        if (empleado != null) {
            try {
                List<Contrato> contratos = controlador.buscarContratosPorEmpleado(empleado.getIdEmpleado());
                StringBuilder sb = new StringBuilder();
                sb.append("=== MIS CONTRATOS ===\n\n");

                if (contratos.isEmpty()) {
                    sb.append("No tienes contratos registrados.");
                } else {
                    for (Contrato contrato : contratos) {
                        sb.append("Contrato ID: ").append(contrato.getIdContrato()).append("\n");
                        sb.append("Tipo: ").append(contrato.getTipoContrato()).append("\n");
                        sb.append("Fecha Inicio: ").append(contrato.getFechaInicio()).append("\n");
                        sb.append("Fecha Fin: ").append(contrato.getFechaFin() != null ? contrato.getFechaFin() : "Indefinido").append("\n");
                        sb.append("Salario Base: ").append(contrato.getSalarioBase()).append("\n");
                        sb.append("Bonificación: ").append(contrato.getBonificacion()).append("\n");
                        sb.append("Descuento AFP: ").append(contrato.getDescuentoAFP()).append("\n");
                        sb.append("Sueldo Final: ").append(contrato.calcularSueldo()).append("\n");
                        sb.append("----------------------------------------\n\n");
                    }
                }

                txtContratos.setText(sb.toString());
            } catch (Exception e) {
                txtContratos.setText("Error al cargar contratos: " + e.getMessage());
            }
        } else {
            txtContratos.setText("No se encontraron datos del empleado.");
        }

        JScrollPane scrollPane = new JScrollPane(txtContratos);

        panelContratos.add(lblTitulo);
        panelContratos.add(Box.createVerticalStrut(20));
        panelContratos.add(scrollPane);

        panelContenido.add(panelContratos, BorderLayout.CENTER);
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
}
