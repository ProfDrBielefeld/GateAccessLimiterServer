package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tempkey_tbl")
@NamedQueries({
        @NamedQuery(name = "model.tempkey.getbykey", query = "select tk from Tempkey tk where tk.tempkey like :apitoken")
})
public class Tempkey
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int tempkey_id;

    @JsonProperty
    private String tempkey;

    @JsonProperty
    private int parentkey;

    @JsonProperty
    private LocalDate startdate;

    @JsonProperty
    private LocalDate enddate;

    public int getTempkey_id() {
        return tempkey_id;
    }

    public void setTempkey_id(int tempkey_id) {
        this.tempkey_id = tempkey_id;
    }

    public String getTempkey() {
        return tempkey;
    }

    public void setTempkey(String tempkey) {
        this.tempkey = tempkey;
    }

    public int getParentkey() {
        return parentkey;
    }

    public void setParentkey(int parentkey) {
        this.parentkey = parentkey;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }
}
