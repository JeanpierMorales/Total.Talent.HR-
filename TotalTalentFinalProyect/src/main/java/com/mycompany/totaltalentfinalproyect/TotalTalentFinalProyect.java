package com.mycompany.totaltalentfinalproyect;

import Controlador.TotalTalentControlador;
import Vista.LoginVista;
import javax.swing.SwingUtilities;

/**
 *
 * @author Omar Morales Silva
 */
public class TotalTalentFinalProyect {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Inicializar controlador
                TotalTalentControlador controlador = new TotalTalentControlador();

                // Mostrar vista de login
                LoginVista loginVista = new LoginVista(controlador);
                loginVista.mostrar();
            }
        });
    }
}
