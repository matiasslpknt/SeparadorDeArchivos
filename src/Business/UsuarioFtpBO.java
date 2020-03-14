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

    public UsuarioFtp getUsuarioFtpByIdAdministrador(int id){
        return daoUsuario.getUsuarioFtpByIdAdministrador(id);
    }
}
