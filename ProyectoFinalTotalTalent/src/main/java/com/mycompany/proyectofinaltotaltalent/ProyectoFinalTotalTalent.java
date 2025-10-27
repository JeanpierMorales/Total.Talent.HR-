/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinaltotaltalent;

import Vista.LoginFrame;
import javax.swing.SwingUtilities;

public class ProyectoFinalTotalTalent {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }

}