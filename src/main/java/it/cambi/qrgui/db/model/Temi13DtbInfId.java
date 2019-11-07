package it.cambi.qrgui.db.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Temi13DtbInfId generated by hbm2java
 */
@Embeddable
public class Temi13DtbInfId implements java.io.Serializable
{

    private String typ;
    private String sch;

    public Temi13DtbInfId()
    {
    }

    public Temi13DtbInfId(String typ, String sch)
    {
        this.typ = typ;
        this.sch = sch;
    }

    @Column(name = "C_TYP", nullable = false, length = 16)
    public String getTyp()
    {
        return this.typ;
    }

    public void setTyp(String typ)
    {
        this.typ = typ;
    }

    @Column(name = "C_SCH", nullable = false, length = 16)
    public String getSch()
    {
        return this.sch;
    }

    public void setSch(String sch)
    {
        this.sch = sch;
    }

    public boolean equals(Object other)
    {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof Temi13DtbInfId))
            return false;
        Temi13DtbInfId castOther = (Temi13DtbInfId) other;

        return ((this.getTyp() == castOther.getTyp())
                || (this.getTyp() != null && castOther.getTyp() != null && this.getTyp().equals(castOther.getTyp())))
                && ((this.getSch() == castOther.getSch())
                        || (this.getSch() != null && castOther.getSch() != null && this.getSch().equals(castOther.getSch())));
    }

    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (getTyp() == null ? 0 : this.getTyp().hashCode());
        result = 37 * result + (getSch() == null ? 0 : this.getSch().hashCode());
        return result;
    }

}
