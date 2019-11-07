/**
 * 
 */
package it.cambi.qrgui.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.cambi.qrgui.db.security.model.GuiUser;
import it.cambi.qrgui.jpa.security.repository.UserRepository;

/**
 * @author luca
 *
 */
@Service
public class UserServiceImpl implements UserService
{

    private @Autowired UserRepository userRepository;

    private @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public GuiUser save(GuiUser user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPasswordConfirm()));
        return userRepository.save(user);
    }

    @Override
    public GuiUser findByUsername(String username)
    {
        return userRepository.findByUserName(username);
    }
}