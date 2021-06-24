/**
 * 
 */
package it.cambi.qrgui.jpa.repository;

import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi13DtbInfId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luca
 *
 */
@Repository
public interface DbInfoJpaRepository extends JpaRepository<Temi13DtbInf, Temi13DtbInfId>
{
}
