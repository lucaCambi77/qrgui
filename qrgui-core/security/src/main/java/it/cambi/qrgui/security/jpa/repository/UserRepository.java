/**
 * 
 */
package it.cambi.qrgui.security.jpa.repository;

import it.cambi.qrgui.security.db.model.SecurityUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author luca
 *
 */
public interface UserRepository extends JpaRepository<SecurityUser, Integer>
{
    Optional<SecurityUser> findByUsername(String username);
}
