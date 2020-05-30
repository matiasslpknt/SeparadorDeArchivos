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

public class Eliminacion extends javax.swing.JFrame {
    private JTextField txtNombreAdministrador;
    private JButton btnEliminarAdministrador;
    private JComboBox cmbAdministradores;
    private JTextField txtNombreConsorcio;
    private JTextField txtDirectorio;
    private JComboBox cmbConsorcios;
    private JTextField txtUsuario;
    private JTextField txtPassword;
    private JButton btnEliminarConsorcio;
    private JButton btnVolver;
    private JPanel panelEliminacion;
    private JTextField txtIdConsorcioWeb;
    private AdministradorBO administradorBO = new AdministradorBO();
    private ConsorcioBO consorcioBO = new ConsorcioBO();
    private UsuarioFtpBO usuarioBO = new UsuarioFtpBO();
    ArrayList<Administrador> administradores = administradorBO.getAdministradores();

    public Eliminacion() {
        super("COTERRANEA");
        setContentPane(panelEliminacion);

        boolean op1 = cargarComboBoxAdministrador(cmbAdministradores);
        boolean op2 = cargarComboBoxConsorcio(cmbConsorcios);
        if(op1 == true && op2 == true){
            setearCamposdesdeComboBox();
        }

        btnEliminarAdministrador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                eliminarAdministrador();
                Eliminacion eliminacion = new Eliminacion();
                eliminacion.pack();
                eliminacion.setVisible(true);
                eliminacion.setSize(717, 421);
                eliminacion.setLocationRelativeTo(null);
                eliminacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        btnEliminarConsorcio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                eliminarConsorcio();
                Eliminacion eliminacion = new Eliminacion();
                eliminacion.pack();
                eliminacion.setVisible(true);
                eliminacion.setSize(717, 421);
                eliminacion.setLocationRelativeTo(null);
                eliminacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
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

    public void eliminarAdministrador(){
        int idAdmin = administradorBO.getAdministradorByNombre(cmbAdministradores.getSelectedItem().toString()).getId();
        ArrayList<Consorcio> consors = consorcioBO.getConsorciosByIdAdministrador(idAdmin);
        for(Consorcio cons : consors){
            usuarioBO.eliminar(cons.getId());
        }
        consorcioBO.eliminarPorAdministrador(idAdmin);
        administradorBO.eliminar(idAdmin);
    }

    public void eliminarConsorcio(){
        int idConsorcio = consorcioBO.getConsorcioByNombre(cmbConsorcios.getSelectedItem().toString()).getId();
        usuarioBO.eliminar(idConsorcio);
        consorcioBO.eliminar(idConsorcio);
    }

    public void setearCamposdesdeComboBox() {
        txtNombreAdministrador.setText(cmbAdministradores.getSelectedItem().toString().trim());
        txtNombreConsorcio.setText(cmbConsorcios.getSelectedItem().toString().trim());
        Consorcio cons = consorcioBO.getConsorcioByNombre(cmbConsorcios.getSelectedItem().toString().trim());
        txtIdConsorcioWeb.setText(cons.getIdConsorcioWeb());
        txtDirectorio.setText(cons.getDirectorioFtp());
        UsuarioFtp user = usuarioBO.getUsuarioFtpByIdConsorcio(cons.getId());
        txtUsuario.setText(user.getUsuario());
        txtPassword.setText(user.getPassword());
    }

    public boolean cargarComboBoxAdministrador(JComboBox cboo) {
        ArrayList<Administrador> administradores = administradorBO.getAdministradores();
        if(administradores.size() > 0){
            for (Administrador ad : administradores) {
                cboo.addItem(ad.getNombre());
            }
            AutoCompleteDecorator.decorate(cboo);
            return true;
        }
        return false;
    }

    public boolean cargarComboBoxConsorcio(JComboBox cboo) {
        ArrayList<Consorcio> consorcios = consorcioBO.getConsorcios();
        if(consorcios.size() > 0){
            for (Consorcio con : consorcios) {
                cboo.addItem(con.getNombre());
            }
            AutoCompleteDecorator.decorate(cboo);
            return true;
        }
        return false;
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
