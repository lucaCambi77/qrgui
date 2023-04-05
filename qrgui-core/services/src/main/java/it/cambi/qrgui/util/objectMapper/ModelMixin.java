
package it.cambi.qrgui.util.objectMapper;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.cambi.qrgui.model.*;

import java.util.List;
import java.util.Set;


public class ModelMixin {

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi14UteCat.class)
    public interface Temi14UteCatMixIn {
        @JsonProperty("id")
        Temi14UteCatId getId();

        @JsonProperty("npar")
        Long getNPar();

        @JsonProperty("tdes")
        String getTDes();

        @JsonProperty("temi20AnaTipCat")
        Temi20AnaTipCat getTemi20AnaTipCat();

        @JsonIgnore
        @JsonProperty("temi16QueCatAsses")
        Set<Temi16QueCatAss> getTemi16QueCatAsses();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi15UteQue.class)
    public interface Temi15UteQueMixIn {
        @JsonProperty("id")
        Temi15UteQueId getId();

        @JsonProperty("tjson")
        String getTJson();

        @JsonProperty("tNam")
        String getTNam();

        @JsonProperty("tenant")
        String getTenant();

        @JsonIgnore
        @JsonProperty("temi16QueCatAsses")
        List<Temi16QueCatAss> getTemi16QueCatAsses();

        @JsonProperty("temi18RouQues")
        List<Temi18RouQue> getTemi18RouQues();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi16QueCatAss.class)
    public interface Temi16QueCatAssMixIn {
        @JsonProperty("id")
        Temi16QueCatAssId getId();

        @JsonProperty("temi14UteCat")
        Temi14UteCat getTemi14UteCat();

        @JsonProperty("temi15UteQue")
        Temi15UteQue getTemi15UteQue();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi17UteRou.class)
    public interface Temi17UteRouMixIn {
        @JsonProperty("id")
        Temi17UteRouId getId();

        @JsonProperty("tdes")
        String getTDes();

        @JsonProperty("temi18RouQues")
        List<Temi18RouQue> getTemi18RouQues();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi18RouQue.class)
    public interface Temi18RouQueMixIn {
        @JsonProperty("id")
        Temi18RouQueId getId();

        @JsonIgnore
        @JsonProperty("temi15UteQue")
        Temi15UteQue getTemi15UteQue();

        @JsonIgnore
        @JsonProperty("temi17UteRou")
        Temi17UteRou getTemi17UteRou();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "cTipCat", scope = Temi20AnaTipCat.class)
    public interface Temi20AnaTipCatMixIn {
        @JsonProperty("cTipCat")
        String getCTipCat();

        @JsonProperty("tDes")
        String getTDes();

        @JsonIgnore
        @JsonProperty("temi14UteCats")
        List<Temi14UteCat> getTemi14UteCats();
    }
}
