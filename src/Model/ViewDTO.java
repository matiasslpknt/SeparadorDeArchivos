package Model;

public class ViewDTO {
    private String action;
    private String object;
    private String idservicio;//tkt

    public ViewDTO(String idservicio){
        action = "view";
        object = "servicios";
        this.idservicio = idservicio;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(String idservicio) {
        this.idservicio = idservicio;
    }
}
