/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.security.db.model.Role;

/**
 * @author luca
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
