/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import it.cambi.qrgui.security.db.model.GuiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<GuiUser, Integer>
{
    Optional<GuiUser> findByUsername(String username);
}
