package Business;

import Model.Consorcio;
import Persistence.DAOConsorcio;

import java.util.ArrayList;

public class ConsorcioBO {
    DAOConsorcio daoConsorcio = new DAOConsorcio();

    public ArrayList<Consorcio> getConsorcios(){
        return daoConsorcio.getConsorcios();
    }

    public Consorcio getConsorcioById(int id){
        return daoConsorcio.getConsorcioById(id);
    }

    public Consorcio getConsorcioByNombre(String nombre){
        return daoConsorcio.getConsorcioByNombre(nombre);
    }

    public ArrayList<Consorcio> getConsorciosByIdAdministrador(int id){
        return daoConsorcio.getConsorciosByIdAdministrador(id);
    }
    public ArrayList<Consorcio> getConsorciosConNombre(String nombre) {
        return daoConsorcio.getConsorciosConNombre(nombre);
    }
    public void guardar(String nombre, String directorio, int idAdministrador) {
        daoConsorcio.guardar(nombre, directorio, idAdministrador);
    }

    public void modificar(String nombre, String directorioFtp, int id) {
        daoConsorcio.modificar(nombre, directorioFtp, id);
    }

    public void eliminar(int idConsorcio) {
        daoConsorcio.eliminar(idConsorcio);
    }

    public void eliminarPorAdministrador(int idAdministrador) {
        daoConsorcio.eliminarPorAdministrador(idAdministrador);
    }
}
