package Model;

public class UsuarioFtp {
    private int id;
    private String usuario;
    private String password;
    private int administradores_id;

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

    public int getAdministradores_id() {
        return administradores_id;
    }

    public void setAdministradores_id(int administradores_id) {
        this.administradores_id = administradores_id;
    }
}
