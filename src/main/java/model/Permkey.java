package model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permkey")
@NamedQueries({
        //@NamedQuery(name = "model.permkey.getbykey", query = "select pk from permkey pk where pk.apikey like :apitoken")
        @NamedQuery(name = "model.permkey.getbykey", query = "select j from Permkey j where j.apikey like :apitoken")
})
public class Permkey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int permkey_id;

    @JsonProperty
    private String apikey;

    @JsonProperty
    private String note;

    @OneToMany(cascade= CascadeType.ALL)
    @JsonProperty
    @JoinColumn(name="parentkey")
    private List<Tempkey> tempkeyListList;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPermkey_id() {
        return permkey_id;
    }

    public List<Tempkey> getTempkeyListList() {
        return tempkeyListList;
    }

    public void setTempkeyListList(List<Tempkey> tempkeyListList) {
        this.tempkeyListList = tempkeyListList;
    }
}
