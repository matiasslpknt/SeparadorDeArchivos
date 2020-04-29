package Model;

import java.util.List;

public class ServicioDTO {
    private String succes;
    private ServicioDTO2 servicios;
    private String version;

    public String getSucces() {
        return succes;
    }

    public void setSucces(String succes) {
        this.succes = succes;
    }

    public ServicioDTO2 getServicios() {
        return servicios;
    }

    public void setServicios(ServicioDTO2 servicios) {
        this.servicios = servicios;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
