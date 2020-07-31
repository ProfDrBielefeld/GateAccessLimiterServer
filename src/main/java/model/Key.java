package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Key
{
    public Key (){}
    public Key (String gatekey)
    {
        this.gatekey = gatekey;
    }
    @JsonProperty
    private String gatekey;
    public String getGatekey() {
        return gatekey;
    }

    public void setGatekey(String gatekey) {
        this.gatekey = gatekey;
    }
}
