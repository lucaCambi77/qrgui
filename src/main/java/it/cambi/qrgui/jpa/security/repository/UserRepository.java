/**
 * 
 */
package it.cambi.qrgui.jpa.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.db.security.model.GuiUser;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<GuiUser, Integer>
{
    GuiUser findByUserName(String username);
}
