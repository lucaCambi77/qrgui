/**
 * 
 */
package it.cambi.qrgui.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.cambi.qrgui.db.model.Temi13DtbInf;
import it.cambi.qrgui.db.model.Temi13DtbInfId;
import it.cambi.qrgui.db.security.model.GuiUser;
import it.cambi.qrgui.db.security.model.Role;
import it.cambi.qrgui.db.security.model.UserRole;
import it.cambi.qrgui.db.security.model.UserRoleId;
import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.jpa.security.repository.RoleRepository;
import it.cambi.qrgui.jpa.security.repository.UserRoleRepository;
import it.cambi.qrgui.services.security.UserServiceImpl;

/**
 * @author luca
 *
 */
@Configuration
public class DbInitConfiguration
{

    @Bean
    CommandLineRunner initialize(DbInfoJpaRepository dbInfoRepository, UserServiceImpl userService, RoleRepository roleRespository,
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
