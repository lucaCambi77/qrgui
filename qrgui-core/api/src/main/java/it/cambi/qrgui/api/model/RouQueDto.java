package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

public class RouQueDto implements java.io.Serializable {

  private RouQueId id;
  private UteQueDto Temi15UteQue;
  private UteRouDto UteRou;

  public RouQueDto() {}

  public RouQueDto(RouQueId id, UteQueDto Temi15UteQue, UteRouDto UteRou) {
    this.id = id;
    this.Temi15UteQue = Temi15UteQue;
    this.UteRou = UteRou;
  }

  public RouQueId getId() {
    return this.id;
  }

  public void setId(RouQueId id) {
    this.id = id;
  }

  public UteQueDto getTemi15UteQue() {
    return this.Temi15UteQue;
  }

  public void setTemi15UteQue(UteQueDto Temi15UteQue) {
    this.Temi15UteQue = Temi15UteQue;
  }

  public UteRouDto getTemi17UteRou() {
    return this.UteRou;
  }

  public void setTemi17UteRou(UteRouDto UteRou) {
    this.UteRou = UteRou;
  }
}
