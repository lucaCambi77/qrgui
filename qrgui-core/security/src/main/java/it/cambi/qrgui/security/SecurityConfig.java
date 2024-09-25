/** */
package it.cambi.qrgui.security;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;
import static org.springframework.security.config.Customizer.withDefaults;

import it.cambi.qrgui.security.db.model.Role;
import it.cambi.qrgui.security.db.model.SecurityUser;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import it.cambi.qrgui.security.jpa.repository.RoleRepository;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author luca
 */
@EnableWebSecurity
@EnableMethodSecurity()
@Import({SecurityDbAppConf.class})
@Configuration
public class SecurityConfig {

  @Value("${spring.security.debug:false}")
  boolean securityDebug;

  @Bean
  static GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  CommandLineRunner initializeSecurityDb(
      UserServiceImpl userService,
      RoleRepository roleRepository,
      UserRoleRepository userRoleRepository) {

    return args -> {
      Role role = roleRepository.save(Role.builder().name(R_FEPQRA).build());

      SecurityUser user =
          userService.save(
              SecurityUser.builder().username("user@gmail.com").password("1234").build());

      userRoleRepository.save(
          UserRole.builder()
              .id(UserRoleId.builder().roleId(role.getRoleId()).userId(user.getUserId()).build())
              .build());
    };
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      UserDetailsService userDetailsService)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorize) ->
                authorize
                    .requestMatchers(new AntPathRequestMatcher("/**/login"))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults())
        .authenticationManager(
            http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build())
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.cors(withDefaults());

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
            .requestMatchers("/css/*", "/js/*", "/img/*", "/lib/*", "/favicon.ico");
  }
}
