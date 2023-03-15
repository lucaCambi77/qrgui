package it.cambi.qrgui.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;

/** Temi17UteRouId generated by hbm2java */
public class Temi17UteRouId implements java.io.Serializable {

  private long rou;
  private Date insRou;

  public Temi17UteRouId() {}

  public Temi17UteRouId(long rou, Date insRou) {
    this.rou = rou;
    this.insRou = insRou;
  }

  public long getRou() {
    return this.rou;
  }

  public void setRou(long rou) {
    this.rou = rou;
  }

  public Date getInsRou() {
    return this.insRou;
  }

  public void setInsRou(Date insRou) {
    this.insRou = insRou;
  }

  public boolean equals(Object other) {
    if ((this == other)) return true;
    if ((other == null)) return false;
    if (!(other instanceof Temi17UteRouId)) return false;
    Temi17UteRouId castOther = (Temi17UteRouId) other;

    return (this.getRou() == castOther.getRou())
        && ((this.getInsRou() == castOther.getInsRou())
            || (this.getInsRou() != null
                && castOther.getInsRou() != null
                && this.getInsRou().equals(castOther.getInsRou())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (int) this.getRou();
    result = 37 * result + (getInsRou() == null ? 0 : this.getInsRou().hashCode());
    return result;
  }
}