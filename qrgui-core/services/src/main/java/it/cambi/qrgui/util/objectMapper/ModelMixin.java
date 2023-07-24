package it.cambi.qrgui.util.objectMapper;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi14UteCatId;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import java.util.List;
import java.util.Set;

import org.springframework.boot.jackson.JsonMixin;

public class ModelMixin {

  @JsonMixin(Temi14UteCat.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi16QueCatAsses"})
  public interface Temi14UteCatMixIn {
    @JsonProperty("id")
    Temi14UteCatId getId();

    @JsonProperty("npar")
    Long getPar();

    @JsonProperty("tdes")
    String getDes();

    @JsonProperty("temi20AnaTipCat")
    Temi20AnaTipCat getTemi20AnaTipCat();
  }

  @JsonMixin(Temi15UteQue.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi16QueCatAsses"})
  public interface Temi15UteQueMixIn {
    @JsonProperty("id")
    Temi15UteQueId getId();

    @JsonProperty("tjson")
    String getTJson();

    @JsonProperty("tNam")
    String getTNam();

    @JsonProperty("tenant")
    String getTenant();

    @JsonProperty("temi18RouQues")
    List<Temi18RouQue> getTemi18RouQues();
  }

  @JsonMixin(Temi16QueCatAss.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public interface Temi16QueCatAssMixIn {
    @JsonProperty("id")
    Temi16QueCatAssId getId();

    @JsonProperty("temi14UteCat")
    Temi14UteCat getTemi14UteCat();

    @JsonProperty("temi15UteQue")
    Temi15UteQue getTemi15UteQue();
  }

  @JsonMixin(Temi17UteRou.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public interface Temi17UteRouMixIn {
    @JsonProperty("id")
    Temi17UteRouId getId();

    @JsonProperty("tdes")
    String getTDes();

    @JsonProperty("temi18RouQues")
    List<Temi18RouQue> getTemi18RouQues();
  }

  @JsonMixin(Temi18RouQue.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi17UteRou"})
  public interface Temi18RouQueMixIn {
    @JsonProperty("id")
    Temi18RouQueId getId();

    @JsonIgnore
    @JsonProperty("temi15UteQue")
    Temi15UteQue getTemi15UteQue();
  }

  @JsonMixin(Temi20AnaTipCat.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi14UteCats"})
  public interface Temi20AnaTipCatMixIn {
    @JsonProperty("tipCat")
    String getTipCat();

    @JsonProperty("des")
    String getDes();
  }
}
