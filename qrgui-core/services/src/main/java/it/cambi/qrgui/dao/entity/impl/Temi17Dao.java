package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi17Dao;
import it.cambi.qrgui.dao.temi.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Temi17Dao extends TemiGenericDao<Temi17UteRou, Temi17UteRouId>
    implements ITemi17Dao<Temi17UteRou, Temi17UteRouId> {

  public Temi17Dao() {
    super(Temi17UteRou.class);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Temi17UteRou merge(Temi17UteRou entity) {
    return super.merge(entity);
  }
}
