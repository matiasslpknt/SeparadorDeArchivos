package Persistence;

import Model.Administrador;
import Model.Bean;

import java.sql.*;
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
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return admin;
    }

    public ArrayList<Administrador> getAdministradoresConNombre(String nombre) {
        ArrayList<Administrador> administradores = new ArrayList<Administrador>();
        String sql = "select * from administradores where nombre like '%" + nombre + "%';";
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

    public void guardar(String nombre) {
        PreparedStatement pps;
        try {
            pps = cn.prepareStatement("INSERT INTO administradores (nombre) VALUES (?)");
            pps.setString(1, nombre);
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificar(String nombre, int id) {
        PreparedStatement pps;
        try {
            pps = cn.prepareStatement("UPDATE administradores SET nombre = '" + nombre + "' WHERE id = '" + id + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(int id) {
        try {
            PreparedStatement pps = cn.prepareStatement("DELETE FROM administradores WHERE id = '" + id + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Bean> getDatos() {
        ArrayList<Bean> administradores = new ArrayList<Bean>();
        String sql = "select *  from administradores inner join consorcios on administradores.id = consorcios.administradores_id inner join usuario on consorcios.id = usuario.consorcios_id;";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Bean administrador = new Bean();
                administrador.setIdAdministrador(Integer.parseInt(rs.getString(1)));
                administrador.setNombreAdministrador(rs.getString(2));
                administrador.setIdConsorcio(Integer.parseInt(rs.getString(3)));
                administrador.setNombreConsorcio(rs.getString(4));
                administrador.setDirectorioFtp(rs.getString(5));
                administrador.setAdministradores_id(Integer.parseInt(rs.getString(6)));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(7)));
                administrador.setUsuario(rs.getString(8));
                administrador.setPassword(rs.getString(9));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(10)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    public ArrayList<Bean> getDatosAdministrador(String buscar) {
        ArrayList<Bean> administradores = new ArrayList<Bean>();
        String sql = "select *  from administradores inner join consorcios on administradores.id = consorcios.administradores_id inner join usuario on consorcios.id = usuario.consorcios_id where administradores.nombre like '%"+ buscar +"%';";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Bean administrador = new Bean();
                administrador.setIdAdministrador(Integer.parseInt(rs.getString(1)));
                administrador.setNombreAdministrador(rs.getString(2));
                administrador.setIdConsorcio(Integer.parseInt(rs.getString(3)));
                administrador.setNombreConsorcio(rs.getString(4));
                administrador.setDirectorioFtp(rs.getString(5));
                administrador.setAdministradores_id(Integer.parseInt(rs.getString(6)));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(7)));
                administrador.setUsuario(rs.getString(8));
                administrador.setPassword(rs.getString(9));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(10)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    public ArrayList<Bean> getDatosConsorcio(String buscar) {
        ArrayList<Bean> administradores = new ArrayList<Bean>();
        String sql = "select *  from administradores inner join consorcios on administradores.id = consorcios.administradores_id inner join usuario on consorcios.id = usuario.consorcios_id where consorcios.nombre like '%"+ buscar +"%';";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Bean administrador = new Bean();
                administrador.setIdAdministrador(Integer.parseInt(rs.getString(1)));
                administrador.setNombreAdministrador(rs.getString(2));
                administrador.setIdConsorcio(Integer.parseInt(rs.getString(3)));
                administrador.setNombreConsorcio(rs.getString(4));
                administrador.setDirectorioFtp(rs.getString(5));
                administrador.setAdministradores_id(Integer.parseInt(rs.getString(6)));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(7)));
                administrador.setUsuario(rs.getString(8));
                administrador.setPassword(rs.getString(9));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(10)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    public ArrayList<Bean> getDatosUsuario(String buscar) {
        ArrayList<Bean> administradores = new ArrayList<Bean>();
        String sql = "select *  from administradores inner join consorcios on administradores.id = consorcios.administradores_id inner join usuario on consorcios.id = usuario.consorcios_id where usuario.usuario like '%"+ buscar +"%';";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Bean administrador = new Bean();
                administrador.setIdAdministrador(Integer.parseInt(rs.getString(1)));
                administrador.setNombreAdministrador(rs.getString(2));
                administrador.setIdConsorcio(Integer.parseInt(rs.getString(3)));
                administrador.setNombreConsorcio(rs.getString(4));
                administrador.setDirectorioFtp(rs.getString(5));
                administrador.setAdministradores_id(Integer.parseInt(rs.getString(6)));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(7)));
                administrador.setUsuario(rs.getString(8));
                administrador.setPassword(rs.getString(9));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(10)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }
}
