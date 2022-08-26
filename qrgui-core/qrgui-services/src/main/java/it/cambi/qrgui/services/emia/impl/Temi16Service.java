/** */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi16Service implements ITemi16Service<Temi16QueCatAss> {
  private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

  private final WrappedResponse<List<Temi16QueCatAss>> responseList;
  private final WrappedResponse<Integer> responseInteger;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Temi16QueCatAss>> findByCategory(HttpServletRequest request) {

    List<Temi16QueCatAss> temi16QueCatAss = queCatAssDao.findByCategory(request);

    return responseList.toBuilder()
        .entity(temi16QueCatAss)
        .count(temi16QueCatAss.size())
        .build()
        .setResponse();
  }

  @Transactional
  @Override
  public WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16) {
    return responseInteger.toBuilder()
        .count(queCatAssDao.addQueriesToCateg(temi16))
        .build()
        .setResponse();
  }
}
