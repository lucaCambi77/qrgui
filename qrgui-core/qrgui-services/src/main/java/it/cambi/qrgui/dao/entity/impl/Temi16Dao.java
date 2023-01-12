package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.temi.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @param <T> Type of the Entity.
 * @param <I> Type of the Primary Key.
 */
@Component
public class Temi16Dao extends TemiGenericDao<Temi16QueCatAss, Temi16QueCatAssId>
    implements ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> {

  public Temi16Dao() {
    super(Temi16QueCatAss.class);
  }

  @Override
  public List<Temi16QueCatAss> findByCategory(List<String> functions) {
    CriteriaQuery<Temi16QueCatAss> criteriaTemi16 =
        getEntityManager().getCriteriaBuilder().createQuery(Temi16QueCatAss.class);

    Root<Temi16QueCatAss> root = criteriaTemi16.from(Temi16QueCatAss.class);

    Path<String> anaTipCatPath = root.join("temi14UteCat").get("temi20AnaTipCat").get("tipCat");

    Predicate predicateAnaTipCat = anaTipCatPath.in(functions);

    if (null != functions && functions.size() > 0) criteriaTemi16.where(predicateAnaTipCat);

    return getEntityListByCriteriaQuery(criteriaTemi16, null);
  }

  @Override
  public Integer addQueriesToCateg(List<Temi16QueCatAss> temi16) {
    temi16.forEach(this::merge);
    return temi16.size();
  }
}
