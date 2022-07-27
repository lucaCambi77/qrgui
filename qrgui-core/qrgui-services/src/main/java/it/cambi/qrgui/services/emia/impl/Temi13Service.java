/** */
package it.cambi.qrgui.services.emia.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.cambi.qrgui.dao.entity.api.ITemi13Dao;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi13DtbInfId;
import it.cambi.qrgui.services.emia.api.ITemi13Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi13Service implements ITemi13Service<Temi13DtbInf> {

  private final ITemi13Dao<Temi13DtbInf, Temi13DtbInfId> databaseInfoDao;

  private final WrappedResponse<List<Temi13DtbInf>> response;

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Temi13DtbInf>> findAll() {
    return response.toBuilder()
        .entity(databaseInfoDao.findAll(null))
        .build()
        .setResponse();
  }
}
