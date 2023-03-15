package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;

public class RouQueId implements java.io.Serializable {

  private long rou;
  private long que;
  private Date insQue;
  private Date insRou;

  public RouQueId() {}

  public RouQueId(long rou, long que, Date insQue, Date insRou) {
    this.rou = rou;
    this.que = que;
    this.insQue = insQue;
    this.insRou = insRou;
  }

  public long getRou() {
    return this.rou;
  }

  public void setRou(long rou) {
    this.rou = rou;
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

  public Date getInsRou() {
    return this.insRou;
  }

  public void setInsRou(Date insRou) {
    this.insRou = insRou;
  }

  public boolean equals(Object other) {
    if ((this == other)) return true;
    if ((other == null)) return false;
    if (!(other instanceof RouQueId)) return false;
    RouQueId castOther = (RouQueId) other;

    return (this.getRou() == castOther.getRou())
        && (this.getQue() == castOther.getQue())
        && ((this.getInsQue() == castOther.getInsQue())
            || (this.getInsQue() != null
                && castOther.getInsQue() != null
                && this.getInsQue().equals(castOther.getInsQue())))
        && ((this.getInsRou() == castOther.getInsRou())
            || (this.getInsRou() != null
                && castOther.getInsRou() != null
                && this.getInsRou().equals(castOther.getInsRou())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (int) this.getRou();
    result = 37 * result + (int) this.getQue();
    result = 37 * result + (getInsQue() == null ? 0 : this.getInsQue().hashCode());
    result = 37 * result + (getInsRou() == null ? 0 : this.getInsRou().hashCode());
    return result;
  }
}
