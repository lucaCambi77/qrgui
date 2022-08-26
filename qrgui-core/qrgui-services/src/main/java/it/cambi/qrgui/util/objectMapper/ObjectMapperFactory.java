
package it.cambi.qrgui.util.objectMapper;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi13DtbInfId;
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

/**
 * Classe per la creazione dell'object mapper che serve per la serializzazione degli oggetti verso il web. Mentre hibernate, tramite l'
 * Hibernate5Module, evitata il loop infinito dovuto alle relazione della jpa, per Weblogic, non dispondendo di un simile modulo, erano state inserite
 * le annotation @JsonIgnore, rendendo però impossibile construire una entity per intero in quanto le relazione venivano appunto ignorate.
 * 
 * Jackson fornisce la possibilità di aggiungere al mapper le MixIn, che permettono di serializzare la entity jpa, tramite oggetti su cui poi
 * intervenire con le modifiche necessarie, senza incorrere nel loop infinito. Se ci troviamo nella sessione jpa, quando un oggetto viene
 * serializzato, scatena automaticamente le query per le get fatte nell'accesso agli attributi della classe.
 * 
 * In questo modo si rende il dao (tpsa) indipendente dal progetto web e da qualsiasi modifica dovuto all'utilizzo di un application server piuttosto
 * di un altro.
 * 
 * Mappare le mix in è piuttosto laborioso nel senso che sono state mappate molte entity anche per limitare il numero di query fatte dalla jpa e deve
 * essere mantenuto nel caso di modifiche alla base dati(Vedi lo unit test nell'obumgmnt) . Allo stesso tempo offre maggiore flessibilità e permette
 * quando si ha necessità di ricreare nell'oggetto serializzato le varie relazione tra le entity senza dover definire oggetti intermedi
 * 
 * Ho aggiunto la possibilità di creare un writer in cui dichiarare ignorable a runtime anche campi che non hanno @JsonIgnore
 */
public class ObjectMapperFactory
{

    @JsonFilter("Filter")
    public class PropertyFilterMixIn
    {
    }

    private ObjectWriter defaultWriter;

    public ObjectMapperFactory()
    {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // Ho aggiunto questo ma non serve...

        objectMapper.addMixIn(Temi13DtbInf.class, Temi13DtbInfMixIn.class);
        objectMapper.addMixIn(Temi14UteCat.class, Temi14UteCatMixIn.class);
        objectMapper.addMixIn(Temi15UteQue.class, Temi15UteQueMixIn.class);
        objectMapper.addMixIn(Temi16QueCatAss.class, Temi16QueCatAssMixIn.class);
        objectMapper.addMixIn(Temi17UteRou.class, Temi17UteRouMixIn.class);
        objectMapper.addMixIn(Temi18RouQue.class, Temi18RouQueMixIn.class);
        objectMapper.addMixIn(Temi20AnaTipCat.class, Temi20AnaTipCatMixIn.class);

        objectMapper.addMixIn(
          Object.class, PropertyFilterMixIn.class);

        objectMapper.setFilterProvider(new SimpleFilterProvider()
          .addFilter("Filter",
            SimpleBeanPropertyFilter.serializeAllExcept()));
    }

    private final ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }

    public ObjectWriter createWriter(String[] ignorableFields)
    {
        return objectMapper.writer(new SimpleFilterProvider()
          .addFilter("Filter",
            SimpleBeanPropertyFilter.serializeAllExcept(
              ignorableFields)));
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi13DtbInf.class)
    public interface Temi13DtbInfMixIn
    {

        @JsonProperty("id")
        Temi13DtbInfId getId();

        @JsonIgnore
        @JsonProperty("temi15UteQues")
        List<Temi15UteQue> getTemi15UteQues();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi14UteCat.class)
    public interface Temi14UteCatMixIn
    {
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
    public interface Temi15UteQueMixIn
    {
        @JsonProperty("id")
        Temi15UteQueId getId();

        @JsonProperty("tjson")
        String getTJson();

        @JsonProperty("tNam")
        String getTNam();

        @JsonProperty("temi13DtbInf")
        Temi13DtbInf getTemi13DtbInf();

        @JsonIgnore
        @JsonProperty("temi16QueCatAsses")
        List<Temi16QueCatAss> getTemi16QueCatAsses();

        @JsonProperty("temi18RouQues")
        List<Temi18RouQue> getTemi18RouQues();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi16QueCatAss.class)
    public interface Temi16QueCatAssMixIn
    {
        @JsonProperty("id")
        Temi16QueCatAssId getId();

        @JsonProperty("temi14UteCat")
        Temi14UteCat getTemi14UteCat();

        @JsonProperty("temi15UteQue")
        Temi15UteQue getTemi15UteQue();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi17UteRou.class)
    public interface Temi17UteRouMixIn
    {
        @JsonProperty("id")
        Temi17UteRouId getId();

        @JsonProperty("tdes")
        String getTDes();

        @JsonProperty("temi18RouQues")
        List<Temi18RouQue> getTemi18RouQues();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = Temi18RouQue.class)
    public interface Temi18RouQueMixIn
    {
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
    public interface Temi20AnaTipCatMixIn
    {
        @JsonProperty("cTipCat")
        String getCTipCat();

        @JsonProperty("tDes")
        String getTDes();

        @JsonIgnore
        @JsonProperty("temi14UteCats")
        List<Temi14UteCat> getTemi14UteCats();
    }
}
