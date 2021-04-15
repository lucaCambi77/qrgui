/**
 * 
 */
package it.cambi.qrgui.jpa.repository;

import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.services.db.model.Temi15UteQueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luca
 *
 */
@Repository
public interface QueryJpaRepository extends JpaRepository<Temi15UteQue, Temi15UteQueId>
{
}
