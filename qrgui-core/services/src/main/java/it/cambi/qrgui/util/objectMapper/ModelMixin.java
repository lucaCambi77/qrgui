package it.cambi.qrgui.util.objectMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import org.springframework.boot.jackson.JsonMixin;

public class ModelMixin {

  @JsonMixin(Temi14UteCat.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi16QueCatAsses"})
  public interface Temi14UteCatMixIn {
  }

  @JsonMixin(Temi15UteQue.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi16QueCatAsses"})
  public interface Temi15UteQueMixIn {
  }

  @JsonMixin(Temi16QueCatAss.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public interface Temi16QueCatAssMixIn {
  }

  @JsonMixin(Temi17UteRou.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public interface Temi17UteRouMixIn {
 
  }

  @JsonMixin(Temi18RouQue.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi17UteRou", "temi15UteQue"})
  public interface Temi18RouQueMixIn {

  }

  @JsonMixin(Temi20AnaTipCat.class)
  @JsonIgnoreProperties(
      ignoreUnknown = true,
      value = {"temi14UteCats"})
  public interface Temi20AnaTipCatMixIn {

  }
}
