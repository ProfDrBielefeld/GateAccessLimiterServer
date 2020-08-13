package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tempkey_tbl")
@NamedQueries({
        @NamedQuery(name = "model.tempkey.getbykey", query = "select tk from Tempkey tk where tk.gatekey like :apitoken")
})
public class Tempkey extends Key
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int tempkey_id;

    @JsonIgnore
    @ManyToOne(targetEntity = Permkey.class)
    @JoinColumn(name="parentkey")
    private Permkey parentkey;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startdate;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enddate;

    @JsonProperty
    private String purpose;

    public Tempkey(){
        // Jackson deserialization
    }

    public Tempkey(int tempkey_id, LocalDateTime startdate, LocalDateTime enddate, String purpose, String gatekey, Permkey parentkey){
        super(gatekey);
        this.tempkey_id = tempkey_id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.purpose = purpose;
        this.parentkey = parentkey;
    }

    public int getTempkey_id() {
        return tempkey_id;
    }

    public void setTempkey_id(int tempkey_id) {
        this.tempkey_id = tempkey_id;
    }

    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }

    public LocalDateTime getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDateTime enddate) {
        this.enddate = enddate;
    }

    public Permkey getParentkey() {
        return parentkey;
    }

    public void setParentkey(Permkey parentkey) {
        this.parentkey = parentkey;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String usage) {
        this.purpose = usage;
    }
}
