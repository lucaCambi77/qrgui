/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import it.cambi.qrgui.security.db.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luca
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
