package it.cambi.qrgui.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/** This is used in test to avoid 415 error in some cases */
@Configuration
@EnableWebMvc
public class SecurityConfigurationTest {

  @Bean(name = "mvcHandlerMappingIntrospector")
  public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
    return new HandlerMappingIntrospector();
  }
}
