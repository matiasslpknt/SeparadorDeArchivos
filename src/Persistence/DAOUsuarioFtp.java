package Persistence;

import Model.UsuarioFtp;

import java.sql.*;
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
                usr.setConsorcios_id(Integer.parseInt(rs.getString(4)));
                usrs.add(usr);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
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
                usr.setConsorcios_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
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
                usr.setConsorcios_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;
    }

    public UsuarioFtp getUsuarioFtpByIdConsorcio(int id)
    {
        String sql = "SELECT * FROM usuario WHERE consorcios_id = "+ id +";";
        Statement st;
        UsuarioFtp usr = new UsuarioFtp();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usr.setId(Integer.parseInt(rs.getString(1)));
                usr.setUsuario(rs.getString(2));
                usr.setPassword(rs.getString(3));
                usr.setConsorcios_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;
    }

    public void guardar(String usuario, String password, int idConsorcio) {
        PreparedStatement pps;
        try {
            pps = cn.prepareStatement("INSERT INTO usuario (usuario, password, consorcios_id) VALUES (?,?,?)");
            pps.setString(1, usuario);
            pps.setString(2, password);
            pps.setString(3, idConsorcio+"");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuarioFtp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificar(String usuario, String password, int idConsorcio) {
        PreparedStatement pps;
        //UPDATE usuario SET usuario = 'carrarayasociad', password = 'Jk2zz14?' WHERE consorcios_id =  1;
        try {
            pps = cn.prepareStatement("UPDATE usuario SET usuario = '" + usuario + "' , password = '" + password + "' WHERE consorcios_id = '" + idConsorcio + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(int idConsorcio) {
        try {
            PreparedStatement pps = cn.prepareStatement("DELETE FROM usuario WHERE consorcios_id = '" + idConsorcio + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
