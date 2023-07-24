/** */
package it.cambi.qrgui.security.jpa.repository;

import it.cambi.qrgui.security.db.model.UserRole;
import it.cambi.qrgui.security.db.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luca
 */
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {}
