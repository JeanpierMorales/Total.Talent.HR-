package Vista;

import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Usuario;
import Vista.Strategy.DashboardStrategy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clase base para todos los dashboards
// Encapsula elementos comunes y delega funcionalidades específicas a estrategias
public class DashboardVista extends JFrame {

    protected TotalTalentControlador controlador;
    protected Usuario usuarioActual;
    protected JPanel panelContenido;
    protected DashboardStrategy strategy;
    protected JTable tablaSeleccionada;

    // Colores base (pueden ser sobrescritos por estrategias)
    protected Color COLOR_FONDO;
    protected Color COLOR_BOTON;
    protected Color COLOR_BOTON_HOVER;
    protected Color COLOR_TEXTO;
    protected Color COLOR_NAVBAR;

    public DashboardVista(TotalTalentControlador controlador, DashboardStrategy strategy) {
        this.controlador = controlador;
        this.usuarioActual = controlador.getUsuarioActual();
        this.strategy = strategy;
        this.COLOR_FONDO = strategy.getColorFondo();
        this.COLOR_BOTON = strategy.getColorBoton();
        this.COLOR_BOTON_HOVER = strategy.getColorBotonHover();
        this.COLOR_TEXTO = strategy.getColorTexto();
        this.COLOR_NAVBAR = strategy.getColorNavbar();

        inicializarComponentes();
        configurarVentana();
        mostrarPanelPrincipal();
    }

    private void inicializarComponentes() {
        setTitle(strategy.getTituloVentana());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior con título central y logo
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo placeholder (izquierda)
        JLabel lblLogo = new JLabel("LOGO");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogo.setForeground(COLOR_TEXTO);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(100, 50));
        lblLogo.setBorder(BorderFactory.createLineBorder(COLOR_TEXTO, 2));
        panelSuperior.add(lblLogo, BorderLayout.WEST);

        // Título central
        Empleado empleadoActual = controlador.empleadoVerMisDatos();
        String nombreEmpleado = empleadoActual != null ? empleadoActual.getNombre() : usuarioActual.getNombreUsuario();
        JLabel lblTitulo = new JLabel("Bienvenid@ " + nombreEmpleado, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXTO);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Botón logout (derecha)
        JButton btnLogout = new JButton("Cerrar Sesión");
        estilizarBoton(btnLogout);
        btnLogout.addActionListener(e -> realizarLogout());
        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLogout.setBackground(COLOR_FONDO);
        panelLogout.add(btnLogout);
        panelSuperior.add(panelLogout, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel izquierdo (navbar) - creado por estrategia
        JPanel panelIzquierdo = (JPanel) strategy.crearNavbar(this);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel central (contenido dinámico)
        panelContenido = new JPanel();
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setLayout(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);
    }

    protected void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON);
            }
        });
    }

    private void configurarVentana() {
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void realizarLogout() {
        controlador.logout();
        dispose();
        new LoginVista(controlador).mostrar();
    }

    private void mostrarPanelPrincipal() {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel();
        panelBienvenida.setBackground(COLOR_FONDO);
        panelBienvenida.setLayout(new BoxLayout(panelBienvenida, BoxLayout.Y_AXIS));
        panelBienvenida.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblBienvenida = new JLabel(strategy.getMensajeBienvenida(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setForeground(COLOR_TEXTO);
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel(strategy.getMensajeInfo(), SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblInfo.setForeground(COLOR_TEXTO);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBienvenida.add(lblBienvenida);
        panelBienvenida.add(Box.createVerticalStrut(20));
        panelBienvenida.add(lblInfo);

        panelContenido.add(panelBienvenida, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // Método para que estrategias manejen paneles específicos
    public void mostrarPanel(String panelNombre) {
        strategy.mostrarPanel(panelNombre, panelContenido, this);
    }

    // Método público para acceder al controlador desde estrategias
    public TotalTalentControlador getControlador() {
        return controlador;
    }

    // Método público para mostrar panel principal desde estrategias
    public void mostrarPanelPrincipalPublico() {
        mostrarPanelPrincipal();
    }

    // Método para obtener la tabla seleccionada
    public JTable getTablaSeleccionada() {
        return tablaSeleccionada;
    }

    // Método para establecer la tabla seleccionada
    public void setTablaSeleccionada(JTable tablaSeleccionada) {
        this.tablaSeleccionada = tablaSeleccionada;
    }

    protected void mostrarError(String mensaje) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelError = new JPanel();
        panelError.setBackground(COLOR_FONDO);
        panelError.setLayout(new BoxLayout(panelError, BoxLayout.Y_AXIS));
        panelError.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblError = new JLabel("ERROR", SwingConstants.CENTER);
        lblError.setFont(new Font("Arial", Font.BOLD, 24));
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMensaje.setForeground(COLOR_TEXTO);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelError.add(lblError);
        panelError.add(Box.createVerticalStrut(20));
        panelError.add(lblMensaje);

        panelContenido.add(panelError, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
