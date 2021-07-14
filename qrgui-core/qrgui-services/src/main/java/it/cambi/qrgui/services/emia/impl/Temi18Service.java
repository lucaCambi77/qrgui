/** */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi18Service implements ITemi18Service<Temi18RouQue> {

  private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> routineQueryDao;

  @Transactional
  @Override
  public WrappedResponse<Temi18RouQue> merge(Temi18RouQueId temi18Pk) {
    Temi18RouQue temi18 = new Temi18RouQue();
    temi18.setId(temi18Pk);

    Temi18RouQue temi18Out = routineQueryDao.merge(temi18);

    return WrappedResponse.<Temi18RouQue>baseBuilder().entity(temi18Out).count(1).build().setResponse();
  }

  @Transactional
  @Override
  public WrappedResponse<Temi18RouQue> deleteQueRoutineAssoc(Temi18RouQueId temi18Pk) {
    Temi18RouQue temi18 = routineQueryDao.getEntityByPrimaryKey(temi18Pk);

    return WrappedResponse.<Temi18RouQue>baseBuilder()
        .entity(routineQueryDao.delete(temi18))
        .count(1)
        .build()
        .setResponse();
  }
}
