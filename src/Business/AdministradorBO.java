package Business;

import Model.Administrador;
import Model.Bean;
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

    public ArrayList<Administrador> getAdministradoresConNombre(String nombre) {
        return daoAdministrador.getAdministradoresConNombre(nombre);
    }

    public void guardar(String nombre) {
        daoAdministrador.guardar(nombre);
    }

    public void modificar(String nombre, int id) {
        daoAdministrador.modificar(nombre, id);
    }

    public void eliminar(int id) {
        daoAdministrador.eliminar(id);
    }

    public ArrayList<Bean> getDatos() {
        return daoAdministrador.getDatos();
    }
    public ArrayList<Bean> getDatosAdministrador(String buscar) {
        return daoAdministrador.getDatosAdministrador(buscar);
    }

    public ArrayList<Bean> getDatosConsorcio(String buscar) {
        return daoAdministrador.getDatosConsorcio(buscar);
    }

    public ArrayList<Bean> getDatosUsuario(String buscar) {
        return daoAdministrador.getDatosUsuario(buscar);
    }
}
