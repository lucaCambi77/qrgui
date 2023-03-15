package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

public class QueCatAssDto implements java.io.Serializable {
  private QueCatAssId id;
  private CategoryDto Temi14UteCat;
  private UteQueDto UteQue;

  public QueCatAssDto() {}

  public QueCatAssDto(
          QueCatAssId id, CategoryDto Temi14UteCat, UteQueDto UteQue) {
    this.id = id;
    this.Temi14UteCat = Temi14UteCat;
    this.UteQue = UteQue;
  }

  public QueCatAssId getId() {
    return this.id;
  }

  public void setId(QueCatAssId id) {
    this.id = id;
  }

  public CategoryDto getTemi14UteCat() {
    return this.Temi14UteCat;
  }

  public void setTemi14UteCat(CategoryDto Temi14UteCat) {
    this.Temi14UteCat = Temi14UteCat;
  }

  public UteQueDto getTemi15UteQue() {
    return this.UteQue;
  }

  public void setTemi15UteQue(UteQueDto UteQue) {
    this.UteQue = UteQue;
  }
}
