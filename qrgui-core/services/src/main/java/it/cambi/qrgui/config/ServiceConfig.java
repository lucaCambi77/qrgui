package it.cambi.qrgui.config;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.impl.Temi20Service;
import it.cambi.qrgui.util.objectMapper.ModelMixin;
import java.time.Duration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {

  @Bean
  CommandLineRunner initializeApplication(Temi20Service temi20Service) {

    return args -> {
      Temi20AnaTipCat temi20AnaTipCat = new Temi20AnaTipCat();
      temi20AnaTipCat.setDes("Tip Category Test");
      temi20AnaTipCat.setTipCat(R_FEPQRA);

      temi20Service.merge(temi20AnaTipCat);
    };
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper() {

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    JsonMixinModule jsonMixinModule = new JsonMixinModule();
    jsonMixinModule.setMixInAnnotation(Temi14UteCat.class, ModelMixin.Temi14UteCatMixIn.class);
    jsonMixinModule.setMixInAnnotation(Temi15UteQue.class, ModelMixin.Temi15UteQueMixIn.class);
    jsonMixinModule.setMixInAnnotation(
        Temi16QueCatAss.class, ModelMixin.Temi16QueCatAssMixIn.class);
    jsonMixinModule.setMixInAnnotation(Temi17UteRou.class, ModelMixin.Temi17UteRouMixIn.class);
    jsonMixinModule.setMixInAnnotation(Temi18RouQue.class, ModelMixin.Temi18RouQueMixIn.class);
    jsonMixinModule.setMixInAnnotation(
        Temi20AnaTipCat.class, ModelMixin.Temi20AnaTipCatMixIn.class);

    mapper.registerModule(jsonMixinModule);
    return mapper;
  }

  /**
   * @param mapper TODO this is not working in Spring Boot 3, the Http converter is messed up and do
   *     not work with a custom ObjectMapper. So I had to serialize and deserialize the objects in
   *     the service controllers
   * @return
   */
  public MappingJackson2HttpMessageConverter myMessageConverter(ObjectMapper mapper) {
    return new MappingJackson2HttpMessageConverter(mapper);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder
        .setConnectTimeout(Duration.ofMillis(30000))
        .setReadTimeout(Duration.ofMillis(30000))
        .build();
  }
}
