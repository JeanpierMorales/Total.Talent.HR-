package Vista.Strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Para filtrar las listas.

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

// --- Importaciones de JFreeChart ---
// Esta estrategia es la única que usa la librería externa JFreeChart
// para generar los gráficos de pastel (Pie Chart) y barras (Bar Chart).
import org.jfree.chart.ChartFactory; // El "Factory" para crear gráficos.
import org.jfree.chart.ChartPanel; // El componente Swing para mostrar un gráfico.
import org.jfree.chart.JFreeChart; // La clase base del gráfico.
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset; // Datos para gráfico de barras.
import org.jfree.data.general.DefaultPieDataset; // Datos para gráfico de pastel.

import Controlador.TotalTalentControlador;
import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Rol;
import Modelo.Usuario;
import Vista.DashboardVista;

// --- Estrategia Concreta: Gerente ---
// Esta es la implementación de DashboardStrategy para el rol de GERENTE.
// Su principal responsabilidad es mostrar reportes y estadísticas,
// especialmente los reportes gráficos usando la librería JFreeChart.
public class GerenteStrategy implements DashboardStrategy {

    // --- Implementación de Colores (Estilo Gerente) ---
    // Define la paleta de colores dorados/naranjas para este rol.
    @Override
    public Color getColorFondo() {
        return new Color(255, 250, 240); // Floral White
    }

    @Override
    public Color getColorBoton() {
        return new Color(255, 215, 0); // Gold
    }

    @Override
    public Color getColorBotonHover() {
        return new Color(255, 165, 0); // Orange
    }

    @Override
    public Color getColorTexto() {
        return new Color(139, 69, 19); // Saddle Brown
    }

    @Override
    public Color getColorNavbar() {
        return new Color(255, 235, 200); // Light Yellow
    }

    // --- Implementación de Textos (Estilo Gerente) ---
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

    // --- Implementación de Métodos de UI ---

    // Implementación de crearNavbar para el Gerente.
    // Solo crea el botón "Ver Reportes".
    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar()); // Usa el color amarillo/dorado.
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        // Crea el único botón para este rol.
        JButton btnVerReportes = crearBotonNavbar("Ver Reportes", vista);

        panelIzquierdo.add(btnVerReportes);

        return panelIzquierdo;
    }

    // Método helper (privado) para estilizar el botón del Navbar.
    private JButton crearBotonNavbar(String texto, DashboardVista vista) {
        JButton boton = new JButton(texto);
        boton.setBackground(getColorBoton()); // Usa colores dorados.
        boton.setForeground(getColorTexto());
        // ... (código de estilo y hover) ...
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBotonHover());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(getColorBoton());
            }
        });
        // Acción: Llama a mostrarPanel de la DashboardVista.
        boton.addActionListener(e -> vista.mostrarPanel(texto));
        return boton;
    }

    // Implementación de mostrarPanel para el Gerente.
    // Este "enrutador" solo responde a "Ver Reportes".
    @Override
    public void mostrarPanel(String panelNombre, JPanel panelContenido, Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        TotalTalentControlador controlador = vista.getControlador();

        switch (panelNombre) {
            case "Ver Reportes":
                // Llama al método (privado) que construye el panel de reportes gráficos.
                mostrarPanelReportes(panelContenido, vista, controlador);
                break;
            default:
                vista.mostrarPanelPrincipalPublico(); // Muestra la bienvenida.
        }
    }

    // --- Métodos Privados para Construir Paneles ---

    /**
     * Construye el panel principal de "Reportes y Estadísticas".
     * Este panel contiene filtros y llama a 'cargarReporteGrafico'
     * para mostrar los gráficos de JFreeChart.
     */
    private void mostrarPanelReportes(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll(); // Limpia el panel.
        panelContenido.setLayout(new BorderLayout());

        JPanel panelReportes = new JPanel();
        panelReportes.setBackground(getColorFondo());
        panelReportes.setLayout(new BoxLayout(panelReportes, BoxLayout.Y_AXIS));
        panelReportes.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Título del panel.
        JLabel lblTitulo = new JLabel("REPORTES Y ESTADÍSTICAS DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(getColorTexto());
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Panel de Filtros ---
        // Permite al gerente filtrar qué reporte ver (Empleados, Contratos, Todos)
        // y buscar por un término (ej. nombre de empleado, DNI).
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

        // Panel de contenido (donde irán los gráficos y listas).
        // Este panel se coloca dentro de un JScrollPane.
        JPanel panelContenidoReportes = new JPanel();
        panelContenidoReportes.setBackground(getColorFondo());
        panelContenidoReportes.setLayout(new BoxLayout(panelContenidoReportes, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelContenidoReportes);

        // Carga inicial: Llama al método 'cargarReporteGrafico' con
        // la opción por defecto ("Empleados", aunque el ComboBox diga "Todos" en el código original)
        // y filtro vacío.
        cargarReporteGrafico(controlador, panelContenidoReportes, (String) cbTipoReporte.getSelectedItem(), "");

        // Acción del botón "Buscar".
        btnBuscar.addActionListener(e -> {
            String tipo = (String) cbTipoReporte.getSelectedItem();
            String filtro = txtBuscar.getText().trim();
            panelContenidoReportes.removeAll(); // Limpia el panel de gráficos.
            
            // Vuelve a llamar al método de carga, pero esta vez con los filtros seleccionados.
            cargarReporteGrafico(controlador, panelContenidoReportes, tipo, filtro);
            
            // Refresca la UI.
            panelContenidoReportes.revalidate();
            panelContenidoReportes.repaint();
        });

        // Acción del botón "Limpiar".
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText(""); // Limpia el campo de texto.
            cbTipoReporte.setSelectedIndex(0); // Resetea el ComboBox a "Empleados".
            panelContenidoReportes.removeAll();
            
            // Carga los reportes por defecto (Todos, sin filtro).
            cargarReporteGrafico(controlador, panelContenidoReportes, "Todos", "");
            panelContenidoReportes.revalidate();
            panelContenidoReportes.repaint();
        });

        // Ensambla el panel.
        panelReportes.add(lblTitulo);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(panelFiltros);
        panelReportes.add(Box.createVerticalStrut(20));
        panelReportes.add(scrollPane);

        panelContenido.add(panelReportes, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    /**
     * --- Lógica de Gráficos (JFreeChart) ---
     * Este es el método más importante de esta estrategia.
     * Carga los datos del controlador, los filtra, y usa JFreeChart
     * para construir y añadir los gráficos y listas al 'panelContenido'.
     */
    private void cargarReporteGrafico(TotalTalentControlador controlador, JPanel panelContenido, String tipo, String filtro) {
        try {
            // 1. Obtiene las listas de datos *completas* del controlador.
            List<Empleado> empleados = new ArrayList<>();
            List<Contrato> contratos = new ArrayList<>();
            try {
                empleados = controlador.obtenerTodosEmpleados();
            } catch (Exception e) {
                // Maneja el caso en que el Gerente no tenga permiso (según el Facade).
                JLabel lblError = new JLabel("No tiene permisos para ver empleados: " + e.getMessage());
                lblError.setForeground(Color.RED);
                panelContenido.add(lblError);
                return;
            }
            try {
                contratos = controlador.obtenerTodosContratos();
            } catch (Exception e) {
                // ... (manejo de error similar para contratos) ...
                return;
            }

            // 2. Aplica el filtro de texto (si existe) usando Streams.
            List<Empleado> empleadosFiltrados = empleados;
            List<Contrato> contratosFiltrados = contratos;

            if (!filtro.isEmpty()) {
                // Filtra empleados por nombre, apellido o DNI (ignora mayúsculas).
                empleadosFiltrados = empleados.stream()
                        .filter(e -> e.getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || e.getApellidos().toLowerCase().contains(filtro.toLowerCase())
                        || e.getDni().contains(filtro))
                        .collect(Collectors.toList());

                // Filtra contratos por tipo o por el nombre/apellido del empleado asociado.
                contratosFiltrados = contratos.stream()
                        .filter(c -> c.getTipoContrato().toLowerCase().contains(filtro.toLowerCase())
                        || (c.getEmpleado() != null
                        && (c.getEmpleado().getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || c.getEmpleado().getApellidos().toLowerCase().contains(filtro.toLowerCase()))))
                        .collect(Collectors.toList());
            }

            // 3. Decide qué gráficos construir (Empleados, Contratos, o Todos).
            
            // --- Bloque de Gráfico de Empleados (Pie Chart) ---
            if ("Empleados".equals(tipo) || "Todos".equals(tipo)) {
                
                // 3a. Prepara el dataset (contenedor de datos) para el gráfico de pastel.
                DefaultPieDataset datasetEmpleados = new DefaultPieDataset();
                
                // 3b. Cuenta los empleados por rol usando Streams sobre la lista *filtrada*.
                long adminCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.ADMINISTRADOR).count();
                long reclutadorCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.RECLUTADOR).count();
                long gerenteCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.GERENTE).count();
                long empleadoCount = empleadosFiltrados.stream().filter(e -> e.getRol() == Rol.EMPLEADO).count();

                // 3c. Añade los conteos al dataset.
                datasetEmpleados.setValue("Administradores", adminCount);
                datasetEmpleados.setValue("Reclutadores", reclutadorCount);
                datasetEmpleados.setValue("Gerentes", gerenteCount);
                datasetEmpleados.setValue("Empleados", empleadoCount);

                // 3d. Crea el gráfico de pastel (Pie Chart) usando el ChartFactory.
                JFreeChart chartEmpleados = ChartFactory.createPieChart(
                        "Distribución de Empleados por Rol", // Título del gráfico
                        datasetEmpleados, // Los datos
                        true, // Incluir leyenda
                        true, // Incluir tooltips
                        false // No incluir URLs
                );

                // 3e. Crea un panel de Swing (ChartPanel) para mostrar el gráfico.
                ChartPanel chartPanelEmpleados = new ChartPanel(chartEmpleados);
                chartPanelEmpleados.setPreferredSize(new Dimension(500, 350));

                // 3f. Calcula estadísticas de texto (edad promedio).
                JPanel panelStatsEmpleados = new JPanel();
                panelStatsEmpleados.setBackground(getColorFondo());
                panelStatsEmpleados.setLayout(new BoxLayout(panelStatsEmpleados, BoxLayout.Y_AXIS));

                // Calcula el promedio de edad usando un stream.
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

                // 3g. Crea la lista de texto de empleados (para ver el detalle).
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

                // 3h. Añade todos los componentes de Empleados al panel principal.
                panelContenido.add(new JLabel("EMPLEADOS", SwingConstants.CENTER));
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(chartPanelEmpleados); // El gráfico.
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(panelStatsEmpleados); // Las estadísticas.
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(scrollEmpleados); // La lista.
                panelContenido.add(Box.createVerticalStrut(30)); // Espaciador grande.
            }

            // --- Bloque de Gráfico de Contratos (Bar Chart) ---
            if ("Contratos".equals(tipo) || "Todos".equals(tipo)) {
                
                // 3a. Prepara el dataset (contenedor de datos) para el gráfico de barras.
                DefaultCategoryDataset datasetContratos = new DefaultCategoryDataset();
                
                // 3b. Cuenta los contratos por tipo (Parcial, Planilla, Locación) usando Streams.
                long parcialCount = contratosFiltrados.stream().filter(c -> "Parcial".equals(c.getTipoContrato())).count();
                long planillaCount = contratosFiltrados.stream().filter(c -> "Planilla".equals(c.getTipoContrato())).count();
                long locacionCount = contratosFiltrados.stream().filter(c -> "Locación".equals(c.getTipoContrato())).count();

                // 3c. Añade los valores al dataset.
                datasetContratos.addValue(parcialCount, "Contratos", "Parcial");
                datasetContratos.addValue(planillaCount, "Contratos", "Planilla");
                datasetContratos.addValue(locacionCount, "Contratos", "Locación");

                // 3d. Crea el gráfico de barras (Bar Chart) usando JFreeChart.
                JFreeChart chartContratos = ChartFactory.createBarChart(
                        "Distribución de Contratos por Tipo", // Título
                        "Tipo de Contrato", // Etiqueta Eje X
                        "Cantidad", // Etiqueta Eje Y
                        datasetContratos,
                        PlotOrientation.VERTICAL, // Orientación vertical.
                        false, true, false
                );

                // 3e. Crea el panel para mostrar el gráfico.
                ChartPanel chartPanelContratos = new ChartPanel(chartContratos);
                chartPanelContratos.setPreferredSize(new Dimension(500, 350));

                // 3f. Calcula estadísticas de texto (salario promedio).
                JPanel panelStatsContratos = new JPanel();
                // ... (configuración del panel) ...
                
                // Calcula el promedio de salario base usando un stream.
                double salarioPromedio = contratosFiltrados.stream().mapToDouble(Contrato::getSalarioBase).average().orElse(0.0);

                JLabel lblTotalContratos = new JLabel("Total contratos: " + contratosFiltrados.size());
                // ... (estilo) ...
                JLabel lblSalarioPromedio = new JLabel("Salario promedio: S/ " + String.format("%.2f", salarioPromedio));
                // ... (estilo) ...
                panelStatsContratos.add(lblTotalContratos);
                panelStatsContratos.add(Box.createVerticalStrut(5));
                panelStatsContratos.add(lblSalarioPromedio);

                // 3g. Crea la lista de texto de contratos (para ver el detalle).
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

                // 3h. Añade todos los componentes de Contratos al panel principal.
                panelContenido.add(new JLabel("CONTRATOS", SwingConstants.CENTER));
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(chartPanelContratos); // El gráfico.
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(panelStatsContratos); // Las estadísticas.
                panelContenido.add(Box.createVerticalStrut(10));
                panelContenido.add(scrollContratos); // La lista.
            }

        } catch (Exception ex) {
            // Captura cualquier error general (ej. del Facade o JFreeChart).
            JLabel lblError = new JLabel("Error al cargar reportes gráficos: " + ex.getMessage());
            lblError.setForeground(Color.RED);
            panelContenido.add(lblError);
        }
    }

    // Método helper (privado) para estilizar botones (copia del de AdminStrategy).
    private void estilizarBoton(JButton boton) {
        boton.setBackground(getColorBoton());
        boton.setForeground(getColorTexto());
        // ... (código de estilo y hover) ...
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