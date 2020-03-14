package Model;

import java.util.ArrayList;

public class Administrador {
    private int id;
    private String nombre;
    private String directorioFtp;
    private ArrayList<Consorcio> consorcios;
    private UsuarioFtp usuarioFtp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Consorcio> getConsorcios() {
        return consorcios;
    }

    public void setConsorcios(ArrayList<Consorcio> consorcios) {
        this.consorcios = consorcios;
    }

    public UsuarioFtp getUsuariosFtp() {
        return usuarioFtp;
    }

    public void setUsuarioFtp(UsuarioFtp usuariosFtp) {
        this.usuarioFtp = usuariosFtp;
    }
}
