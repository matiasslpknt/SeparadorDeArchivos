package Business;

import Model.UsuarioFtp;
import Persistence.DAOUsuarioFtp;

import java.util.ArrayList;

public class UsuarioFtpBO {
    DAOUsuarioFtp daoUsuario = new DAOUsuarioFtp();

    public ArrayList<UsuarioFtp> getUsuariosFtp(){
        return daoUsuario.getUsuariosFtp();
    }

    public UsuarioFtp getUsuarioFtpById(int id){
        return daoUsuario.getUsuarioFtpById(id);
    }

    public UsuarioFtp getUsuarioFtpByUsuario(String usuario){
        return daoUsuario.getUsuarioFtpByUsuario(usuario);
    }

    public UsuarioFtp getUsuarioFtpByIdConsorcio(int id){
        return daoUsuario.getUsuarioFtpByIdConsorcio(id);
    }

    public void guardar(String usuario, String password, int idConsorcio) {
        daoUsuario.guardar(usuario, password, idConsorcio);
    }

    public void modificar(String usuario, String password, int idConsorcio) {
        daoUsuario.modificar(usuario, password, idConsorcio);
    }

    public void eliminar(int idConsorcio) {
        daoUsuario.eliminar(idConsorcio);
    }
}
