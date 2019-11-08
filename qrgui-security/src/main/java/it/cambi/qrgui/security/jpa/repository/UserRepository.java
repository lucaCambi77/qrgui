/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.security.db.model.GuiUser;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<GuiUser, Integer>
{
    GuiUser findByUserName(String username);
}
