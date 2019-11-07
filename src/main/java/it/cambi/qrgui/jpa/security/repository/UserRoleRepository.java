/**
 * 
 */
package it.cambi.qrgui.jpa.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.db.security.model.UserRole;
import it.cambi.qrgui.db.security.model.UserRoleId;

/**
 * @author luca
 *
 */
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>
{
}
