package Vista.Strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat; // Para formatear las fechas del contrato.
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
import javax.swing.JCheckBox; // Usado en el panel "Editar".
import javax.swing.SwingConstants;

import Controlador.TotalTalentControlador;
import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Usuario;
import Utilidades.Validaciones; // Importa la clase de validaciones.
import Vista.DashboardVista;

// --- Estrategia Concreta: Empleado ---
// Esta es la implementación de DashboardStrategy para el rol de EMPLEADO.
// Es la estrategia más simple y con menos permisos.
// Su única responsabilidad es permitir al usuario ver sus propios datos
// personales y de contrato, y editar una pequeña parte de ellos
// (como su dirección, correo y teléfono).
public class EmpleadoStrategy implements DashboardStrategy {

    // --- Implementación de Colores (Estilo Empleado) ---
    // Define la paleta de colores grises para este rol.
    @Override
    public Color getColorFondo() {
        return new Color(248, 248, 248); // Gris muy claro
    }

    @Override
    public Color getColorBoton() {
        return new Color(169, 169, 169); // Gris oscuro
    }

    @Override
    public Color getColorBotonHover() {
        return new Color(105, 105, 105); // Gris más oscuro (Dim Gray)
    }

    @Override
    public Color getColorTexto() {
        return new Color(47, 79, 79); // Dark Slate Gray
    }

    @Override
    public Color getColorNavbar() {
        return new Color(220, 220, 220); // Gainsboro
    }

    // --- Implementación de Textos (Estilo Empleado) ---
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

    // --- Implementación de Métodos de UI ---

    // Implementación de crearNavbar para el Empleado.
    // Construye el panel lateral solo con los botones de "Ver Datos" y "Editar Datos".
    @Override
    public JPanel crearNavbar(Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(getColorNavbar()); // Usa el color gris.
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelIzquierdo.setPreferredSize(new Dimension(200, vista.getHeight()));

        // Crea los botones específicos del Empleado.
        JButton btnVerDatosPersonales = crearBotonNavbar("Ver Datos Personales", vista);
        JButton btnEditarDatosPersonales = crearBotonNavbar("Editar Datos Personales", vista);
        // El botón "Ver Mis Contratos" fue comentado en el desarrollo original.

        // Añade los botones al panel lateral.
        panelIzquierdo.add(btnVerDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnEditarDatosPersonales);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        // panelIzquierdo.add(btnVerContratos);

        return panelIzquierdo;
    }

    // Método helper (privado) para estilizar el botón del Navbar.
    private JButton crearBotonNavbar(String texto, DashboardVista vista) {
        JButton boton = new JButton(texto);
        boton.setBackground(getColorBoton()); // Usa colores grises.
        boton.setForeground(getColorTexto());
        // ... (código de estilo y hover) ...
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
        // Acción: Llama a mostrarPanel de la DashboardVista.
        boton.addActionListener(e -> vista.mostrarPanel(texto));
        return boton;
    }

    // Implementación de mostrarPanel para el Empleado.
    // Este es el "enrutador" que decide qué panel mostrar.
    @Override
    public void mostrarPanel(String panelNombre, JPanel panelContenido, Object dashboard) {
        DashboardVista vista = (DashboardVista) dashboard;
        TotalTalentControlador controlador = vista.getControlador();

        // Solo responde a los dos botones definidos en el navbar.
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

    // --- Métodos Privados para Construir Paneles ---

    /**
     * Construye el panel para "Ver Datos Personales".
     * Este panel es de solo lectura y muestra la información completa
     * del empleado, su contrato activo y su usuario.
     */
    private void mostrarPanelDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        panelContenido.removeAll(); // Limpia el panel.
        panelContenido.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(getColorFondo());
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Obtiene el usuario de la sesión actual.
        Usuario usuarioActual = controlador.getUsuarioActual();
        if (usuarioActual == null || usuarioActual.getEmpleado() == null) {
            JOptionPane.showMessageDialog(vista, "No se encontraron datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Obtiene los datos FRESCOS del empleado (incluyendo su contrato).
        // Llama a buscarEmpleadoPorId en lugar de usar getEmpleado() directamente
        // para asegurar que los datos (especialmente el contrato) estén actualizados
        // desde la base de datos (cargados por el EmpleadoMysqlRepository).
        Empleado empleado = controlador.buscarEmpleadoPorId(usuarioActual.getEmpleado().getIdEmpleado());
        if (empleado == null) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar los datos del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 3. Construcción de Secciones ---

        // Sección "Información Personal" (Datos de Empleado).
        JPanel panelPersonal = crearSeccionPanel("Información Personal", getColorFondo());
        panelPersonal.setLayout(new java.awt.GridLayout(5, 2, 10, 5)); // Grilla para alinear.
        // Usa el método helper 'agregarCampoGrid' para añadir filas de Label+TextField.
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

        // Sección "Información Laboral" (Datos del Contrato).
        JPanel panelLaboral = crearSeccionPanel("Información Laboral", getColorFondo());
        panelLaboral.setLayout(new java.awt.GridLayout(4, 2, 10, 5));
        
        // 4. Obtiene el contrato activo del empleado.
        // Este contrato fue cargado por el EmpleadoMysqlRepository
        // cuando se llamó a controlador.buscarEmpleadoPorId(...).
        Contrato contrato = empleado.getContrato();
        
        if (contrato != null) {
            // Si tiene un contrato, muestra los detalles.
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            agregarCampoGrid(panelLaboral, "Tipo Contrato:", contrato.getTipoContrato());
            agregarCampoGrid(panelLaboral, "Salario Base:", "S/ " + String.format("%.2f", contrato.getSalarioBase()));
            agregarCampoGrid(panelLaboral, "Fecha Inicio:", contrato.getFechaInicio() != null ? dateFormat.format(contrato.getFechaInicio()) : "N/A");
            agregarCampoGrid(panelLaboral, "Fecha Fin:", contrato.getFechaFin() != null ? dateFormat.format(contrato.getFechaFin()) : "Indefinido");
            agregarCampoGrid(panelLaboral, "Bonificación:", "S/ " + String.format("%.2f", contrato.getBonificacion()));
            agregarCampoGrid(panelLaboral, "Descuento AFP:", "S/ " + String.format("%.2f", contrato.getDescuentoAFP()));
            
            // --- Polimorfismo en Acción ---
            // Llama al método abstracto calcularSueldo().
            // El objeto 'contrato' (sea Planilla, Parcial o Locacion)
            // sabrá qué fórmula específica ejecutar para calcular el neto.
            agregarCampoGrid(panelLaboral, "Sueldo Neto:", "S/ " + String.format("%.2f", contrato.calcularSueldo()));
        } else {
            // Si no tiene contrato, muestra campos vacíos o N/A.
            agregarCampoGrid(panelLaboral, "Tipo Contrato:", "No asignado");
            agregarCampoGrid(panelLaboral, "Salario Base:", "S/ 0.00");
            // ... (más campos N/A) ...
        }

        // Sección "Información de Usuario" (Datos de Usuario).
        JPanel panelUsuario = crearSeccionPanel("Información de Usuario", getColorFondo());
        panelUsuario.setLayout(new java.awt.GridLayout(2, 2, 10, 5));
        agregarCampoGrid(panelUsuario, "Usuario:", usuarioActual.getNombreUsuario());
        
        // Panel especial para la contraseña (con botón "Mostrar").
        JPanel panelPassword = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPassword.setBackground(getColorFondo());
        panelPassword.add(new JLabel("Contraseña:"));
        JTextField txtPassword = new JTextField("********");
        txtPassword.setEditable(false);
        txtPassword.setPreferredSize(new Dimension(120, 25));
        panelPassword.add(txtPassword);
        JButton btnVerPassword = new JButton("Mostrar");
        // ActionListener que revela la contraseña (obtenida del objeto 'usuarioActual').
        btnVerPassword.addActionListener(e -> txtPassword.setText(usuarioActual.getContrasena()));
        panelPassword.add(btnVerPassword);
        panelUsuario.add(new JLabel("")); // Espacio vacío en la grilla.
        panelUsuario.add(panelPassword);

        // 5. Ensambla el panel.
        panelPrincipal.add(panelPersonal);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelLaboral);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelUsuario);

        // 6. Añade un JScrollPane por si el contenido es muy largo.
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenido.add(scrollPane, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // Método helper (privado) para crear un panel de sección con borde y título.
    private JPanel crearSeccionPanel(String titulo, Color colorFondo) {
        JPanel panelSeccion = new JPanel();
        panelSeccion.setBackground(colorFondo);
        // Crea un borde con título (TitledBorder) usando la fuente y color de la estrategia.
        panelSeccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        titulo,
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        getColorTexto()
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding interno.
        ));
        return panelSeccion;
    }

    // Método helper (privado) para añadir una fila (Label + TextField no editable).
    private void agregarCampoGrid(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        JTextField campo = new JTextField(valor != null ? valor : ""); // Controla valores nulos.
        campo.setEditable(false); // Solo lectura.
        campo.setPreferredSize(new Dimension(150, 25));
        panel.add(campo);
    }

    /**
     * Construye el panel para "Editar Datos Personales".
     * Este panel permite al empleado actualizar ciertos campos.
     */
    private void mostrarPanelEditarDatosPersonales(JPanel panelContenido, DashboardVista vista, TotalTalentControlador controlador) {
        if (panelContenido == null || vista == null) {
            return;
        }
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());
        JPanel panelPrincipal = new JPanel();
        // ... (configuración del panel principal) ...
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("EDITAR DATOS PERSONALES", JLabel.CENTER);
        // ... (estilo del título) ...

        // 1. Obtiene el empleado actual de la sesión.
        Usuario usuarioActual = controlador.getUsuarioActual();
        Empleado empleado = usuarioActual != null ? usuarioActual.getEmpleado() : null;
        if (empleado == null) { /* ... (manejo de error) ... */ return; }

        // 2. Sección de Datos Editables.
        JPanel panelEditable = crearSeccionPanel("Datos Editables", getColorFondo());
        // Grilla de 4 filas x 3 columnas (Label, TextField, CheckBox).
        panelEditable.setLayout(new java.awt.GridLayout(4, 3, 10, 5));

        // 3. Crea los campos y los llena con los datos actuales.
        JTextField txtDireccion = new JTextField(empleado.getDireccion() != null ? empleado.getDireccion() : "");
        JTextField txtCorreo = new JTextField(empleado.getCorreo() != null ? empleado.getCorreo() : "");
        JTextField txtTelefono = new JTextField(empleado.getNumero() != null ? empleado.getNumero() : "");
        JTextField txtGradoInstruccion = new JTextField(empleado.getGradoInstruccion() != null ? empleado.getGradoInstruccion() : "");

        // Checkboxes para habilitar la edición.
        JCheckBox chkDireccion = new JCheckBox("Actualizar");
        JCheckBox chkCorreo = new JCheckBox("Actualizar");
        JCheckBox chkTelefono = new JCheckBox("Actualizar");
        JCheckBox chkGradoInstruccion = new JCheckBox("Actualizar");

        // 4. Campos de texto inician deshabilitados.
        txtDireccion.setEditable(false);
        txtCorreo.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoInstruccion.setEditable(false);

        // 5. --- Lógica de UI Dinámica ---
        // ActionListeners para que los CheckBox habiliten/deshabiliten los JTextField.
        chkDireccion.addActionListener(e -> txtDireccion.setEditable(chkDireccion.isSelected()));
        chkCorreo.addActionListener(e -> txtCorreo.setEditable(chkCorreo.isSelected()));
        chkTelefono.addActionListener(e -> txtTelefono.setEditable(chkTelefono.isSelected()));
        chkGradoInstruccion.addActionListener(e -> txtGradoInstruccion.setEditable(chkGradoInstruccion.isSelected()));

        // 6. Añade los componentes a la grilla.
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

        // 7. Panel de botones (Guardar, Cancelar).
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        // ... (configuración panel botones) ...
        JButton btnGuardar = new JButton("Guardar Cambios");
        estilizarBoton(btnGuardar);
        JButton btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnCancelar);

        // 8. Acción del botón "Guardar Cambios".
        btnGuardar.addActionListener(e -> {
            try {
                boolean actualizado = false; // Flag para saber si se cambió algo.

                // 8a. Verifica campo por campo si el CheckBox está seleccionado.
                if (chkDireccion.isSelected()) {
                    String nuevaDireccion = txtDireccion.getText().trim();
                    // 8b. Valida el dato (usando la clase Validaciones).
                    if (!Validaciones.validarNoVacio(nuevaDireccion)) {
                        JOptionPane.showMessageDialog(vista, "La dirección no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Detiene la acción.
                    }
                    // 8c. Actualiza el objeto 'empleado' (en memoria).
                    empleado.setDireccion(nuevaDireccion);
                    actualizado = true;
                }

                // Repite la lógica para Correo.
                if (chkCorreo.isSelected()) {
                    String nuevoCorreo = txtCorreo.getText().trim();
                    if (!Validaciones.validarEmail(nuevoCorreo)) {
                        JOptionPane.showMessageDialog(vista, "Correo electrónico inválido", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setCorreo(nuevoCorreo);
                    actualizado = true;
                }

                // Repite la lógica para Teléfono.
                if (chkTelefono.isSelected()) {
                    String nuevoTelefono = txtTelefono.getText().trim();
                    if (!Validaciones.validarTelefono(nuevoTelefono)) {
                        JOptionPane.showMessageDialog(vista, "Teléfono inválido (debe tener 9 dígitos empezando con 9)", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    empleado.setNumero(nuevoTelefono);
                    actualizado = true;
                }

                // Repite la lógica para Grado de Instrucción.
                if (chkGradoInstruccion.isSelected()) {
                    // ... (validación y seteo) ...
                    actualizado = true;
                }

                if (!actualizado) {
                    // Si no seleccionó ningún CheckBox.
                    JOptionPane.showMessageDialog(vista, "Seleccione al menos un campo para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 8d. --- Llamada al Controlador ---
                // Llama al método 'actualizarEmpleado' del controlador.
                // Aunque el método es el mismo que usa el Admin, el Facade (capa Modelo)
                // verificará que el ID del empleado que se actualiza sea el mismo
                // que el del 'usuarioActual' logueado.
                controlador.actualizarEmpleado(empleado);
                JOptionPane.showMessageDialog(vista, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // 8e. Regresa a la vista de "Ver Datos" para ver los cambios.
                vista.mostrarPanel("Ver Datos Personales");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al actualizar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 9. Acción del botón "Cancelar".
        // Simplemente regresa a la vista "Ver Datos Personales" sin guardar.
        btnCancelar.addActionListener(e -> vista.mostrarPanel("Ver Datos Personales"));

        // 10. Ensambla el panel.
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelEditable);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelBotones);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        // ... (configuración scrollPane) ...

        panelContenido.add(scrollPane, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    // (Métodos comentados 'mostrarPanelMisContratos' y 'estilizarBoton' omitidos
    //  ya que el primero está comentado en el código original y el segundo
    //  es un helper simple).
    public void estilizarBoton(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(getColorBotonHover());
    }
}