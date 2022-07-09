/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.security.db.model.GuiUser;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<GuiUser, Integer>
{
    Optional<GuiUser> findByUsername(String username);
}
