/**
 *
 */
package it.cambi.qrgui.security.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author luca
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        return Optional.of(userDetails).filter(ud -> ud instanceof UserDetails)
                .map(ud -> ((UserDetails) ud).getUsername())
                .orElse(null);
    }

    @Override
    public boolean autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, password,
              userDetails.getAuthorities()));
            log.debug(String.format("Auto login %s successfully!", username));

            return true;
        }

        return false;
    }
}
