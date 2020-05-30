package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServicioDTOAdd {
    private String success;
    private ServicioDTO2 servicios;
    private String totalRecordCount;
    private String version;

    public String getSucces() {
        return success;
    }

    //@JsonProperty("success")
    public void setSuccess(String success) {
        this.success = success;
    }

    public ServicioDTO2 getServicios() {
        return servicios;
    }

    public void setServicios(ServicioDTO2 servicios) {
        this.servicios = servicios;
    }

    public String getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(String totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}