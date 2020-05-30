package Main;

import Business.AdministradorBO;
import Business.ConsorcioBO;
import Business.UsuarioFtpBO;
import Model.Administrador;
import Model.Consorcio;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Carga extends javax.swing.JFrame {
    private JTextField txtNombreAdministrador;
    private JButton btnGuardarAdministrador;
    private JButton btnGuardarConsorcio;
    private JTextField txtNombreConsorcio;
    private JTextField txtDirectorio;
    private JComboBox cmbAdministradores;
    private JPanel panelCarga;
    private JTextField txtUsuario;
    private JTextField txtPassword;
    private JButton btnVolver;
    private JTextField txtIdConsorcio;
    private AdministradorBO administradorBO = new AdministradorBO();
    private ConsorcioBO consorcioBO = new ConsorcioBO();
    private UsuarioFtpBO usuarioBO = new UsuarioFtpBO();
    ArrayList<Administrador> administradores = administradorBO.getAdministradores();

    public Carga() {
        super("COTERRANEA");
        setContentPane(panelCarga);

        cargarComboBoxAdministrador(cmbAdministradores);

        btnGuardarAdministrador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (txtNombreAdministrador.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Complete el nombre del administrador.");
                } else {
                    if (validarNombreAdministrador()) {
                        administradorBO.guardar(txtNombreAdministrador.getText().trim());
                        Carga carga = new Carga();
                        carga.pack();
                        carga.setVisible(true);
                        carga.setLocationRelativeTo(null);
                        carga.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "El nombre ingresado esta contenido dentro del nombre de otro administrador o el nombre contiene el nombre de otro administrador.");
                    }
                }
            }
        });
        btnGuardarConsorcio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (txtNombreConsorcio.getText().trim().equals("") || txtDirectorio.getText().trim().equals("") || txtUsuario.getText().trim().equals("") || txtPassword.getText().trim().equals("") || txtIdConsorcio.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos del consorcio.");
                } else {
                    if (validarNombreConsorcio()) {
                        int idAdministrador = administradorBO.getAdministradorByNombre(cmbAdministradores.getSelectedItem().toString()).getId();
                        String dir = txtDirectorio.getText().trim();
                        if(dir.contains("ftp://")){
                            dir = dir.replace("ftp://", "");
                        }
                        consorcioBO.guardar(txtNombreConsorcio.getText().trim(), dir, idAdministrador, txtIdConsorcio.getText().trim());
                        int idConsorcio = consorcioBO.getConsorcioByNombre(txtNombreConsorcio.getText().trim()).getId();
                        usuarioBO.guardar(txtUsuario.getText().trim(), txtPassword.getText().trim(), idConsorcio);
                        limpiarCamposConsorcio();
                    } else {
                        JOptionPane.showMessageDialog(null, "El nombre ingresado esta contenido dentro del nombre de otro consorcio o el nombre contiene el nombre de otro consorcio.");
                    }
                }
            }
        });
        btnVolver.addActionListener(new ActionListener() {
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
    }

    public void limpiarCamposConsorcio() {
        txtNombreConsorcio.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        txtDirectorio.setText("");
        txtIdConsorcio.setText("");
    }

    public void cargarComboBoxAdministrador(JComboBox cboo) {
        ArrayList<Administrador> administradores = administradorBO.getAdministradores();
        for (Administrador ad : administradores) {
            cboo.addItem(ad.getNombre());
        }
        AutoCompleteDecorator.decorate(cboo);
    }

    public boolean validarNombreAdministrador() {
        boolean bandera1 = validarInclusionAdministradorDeNombreEnBase();
        boolean bandera2 = validarInclusionAdministradorDeBaseEnNombre();
        if (bandera1 && bandera2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarInclusionAdministradorDeNombreEnBase() {
        String nombreAdmin = txtNombreAdministrador.getText().trim();
        ArrayList<Administrador> adms = administradorBO.getAdministradoresConNombre(nombreAdmin);
        if (adms.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarInclusionAdministradorDeBaseEnNombre() {
        boolean bandera = true;
        String nombreAdmin = txtNombreAdministrador.getText().trim();
        for (Administrador a : administradores) {
            if (a.getNombre().contains(nombreAdmin)) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }

    public boolean validarNombreConsorcio() {
        boolean bandera1 = validarInclusionConsorcioDeNombreEnBase();
        boolean bandera2 = validarInclusionConsorcioDeBaseEnNombre();
        if (bandera1 && bandera2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarInclusionConsorcioDeNombreEnBase() {
        String nombreCons = txtNombreConsorcio.getText().trim();
        ArrayList<Consorcio> cons = consorcioBO.getConsorciosConNombre(nombreCons);
        if (cons.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarInclusionConsorcioDeBaseEnNombre() {
        boolean bandera = true;
        String nombreCons = txtNombreConsorcio.getText().trim();
        ArrayList<Consorcio> consorcios = consorcioBO.getConsorcios();
        for (Consorcio a : consorcios) {
            if (a.getNombre().contains(nombreCons)) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }
}
