package Vista;

import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Usuario;
import Vista.Strategy.DashboardStrategy; // Importa la INTERFAZ Strategy.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

// --- Vista Principal (Dashboard) ---
// Esta clase es la ventana principal del sistema (el "Dashboard").
// Es una clase "base" común para todos los roles (Admin, Gerente, etc.).
//
// --- APLICACIÓN DEL PATRÓN STRATEGY (Estrategia) ---
// Esta clase actúa como el "Contexto" en el patrón Strategy.
// No sabe qué botones específicos mostrar en el menú lateral ni qué paneles
// de gestión abrir. En lugar de eso, "delega" esa responsabilidad
// al objeto 'strategy' que recibe en su constructor.
// Si 'strategy' es un AdminStrategy, mostrará botones de admin.
// Si 'strategy' es un EmpleadoStrategy, mostrará botones de empleado.
public class DashboardVista extends JFrame {

    private TotalTalentControlador controlador; // Referencia al controlador.
    private Usuario usuarioActual; // El usuario que inició sesión.
    private JPanel panelContenido; // El panel central que cambiará dinámicamente.
    private DashboardStrategy strategy; // El objeto Estrategia (la clave del patrón).
    
    // Referencia a la JTable que se esté mostrando actualmente.
    // La estrategia (ej. AdminStrategy) la "seteará" al crear el panel.
    // Esto permite a la estrategia saber qué fila está seleccionada al "Editar" o "Eliminar".
    private JTable tablaSeleccionada;

    // Colores base (se definen según la estrategia).
    private Color COLOR_FONDO;
    private Color COLOR_BOTON;
    private Color COLOR_BOTON_HOVER;
    private Color COLOR_TEXTO;
    private Color COLOR_NAVBAR;

    // Constructor del Dashboard.
    // Recibe el controlador y la Estrategia específica (decidida en LoginVista).
    public DashboardVista(TotalTalentControlador controlador, DashboardStrategy strategy) {
        this.controlador = controlador;
        this.usuarioActual = controlador.getUsuarioActual();
        this.strategy = strategy; // Almacena la estrategia (Admin, Gerente, etc.).
        
        // Pide a la estrategia los colores para esta vista.
        // Así, el dashboard de Admin es azul, el de Reclutador verde, etc.
        this.COLOR_FONDO = strategy.getColorFondo();
        this.COLOR_BOTON = strategy.getColorBoton();
        this.COLOR_BOTON_HOVER = strategy.getColorBotonHover();
        this.COLOR_TEXTO = strategy.getColorTexto();
        this.COLOR_NAVBAR = strategy.getColorNavbar();

        inicializarComponentes(); // Construye la GUI base.
        configurarVentana();
        mostrarPanelPrincipal(); // Muestra la bienvenida inicial.
    }

    // Construye los componentes comunes (base) a todos los dashboards.
    private void inicializarComponentes() {
        // Pide el título a la estrategia (ej. "Dashboard Administrador").
        setTitle(strategy.getTituloVentana());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior (logo, bienvenida, logout).
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo
        JLabel lblLogo = new JLabel("LOGO");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogo.setForeground(COLOR_TEXTO);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(100, 50));
        lblLogo.setBorder(BorderFactory.createLineBorder(COLOR_TEXTO, 2));
        panelSuperior.add(lblLogo, BorderLayout.WEST);

        // Título central (Mensaje de Bienvenida)
        // Llama al controlador para obtener los datos del empleado actual.
        Empleado empleadoActual = controlador.empleadoVerMisDatos();
        String nombreEmpleado = empleadoActual != null ? empleadoActual.getNombre() : usuarioActual.getNombreUsuario();
        JLabel lblTitulo = new JLabel("Bienvenid@ " + nombreEmpleado, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXTO);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Botón de Logout (común a todos los roles).
        JButton btnLogout = new JButton("Cerrar Sesión");
        estilizarBoton(btnLogout); // Aplica el estilo de la estrategia.
        btnLogout.addActionListener(e -> realizarLogout());
        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLogout.setBackground(COLOR_FONDO);
        panelLogout.add(btnLogout);
        panelSuperior.add(panelLogout, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Delegación a la Estrategia (Navbar) ---
        // Aquí es donde la magia del patrón Strategy ocurre:
        // Llama al método crearNavbar de la Estrategia inyectada.
        // Si la estrategia es AdminStrategy, creará los botones de Admin.
        // Si es EmpleadoStrategy, creará los botones de Empleado.
        // Esta DashboardVista no sabe ni le importa qué botones se crean.
        JPanel panelIzquierdo = (JPanel) strategy.crearNavbar(this);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel central (aquí es donde la estrategia dibujará los paneles).
        panelContenido = new JPanel();
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setLayout(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);
    }

    // Método helper para estilizar botones (usa los colores de la estrategia).
    private void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        // Efecto Hover (MouseListener)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON_HOVER); // Color hover de la estrategia.
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON); // Color normal de la estrategia.
            }
        });
    }

    // Configura el tamaño y la visibilidad de la ventana.
    private void configurarVentana() {
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null); // Centra la ventana.
        setResizable(true);
    }

    // Lógica de Logout.
    private void realizarLogout() {
        controlador.logout(); // Llama al controlador para limpiar la sesión.
        dispose(); // Cierra esta ventana (Dashboard).
        // Crea y muestra la LoginVista nuevamente.
        new LoginVista(controlador).mostrar();
    }

    // Muestra el panel de bienvenida por defecto.
    private void mostrarPanelPrincipal() {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelBienvenida = new JPanel();
        panelBienvenida.setBackground(COLOR_FONDO);
        panelBienvenida.setLayout(new BoxLayout(panelBienvenida, BoxLayout.Y_AXIS));
        panelBienvenida.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Pide los mensajes de bienvenida específicos del rol a la Estrategia.
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

    // --- Método Clave del Patrón Strategy ---
    // Este método público es llamado por los botones del Navbar (que fueron creados
    // por la estrategia, ej. AdminStrategy.crearBotonNavbar).
    // Cuando el usuario hace clic en "Gestionar Usuarios", ese botón llama a
    // vista.mostrarPanel("Gestionar Usuarios").
    public void mostrarPanel(String panelNombre) {
        // Esta vista (Contexto) no sabe cómo gestionar usuarios.
        // Simplemente le dice a su estrategia: "Oye, el usuario quiere
        // mostrar 'Gestionar Usuarios', encárgate tú".
        strategy.mostrarPanel(panelNombre, panelContenido, this);
    }

    // --- Getters y Setters (usados por las Estrategias) ---

    // Permite a las clases Strategy (AdminStrategy, etc.) acceder al Controlador
    // para pedir datos (ej. controlador.obtenerTodosEmpleados()).
    public TotalTalentControlador getControlador() {
        return controlador;
    }

    // Permite a las estrategias llamar (resetear) al panel de bienvenida.
    public void mostrarPanelPrincipalPublico() {
        mostrarPanelPrincipal();
    }

    // Permite a las estrategias obtener la tabla seleccionada (para editar/eliminar).
    public JTable getTablaSeleccionada() {
        return tablaSeleccionada;
    }

    // Permite a las estrategias "registrar" la tabla que están mostrando actualmente.
    public void setTablaSeleccionada(JTable tablaSeleccionada) {
        this.tablaSeleccionada = tablaSeleccionada;
    }

    // Método helper para mostrar un panel de error genérico.
    private void mostrarError(String mensaje) {
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