package Vista.Strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Controlador.TotalTalentControlador;
import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Rol;
import Modelo.Usuario;
import Vista.DashboardVista;

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

        panelIzquierdo.add(btnVerReportes);

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

        JLabel lblTitulo = new JLabel("REPORTES Y ESTADÍSTICAS DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de filtros y búsqueda
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBackground(getColorFondo());

        JComboBox<String> cbTipoReporte = new JComboBox<>(new String[]{"Empleados", "Contratos", "Todos"});
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        estilizarBoton(btnBuscar);
        estilizarBoton(btnLimpiar);

        panelFiltros.add(new JLabel("Tipo de Reporte:"));
        panelFiltros.add(cbTipoReporte);
        panelFiltros.add(new JLabel("Buscar:"));
        panelFiltros.add(txtBuscar);
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnLimpiar);

        // Panel para reportes y gráficos
        JPanel panelContenidoReportes = new JPanel();
        panelContenidoReportes.setBackground(getColorFondo());
        panelContenidoReportes.setLayout(new BoxLayout(panelContenidoReportes, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelContenidoReportes);

        // Cargar reporte inicial con gráficos
        cargarReporteGrafico(controlador, panelContenidoReportes, (String) cbTipoReporte.getSelectedItem(), "");

        btnBuscar.addActionListener(e -> {
            String tipo = (String) cbTipoReporte.getSelectedItem();
            String filtro = txtBuscar.getText().trim();
            panelContenidoReportes.removeAll();
            cargarReporteGrafico(controlador, panelContenidoReportes, tipo, filtro);
            panelContenidoReportes.revalidate();
            panelContenidoReportes.repaint();
        });

        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            cbTipoReporte.setSelectedIndex(0);
            panelContenidoReportes.removeAll();
            cargarReporteGrafico(controlador, panelContenidoReportes, "Todos", "");
            panelContenidoReportes.revalidate();
            panelContenidoReportes.repaint();
        });

        panelReportes.add(lblTitulo);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(panelFiltros);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(scrollPane);

        panelContenido.add(panelReportes, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void cargarReporteGrafico(TotalTalentControlador controlador, JPanel panelContenido, String tipo, String filtro) {
        try {
            List<Empleado> empleados = new ArrayList<>();
            List<Contrato> contratos = new ArrayList<>();
            try {
                empleados = controlador.obtenerTodosEmpleados();
            } catch (Exception e) {
                // Si no tiene permisos, mostrar mensaje de error
                JLabel lblError = new JLabel("No tiene permisos para ver empleados: " + e.getMessage());
                lblError.setForeground(Color.RED);
                panelContenido.add(lblError);
                return;
            }
            try {
                contratos = controlador.obtenerTodosContratos();
            } catch (Exception e) {
                // Si no tiene permisos, mostrar mensaje de error
                JLabel lblError = new JLabel("No tiene permisos para ver contratos: " + e.getMessage());
                lblError.setForeground(Color.RED);
                panelContenido.add(lblError);
                return;
            }

            // Aplicar filtro si existe
            List<Empleado> empleadosFiltrados = empleados;
            List<Contrato> contratosFiltrados = contratos;

            if (!filtro.isEmpty()) {
                empleadosFiltrados = empleados.stream()
                        .filter(e -> e.getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || e.getApellidos().toLowerCase().contains(filtro.toLowerCase())
                        || e.getDni().contains(filtro))
                        .collect(Collectors.toList());

                contratosFiltrados = contratos.stream()
                        .filter(c -> c.getTipoContrato().toLowerCase().contains(filtro.toLowerCase())
                        || (c.getEmpleado() != null
                        && (c.getEmpleado().getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || c.getEmpleado().getApellidos().toLowerCase().contains(filtro.toLowerCase()))))
                        .collect(Collectors.toList());
            }

            if ("Empleados".equals(tipo) || "Todos".equals(tipo)) {
                // Gráfico de pastel: Distribución de empleados por rol
                DefaultPieDataset datasetEmpleados = new DefaultPieDataset();
                long adminCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.ADMINISTRADOR).count();
                long reclutadorCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.RECLUTADOR).count();
                long gerenteCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.GERENTE).count();
                long empleadoCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.EMPLEADO).count();

                datasetEmpleados.setValue("Administradores", adminCount);
                datasetEmpleados.setValue("Reclutadores", reclutadorCount);
                datasetEmpleados.setValue("Gerentes", gerenteCount);
                datasetEmpleados.setValue("Empleados", empleadoCount);

                JFreeChart chartEmpleados = ChartFactory.createPieChart(
                        "Distribución de Empleados por Rol",
                        datasetEmpleados,
                        true, true, false
                );

                ChartPanel chartPanelEmpleados = new ChartPanel(chartEmpleados);
                chartPanelEmpleados.setPreferredSize(new Dimension(500, 350));

                // Estadísticas textuales
                JPanel panelStatsEmpleados = new JPanel();
                panelStatsEmpleados.setBackground(getColorFondo());
                panelStatsEmpleados.setLayout(new BoxLayout(panelStatsEmpleados, BoxLayout.Y_AXIS));

                double edadPromedio = empleadosFiltrados.stream().mapToInt(Empleado::getEdad).average().orElse(0.0);

                JLabel lblTotalEmpleados = new JLabel("Total empleados: " + empleadosFiltrados.size());
                lblTotalEmpleados.setFont(new Font("Arial", Font.BOLD, 14));
                lblTotalEmpleados.setForeground(getColorTexto());

                JLabel lblEdadPromedio = new JLabel("Edad promedio: " + String.format("%.1f", edadPromedio) + " años");
                lblEdadPromedio.setFont(new Font("Arial", Font.PLAIN, 12));
                lblEdadPromedio.setForeground(getColorTexto());

                panelStatsEmpleados.add(lblTotalEmpleados);
                panelStatsEmpleados.add(Box.createVerticalStrut(5));
                panelStatsEmpleados.add(lblEdadPromedio);

                // Lista de empleados
                JTextArea txtEmpleados = new JTextArea(10, 50);
                txtEmpleados.setEditable(false);
                StringBuilder empleadosText = new StringBuilder("LISTA DE EMPLEADOS:\n");
                empleadosText.append("==================\n");
                for (Empleado emp : empleadosFiltrados) {
                    empleadosText.append(String.format("ID: %d - %s %s - %s - %s\n",
                            emp.getIdEmpleado(), emp.getNombre(), emp.getApellidos(),
                            emp.getDni(), emp.getRol()));
                }
                txtEmpleados.setText(empleadosText.toString());
                JScrollPane scrollEmpleados = new JScrollPane(txtEmpleados);

                panelContenido.add(new JLabel("EMPLEADOS", SwingConstants.CENTER));
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(chartPanelEmpleados);
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(panelStatsEmpleados);
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(scrollEmpleados);
                panelContenido.add(Box.createVerticalStrut(30));
            }

            if ("Contratos".equals(tipo) || "Todos".equals(tipo)) {
                // Gráfico de barras: Distribución de contratos por tipo
                DefaultCategoryDataset datasetContratos = new DefaultCategoryDataset();
                long parcialCount = contratosFiltrados.stream().filter(c -> "Parcial".equals(c.getTipoContrato())).count();
                long planillaCount = contratosFiltrados.stream().filter(c -> "Planilla".equals(c.getTipoContrato())).count();
                long locacionCount = contratosFiltrados.stream().filter(c -> "Locación".equals(c.getTipoContrato())).count();

                datasetContratos.addValue(parcialCount, "Contratos", "Parcial");
                datasetContratos.addValue(planillaCount, "Contratos", "Planilla");
                datasetContratos.addValue(locacionCount, "Contratos", "Locación");

                JFreeChart chartContratos = ChartFactory.createBarChart(
                        "Distribución de Contratos por Tipo",
                        "Tipo de Contrato",
                        "Cantidad",
                        datasetContratos,
                        PlotOrientation.VERTICAL,
                        false, true, false
                );

                ChartPanel chartPanelContratos = new ChartPanel(chartContratos);
                chartPanelContratos.setPreferredSize(new Dimension(500, 350));

                // Estadísticas textuales
                JPanel panelStatsContratos = new JPanel();
                panelStatsContratos.setBackground(getColorFondo());
                panelStatsContratos.setLayout(new BoxLayout(panelStatsContratos, BoxLayout.Y_AXIS));

                double salarioPromedio = contratosFiltrados.stream().mapToDouble(Contrato::getSalarioBase).average().orElse(0.0);

                JLabel lblTotalContratos = new JLabel("Total contratos: " + contratosFiltrados.size());
                lblTotalContratos.setFont(new Font("Arial", Font.BOLD, 14));
                lblTotalContratos.setForeground(getColorTexto());

                JLabel lblSalarioPromedio = new JLabel("Salario promedio: S/ " + String.format("%.2f", salarioPromedio));
                lblSalarioPromedio.setFont(new Font("Arial", Font.PLAIN, 12));
                lblSalarioPromedio.setForeground(getColorTexto());

                panelStatsContratos.add(lblTotalContratos);
                panelStatsContratos.add(Box.createVerticalStrut(5));
                panelStatsContratos.add(lblSalarioPromedio);

                // Lista de contratos
                JTextArea txtContratos = new JTextArea(10, 50);
                txtContratos.setEditable(false);
                StringBuilder contratosText = new StringBuilder("LISTA DE CONTRATOS:\n");
                contratosText.append("==================\n");
                for (Contrato cont : contratosFiltrados) {
                    contratosText.append(String.format("ID: %d - %s - %s - S/ %.2f\n",
                            cont.getIdContrato(), cont.getTipoContrato(),
                            cont.getEmpleado() != null ? cont.getEmpleado().getNombre() + " " + cont.getEmpleado().getApellidos() : "Sin asignar",
                            cont.getSalarioBase()));
                }
                txtContratos.setText(contratosText.toString());
                JScrollPane scrollContratos = new JScrollPane(txtContratos);

                panelContenido.add(new JLabel("CONTRATOS", SwingConstants.CENTER));
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(chartPanelContratos);
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(panelStatsContratos);
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(scrollContratos);
            }

        } catch (Exception ex) {
            JLabel lblError = new JLabel("Error al cargar reportes gráficos: " + ex.getMessage());
            lblError.setForeground(Color.RED);
            panelContenido.add(lblError);
        }
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
