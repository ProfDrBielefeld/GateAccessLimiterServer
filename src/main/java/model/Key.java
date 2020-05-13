package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Key
{
    @JsonProperty
    private String gatekey;
    public String getGatekey() {
        return gatekey;
    }

    public void setGatekey(String gatekey) {
        this.gatekey = gatekey;
    }
}
