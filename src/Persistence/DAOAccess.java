package Persistence;

import Business.ConectionAccessBO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOAccess {
    public static ConectionAccessBO controller;
    public static ResultSet rs;


    public DAOAccess(){

    }

    /**
     * busca un string en un archivo
     *
     * @param urlBase : directorio del archivo MDB
     * @param usuario : usuario access
     * @param password : contraseña access
     *
     * @retun String nombre_administrador : nombre del administrador
     **/
    public String getNombreAdministrador(String urlBase, String usuario, String password){
        try{
            controller = new ConectionAccessBO(urlBase, usuario, password);
            controller.crearConexion();
        }catch(SQLException ex){
            Logger.getLogger(DAOAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT [DB_Nombre_Consorcio] FROM [INDICE] LIMIT 1;";
        try {
            rs = controller.mandarSql(sql);
            while(rs.next()){
                return(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
