package Vista.Strategy;


import java.awt.Color;

import javax.swing.JPanel;

// Interfaz para estrategias de dashboard
// Define el comportamiento específico de cada rol
public interface DashboardStrategy {

    // Colores específicos del rol
    Color getColorFondo();

    Color getColorBoton();

    Color getColorBotonHover();

    Color getColorTexto();

    Color getColorNavbar();

    // Configuración de la ventana
    String getTituloVentana();

    String getMensajeBienvenida();

    String getMensajeInfo();

    // Creación del navbar específico del rol
    JPanel crearNavbar(Object dashboard);

    // Manejo de paneles específicos
    void mostrarPanel(String panelNombre, JPanel panelContenido, Object dashboard);
}
