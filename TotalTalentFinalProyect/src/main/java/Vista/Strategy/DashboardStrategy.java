package Vista.Strategy;


import java.awt.Color;

import javax.swing.JPanel;

// --- Interfaz DashboardStrategy (El Contrato de la Estrategia) ---
// Esta interfaz define el "contrato" que todas las estrategias de rol (Admin, Gerente, etc.)
// deben cumplir. Es la base del Patrón Strategy.
// La clase DashboardVista (el Contexto) tendrá una variable de este tipo
// y llamará a estos métodos sin saber qué estrategia concreta los está ejecutando.
public interface DashboardStrategy {

    // --- Métodos para la Apariencia (Look and Feel) ---
    // Definen los colores específicos para el dashboard de cada rol.
    
    // Devuelve el color de fondo principal del panel de contenido.
    Color getColorFondo();

    // Devuelve el color base de los botones.
    Color getColorBoton();

    // Devuelve el color del botón cuando el mouse pasa por encima (hover).
    Color getColorBotonHover();

    // Devuelve el color del texto principal.
    Color getColorTexto();

    // Devuelve el color de fondo de la barra de navegación lateral.
    Color getColorNavbar();

    // --- Métodos para Textos Específicos ---
    // Definen los textos que varían según el rol.
    
    // Devuelve el título de la ventana principal (JFrame).
    String getTituloVentana();

    // Devuelve el mensaje de bienvenida en el panel principal.
    String getMensajeBienvenida();

    // Devuelve el texto informativo en el panel principal.
    String getMensajeInfo();

    // --- Métodos de Construcción de UI ---

    // Método clave del patrón Strategy.
    // Cada estrategia (Admin, Empleado) implementará este método para construir
    // y devolver el JPanel de la barra lateral (el Navbar) con los
    // botones específicos para ese rol.
    // Recibe el 'dashboard' (la instancia de DashboardVista) como parámetro
    // para que los botones creados puedan llamarla (ej. vista.mostrarPanel(...)).
    JPanel crearNavbar(Object dashboard);

    // Método clave del patrón Strategy.
    // Es llamado por DashboardVista cuando el usuario hace clic en un botón del Navbar.
    // Cada estrategia implementará un 'switch' aquí para decidir qué
    // panel (ej. "Gestionar Usuarios", "Ver Mis Datos") debe construir
    // y mostrar en el 'panelContenido' principal.
    void mostrarPanel(String panelNombre, JPanel panelContenido, Object dashboard);
}