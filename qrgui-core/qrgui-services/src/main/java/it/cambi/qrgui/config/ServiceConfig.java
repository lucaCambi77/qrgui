package it.cambi.qrgui.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi13DtbInfId;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.impl.Temi20Service;
import it.cambi.qrgui.util.objectMapper.ObjectMapperFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@Configuration
public class ServiceConfig {

  @Bean
  CommandLineRunner initializeApplication(
      DbInfoJpaRepository dbInfoRepository, Temi20Service temi20Service) {

    return args -> {
      Temi13DtbInf temi13 = new Temi13DtbInf();
      Temi13DtbInfId id = new Temi13DtbInfId("ORACLE", Schema.TEST.name());
      temi13.setId(id);

      dbInfoRepository.save(temi13);

      Temi20AnaTipCat temi20AnaTipCat = new Temi20AnaTipCat();
      temi20AnaTipCat.setDes("Tip Category Test");
      temi20AnaTipCat.setTipCat(R_FEPQRA);

      temi20Service.merge(temi20AnaTipCat);
    };
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.disable(
        SerializationFeature.FAIL_ON_EMPTY_BEANS); // Ho aggiunto questo ma non serve...

    objectMapper.addMixIn(Temi13DtbInf.class, ObjectMapperFactory.Temi13DtbInfMixIn.class);
    objectMapper.addMixIn(Temi14UteCat.class, ObjectMapperFactory.Temi14UteCatMixIn.class);
    objectMapper.addMixIn(Temi15UteQue.class, ObjectMapperFactory.Temi15UteQueMixIn.class);
    objectMapper.addMixIn(Temi16QueCatAss.class, ObjectMapperFactory.Temi16QueCatAssMixIn.class);
    objectMapper.addMixIn(Temi17UteRou.class, ObjectMapperFactory.Temi17UteRouMixIn.class);
    objectMapper.addMixIn(Temi18RouQue.class, ObjectMapperFactory.Temi18RouQueMixIn.class);
    objectMapper.addMixIn(Temi20AnaTipCat.class, ObjectMapperFactory.Temi20AnaTipCatMixIn.class);

    return objectMapper;
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer personCustomizer() {
    return jacksonObjectMapperBuilder -> {
      jacksonObjectMapperBuilder.mixIn(
          Temi13DtbInf.class, ObjectMapperFactory.Temi13DtbInfMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi14UteCat.class, ObjectMapperFactory.Temi14UteCatMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi15UteQue.class, ObjectMapperFactory.Temi15UteQueMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi16QueCatAss.class, ObjectMapperFactory.Temi16QueCatAssMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi17UteRou.class, ObjectMapperFactory.Temi17UteRouMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi18RouQue.class, ObjectMapperFactory.Temi18RouQueMixIn.class);
      jacksonObjectMapperBuilder.mixIn(
          Temi20AnaTipCat.class, ObjectMapperFactory.Temi20AnaTipCatMixIn.class);
    };
  }

  @Bean
  public MappingJackson2HttpMessageConverter myMessageConverter(
      RequestMappingHandlerAdapter reqAdapter,
      Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {

    ObjectMapper mapper =
        jacksonObjectMapperBuilder
            .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .modulesToInstall(new JavaTimeModule())
            .build();

    // **replace previous MappingJackson converter**
    List<HttpMessageConverter<?>> converters = reqAdapter.getMessageConverters();
    converters.removeIf(
        httpMessageConverter ->
            httpMessageConverter.getClass().equals(MappingJackson2HttpMessageConverter.class));

    MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter(mapper);
    converters.add(jackson);
    reqAdapter.setMessageConverters(converters);

    return jackson;
  }
}
