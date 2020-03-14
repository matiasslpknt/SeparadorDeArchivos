package Persistence;

import Model.Administrador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOAdministrador {
    ConectionBD con = new ConectionBD();
    Connection cn = con.conexion();

    public ArrayList<Administrador> getAdministradores() {
        ArrayList<Administrador> administradores = new ArrayList<Administrador>();
        String sql = "select * from administradores;";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Administrador administrador = new Administrador();
                administrador.setId(Integer.parseInt(rs.getString(1)));
                administrador.setNombre(rs.getString(2));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    public Administrador getAdministradorById(int id) {
        String sql = "SELECT * FROM administradores WHERE id = '" + id + "'";
        Statement st;
        Administrador admin = new Administrador();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                admin.setId(Integer.parseInt(rs.getString(1)));
                admin.setNombre(rs.getString(2));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return admin;
    }

    public Administrador getAdministradorByNombre(String nombre) {
        String sql = "SELECT * FROM administradores WHERE nombre = '" + nombre + "'";
        Statement st;
        Administrador admin = new Administrador();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                admin.setId(Integer.parseInt(rs.getString(1)));
                admin.setNombre(rs.getString(2));
            }
            rs.close();
        } catch (SQLException ex) {
            return null;
        }
        return admin;
    }
}
