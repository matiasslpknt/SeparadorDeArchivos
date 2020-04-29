package Model;

public class UsuarioFtp {
    private int id;
    private String usuario;
    private String password;
    private int consorcios_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConsorcios_id() {
        return consorcios_id;
    }

    public void setConsorcios_id(int consorcios_id) {
        this.consorcios_id = consorcios_id;
    }
}
