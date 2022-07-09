package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Temi13DtbInf generated by hbm2java
 */
@Entity
@Table(name = "Temi13_DTB_INF", schema = "EMIA")
public class Temi13DtbInf implements java.io.Serializable
{

    private Temi13DtbInfId id;
    private Set<Temi15UteQue> Temi15UteQues = new HashSet<Temi15UteQue>(0);

    public Temi13DtbInf()
    {
    }

    public Temi13DtbInf(Temi13DtbInfId id)
    {
        this.id = id;
    }

    public Temi13DtbInf(Temi13DtbInfId id, Set<Temi15UteQue> Temi15UteQues)
    {
        this.id = id;
        this.Temi15UteQues = Temi15UteQues;
    }

    @EmbeddedId

    @AttributeOverrides({
            @AttributeOverride(name = "typ", column = @Column(name = "C_TYP", nullable = false, length = 16)),
            @AttributeOverride(name = "sch", column = @Column(name = "C_SCH", nullable = false, length = 16)) })
    public Temi13DtbInfId getId()
    {
        return this.id;
    }

    public void setId(Temi13DtbInfId id)
    {
        this.id = id;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi13DtbInf")
    public Set<Temi15UteQue> getTemi15UteQues()
    {
        return this.Temi15UteQues;
    }

    public void setTemi15UteQues(Set<Temi15UteQue> Temi15UteQues)
    {
        this.Temi15UteQues = Temi15UteQues;
    }

}
