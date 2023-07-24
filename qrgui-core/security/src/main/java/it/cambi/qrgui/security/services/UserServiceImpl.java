/** */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.SecurityUser;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author luca
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public SecurityUser save(SecurityUser user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPasswordConfirm()));
    return userRepository.save(user);
  }

  @Override
  public SecurityUser findByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }
}
