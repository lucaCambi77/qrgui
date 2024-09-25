package it.cambi.qrgui;

import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableFeignClients(basePackages = "it.cambi.qrgui.client")
@ComponentScan(basePackages = {"it.cambi.qrgui.client", "it.cambi.qrgui.security", "it.cambi.qrgui.rest"})
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
