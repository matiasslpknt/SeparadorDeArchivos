package Main;

import Business.AdministradorBO;
import Business.ConsorcioBO;
import Business.UsuarioFtpBO;
import Model.Administrador;
import Model.Consorcio;
import Model.UsuarioFtp;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Modificacion extends javax.swing.JFrame {
    private JPanel panelModificacion;
    private JTextField txtNombreAdministrador;
    private JTextField txtNombreConsorcio;
    private JTextField txtDirectorio;
    private JComboBox cmbConsorcios;
    private JTextField txtUsuario;
    private JTextField txtPassword;
    private JButton btnGuardarConsorcio;
    private JButton btnVolver;
    private JButton btnGuardarAdministrador;
    private JComboBox cmbAdministradores;
    private AdministradorBO administradorBO = new AdministradorBO();
    private ConsorcioBO consorcioBO = new ConsorcioBO();
    private UsuarioFtpBO usuarioBO = new UsuarioFtpBO();
    ArrayList<Administrador> administradores = administradorBO.getAdministradores();

    public Modificacion() {
        super("COTERRANEA");
        setContentPane(panelModificacion);

        cargarComboBoxAdministrador(cmbAdministradores);
        cargarComboBoxConsorcio(cmbConsorcios);
        setearCamposdesdeComboBox();

        btnGuardarAdministrador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (txtNombreAdministrador.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Complete el nombre del administrador.");
                } else if (txtNombreAdministrador.getText().trim().equals(cmbAdministradores.getSelectedItem().toString())) {
                    JOptionPane.showMessageDialog(null, "El nombre ingresado es igual al actual.");
                } else {
                    if (validarNombreAdministrador()) {
                        int id = administradorBO.getAdministradorByNombre(cmbAdministradores.getSelectedItem().toString()).getId();
                        administradorBO.modificar(txtNombreAdministrador.getText().trim(), id);
                        Modificacion modificacion = new Modificacion();
                        modificacion.pack();
                        modificacion.setVisible(true);
                        modificacion.setLocationRelativeTo(null);
                        modificacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                if (txtNombreConsorcio.getText().trim().equals("") || txtDirectorio.getText().trim().equals("") || txtUsuario.getText().trim().equals("") || txtPassword.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos del consorcio.");
                } else if (txtNombreConsorcio.getText().trim().equals(cmbConsorcios.getSelectedItem().toString())) {
                    JOptionPane.showMessageDialog(null, "El nombre ingresado es igual al actual.");
                } else {
                    if (validarNombreConsorcio()) {
                        int idConsorcio = consorcioBO.getConsorcioByNombre(cmbConsorcios.getSelectedItem().toString()).getId();
                        consorcioBO.modificar(txtNombreConsorcio.getText().trim(), txtDirectorio.getText().trim(), idConsorcio);
                        usuarioBO.modificar(txtUsuario.getText().trim(), txtPassword.getText().trim(), idConsorcio);
                        Modificacion modificacion = new Modificacion();
                        modificacion.pack();
                        modificacion.setVisible(true);
                        modificacion.setLocationRelativeTo(null);
                        modificacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dispose();
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
        cmbAdministradores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                setearCamposdesdeComboBox();
            }
        });
        cmbConsorcios.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                setearCamposdesdeComboBox();
            }
        });
    }

    public void setearCamposdesdeComboBox(){
        txtNombreAdministrador.setText(cmbAdministradores.getSelectedItem().toString().trim());
        txtNombreConsorcio.setText(cmbConsorcios.getSelectedItem().toString().trim());
        Consorcio cons = consorcioBO.getConsorcioByNombre(cmbConsorcios.getSelectedItem().toString().trim());
        txtDirectorio.setText(cons.getDirectorioFtp());
        UsuarioFtp user = usuarioBO.getUsuarioFtpByIdConsorcio(cons.getId());
        txtUsuario.setText(user.getUsuario());
        txtPassword.setText(user.getPassword());
    }

    public void cargarComboBoxAdministrador(JComboBox cboo) {
        ArrayList<Administrador> administradores = administradorBO.getAdministradores();
        for (Administrador ad : administradores) {
            cboo.addItem(ad.getNombre());
        }
        AutoCompleteDecorator.decorate(cboo);
    }

    public void cargarComboBoxConsorcio(JComboBox cboo) {
        ArrayList<Consorcio> consorcios = consorcioBO.getConsorcios();
        for (Consorcio con : consorcios) {
            cboo.addItem(con.getNombre());
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
