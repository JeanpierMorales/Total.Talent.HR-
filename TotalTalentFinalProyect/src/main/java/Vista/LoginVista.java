package Vista;

import Controlador.TotalTalentControlador;
import Modelo.Rol;
import Modelo.Usuario;
import Vista.Strategy.AdminStrategy;
import Vista.Strategy.DashboardStrategy;
import Vista.Strategy.EmpleadoStrategy;
import Vista.Strategy.GerenteStrategy;
import Vista.Strategy.ReclutadorStrategy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Vista de login del sistema Total Talent HR
// Interfaz gráfica para autenticación de usuarios
public class LoginVista extends JFrame {

    private TotalTalentControlador controlador;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnMostrarContrasena;
    private JLabel lblMensaje;

    // Colores pastel
    private final Color COLOR_FONDO = new Color(240, 248, 255); // Alice Blue
    private final Color COLOR_BOTON = new Color(173, 216, 230); // Light Blue
    private final Color COLOR_BOTON_HOVER = new Color(135, 206, 250); // Sky Blue
    private final Color COLOR_TEXTO = new Color(25, 25, 112); // Midnight Blue

    public LoginVista(TotalTalentControlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarVentana();
    }

    private void inicializarComponentes() {
        setTitle("Total Talent HR - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // Panel principal con padding y GridBag para buena alineación
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo / Título centralizadas
        JLabel lblLogo = new JLabel("TOTAL TALENT HR", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(COLOR_TEXTO);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(6, 6, 12, 6));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblLogo, gbc);

        // Línea descriptiva
        JLabel lblSubtitulo = new JLabel("Sistema de gestión de talento humano", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblSubtitulo, gbc);

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(COLOR_TEXTO);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelPrincipal.add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario.setPreferredSize(new Dimension(220, 28));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelPrincipal.add(txtUsuario, gbc);

        // Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(COLOR_TEXTO);
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(lblContrasena, gbc);

        JPanel panelContrasena = new JPanel(new BorderLayout(6, 0));
        panelContrasena.setBackground(COLOR_FONDO);
        txtContrasena = new JPasswordField(20);
        txtContrasena.setBackground(Color.WHITE);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContrasena.setPreferredSize(new Dimension(220, 28));
        panelContrasena.add(txtContrasena, BorderLayout.CENTER);

        btnMostrarContrasena = new JButton("👁");
        btnMostrarContrasena.setBackground(COLOR_BOTON);
        btnMostrarContrasena.setFocusPainted(false);
        btnMostrarContrasena.setPreferredSize(new Dimension(44, 28));
        btnMostrarContrasena.addActionListener(new ActionListener() {
            private boolean mostrando = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                mostrando = !mostrando;
                txtContrasena.setEchoChar(mostrando ? (char) 0 : '•');
                btnMostrarContrasena.setText(mostrando ? "🙈" : "👁");
            }
        });
        panelContrasena.add(btnMostrarContrasena, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panelPrincipal.add(panelContrasena, gbc);

        // Botón Login
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(COLOR_BOTON);
        btnLogin.setForeground(COLOR_TEXTO);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(240, 36));
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON);
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(btnLogin, gbc);

        // Mensaje de error/info
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblMensaje, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        // Configurar autoajuste y accesibilidad
        pack();
        setMinimumSize(new Dimension(420, 340));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void configurarVentana() {
        setResizable(true);
        // Hacer que se ajuste automáticamente
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                revalidate();
                repaint();
            }
        });
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor ingrese usuario y contraseña");
            return;
        }

        // Intentar login
        if (controlador.login(usuario, contrasena)) {
            lblMensaje.setText("Login exitoso. Redirigiendo...");
            lblMensaje.setForeground(new Color(0, 128, 0)); // Verde

            // Obtener usuario actual
            Usuario usuarioActual = controlador.getUsuarioActual();

            // Mostrar dashboard según rol
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mostrarDashboardSegunRol();
                }
            });
        } else {
            lblMensaje.setText("Credenciales incorrectas. Intente nuevamente.");
            lblMensaje.setForeground(Color.RED);
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }

    private void mostrarDashboardSegunRol() {
        Usuario usuarioActual = controlador.getUsuarioActual();
        if (usuarioActual == null) {
            return;
        }

        Rol rol = usuarioActual.getRol();

        // Cerrar ventana de login
        dispose();

        // Usar DashboardVista con estrategias según rol
        DashboardStrategy strategy;
        switch (rol) {
            case ADMINISTRADOR:
                strategy = new AdminStrategy();
                break;
            case RECLUTADOR:
                strategy = new ReclutadorStrategy();
                break;
            case GERENTE:
                strategy = new GerenteStrategy();
                break;
            case EMPLEADO:
                strategy = new EmpleadoStrategy();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        new DashboardVista(controlador, strategy).setVisible(true);
    }

    // Método para mostrar la vista
    public void mostrar() {
        setVisible(true);
    }
}
