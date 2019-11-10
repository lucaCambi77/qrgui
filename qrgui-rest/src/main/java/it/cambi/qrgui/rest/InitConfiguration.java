/**
 * 
 */
package it.cambi.qrgui.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.security.SpringSecurityConfig;
import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.db.model.Role;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import it.cambi.qrgui.security.jpa.repository.RoleRepository;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.UserServiceImpl;
import it.cambi.qrgui.services.EmiaDbAppConf;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;
import it.cambi.qrgui.services.db.model.Temi13DtbInfId;

/**
 * @author luca
 * 
 *         Db init and Security import
 */
@Configuration
@Import({ SpringSecurityConfig.class, EmiaDbAppConf.class })
public class InitConfiguration
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

            Role role = new Role();
            role.setName("ROLE_USER");

            Role newRole = roleRespository.save(role);

            GuiUser user = new GuiUser();

            user.setUserName("fake@gmail.com");
            user.setPasswordConfirm("1234");

            GuiUser newUser = userService.save(user);

            UserRole userRole = new UserRole();

            UserRoleId userRoleId = new UserRoleId();
            userRoleId.setRoleId(newRole.getRoleId());
            userRoleId.setUserId(newUser.getUserId());

            userRole.setId(userRoleId);

            userRoleRepository.save(userRole);

        };
    }
}
