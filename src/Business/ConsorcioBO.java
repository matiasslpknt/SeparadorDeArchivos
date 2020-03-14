package Business;

import Model.Consorcio;
import Persistence.DAOConsorcio;

import java.util.ArrayList;

public class ConsorcioBO {
    DAOConsorcio daoConsorcio = new DAOConsorcio();

    public ArrayList<Consorcio> getComplejos(){
        return daoConsorcio.getConsorcios();
    }

    public Consorcio getComplejoById(int id){
        return getComplejoById(id);
    }

    public Consorcio getComplejoByNombre(String nombre){
        return daoConsorcio.getConsorcioByNombre(nombre);
    }

    public ArrayList<Consorcio> getComplejosByIdAdministrador(int id){
        return daoConsorcio.getConsorciosByIdAdministrador(id);
    }
}
