/**
 * 
 */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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