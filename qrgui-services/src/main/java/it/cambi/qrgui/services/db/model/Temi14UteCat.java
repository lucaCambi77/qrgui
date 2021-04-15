package it.cambi.qrgui.services.db.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Temi14UteCat generated by hbm2java
 */
@Entity
@Table(name = "Temi14_UTE_CAT", schema = "EMIA")
public class Temi14UteCat implements java.io.Serializable
{

    private Temi14UteCatId id;
    private Temi20AnaTipCat Temi20AnaTipCat;
    private Integer par;
    private Date insPar;
    private String des;
    private Set<Temi16QueCatAss> Temi16QueCatAsses = new HashSet<Temi16QueCatAss>(0);

    public Temi14UteCat()
    {
    }

    public Temi14UteCat(Temi14UteCatId id, Temi20AnaTipCat TEMI20AnaTipCat, String des)
    {
        this.id = id;
        this.Temi20AnaTipCat = TEMI20AnaTipCat;
        this.des = des;
    }

    public Temi14UteCat(Temi14UteCatId id, Temi20AnaTipCat Temi20AnaTipCat, Integer par, String des, Set<Temi16QueCatAss> Temi16QueCatAsses)
    {
        this.id = id;
        this.Temi20AnaTipCat = Temi20AnaTipCat;
        this.par = par;
        this.des = des;
        this.Temi16QueCatAsses = Temi16QueCatAsses;
    }

    @EmbeddedId

    @AttributeOverrides({
            @AttributeOverride(name = "cat", column = @Column(name = "C_CAT", nullable = false, precision = 10, scale = 0)),
            @AttributeOverride(name = "insCat", column = @Column(name = "D_INS_CAT", nullable = false, length = 7)) })
    public Temi14UteCatId getId()
    {
        return this.id;
    }

    public void setId(Temi14UteCatId id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_TIP_CAT", nullable = false)
    public Temi20AnaTipCat getTemi20AnaTipCat()
    {
        return this.Temi20AnaTipCat;
    }

    public void setTemi20AnaTipCat(Temi20AnaTipCat Temi20AnaTipCat)
    {
        this.Temi20AnaTipCat = Temi20AnaTipCat;
    }

    @Column(name = "N_PAR", precision = 6, scale = 0)
    public Integer getPar()
    {
        return this.par;
    }

    public void setPar(Integer par)
    {
        this.par = par;
    }

    @Column(name = "D_INS_PAR", length = 7)
    public Date getInsPar()
    {
        return this.insPar;
    }

    public void setInsPar(Date insPar)
    {
        this.insPar = insPar;
    }

    @Column(name = "T_DES", nullable = false, length = 80)
    public String getDes()
    {
        return this.des;
    }

    public void setDes(String des)
    {
        this.des = des;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi14UteCat")
    public Set<Temi16QueCatAss> getTemi16QueCatAsses()
    {
        return this.Temi16QueCatAsses;
    }

    public void setTemi16QueCatAsses(Set<Temi16QueCatAss> Temi16QueCatAsses)
    {
        this.Temi16QueCatAsses = Temi16QueCatAsses;
    }

}
