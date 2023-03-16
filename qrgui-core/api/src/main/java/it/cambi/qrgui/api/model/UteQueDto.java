package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
public class UteQueDto implements java.io.Serializable {

    private String tenant;
    private String nam;
    private String json;
    @Builder.Default
    private Set<QueCatAssDto> Temi16QueCatAsses = new HashSet<>(0);
    @Builder.Default
    private Set<RouQueDto> rouQues = new HashSet<>(0);
    private long que;
    private Date insQue;

    public UteQueDto() {

    }

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

    public String getTenant() {
        return this.tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
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
