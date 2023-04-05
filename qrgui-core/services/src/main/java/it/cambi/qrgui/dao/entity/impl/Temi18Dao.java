package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.dao.temi.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * @param <T> Type of the Entity.
 * @param <I> Type of the Primary Key.
 */
@Component
public class Temi18Dao extends TemiGenericDao<Temi18RouQue, Temi18RouQueId>
    implements ITemi18Dao<Temi18RouQue, Temi18RouQueId> {

  public Temi18Dao() {
    super(Temi18RouQue.class);
  }

  @Override
  public List<Temi18RouQue> getQueRoutineByQueryId(Temi15UteQueId cque) {
    CriteriaQuery<Temi18RouQue> criteriaQuery =
        getEntityManager().getCriteriaBuilder().createQuery(Temi18RouQue.class);
    Root<Temi18RouQue> root = criteriaQuery.from(Temi18RouQue.class);

    ParameterExpression<Long> cQueParam =
        getEntityManager().getCriteriaBuilder().parameter(Long.class, "cQue");

    ParameterExpression<Date> cInsParam =
        getEntityManager().getCriteriaBuilder().parameter(Date.class, "insQue");

    Expression<?> cqueExpr = root.get("id").get("que");
    Expression<?> cinsExpr = root.get("id").get("insQue");

    Predicate predicateCQue = getEntityManager().getCriteriaBuilder().equal(cqueExpr, cQueParam);
    Predicate predicateIns = getEntityManager().getCriteriaBuilder().equal(cinsExpr, cInsParam);

    return getEntityManager()
        .createQuery(criteriaQuery.where(predicateCQue, predicateIns))
        .setParameter("cQue", cque.getQue())
        .setParameter("insQue", cque.getInsQue())
        .getResultList();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Temi18RouQue merge(Temi18RouQue entity) {
    return super.merge(entity);
  }
}
