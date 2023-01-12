/** */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class Temi16Service implements ITemi16Service<Temi16QueCatAss> {
  private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public List<Temi16QueCatAss> findByCategory(List<String> functions) {
    return queCatAssDao.findByCategory(functions);
  }

  @Transactional
  @Override
  public Integer addQueriesToCateg(List<Temi16QueCatAss> temi16) {
    return queCatAssDao.addQueriesToCateg(temi16);
  }
}
