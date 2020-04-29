package Main;

import Business.AdministradorBO;
import Model.Bean;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Visualizacion extends JFrame {
    private JPanel panelVisualizacion;
    private JRadioButton chkAdministrador;
    private JRadioButton chkUsuario;
    private JRadioButton chkConsorcio;
    private JButton VOLVERButton;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JScrollPane scrollPane;
    private JTable tabla;
    private AdministradorBO administradorBO = new AdministradorBO();

    public Visualizacion() {
        super("COTERRANEA");
        setContentPane(panelVisualizacion);
        ArrayList<Bean> beans = administradorBO.getDatos();
        actualizarTabla(tabla, beans);

        VOLVERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Principal principal = new Principal();
                principal.pack();
                principal.setVisible(true);
                principal.setLocationRelativeTo(null);
                principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        chkAdministrador.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(chkAdministrador.isSelected()){
                    chkConsorcio.setSelected(false);
                    chkUsuario.setSelected(false);
                }
                if(!chkConsorcio.isSelected() && !chkUsuario.isSelected()){
                    chkAdministrador.setSelected(true);
                }
            }
        });
        chkConsorcio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(chkConsorcio.isSelected()){
                    chkConsorcio.setSelected(true);
                    chkAdministrador.setSelected(false);
                    chkUsuario.setSelected(false);
                }
                if(!chkAdministrador.isSelected() && !chkUsuario.isSelected()){
                    chkConsorcio.setSelected(true);
                }
            }
        });
        chkUsuario.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(chkUsuario.isSelected()){
                    chkUsuario.setSelected(true);
                    chkAdministrador.setSelected(false);
                    chkConsorcio.setSelected(false);
                }
                if(!chkAdministrador.isSelected() && !chkConsorcio.isSelected()){
                    chkUsuario.setSelected(true);
                }
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(txtBuscar.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Complete el campo de búsqueda.");
                }else{
                    if(chkAdministrador.isSelected()){
                        ArrayList<Bean> beans = administradorBO.getDatosAdministrador(txtBuscar.getText());
                        actualizarTabla(tabla, beans);
                    }else if(chkConsorcio.isSelected()){
                        ArrayList<Bean> beans = administradorBO.getDatosConsorcio(txtBuscar.getText());
                        actualizarTabla(tabla, beans);
                    } else if(chkUsuario.isSelected()){
                        ArrayList<Bean> beans = administradorBO.getDatosUsuario(txtBuscar.getText());
                        actualizarTabla(tabla, beans);
                    }else{
                        ArrayList<Bean> beans = administradorBO.getDatos();
                        actualizarTabla(tabla, beans);
                    }
                }

            }
        });
    }

    public void actualizarTabla(javax.swing.JTable t, ArrayList<Bean> beans) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ADMINISTRADOR");
        modelo.addColumn("CONSORCIO");
        modelo.addColumn("DIRECTORIO FTP");
        modelo.addColumn("USUARIO");
        modelo.addColumn("PASSWORD");
        t.setModel(modelo);
        for (Bean admin : beans) {
            String[] datos = new String[5];
            String nombreAdmin = admin.getNombreAdministrador();
            datos[0] = nombreAdmin;
            String consorcioNombre = admin.getNombreConsorcio();
            datos[1] = consorcioNombre;
            String directorio = admin.getDirectorioFtp();
            datos[2] = directorio;
            String usuario = admin.getUsuario();
            datos[3] = usuario;
            String password = admin.getPassword();
            datos[4] = password;
            modelo.addRow(datos);
        }
        t.setModel(modelo);
    }
}
