/**
 * 
 */
package it.cambi.qrgui.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.db.model.Role;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import it.cambi.qrgui.security.jpa.repository.RoleRepository;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.UserService;

/**
 * @author luca
 *
 */
@Configuration
public class SecurityTestConfiguration
{

    @Bean
    CommandLineRunner initializeFakeUser(UserService userService, RoleRepository roleRespository,
            UserRoleRepository userRoleRepository)
    {
        return args -> {

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
