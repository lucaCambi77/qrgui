/**
 * 
 */
package it.cambi.qrgui.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;

/**
 * @author luca
 *
 */
@Repository
public interface QueryJpaRepository extends JpaRepository<Temi15UteQue, Temi15UteQueId>
{
}
