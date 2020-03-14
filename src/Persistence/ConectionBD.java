package Persistence;

import java.sql.*;

/**
 * @author Matias Augusto Manzanelli y Marcos Javier Lujan Garcia
 */
public class ConectionBD {
    Connection cn;
    private boolean vof = true;

    public Connection conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cupones?useUnicode=true&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
            vof = true;
        } catch (Exception e) {
            e.printStackTrace();
            vof = false;
        }
        return cn;
    }

    public boolean conection() {
        conexion();
        return vof;
    }

    Statement createStatement() {
        throw new UnsupportedOperationException("NO SOPORTADO.");
    }
}