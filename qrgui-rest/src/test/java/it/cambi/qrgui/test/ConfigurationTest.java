/**
 * 
 */
package it.cambi.qrgui.test;

import it.cambi.qrgui.config.EmiaDbAppConf;
import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.security.SpringSecurityConfig;
import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.db.model.Role;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import it.cambi.qrgui.security.jpa.repository.RoleRepository;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.UserServiceImpl;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;
import it.cambi.qrgui.services.db.model.Temi13DtbInfId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author luca
 * 
 *         Db init and Security import
 */
@Configuration
@Import({ SpringSecurityConfig.class, EmiaDbAppConf.class })
@Profile("test")
public class ConfigurationTest
{

    @Bean
    CommandLineRunner initializeApplication(DbInfoJpaRepository dbInfoRepository, UserServiceImpl userService, RoleRepository roleRespository,
            UserRoleRepository userRoleRepository)
    {
        return args -> {

            Temi13DtbInf temi13 = new Temi13DtbInf();
            Temi13DtbInfId id = new Temi13DtbInfId("ORACLE", "TEST");
            temi13.setId(id);

            dbInfoRepository.save(temi13);

            Role role_user = roleRespository.save(Role.builder().name("ROLE_USER").build());

            GuiUser new_user = userService.save(GuiUser.builder().username("user@gmail.com").passwordConfirm("1234").build());

            userRoleRepository.save(UserRole.builder().id(UserRoleId.builder().roleId(role_user.getRoleId()).userId(new_user.getUserId()).build()).build());

            Role role_admin = roleRespository.save(Role.builder().name("ROLE_ADMIN").build());

            GuiUser new_admin = userService.save(GuiUser.builder().username("admin@gmail.com").passwordConfirm("1234").build());

            userRoleRepository.save(UserRole.builder().id(UserRoleId.builder().roleId(role_admin.getRoleId()).userId(new_admin.getUserId()).build()).build());
        };
    }
}
