package it.cambi.qrgui.config;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

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
import it.cambi.qrgui.util.objectMapper.ModelMixin;
import java.time.Duration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
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
                    Temi14UteCat.class, ModelMixin.Temi14UteCatMixIn.class);
            jacksonObjectMapperBuilder.mixIn(
                    Temi15UteQue.class, ModelMixin.Temi15UteQueMixIn.class);
            jacksonObjectMapperBuilder.mixIn(
                    Temi16QueCatAss.class, ModelMixin.Temi16QueCatAssMixIn.class);
            jacksonObjectMapperBuilder.mixIn(
                    Temi17UteRou.class, ModelMixin.Temi17UteRouMixIn.class);
            jacksonObjectMapperBuilder.mixIn(
                    Temi18RouQue.class, ModelMixin.Temi18RouQueMixIn.class);
            jacksonObjectMapperBuilder.mixIn(
                    Temi20AnaTipCat.class, ModelMixin.Temi20AnaTipCatMixIn.class);
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(30000))
                .setReadTimeout(Duration.ofMillis(30000))
                .build();
    }
}
