package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;

/** Temi14UteCatId generated by hbm2java */
public class Temi14UteCatId implements java.io.Serializable {

  private int cat;
  private Date insCat;

  public Temi14UteCatId() {}

  public Temi14UteCatId(int cat, Date insCat) {
    this.cat = cat;
    this.insCat = insCat;
  }

  public int getCat() {
    return this.cat;
  }

  public void setCat(int cat) {
    this.cat = cat;
  }

  public Date getInsCat() {
    return this.insCat;
  }

  public void setInsCat(Date insCat) {
    this.insCat = insCat;
  }

  public boolean equals(Object other) {
    if ((this == other)) return true;
    if ((other == null)) return false;
    if (!(other instanceof Temi14UteCatId)) return false;
    Temi14UteCatId castOther = (Temi14UteCatId) other;

    return (this.getCat() == castOther.getCat())
        && ((this.getInsCat() == castOther.getInsCat())
            || (this.getInsCat() != null
                && castOther.getInsCat() != null
                && this.getInsCat().equals(castOther.getInsCat())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (int) this.getCat();
    result = 37 * result + (getInsCat() == null ? 0 : this.getInsCat().hashCode());
    return result;
  }
}
