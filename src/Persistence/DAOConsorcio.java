package Persistence;

import Model.Consorcio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOConsorcio {
    ConectionBD con = new ConectionBD();
    Connection cn = con.conexion();
    public ArrayList<Consorcio> getConsorcios()
    {
        ArrayList<Consorcio> consorcios = new ArrayList<Consorcio>();
        String sql = "select * from consorcios;";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs  = st.executeQuery(sql);
            while(rs.next())
            {
                Consorcio consorcio = new Consorcio();
                consorcio.setId(Integer.parseInt(rs.getString(1)));
                consorcio.setNombre(rs.getString(2));
                consorcio.setDirectorioFtp(rs.getString(3));
                consorcio.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                consorcios.add(consorcio);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consorcios;
    }

    public Consorcio getConsorcioById(int id)
    {
        String sql = "SELECT * FROM consorcios WHERE id = '"+ id +"'";
        Statement st;
        Consorcio compl = new Consorcio();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                compl.setId(Integer.parseInt(rs.getString(1)));
                compl.setNombre(rs.getString(2));
                compl.setDirectorioFtp(rs.getString(3));
                compl.setAdministradores_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compl;
    }

    public Consorcio getConsorcioByNombre(String nombre)
    {
        String sql = "SELECT * FROM consorcios WHERE nombre = '"+ nombre +"'";
        Statement st;
        Consorcio compl = new Consorcio();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                compl.setId(Integer.parseInt(rs.getString(1)));
                compl.setNombre(rs.getString(2));
                compl.setDirectorioFtp(rs.getString(3));
                compl.setAdministradores_id(Integer.parseInt(rs.getString(4)));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compl;
    }

    public ArrayList<Consorcio> getConsorciosByIdAdministrador(int id)
    {
        ArrayList<Consorcio> consorcios = new ArrayList<Consorcio>();
        String sql = "select * from consorcios WHERE administradores_id = " + id + ";";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs  = st.executeQuery(sql);
            while(rs.next())
            {
                Consorcio consorcio = new Consorcio();
                consorcio.setId(Integer.parseInt(rs.getString(1)));
                consorcio.setNombre(rs.getString(2));
                consorcio.setDirectorioFtp(rs.getString(3));
                consorcio.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                consorcios.add(consorcio);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consorcios;
    }
}
