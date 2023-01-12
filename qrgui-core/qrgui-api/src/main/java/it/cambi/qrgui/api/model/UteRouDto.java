package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UteRouDto implements java.io.Serializable {
  private String des;
  private Set<RouQueDto> rouQues = new HashSet<RouQueDto>(0);
  private long rou;
  private Date insRou;

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

  public UteRouDto() {}

  public UteRouDto(long rou, Date insRou, String des) {
    this.rou = rou;
    this.insRou = insRou;
    this.des = des;
  }

  public UteRouDto(long rou, Date insRou, String des, Set<RouQueDto> rouQues) {
    this.rou = rou;
    this.insRou = insRou;
    this.des = des;
    this.rouQues = rouQues;
  }

  public String getDes() {
    return this.des;
  }

  public void setDes(String des) {
    this.des = des;
  }

  public Set<RouQueDto> getTemi18RouQues() {
    return this.rouQues;
  }

  public void setTemi18RouQues(Set<RouQueDto> rouQues) {
    this.rouQues = rouQues;
  }
}
