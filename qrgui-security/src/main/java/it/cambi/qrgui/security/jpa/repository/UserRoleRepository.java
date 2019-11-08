/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;

/**
 * @author luca
 *
 */
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>
{
}
