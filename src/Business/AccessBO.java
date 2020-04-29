package Business;

import Persistence.DAOAccess;
import Persistence.DAOAdministrador;

public class AccessBO {
    DAOAccess daoAccess = new DAOAccess();

    public String getNombreAdministrador(String urlBase, String usuario, String password){
        return daoAccess.getNombreAdministrador(urlBase, usuario, password);
    }
}
