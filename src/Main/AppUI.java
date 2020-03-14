package Main;

import Business.ConeccionBO;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author Matias Augusto Manzanelli y Marcos Javier Lujan Garcia
 */
public class AppUI {

    public static void init() {


        /*try {
        UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");*/
        ConeccionBO con = new ConeccionBO();
        if (con.conection()) {
            Principal principal = new Principal();
            principal.pack();
            principal.setVisible(true);
            principal.setLocationRelativeTo(null);
            principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            JOptionPane.showMessageDialog(null, "CONEXION FALLIDA.");
        }
        /*} catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO SE PUDO CARGAR EL LOOK AND FELL.");
        }*/
    }
}