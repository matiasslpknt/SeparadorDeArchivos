package Business;

import Persistence.ConectionBD;

/**
 *
 * @author Matias Augusto Manzanelli y Marcos Javier Lujan Garcia
 */
public class ConeccionBO {

    ConectionBD con = new ConectionBD();

    public boolean conection()
    {
        return con.conection();
    }

}