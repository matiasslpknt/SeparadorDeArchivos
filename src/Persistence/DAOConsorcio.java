package Persistence;

import Model.Consorcio;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOConsorcio {
    ConectionBD con = new ConectionBD();
    Connection cn = con.conexion();

    /**
     * devuelve todos los consorcios
     *
     * @return ArrayList<Consorcio> consorcios : lista de todos los consorcios en base
     **/
    public ArrayList<Consorcio> getConsorcios() {
        ArrayList<Consorcio> consorcios = new ArrayList<Consorcio>();
        String sql = "select * from consorcios;";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Consorcio consorcio = new Consorcio();
                consorcio.setId(Integer.parseInt(rs.getString(1)));
                consorcio.setNombre(rs.getString(2));
                consorcio.setDirectorioFtp(rs.getString(3));
                consorcio.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                consorcio.setIdConsorcioWeb(rs.getString(5));
                consorcios.add(consorcio);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consorcios;
    }

    /**
     * devuelve todos los consorcios por ID
     *
     * @param id : id consorcio
     *
     * @return Consorcio consorcio : consorcio buscado por ID
     **/
    public Consorcio getConsorcioById(int id) {
        String sql = "SELECT * FROM consorcios WHERE id = '" + id + "'";
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
                compl.setIdConsorcioWeb(rs.getString(5));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compl;
    }

    /**
     * devuelve todos los consorcios por nombre
     *
     * @param nombre : nombre consorcio
     *
     * @return Consorcio consorcio : consorcio buscado por nombre
     **/
    public Consorcio getConsorcioByNombre(String nombre) {
        String sql = "SELECT * FROM consorcios WHERE nombre = '" + nombre + "'";
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
                compl.setIdConsorcioWeb(rs.getString(5));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compl;
    }

    /**
     * devuelve todos los consorcios por ID de administrador
     *
     * @param id : id administrador
     *
     * @return ArrayList<Consorcio> consorcios : lista de consorcios de un administrador
     **/
    public ArrayList<Consorcio> getConsorciosByIdAdministrador(int id) {
        ArrayList<Consorcio> consorcios = new ArrayList<Consorcio>();
        String sql = "select * from consorcios WHERE administradores_id = " + id + ";";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Consorcio consorcio = new Consorcio();
                consorcio.setId(Integer.parseInt(rs.getString(1)));
                consorcio.setNombre(rs.getString(2));
                consorcio.setDirectorioFtp(rs.getString(3));
                consorcio.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                consorcio.setIdConsorcioWeb(rs.getString(5));
                consorcios.add(consorcio);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consorcios;
    }

    /**
     * devuelve todos los consorcios que contengan un nombre
     *
     * @param nombre : nombre de consorcio contenido
     *
     * @return ArrayList<Consorcio> consorcios : lista de consorcios que contienen la busqueda
     **/
    public ArrayList<Consorcio> getConsorciosConNombre(String nombre) {
        ArrayList<Consorcio> consorcios = new ArrayList<Consorcio>();
        String sql = "select * from consorcios where nombre like '%" + nombre + "%';";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Consorcio consorcio = new Consorcio();
                consorcio.setId(Integer.parseInt(rs.getString(1)));
                consorcio.setNombre(rs.getString(2));
                consorcio.setDirectorioFtp(rs.getString(3));
                consorcio.setAdministradores_id(Integer.parseInt(rs.getString(4)));
                consorcio.setIdConsorcioWeb(rs.getString(5));
                consorcios.add(consorcio);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consorcios;
    }

    /**
     * guarda un nuevo consorcio
     *
     * @param nombre : nombre a asignar al consorcio
     * @param directorio : directorio ftp a asignar al consorcio
     * @param idAdministrador : id de administrador a asignar al consorcio
     * @param idConsorcioWeb : id del consorcio en el panel web
     **/
    public void guardar(String nombre, String directorio, int idAdministrador, String idConsorcioWeb) {
        PreparedStatement pps;
        try {
            pps = cn.prepareStatement("INSERT INTO consorcios (nombre, directorioFtp, administradores_id, idConsorcioWeb) VALUES (?,?,?,?)");
            pps.setString(1, nombre);
            pps.setString(2, directorio);
            pps.setString(3, idAdministrador+"");
            pps.setString(4, idConsorcioWeb);
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOConsorcio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * modifica un consorcio consorcio
     *
     * @param nombre : nombre a asignar al consorcio
     * @param directorioFtp : directorio ftp a asignar al consorcio
     * @param id : id consorcio a modificar
     * @param idConsorcioWeb : id del consorcio en el panel web
     **/
    public void modificar(String nombre, String directorioFtp, int id, String idConsorcioWeb) {
        PreparedStatement pps;
        try {
            pps = cn.prepareStatement("UPDATE consorcios SET nombre = '" + nombre + "' , directorioFtp = '" + directorioFtp + "', idConsorcioWeb = '" + idConsorcioWeb + "' WHERE id = '" + id + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * elimina un consorcio por id
     *
     * @param idConsorcio : id consorcio a eliminar
     **/
    public void eliminar(int idConsorcio) {
        try {
            PreparedStatement pps = cn.prepareStatement("DELETE FROM consorcios WHERE id = '" + idConsorcio + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * elimina todos los usuarios que tenga un administrador en sus distintos consorcios
     *
     * @param idAdministrador : id administrador
     **/
    public void eliminarPorAdministrador(int idAdministrador) {
        try {
            PreparedStatement pps = cn.prepareStatement("DELETE FROM consorcios WHERE administradores_id = '" + idAdministrador + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
