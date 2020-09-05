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

    /**
     * trae la lista de todos los administrdores cargados en la base
     *
     * @retun ArrayList<Administrador> administradores : devuelve todos los administradores
     **/
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

    /**
     * trae un administrador por ID
     *
     * @retun Administrador administrador : devuelve el administrador buscado por ID
     **/
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

    /**
     * trae un administrador por nombre
     *
     * @param nombre : nombre del administrador
     *
     * @retun Administrador administrador : devuelve el administrador buscado por nombre
     **/
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

    /**
     * trae un administrador que contenga un nombre
     *
     * @param nombre : nombre a buscar
     *
     * @retun Administrador administrador : devuelve el administrador que contenga
     *                                      el nombre buscado
     **/
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

    /**
     * Guarda en la base un nuevo administrador
     *
     * @param nombre : nombre del administrador
     **/
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

    /**
     * actualiza el nombre de un administrador a traves de su ID
     *
     * @param nombre : nuevo nombre de administrador
     **/
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

    /**
     * eliminad un administrador a traves de su ID
     *
     * @param id : id administraor
     **/
    public void eliminar(int id) {
        try {
            PreparedStatement pps = cn.prepareStatement("DELETE FROM administradores WHERE id = '" + id + "'");
            pps.executeUpdate();
            pps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * devuelve todos los datos de un administrado junto con sus consorcios y usuarios FTP
     *
     * @return ArrayList<Bean> administrador : administrador con todos sus datos completos
     **/
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
                administrador.setIdConsorcioWeb(rs.getString(7));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(8)));
                administrador.setUsuario(rs.getString(9));
                administrador.setPassword(rs.getString(10));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(11)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    /**
     * devuelve todos los datos de todos los administradores que coincidan con un nombre
     *
     * @param buscar : nombre a buscar
     *
     * @return ArrayList<Bean> administrador : administradores con todos sus datos completos
     **/
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
                administrador.setIdConsorcioWeb(rs.getString(7));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(8)));
                administrador.setUsuario(rs.getString(9));
                administrador.setPassword(rs.getString(10));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(11)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    /**
     * devuelve todos los datos de todos los administradores que contengan consorcio con un nombre
     * de consorcio específico
     *
     * @param buscar : nombre consorcio a buscar
     *
     * @return ArrayList<Bean> administrador : administradores con todos sus datos completos
     **/
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
                administrador.setIdConsorcioWeb(rs.getString(7));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(8)));
                administrador.setUsuario(rs.getString(9));
                administrador.setPassword(rs.getString(10));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(11)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }

    /**
     * devuelve todos los datos de todos los administradores que contengan usuario con un nombre
     * de usuario específico
     *
     * @param buscar : nombre usuario a buscar
     *
     * @return ArrayList<Bean> administrador : administradores con todos sus datos completos
     **/
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
                administrador.setIdConsorcioWeb(rs.getString(7));
                administrador.setIdUsuario(Integer.parseInt(rs.getString(8)));
                administrador.setUsuario(rs.getString(9));
                administrador.setPassword(rs.getString(10));
                administrador.setConsorcios_id(Integer.parseInt(rs.getString(11)));
                administradores.add(administrador);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return administradores;
    }
}
