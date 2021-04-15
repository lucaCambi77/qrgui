/**
 * 
 */
package it.cambi.qrgui.jpa.repository;

import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.services.db.model.Temi15UteQueId;
import org.springframework.data.repository.Repository;

/**
 * @author luca
 *
 */

public interface QueryRepository extends Repository<Temi15UteQue, Temi15UteQueId>
{
}
