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
    private String idservicioxconsorcio;
    private String idestado;
    private String imagen1;
    private String imagen2;
    private String imagen3;
    private String idnewsletter;
    private String servicios_tipo_ticket;
    private String prioridad;
    private String action;
    private String object;

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

    public String getIdservicioxconsorcio() {
        return idservicioxconsorcio;
    }

    public void setIdservicioxconsorcio(String idservicioxconsorcio) {
        this.idservicioxconsorcio = idservicioxconsorcio;
    }

    public String getIdestado() {
        return idestado;
    }

    public void setIdestado(String idestado) {
        this.idestado = idestado;
    }

    public String getImagen1() {
        return imagen1;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public String getImagen2() {
        return imagen2;
    }

    public void setImagen2(String imagen2) {
        this.imagen2 = imagen2;
    }

    public String getImagen3() {
        return imagen3;
    }

    public void setImagen3(String imagen3) {
        this.imagen3 = imagen3;
    }

    public String getIdnewsletter() {
        return idnewsletter;
    }

    public void setIdnewsletter(String idnewsletter) {
        this.idnewsletter = idnewsletter;
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

    public String getAction(){
        return "add";
    }

    public String getObject(){
        return "servicios";
    }

    public void setAction(String action){
        this.action = action;
    }

    public void setObject(String object){
        this.object = object;
    }
}
