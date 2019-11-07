/**
 * 
 */
package it.cambi.qrgui.jpa.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.cambi.qrgui.db.security.model.Role;

/**
 * @author luca
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
