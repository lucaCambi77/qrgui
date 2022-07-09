/** */
package it.cambi.qrgui.services.emia.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi16Service implements ITemi16Service<Temi16QueCatAss> {
  private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Temi16QueCatAss>> findByCategory(HttpServletRequest request) {
    return queCatAssDao.findByCategory(request);
  }

  @Transactional
  @Override
  public WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16) {
    return queCatAssDao.addQueriesToCateg(temi16);
  }
}
