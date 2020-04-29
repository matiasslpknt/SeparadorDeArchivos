package Persistence;

import javax.swing.*;
import java.sql.*;

public class ConectionAccess {
    private Connection conexion;
    private Statement sentencia;

    private final String controlador;
    private final String nombre_bd;
    private final String usuarioBD;
    private final String passwordBD;

    public ConectionAccess(String urlBase, String usuarioBD, String passwordBD){
        this.controlador = "sun.jdbc.odbc.JdbcOdbcDriver";
        this.nombre_bd = urlBase;
        this.usuarioBD = usuarioBD;
        this.passwordBD = passwordBD;
    }

    public boolean establecerConexion() throws SQLException {
        try{
            conexion = DriverManager.getConnection("jdbc:ucanaccess://" + this.nombre_bd, this.usuarioBD, this.passwordBD);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al conectarse a base de datos Access: " + e);
            return false;
        }
        try{
            this.sentencia = this.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al crear Statement: " + e);
            return false;
        }
        return true;
    }

    public ResultSet EjecutarSentencia(String sql) throws  SQLException{
        return this.sentencia.executeQuery(sql);
    }
}
