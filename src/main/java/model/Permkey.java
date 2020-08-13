package model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permkey")
@NamedQueries({
        @NamedQuery(name = "model.permkey.getbykey", query = "select j from Permkey j where j.gatekey like :apitoken")
})
public class Permkey extends Key{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int permkey_id;

    @JsonProperty
    private String note;

    @OneToMany(cascade= CascadeType.ALL)
    @JsonProperty
    @JoinColumn(name="parentkey")
    private List<Tempkey> tempkeyList;

    public Permkey(int permkey_id,String gatekey, String note, List tempkeyList)
    {
        super(gatekey);
        this.permkey_id = permkey_id;
        this.note = note;
        this.tempkeyList = tempkeyList;
    }

    public Permkey(){
        // Jackson deserialization
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

    public List<Tempkey> getTempkeyList() {
        return tempkeyList;
    }

    public void setTempkeyList(List<Tempkey> tempkeyList) {
        this.tempkeyList = tempkeyList;
    }
}
