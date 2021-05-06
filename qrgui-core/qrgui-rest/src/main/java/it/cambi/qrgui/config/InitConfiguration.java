/**
 *
 */
package it.cambi.qrgui.config;

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
import it.cambi.qrgui.services.db.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.impl.Temi20Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

/**
 * @author luca
 *
 *         Db init and Security import
 */
@Configuration
@Import({SpringSecurityConfig.class, EmiaDbAppConf.class})
@Profile("!test")
public class InitConfiguration {
    @Bean
    CommandLineRunner initializeApplication(DbInfoJpaRepository dbInfoRepository, UserServiceImpl userService, RoleRepository roleRespository,
                                            UserRoleRepository userRoleRepository, Temi20Service temi20Service) {
        return args -> {

            Temi13DtbInf temi13 = new Temi13DtbInf();
            Temi13DtbInfId id = new Temi13DtbInfId("ORACLE", "TEST");
            temi13.setId(id);

            dbInfoRepository.save(temi13);

            Role role_user = roleRespository.save(Role.builder().name("ROLE_" + R_FEPQRA).build());

            GuiUser new_user = userService.save(GuiUser.builder().username("user@gmail.com").passwordConfirm("1234").build());

            userRoleRepository.save(UserRole.builder().id(UserRoleId.builder().roleId(role_user.getRoleId()).userId(new_user.getUserId()).build()).build());

            Temi20AnaTipCat temi20AnaTipCat = new Temi20AnaTipCat();
            temi20AnaTipCat.setDes("Tip Category Test");
            temi20AnaTipCat.setTipCat(R_FEPQRA);

            temi20Service.merge(temi20AnaTipCat);

        };
    }
}
