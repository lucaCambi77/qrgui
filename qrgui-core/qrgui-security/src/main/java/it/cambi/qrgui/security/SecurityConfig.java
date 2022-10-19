/** */
package it.cambi.qrgui.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** @author luca */
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Import({SecurityDbAppConf.class})
public class SecurityConfig {

  @Value("${spring.security.debug:false}")
  boolean securityDebug;

  @Bean
  public AuthenticationManager authenticationManager(
      HttpSecurity http,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      UserDetailsService userDetailsService)
      throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder)
        .and()
        .build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.DELETE)
        .hasRole("ADMIN")
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/api/login/**")
        .anonymous()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.debug(securityDebug)
            .ignoring()
            .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
  }
}
