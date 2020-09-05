package Business;

import Persistence.DAOAccess;
import Persistence.DAOAdministrador;

public class AccessBO {
    DAOAccess daoAccess = new DAOAccess();

    /**
     * Devuelve el nombre de administrador en access
     *
     * @retun ArrayList<String> archivos : lista de archivos en un directorio
     **/
    public String getNombreAdministrador(String urlBase, String usuario, String password){
        return daoAccess.getNombreAdministrador(urlBase, usuario, password);
    }
}
