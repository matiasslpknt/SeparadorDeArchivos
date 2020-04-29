package Business;

import Persistence.ConectionAccess;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConectionAccessBO {
    private final ConectionAccess con;

    public ConectionAccessBO(String urlBase, String usuario, String password){
        this.con = new ConectionAccess(urlBase, usuario, password);
    }

    public void crearConexion() throws SQLException {
        this.con.establecerConexion();
    }

    public ResultSet mandarSql(String sql) throws SQLException{
        return this.con.EjecutarSentencia(sql);
    }
}
