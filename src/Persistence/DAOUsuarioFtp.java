package Persistence;

import Model.UsuarioFtp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOUsuarioFtp {
    ConectionBD con = new ConectionBD();
    Connection cn = con.conexion();

    public ArrayList<UsuarioFtp> getUsuariosFtp()
    {
        ArrayList<UsuarioFtp> usrs = new ArrayList<UsuarioFtp>();
        String sql = "select * from usuario;";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs  = st.executeQuery(sql);
            while(rs.next())
            {
                UsuarioFtp usr = new UsuarioFtp();
                usr.setId(Integer.parseInt(rs.getString(1)));
                usr.setUsuario(rs.getString(2));
                usr.setPassword(rs.getString(3));
                usr.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                usrs.add(usr);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usrs;
    }

    public UsuarioFtp getUsuarioFtpById(int id)
    {
        String sql = "SELECT * FROM usuario WHERE id = '"+ id +"'";
        Statement st;
        UsuarioFtp usr = new UsuarioFtp();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usr.setId(Integer.parseInt(rs.getString(1)));
                usr.setUsuario(rs.getString(2));
                usr.setPassword(rs.getString(3));
                usr.setAdministradores_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;
    }

    public UsuarioFtp getUsuarioFtpByUsuario(String nombre)
    {
        String sql = "SELECT * FROM usuario WHERE usuario = '"+ nombre +"'";
        Statement st;
        UsuarioFtp usr = new UsuarioFtp();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usr.setId(Integer.parseInt(rs.getString(1)));
                usr.setUsuario(rs.getString(2));
                usr.setPassword(rs.getString(3));
                usr.setAdministradores_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;
    }

    public UsuarioFtp getUsuarioFtpByIdAdministrador(int id)
    {
        String sql = "SELECT * FROM usuario WHERE administradores_id = "+ id +";";
        Statement st;
        UsuarioFtp usr = new UsuarioFtp();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usr.setId(Integer.parseInt(rs.getString(1)));
                usr.setUsuario(rs.getString(2));
                usr.setPassword(rs.getString(3));
                usr.setAdministradores_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;
    }
}
