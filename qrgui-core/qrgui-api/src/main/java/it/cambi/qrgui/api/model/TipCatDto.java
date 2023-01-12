package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.HashSet;
import java.util.Set;

public class TipCatDto implements java.io.Serializable {

  private String tipCat;
  private String des;
  private Set<CategoryDto> Temi14UteCats = new HashSet<CategoryDto>(0);

  public TipCatDto() {}

  public TipCatDto(String tipCat, String des) {
    this.tipCat = tipCat;
    this.des = des;
  }

  public TipCatDto(String tipCat, String des, Set<CategoryDto> Temi14UteCats) {
    this.tipCat = tipCat;
    this.des = des;
    this.Temi14UteCats = Temi14UteCats;
  }

  public String getTipCat() {
    return this.tipCat;
  }

  public void setTipCat(String tipCat) {
    this.tipCat = tipCat;
  }

  public String getDes() {
    return this.des;
  }

  public void setDes(String des) {
    this.des = des;
  }

  public Set<CategoryDto> getTemi14UteCats() {
    return this.Temi14UteCats;
  }

  public void setTemi14UteCats(Set<CategoryDto> Temi14UteCats) {
    this.Temi14UteCats = Temi14UteCats;
  }
}
