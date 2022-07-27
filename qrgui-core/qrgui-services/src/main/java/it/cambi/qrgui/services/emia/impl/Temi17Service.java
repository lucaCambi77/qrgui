/** */
package it.cambi.qrgui.services.emia.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.cambi.qrgui.dao.entity.api.ITemi17Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi17Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi17Service implements ITemi17Service<Temi17UteRou> {

  private final ITemi17Dao<Temi17UteRou, Temi17UteRouId> routineDao;

  private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> queRoutineDao;

  private final ITemi20Dao<Temi20AnaTipCat, String> tipCatDao;

  private final WrappedResponse<Temi17UteRou> response;

  private final WrappedResponse<List<Temi17UteRou>> responseList;

  /**
   * @param temi17
   * @return
   */
  @Transactional
  @Override
  public WrappedResponse<Temi17UteRou> merge(Temi17UteRou temi17) {
    if (null == temi17.getInsRou()) temi17.setInsRou(new Date());

    Temi17UteRou temi17Out = routineDao.merge(temi17);

    return response.toBuilder().entity(temi17Out).count(1).build().setResponse();
  }

  /**
   * @param crou
   * @return
   */
  @Transactional
  @Override
  public WrappedResponse<Temi17UteRou> delete(Temi17UteRouId crou) {
    CriteriaQuery<Temi17UteRou> criteriaQueryPk =
        routineDao.getEntityManager().getCriteriaBuilder().createQuery(Temi17UteRou.class);
    Root<Temi17UteRou> rootPk = criteriaQueryPk.from(Temi17UteRou.class);

    Predicate predicateRouPk =
        routineDao
            .getEntityManager()
            .getCriteriaBuilder()
            .equal(
                rootPk.get("rou"),
                routineDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "crou"));

    Predicate predicateInsPk =
        routineDao
            .getEntityManager()
            .getCriteriaBuilder()
            .equal(
                rootPk.get("insRou"),
                routineDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insRou"));

    Temi17UteRou temi17 =
        routineDao
            .getEntityManager()
            .createQuery(criteriaQueryPk.where(predicateRouPk, predicateInsPk))
            .setParameter("crou", crou.getRou())
            .setParameter("insRou", crou.getInsRou())
            .getSingleResult();

    /** Cancello le associazioni alle query */
    CriteriaQuery<Temi18RouQue> criteriaQuery =
        queRoutineDao.getEntityManager().getCriteriaBuilder().createQuery(Temi18RouQue.class);
    Root<Temi18RouQue> root = criteriaQuery.from(Temi18RouQue.class);

    ParameterExpression<Long> cRouParam =
        queRoutineDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "crou");

    Expression<?> cRouExpr = root.get("id").get("rou");

    Predicate predicate =
        queRoutineDao.getEntityManager().getCriteriaBuilder().equal(cRouExpr, cRouParam);

    ParameterExpression<Date> cRouInsParam =
        queRoutineDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insRou");

    Expression<?> cInsExpr = root.get("id").get("insRou");

    Predicate predicateIns =
        queRoutineDao.getEntityManager().getCriteriaBuilder().equal(cInsExpr, cRouInsParam);

    List<Temi18RouQue> listTemi18 =
        queRoutineDao
            .getEntityManager()
            .createQuery(criteriaQuery.where(predicate, predicateIns))
            .setParameter("crou", crou.getRou())
            .setParameter("insRou", crou.getInsRou())
            .getResultList();

    listTemi18.forEach(temi18RouQue -> queRoutineDao.delete(temi18RouQue));

    return response.toBuilder().entity(routineDao.delete(temi17)).count(1).build().setResponse();
  }

  /**
   * @param request
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Temi17UteRou>> findAll(HttpServletRequest request) {
    List<String> listAllowedString = tipCatDao.getFunctionsByRequest(request);

    if (null != listAllowedString && listAllowedString.size() > 0) {
      CriteriaQuery<Temi17UteRou> criteriaQuery =
          routineDao.getEntityManager().getCriteriaBuilder().createQuery(Temi17UteRou.class);
      Root<Temi17UteRou> root = criteriaQuery.from(Temi17UteRou.class);

      criteriaQuery.orderBy(
          routineDao.getEntityManager().getCriteriaBuilder().asc(root.get("rou")));

      List<Temi17UteRou> temi17List = routineDao.getEntityListByCriteriaQuery(criteriaQuery, null);

      return responseList.toBuilder()
          .entity(temi17List)
          .count(temi17List.size())
          .build()
          .setResponse();
    }

    return responseList.toBuilder()
        .entity(new ArrayList<Temi17UteRou>())
        .count(0)
        .build()
        .setResponse();
  }
}
