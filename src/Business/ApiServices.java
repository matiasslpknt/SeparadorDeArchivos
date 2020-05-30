package Business;

import Model.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

public class ApiServices {
    private RestService restService = new RestService(new RestTemplateBuilder());
    private String urlApi = "https://coterranea.net/api/api/";
    private String objeto = "servicios";
    private String campo = "idservicio";
    private String username = "api";
    private String pass = "M@nz@nelli";
    private String token = "";

    private JwtDTO loginService() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO login = new LoginDTO(username, pass);
        HttpEntity<LoginDTO> request = new HttpEntity<>(login, headers);
        ResponseEntity<JwtDTO> response = null;
        try {
            response = this.restService.template().postForEntity(urlApi, request, JwtDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }

    public ServicioDTO listService(String idCountry) {
        String url = "https://coterranea.net/api/api/?action=list&object=servicios&idCountry=" + idCountry + "&idTipoServicio=5";
        // Request headers
        HttpHeaders headers = new HttpHeaders();
        if (token.equals("")) {
            token = loginService().getJWT();
        }
        headers.add("X-Authorization", "Bearer " + token);
        try {
            HttpEntity<String> entity = new HttpEntity<String>("", headers);
            ResponseEntity<ServicioDTO> response = this.restService.template().exchange(url, HttpMethod.GET, entity, ServicioDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }

    public ServicioDTOAdd addService(ServicioDTO2 service) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token.equals("")) {
            token = loginService().getJWT();
        }
        headers.add("X-Authorization", "Bearer " + token);

        HttpEntity<ServicioDTO2> request = new HttpEntity<>(service, headers);
        ResponseEntity<ServicioDTOAdd> response = null;
        try {
            response = this.restService.template().postForEntity(urlApi, request, ServicioDTOAdd.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }
}
