package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Temi15UteQue generated by hbm2java
 */
@Entity
@Table(name = "Temi15_UTE_QUE", schema = "EMIA")
@IdClass(Temi15UteQueId.class)
public class Temi15UteQue implements java.io.Serializable {

    private String tenant;

    private String nam;

    private String json;

    private Set<Temi16QueCatAss> Temi16QueCatAsses = new HashSet<>(0);

    private Set<Temi18RouQue> Temi18RouQues = new HashSet<>(0);

    private long que;

    private Date insQue;

    @SequenceGenerator(
            name = "ttps15generator",
            sequenceName = "EMIA.SEMI15_ANA_QUE",
            allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "ttps15generator")
    @Column(name = "C_QUE", nullable = false, precision = 10, scale = 0)
    @Id
    public long getQue() {
        return this.que;
    }

    public void setQue(long que) {
        this.que = que;
    }

    @Column(name = "D_INS_QUE", nullable = false, length = 7)
    @Id
    public Date getInsQue() {
        return this.insQue;
    }

    public void setInsQue(Date insQue) {
        this.insQue = insQue;
    }

    public Temi15UteQue() {
    }

    public Temi15UteQue(long que, Date insQue, String nam, String json) {
        this.que = que;
        this.insQue = insQue;
        this.nam = nam;
        this.json = json;
    }

    public Temi15UteQue(
            long que,
            Date insQue,
            String tenant,
            String nam,
            String json,
            Set<Temi16QueCatAss> Temi16QueCatAsses,
            Set<Temi18RouQue> Temi18RouQues) {
        this.que = que;
        this.insQue = insQue;
        this.tenant = tenant;
        this.nam = nam;
        this.json = json;
        this.Temi16QueCatAsses = Temi16QueCatAsses;
        this.Temi18RouQues = Temi18RouQues;
    }


    public String getTenant() {
        return this.tenant;
    }

    public void setTenant(String Temi13DtbInf) {
        this.tenant = Temi13DtbInf;
    }

    @Column(name = "T_NAM", nullable = false, length = 80)
    public String getNam() {
        return this.nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    @Column(name = "T_JSON", nullable = false)
    @Lob
    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi15UteQue")
    public Set<Temi16QueCatAss> getTemi16QueCatAsses() {
        return this.Temi16QueCatAsses;
    }

    public void setTemi16QueCatAsses(Set<Temi16QueCatAss> Temi16QueCatAsses) {
        this.Temi16QueCatAsses = Temi16QueCatAsses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi15UteQue")
    public Set<Temi18RouQue> getTemi18RouQues() {
        return this.Temi18RouQues;
    }

    public void setTemi18RouQues(Set<Temi18RouQue> Temi18RouQues) {
        this.Temi18RouQues = Temi18RouQues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temi15UteQue that = (Temi15UteQue) o;
        return que == that.que && insQue.equals(that.insQue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(que, insQue);
    }
}
