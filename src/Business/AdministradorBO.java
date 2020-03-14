package Business;

import Model.Administrador;
import Persistence.DAOAdministrador;

import java.util.ArrayList;

public class AdministradorBO {

    DAOAdministrador daoAdministrador = new DAOAdministrador();

    public ArrayList<Administrador> getAdministradores() {
        return daoAdministrador.getAdministradores();
    }

    public Administrador getAdministradorById(int id) {
        return daoAdministrador.getAdministradorById(id);
    }

    public Administrador getAdministradorByNombre(String nombre) {
        return daoAdministrador.getAdministradorByNombre(nombre);
    }
}
