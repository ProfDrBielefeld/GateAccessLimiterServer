package configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Secret {
    @NotNull
    private String secretKey;

    @JsonProperty
    public String getSecretKey() {
        return secretKey;
    }

    @JsonProperty
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
