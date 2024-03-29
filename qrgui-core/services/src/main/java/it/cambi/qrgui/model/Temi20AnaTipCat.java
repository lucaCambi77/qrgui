package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/** Temi20AnaTipCat generated by hbm2java */
@Entity
@Table(name = "Temi20_ANA_TIP_CAT", schema = "EMIA")
public class Temi20AnaTipCat implements java.io.Serializable {

  private String tipCat;
  private String des;
  private Set<Temi14UteCat> Temi14UteCats = new HashSet<Temi14UteCat>(0);

  public Temi20AnaTipCat() {}

  public Temi20AnaTipCat(String tipCat, String des) {
    this.tipCat = tipCat;
    this.des = des;
  }

  public Temi20AnaTipCat(String tipCat, String des, Set<Temi14UteCat> Temi14UteCats) {
    this.tipCat = tipCat;
    this.des = des;
    this.Temi14UteCats = Temi14UteCats;
  }

  @Id
  @Column(name = "C_TIP_CAT", unique = true, nullable = false, length = 32)
  public String getTipCat() {
    return this.tipCat;
  }

  public void setTipCat(String tipCat) {
    this.tipCat = tipCat;
  }

  @Column(name = "T_DES", nullable = false, length = 32)
  public String getDes() {
    return this.des;
  }

  public void setDes(String des) {
    this.des = des;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "temi20AnaTipCat")
  public Set<Temi14UteCat> getTemi14UteCats() {
    return this.Temi14UteCats;
  }

  public void setTemi14UteCats(Set<Temi14UteCat> Temi14UteCats) {
    this.Temi14UteCats = Temi14UteCats;
  }
}
