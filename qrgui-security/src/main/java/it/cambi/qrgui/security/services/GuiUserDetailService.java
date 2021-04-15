/**
 *
 */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @author luca
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GuiUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("... attempting to authenticate user " + userName);

        GuiUser user = userRepository.findByUserName(userName);

        if (user == null)
            throw new UsernameNotFoundException(userName);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (UserRole role : user.getUserRoles()) {
            log.info("User " + userName + " has role " + role.getRole().getName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
        }
        return new User(user.getUserName(), user.getPassword(), grantedAuthorities);

    }

}
