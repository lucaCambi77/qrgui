package it.cambi.qrgui;

import it.cambi.qrgui.config.InitConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Duration;

@SpringBootApplication
@EnableWebMvc
@Import({InitConfiguration.class})
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(30000))
                .setReadTimeout(Duration.ofMillis(30000))
                .build();
    }
}
