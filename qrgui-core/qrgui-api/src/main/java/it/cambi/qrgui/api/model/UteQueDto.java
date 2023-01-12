package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UteQueDto implements java.io.Serializable {

    private DtbInfDto DtbInf;
    private String nam;
    private String json;
    private Set<QueCatAssDto> Temi16QueCatAsses = new HashSet<>(0);
    private Set<RouQueDto> rouQues = new HashSet<>(0);
    private long que;
    private Date insQue;

    public long getQue() {
        return this.que;
    }

    public void setQue(long que) {
        this.que = que;
    }

    public Date getInsQue() {
        return this.insQue;
    }

    public void setInsQue(Date insQue) {
        this.insQue = insQue;
    }

    public UteQueDto() {
    }

    public UteQueDto(long que, Date insQue, String nam, String json) {
        this.que = que;
        this.insQue = insQue;
        this.nam = nam;
        this.json = json;
    }

    public UteQueDto(
            long que,
            Date insQue,
            DtbInfDto DtbInf,
            String nam,
            String json,
            Set<QueCatAssDto> Temi16QueCatAsses,
            Set<RouQueDto> rouQues) {
        this.que = que;
        this.insQue = insQue;
        this.DtbInf = DtbInf;
        this.nam = nam;
        this.json = json;
        this.Temi16QueCatAsses = Temi16QueCatAsses;
        this.rouQues = rouQues;
    }

    public DtbInfDto getTemi13DtbInf() {
        return this.DtbInf;
    }

    public void setTemi13DtbInf(DtbInfDto DtbInf) {
        this.DtbInf = DtbInf;
    }

    public String getNam() {
        return this.nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Set<QueCatAssDto> getTemi16QueCatAsses() {
        return this.Temi16QueCatAsses;
    }

    public void setTemi16QueCatAsses(Set<QueCatAssDto> Temi16QueCatAsses) {
        this.Temi16QueCatAsses = Temi16QueCatAsses;
    }

    public Set<RouQueDto> getTemi18RouQues() {
        return this.rouQues;
    }

    public void setTemi18RouQues(Set<RouQueDto> rouQues) {
        this.rouQues = rouQues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UteQueDto that = (UteQueDto) o;
        return que == that.que && insQue.equals(that.insQue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(que, insQue);
    }
}
