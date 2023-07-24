/** */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class Temi18Service implements ITemi18Service<Temi18RouQue> {

  private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> routineQueryDao;

  @Transactional
  @Override
  public Temi18RouQue merge(Temi18RouQueId temi18Pk) {
    Temi18RouQue temi18 = new Temi18RouQue();
    temi18.setId(temi18Pk);

    return routineQueryDao.merge(temi18);
  }

  @Transactional
  @Override
  public Temi18RouQue deleteQueRoutineAssoc(Temi18RouQueId temi18Pk) {
    return routineQueryDao.delete(routineQueryDao.getEntityByPrimaryKey(temi18Pk));
  }

  @Override
  public List<Temi18RouQue> getQueRoutineByQueryId(Temi15UteQueId cque) {
    return routineQueryDao.getQueRoutineByQueryId(cque);
  }
}
