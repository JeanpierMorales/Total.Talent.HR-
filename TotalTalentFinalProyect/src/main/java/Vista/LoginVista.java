package Vista;

import Controlador.TotalTalentControlador;
import Modelo.Rol;
import Modelo.Usuario;
// Importamos todas las estrategias (Strategy) que esta vista necesitará "inyectar"
// en el DashboardVista según el rol del usuario.
import Vista.Strategy.AdminStrategy;
import Vista.Strategy.DashboardStrategy;
import Vista.Strategy.EmpleadoStrategy;
import Vista.Strategy.GerenteStrategy;
import Vista.Strategy.ReclutadorStrategy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// --- Vista de Login (Inicio de Sesión) ---
// Esta clase representa la ventana de inicio de sesión del sistema.
// Es la primera interfaz gráfica que ve el usuario al ejecutar la aplicación.
// Extiende de JFrame, lo que significa que es una ventana principal.
// Su responsabilidad es capturar el usuario y la contraseña y enviarlos
// al TotalTalentControlador para su validación.
public class LoginVista extends JFrame {

    // Referencia al controlador principal de la aplicación.
    private TotalTalentControlador controlador;
    
    // Componentes de la interfaz gráfica (Swing).
    private JTextField txtUsuario; // Campo para el nombre de usuario.
    private JPasswordField txtContrasena; // Campo para la contraseña.
    private JButton btnLogin; // Botón para "Iniciar Sesión".
    private JButton btnMostrarContrasena; // Botón para ver/ocultar contraseña.
    private JLabel lblMensaje; // Etiqueta para mostrar mensajes de error (ej. "Datos incorrectos").

    // --- Paleta de Colores ---
    // Define los colores pastel (tonos azules) para esta vista.
    private final Color COLOR_FONDO = new Color(240, 248, 255); // Alice Blue
    private final Color COLOR_BOTON = new Color(173, 216, 230); // Light Blue
    private final Color COLOR_BOTON_HOVER = new Color(135, 206, 250); // Sky Blue
    private final Color COLOR_TEXTO = new Color(25, 25, 112); // Midnight Blue

    // Constructor de la LoginVista.
    // Recibe la instancia del controlador cuando es creada (en la clase main).
    public LoginVista(TotalTalentControlador controlador) {
        this.controlador = controlador;
        inicializarComponentes(); // Llama al método que construye la GUI.
        configurarVentana(); // Llama al método que ajusta la ventana.
    }

    // Método principal para construir todos los componentes gráficos de la ventana.
    private void inicializarComponentes() {
        setTitle("Total Talent HR - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación si se cierra esta ventana.
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // Usamos un JPanel con GridBagLayout para centrar y alinear
        // fácilmente los campos y etiquetas.
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para configurar la posición en la grilla.
        gbc.insets = new Insets(8, 8, 8, 8); // Espaciado entre componentes.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que los componentes se estiren horizontalmente.

        // Título
        JLabel lblLogo = new JLabel("TOTAL TALENT HR", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(COLOR_TEXTO);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(6, 6, 12, 6));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        panelPrincipal.add(lblLogo, gbc);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema de gestión de talento humano", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        panelPrincipal.add(lblSubtitulo, gbc);

        // Etiqueta y campo de Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(COLOR_TEXTO);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Resetea a 1 columna.
        panelPrincipal.add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario.setPreferredSize(new Dimension(220, 28));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelPrincipal.add(txtUsuario, gbc);

        // Etiqueta y campo de Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(COLOR_TEXTO);
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(lblContrasena, gbc);

        // Creamos un panel especial para la contraseña, que contendrá
        // el JPasswordField y el botón de "mostrar" (ojo).
        JPanel panelContrasena = new JPanel(new BorderLayout(6, 0));
        panelContrasena.setBackground(COLOR_FONDO);
        txtContrasena = new JPasswordField(20);
        txtContrasena.setBackground(Color.WHITE);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContrasena.setPreferredSize(new Dimension(220, 28));
        panelContrasena.add(txtContrasena, BorderLayout.CENTER);

        // Botón para mostrar/ocultar contraseña
        btnMostrarContrasena = new JButton("👁");
        btnMostrarContrasena.setBackground(COLOR_BOTON);
        btnMostrarContrasena.setFocusPainted(false);
        btnMostrarContrasena.setPreferredSize(new Dimension(44, 28));
        // Lógica interna (ActionListener) para el botón del ojo.
        btnMostrarContrasena.addActionListener(new ActionListener() {
            private boolean mostrando = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                mostrando = !mostrando; // Invierte el estado (mostrando/oculto).
                // setEchoChar((char) 0) hace visible el texto. '•' lo oculta.
                txtContrasena.setEchoChar(mostrando ? (char) 0 : '•');
                btnMostrarContrasena.setText(mostrando ? "🙈" : "👁"); // Cambia el ícono.
            }
        });
        panelContrasena.add(btnMostrarContrasena, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panelPrincipal.add(panelContrasena, gbc);

        // Botón de Login
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(COLOR_BOTON);
        btnLogin.setForeground(COLOR_TEXTO);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(240, 36));
        // Efecto Hover (cambio de color al pasar el mouse)
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON);
            }
        });
        // Acción principal del botón Login (ActionListener)
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin(); // Llama al método clave de esta clase.
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.anchor = GridBagConstraints.CENTER; // Centra el botón.
        panelPrincipal.add(btnLogin, gbc);

        // Etiqueta para mensajes de error (inicialmente vacía y oculta).
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblMensaje, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        // --- Configuración final de la ventana ---
        pack(); // Ajusta el tamaño de la ventana al contenido.
        setMinimumSize(new Dimension(420, 340));
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        // Permite que el usuario presione "Enter" para activar el btnLogin.
        getRootPane().setDefaultButton(btnLogin);
    }

    // Configura la ventana para que se ajuste al cambiar su tamaño.
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

    // --- Lógica de Negocio de la Vista ---

    // Método llamado por el btnLogin.
    private void realizarLogin() {
        // 1. Obtiene los datos de los campos de texto.
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        // 2. Validación simple de campos vacíos.
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor ingrese usuario y contraseña");
            return;
        }

        // 3. Llama al Controlador para que valide las credenciales.
        if (controlador.login(usuario, contrasena)) {
            // 4. Si el controlador.login() devuelve TRUE (éxito):
            lblMensaje.setText("Login exitoso. Redirigiendo...");
            lblMensaje.setForeground(new Color(0, 128, 0)); // Color verde.

            // Pide al controlador el objeto Usuario que acaba de iniciar sesión.
            Usuario usuarioActual = controlador.getUsuarioActual();

            // 5. Inicia la transición a la siguiente ventana (Dashboard).
            // Se usa SwingUtilities.invokeLater para asegurar que la nueva ventana
            // se cree en el hilo correcto de Swing (Event Dispatch Thread).
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mostrarDashboardSegunRol(); // Llama al método de transición.
                }
            });
        } else {
            // 6. Si el controlador.login() devuelve FALSE (fallo):
            lblMensaje.setText("Credenciales incorrectas. Intente nuevamente.");
            lblMensaje.setForeground(Color.RED);
            txtContrasena.setText(""); // Limpia la contraseña.
            txtContrasena.requestFocus(); // Pone el foco de nuevo en la contraseña.
        }
    }

    // --- Transición a Dashboard (Patrón Strategy) ---
    // Este método se encarga de cerrar el Login y abrir el Dashboard
    // con la estrategia (vista) correcta según el rol del usuario.
    private void mostrarDashboardSegunRol() {
        Usuario usuarioActual = controlador.getUsuarioActual();
        if (usuarioActual == null) {
            return;
        }

        // Obtiene el Rol (ADMINISTRADOR, GERENTE, etc.) del empleado asociado al usuario.
        Rol rol = usuarioActual.getRol();

        // 1. Cierra la ventana de login actual.
        dispose();

        // --- APLICACIÓN DEL PATRÓN STRATEGY ---
        // 2. Decide qué "estrategia" (comportamiento) debe tener el Dashboard.
        // La variable 'strategy' contendrá el objeto específico del rol.
        DashboardStrategy strategy;
        switch (rol) {
            case ADMINISTRADOR:
                strategy = new AdminStrategy(); // Usa la estrategia de Admin.
                break;
            case RECLUTADOR:
                strategy = new ReclutadorStrategy(); // Usa la estrategia de Reclutador.
                break;
            case GERENTE:
                strategy = new GerenteStrategy(); // Usa la estrategia de Gerente.
                break;
            case EMPLEADO:
                strategy = new EmpleadoStrategy(); // Usa la estrategia de Empleado.
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        // 3. Crea la nueva ventana DashboardVista y le "inyecta"
        //    tanto el controlador como la estrategia seleccionada.
        new DashboardVista(controlador, strategy).setVisible(true);
    }

    // Método público para hacer visible esta ventana.
    // Es llamado por el main (TotalTalentFinalProyect.java).
    public void mostrar() {
        setVisible(true);
    }
}