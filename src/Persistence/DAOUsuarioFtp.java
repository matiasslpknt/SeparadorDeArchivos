package Persistence;

import Model.UsuarioFtp;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOUsuarioFtp {
    ConectionBD con = new ConectionBD();
    Connection cn = con.conexion();

    /**
     * trae todos los usuarios cargados en la base.
     *
     * @return ArrayList<UsuarioFtp> usuarios : lista de todos los usuarios cargados
     **/
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

    /**
     * trae un usuario por ID
     *
     * @param id : id usuario
     *
     * @return UsuarioFtp usuario : usuario buscado por id
     **/
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

    /**
     * trae un usuario por nombre de usuario
     *
     * @param nombre : nombre de usuario
     *
     * @return UsuarioFtp usuario : usuario buscado por nombre/usuario
     **/
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

    /**
     * trae el usuario por ID de consorcio
     *
     * @return UsuarioFtp usuario : usuario buscado por ID consorcio
     **/
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

    /**
     * guarda un nuevo usuario
     *
     * @param usuario : nombre de usuario
     * @param password : contraseña de usuario
     * @param idConsorcio : id del consorcio al que pertenece
     **/
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

    /**
     * modifica un usuario por id de consorcio
     *
     * @param usuario : nombre de usuario
     * @param password : contraseña de usuario
     * @param idConsorcio : id del consorcio del usuario
     **/
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

    /**
     * elimina un usuario por id Consorcio
     *
     * @param idConsorcio : id consorcio del usuario a eliminar
     **/
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
