package com.mycompany.totaltalentfinalproyect;

import Controlador.TotalTalentControlador; // Importa el Controlador principal.
import Vista.LoginVista; // Importa la primera ventana de la aplicación.
import javax.swing.SwingUtilities; // Importa la utilidad de Swing para manejar hilos de UI.

/**
 *
 * @author Omar Morales Silva
 */
// --- Clase Principal (Main) - Punto de Entrada ---
// Esta es la clase que arranca toda la aplicación Total Talent HR.
// Su única responsabilidad es instanciar las piezas clave iniciales:
// 1. El TotalTalentControlador.
// 2. La LoginVista.
// Y luego inyectar el controlador en la vista.
public class TotalTalentFinalProyect {

    // El método 'main' es el punto de entrada estándar de una aplicación Java.
    // Es lo primero que se ejecuta al correr el proyecto.
    public static void main(String[] args) {

        // --- Hilo de Despacho de Eventos (Event Dispatch Thread - EDT) ---
        // Se utiliza SwingUtilities.invokeLater para asegurar que toda la interfaz gráfica (GUI)
        // se cree y se manipule en el hilo correcto de Swing, conocido como el EDT.
        // Esto es una práctica estándar en Swing para evitar problemas de concurrencia
        // y asegurar que la UI sea segura (thread-safe).
        // Todo el código dentro del método 'run()' se ejecutará en ese hilo.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // 1. Inicializar el controlador.
                // Se crea la única instancia del TotalTalentControlador.
                // Este objeto 'controlador' vivirá durante toda la ejecución de la app
                // y se compartirá entre la LoginVista y la DashboardVista.
                TotalTalentControlador controlador = new TotalTalentControlador();

                // 2. Mostrar la vista de login.
                // Se crea la primera ventana que verá el usuario, la LoginVista.
                // Es fundamental que le "inyectamos" (pasamos por el constructor) la
                // instancia del 'controlador' que acabamos de crear.
                LoginVista loginVista = new LoginVista(controlador);
                
                // 3. Llama al método 'mostrar' (que internamente llama a setVisible(true))
                // de la LoginVista para hacerla visible y arrancar la aplicación.
                loginVista.mostrar();
            }
        });
    }
}