package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtDTO {
    private String JWT;

    public String getJWT() {
        return JWT;
    }

    @JsonProperty("JWT")
    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
}