package it.cambi.qrgui.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.time.Duration;
import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

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
    public ObjectMapper objectMapper(
            Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {

        return
                jacksonObjectMapperBuilder
                        .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                        .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .modulesToInstall(new JavaTimeModule())
                        .build();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer personCustomizer() {
        return jacksonObjectMapperBuilder -> {
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
            RequestMappingHandlerAdapter reqAdapter, ObjectMapper mapper) {

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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(30000))
                .setReadTimeout(Duration.ofMillis(30000))
                .build();
    }
}
