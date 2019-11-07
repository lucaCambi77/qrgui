package it.cambi.qrgui.db.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Temi17UteRouId generated by hbm2java
 */
@Embeddable
public class Temi17UteRouId implements java.io.Serializable
{

    private long rou;
    private Date insRou;

    public Temi17UteRouId()
    {
    }

    public Temi17UteRouId(long rou, Date insRou)
    {
        this.rou = rou;
        this.insRou = insRou;
    }

    @SequenceGenerator(name = "ttps17generator", sequenceName = "EMIA.SEMI17_ANA_ROU", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "ttps17generator")
    @Column(name = "C_ROU", nullable = false, precision = 10, scale = 0)
    public long getRou()
    {
        return this.rou;
    }

    public void setRou(long rou)
    {
        this.rou = rou;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "D_INS_ROU", nullable = false, length = 7)
    public Date getInsRou()
    {
        return this.insRou;
    }

    public void setInsRou(Date insRou)
    {
        this.insRou = insRou;
    }

    public boolean equals(Object other)
    {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof Temi17UteRouId))
            return false;
        Temi17UteRouId castOther = (Temi17UteRouId) other;

        return (this.getRou() == castOther.getRou())
                && ((this.getInsRou() == castOther.getInsRou())
                        || (this.getInsRou() != null && castOther.getInsRou() != null && this.getInsRou().equals(castOther.getInsRou())));
    }

    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (int) this.getRou();
        result = 37 * result + (getInsRou() == null ? 0 : this.getInsRou().hashCode());
        return result;
    }

}
