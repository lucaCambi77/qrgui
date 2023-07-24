/** */
package it.cambi.qrgui.test;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG00;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import it.cambi.qrgui.security.db.model.Role;
import it.cambi.qrgui.security.db.model.SecurityUser;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import it.cambi.qrgui.security.jpa.repository.RoleRepository;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author luca
 *     <p>Db init and Security import
 */
@Configuration
@Profile("test")
public class TestConfiguration {

  @Bean
  CommandLineRunner initializeApplication(
      UserServiceImpl userService,
      RoleRepository roleRespository,
      UserRoleRepository userRoleRepository) {
    return args -> {
      Role roleUser = roleRespository.save(Role.builder().name(F_QRCG00).build());

      SecurityUser user =
          userService.save(
              SecurityUser.builder().username("user@xxx.com").passwordConfirm("1234").build());

      userRoleRepository.save(
          UserRole.builder()
              .id(
                  UserRoleId.builder()
                      .roleId(roleUser.getRoleId())
                      .userId(user.getUserId())
                      .build())
              .build());

      Role roleAdmin = roleRespository.save(Role.builder().name(R_FEPQRA).build());

      SecurityUser admin =
          userService.save(
              SecurityUser.builder().username("admin@xxx.com").passwordConfirm("1234").build());

      userRoleRepository.save(
          UserRole.builder()
              .id(
                  UserRoleId.builder()
                      .roleId(roleAdmin.getRoleId())
                      .userId(admin.getUserId())
                      .build())
              .build());
    };
  }
}
