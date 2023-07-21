/**
 *
 */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.SecurityUser;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
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
    @Transactional(value = "securityTransactionManager", readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("... attempting to authenticate user " + userName);

        SecurityUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        user.getUserRoles().forEach(role -> {
            log.info("User " + userName + " has role " + role.getRole().getName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
        });

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
