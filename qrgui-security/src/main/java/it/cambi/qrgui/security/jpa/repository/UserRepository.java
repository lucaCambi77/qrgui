/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import it.cambi.qrgui.security.db.model.GuiUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<GuiUser, Integer>
{
    GuiUser findByUserName(String username);
}
