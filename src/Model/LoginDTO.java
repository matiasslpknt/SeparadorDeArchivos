package Model;

public class LoginDTO {
    private String username;
    private String password;
    private String action;

    public LoginDTO(String username, String password){
        this.username = username;
        this.password = password;
        this.action = "login";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}