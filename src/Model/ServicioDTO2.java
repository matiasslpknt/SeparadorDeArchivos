package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServicioDTO2 {
    private String idservicio;
    private String idTipoServicio;
    private String Resumen;
    private String idUsuario;
    private String idCountry;
    private String idAdministrador;
    private String idintendente;
    private String Observacion;
    private String FechaDesde;
    private String FechaHasta;
    private String FechaAlta;
    private String Respondido;
    private String FechaRespondido;
    private String Origen;
    private String idServicioPadre;
    private String TimeStamp;
    private String servicios_tipo_ticket;
    private String prioridad;

    public String getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(String idservicio) {
        this.idservicio = idservicio;
    }

    public String getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(String idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public String getResumen() {
        return Resumen;
    }

    @JsonProperty("Resumen")
    public void setResumen(String resumen) {
        Resumen = resumen;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getIdintendente() {
        return idintendente;
    }

    public void setIdintendente(String idintendente) {
        this.idintendente = idintendente;
    }

    public String getObservacion() {
        return Observacion;
    }

    @JsonProperty("Observacion")
    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getFechaDesde() {
        return FechaDesde;
    }

    @JsonProperty("FechaDesde")
    public void setFechaDesde(String fechaDesde) {
        FechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return FechaHasta;
    }

    @JsonProperty("FechaHasta")
    public void setFechaHasta(String fechaHasta) {
        FechaHasta = fechaHasta;
    }

    public String getFechaAlta() {
        return FechaAlta;
    }

    @JsonProperty("FechaAlta")
    public void setFechaAlta(String fechaAlta) {
        FechaAlta = fechaAlta;
    }

    public String getRespondido() {
        return Respondido;
    }

    @JsonProperty("Respondido")
    public void setRespondido(String respondido) {
        Respondido = respondido;
    }

    public String getFechaRespondido() {
        return FechaRespondido;
    }

    @JsonProperty("FechaRespondido")
    public void setFechaRespondido(String fechaRespondido) {
        FechaRespondido = fechaRespondido;
    }

    public String getOrigen() {
        return Origen;
    }

    @JsonProperty("Origen")
    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getIdServicioPadre() {
        return idServicioPadre;
    }

    public void setIdServicioPadre(String idServicioPadre) {
        this.idServicioPadre = idServicioPadre;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    @JsonProperty("TimeStamp")
    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getServicios_tipo_ticket() {
        return servicios_tipo_ticket;
    }

    public void setServicios_tipo_ticket(String servicios_tipo_ticket) {
        this.servicios_tipo_ticket = servicios_tipo_ticket;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
