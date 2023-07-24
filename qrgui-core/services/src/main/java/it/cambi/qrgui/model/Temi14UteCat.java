package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/** Temi14UteCat generated by hbm2java */
@Entity
@Table(name = "Temi14_UTE_CAT", schema = "EMIA")
@IdClass(Temi14UteCatId.class)
public class Temi14UteCat implements java.io.Serializable {

  private Temi20AnaTipCat Temi20AnaTipCat;
  private Integer par;
  private Date insPar;
  private String des;
  private Set<Temi16QueCatAss> Temi16QueCatAsses = new HashSet<Temi16QueCatAss>(0);
  private int cat;
  private Date insCat;

  public Temi14UteCat() {}

  public Temi14UteCat(Integer cat) {
    this.cat = cat;
  }

  public Temi14UteCat(int cat, Date insCat, Temi20AnaTipCat TEMI20AnaTipCat, String des) {
    this.cat = cat;
    this.insCat = insCat;
    this.Temi20AnaTipCat = TEMI20AnaTipCat;
    this.des = des;
  }

  public Temi14UteCat(
      int cat,
      Date insCat,
      Temi20AnaTipCat Temi20AnaTipCat,
      Integer par,
      String des,
      Set<Temi16QueCatAss> Temi16QueCatAsses) {
    this.cat = cat;
    this.insCat = insCat;
    this.Temi20AnaTipCat = Temi20AnaTipCat;
    this.par = par;
    this.des = des;
    this.Temi16QueCatAsses = Temi16QueCatAsses;
  }

  @SequenceGenerator(
      name = "ttps14generator",
      sequenceName = "EMIA.SEMI14_ANA_CAT",
      allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "ttps14generator")
  @Column(name = "C_CAT", nullable = false, precision = 10)
  @Id
  public int getCat() {
    return this.cat;
  }

  public void setCat(Integer cat) {
    this.cat = cat;
  }

  @Id
  @Column(name = "D_INS_CAT", nullable = false, length = 7)
  public Date getInsCat() {
    return this.insCat;
  }

  public void setInsCat(Date insCat) {
    this.insCat = insCat;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "C_TIP_CAT", nullable = false)
  public Temi20AnaTipCat getTemi20AnaTipCat() {
    return this.Temi20AnaTipCat;
  }

  public void setTemi20AnaTipCat(Temi20AnaTipCat Temi20AnaTipCat) {
    this.Temi20AnaTipCat = Temi20AnaTipCat;
  }

  @Column(name = "N_PAR", precision = 6, scale = 0)
  public Integer getPar() {
    return this.par;
  }

  public void setPar(Integer par) {
    this.par = par;
  }

  @Column(name = "D_INS_PAR", length = 7)
  public Date getInsPar() {
    return this.insPar;
  }

  public void setInsPar(Date insPar) {
    this.insPar = insPar;
  }

  @Column(name = "T_DES", nullable = false, length = 80)
  public String getDes() {
    return this.des;
  }

  public void setDes(String des) {
    this.des = des;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi14UteCat")
  public Set<Temi16QueCatAss> getTemi16QueCatAsses() {
    return this.Temi16QueCatAsses;
  }

  public void setTemi16QueCatAsses(Set<Temi16QueCatAss> Temi16QueCatAsses) {
    this.Temi16QueCatAsses = Temi16QueCatAsses;
  }
}
