package Vista.Strategy;


import Controlador.TotalTalentControlador;
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Rol;
import Modelo.Usuario;
import Vista.DashboardVista;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Estrategia específica para el rol de Gerente
// Implementa funcionalidades de visualización de reportes y estadísticas
public class GerenteStrategy implements DashboardStrategy {

    // Colores específicos del gerente (amarillos)
    @Override
    public Color getColorFondo() {
        return new Color(255, 250, 240);
    } // Floral White

    @Override
    public Color getColorBoton() {
        return new Color(255, 215, 0);
    } // Gold

    @Override
    public Color getColorBotonHover() {
        return new Color(255, 165, 0);
    } // Orange

    @Override
    public Color getColorTexto() {
        return new Color(139, 69, 19);
    } // Saddle Brown

    @Override
    public Color getColorNavbar() {
        return new Color(255, 235, 200);
    } // Light Yellow for navbar

    @Override
    public String getTituloVentana() {
        return "Dashboard Gerente - Total Talent HR";
    }

    @Override
    public String getMensajeBienvenida() {
        return "Panel Principal - Gerente";
    }

    @Override
    public String getMensajeInfo() {
        return "Selecciona una opción del menú lateral para ver reportes y estadísticas.";
    }

    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        JButton btnVerReportes = crearBotonNavbar("Ver Reportes", vista);
        JButton btnVerEstadisticas = crearBotonNavbar("Ver Estadísticas", vista);

        panelIzquierdo.add(btnVerReportes);
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
            case "Ver Reportes":
                mostrarPanelReportes(panelContenido, vista, controlador);
                break;
            case "Ver Estadísticas":
                mostrarPanelEstadisticas(panelContenido, vista, controlador);
                break;
            default:
                vista.mostrarPanelPrincipalPublico();
        }
    }

    private void mostrarPanelReportes(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        JPanel panelReportes = new JPanel();
        panelReportes.setBackground(getColorFondo());
        panelReportes.setLayout(new BoxLayout(panelReportes, BoxLayout.Y_AXIS));
        panelReportes.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("REPORTES DEL SISTEMA", SwingConstants.CENTER);
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
