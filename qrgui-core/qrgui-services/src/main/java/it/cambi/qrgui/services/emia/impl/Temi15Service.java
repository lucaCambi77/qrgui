/** */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.model.*;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import it.cambi.qrgui.util.Messages;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static it.cambi.qrgui.util.IConstants.ERROR_NO_QUERY_ASSOCIATION;

/** @author luca */
@Component
@RequiredArgsConstructor
public class Temi15Service implements ITemi15Service<Temi15UteQue> {

  private final ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

  private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

  private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> queRouAssDao;

  private final ITemi20Dao<Temi20AnaTipCat, String> tipCatDao;

  /**
   * @param key
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<Temi15UteQue> getByPk(Long cQue, Long dateIns) {

    Temi15UteQueId key = new Temi15UteQueId();
    key.setQue(cQue);
    key.setInsQue(new Date(dateIns));

    Temi15UteQue query = queryDao.getEntityByPrimaryKey(key);

    return new WrappedResponse<Temi15UteQue>()
        .setEntity(query == null ? new Temi15UteQue() : query)
        .setCount(query == null ? 0 : 1)
        .setResponse();
  }

  /**
   * @param schema
   * @param type
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public List<Temi15UteQue> getByDb(String schema, String type) {
    return queryDao.getByDb(schema, type);
  }

  @SuppressWarnings("serial")
  @Transactional
  @Override
  public WrappedResponse<Temi15UteQue> postQuery(Temi15UteQue que, String locale) {

    if (null == que.getTemi16QueCatAsses())
      return new WrappedResponse<Temi15UteQue>()
          .setCount(0)
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add(new Messages(locale).getString(ERROR_NO_QUERY_ASSOCIATION));
                }
              })
          .setResponse();

    que.setInsQue(new Date());

    Set<Temi16QueCatAss> temi16Copy =
        new HashSet<Temi16QueCatAss>() {
          {
            addAll(que.getTemi16QueCatAsses());
          }
        };

    que.setTemi16QueCatAsses(null);
    Temi15UteQue newQuery = queryDao.merge(que);

    temi16Copy.forEach(
        temi16 -> {
          temi16.getId().setQue(newQuery.getQue());
          temi16.getId().setInsQue(newQuery.getInsQue());

          queCatAssDao.merge(temi16);
        });

    return new WrappedResponse<Temi15UteQue>().setEntity(newQuery).setCount(1).setResponse();
  }

  /**
   * Cancello tutte le associazioni e la query
   *
   * @param cque
   * @param ccat
   * @return
   */
  @Transactional
  @Override
  public WrappedResponse<Temi15UteQue> deleteQuery(Temi15UteQueId cque) {
    List<Temi16QueCatAss> assocList = getQueCatByQueryId(cque);

    assocList.forEach((temi16) -> queCatAssDao.delete(temi16));

    /* Cancello le associazioni con le routine */
    queRouAssDao.getQueRoutineByQueryId(cque).forEach((temi18) -> queRouAssDao.delete(temi18));

    return new WrappedResponse<Temi15UteQue>()
        .setEntity(queryDao.delete(queryDao.getEntityByPrimaryKey(cque)))
        .setResponse();
  }

  /**
   * @param cque
   * @return
   */
  public List<Temi16QueCatAss> getQueCatByQueryId(Temi15UteQueId cque) {
    CriteriaQuery<Temi16QueCatAss> criteriaQuery =
        queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Temi16QueCatAss.class);
    Root<Temi16QueCatAss> root = criteriaQuery.from(Temi16QueCatAss.class);

    ParameterExpression<Long> cQueParam =
        queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "cQue");

    ParameterExpression<Date> cInsParam =
        queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insQue");

    Expression<?> cqueExpr = root.get("que");
    Expression<?> cinsExpr = root.get("insQue");

    Predicate predicateCQue =
        queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cqueExpr, cQueParam);
    Predicate predicateIns =
        queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cinsExpr, cInsParam);

    List<Temi16QueCatAss> temi16List =
        queCatAssDao
            .getEntityManager()
            .createQuery(criteriaQuery.where(predicateCQue, predicateIns))
            .setParameter("cQue", cque.getQue())
            .setParameter("insQue", cque.getInsQue())
            .getResultList();

    return temi16List;
  }

  @Override
  @SuppressWarnings("serial")
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Temi15UteQue>> getByTipCateg(
      List<String> listAllowedCat, List<Temi15UteQue> queriesIn, HttpServletRequest request) {

    CriteriaQuery<Tuple> criteriaQueryPar =
        queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Tuple.class);

    Root<Temi16QueCatAss> rootPar = criteriaQueryPar.from(Temi16QueCatAss.class);

    Join<Temi14UteCat, Temi16QueCatAss> join14 = rootPar.join("temi14UteCat");

    /**
     * Cerco le query per quel tipo di categoria, escluso quelle che sono già associate alla
     * categoria corrente
     */
    List<Predicate> predicateList =
        new ArrayList<Predicate>() {
          {
            if (null != queriesIn && queriesIn.size() > 0)
              queriesIn.forEach(
                  (temi15) -> {
                    Expression<?> que = rootPar.get("que");

                    Predicate predicateQue =
                        queCatAssDao
                            .getEntityManager()
                            .getCriteriaBuilder()
                            .equal(que, temi15.getQue());

                    Expression<?> insQue = rootPar.get("insQue");

                    Predicate predicateinsQue =
                        queCatAssDao
                            .getEntityManager()
                            .getCriteriaBuilder()
                            .equal(insQue, temi15.getInsQue());

                    add(
                        queCatAssDao
                            .getEntityManager()
                            .getCriteriaBuilder()
                            .not(
                                queCatAssDao
                                    .getEntityManager()
                                    .getCriteriaBuilder()
                                    .and(predicateQue, predicateinsQue)));
                  });

            Expression<String> nPar = join14.get("temi20AnaTipCat").get("tipCat");

            /** Se non cerco una categoria in particolare, metto tutte quelle visibili */
            if (null == listAllowedCat || listAllowedCat.size() == 0) {
              add(nPar.in(tipCatDao.getFunctionsByRequest(request)));

            } else {

              add(nPar.in(listAllowedCat));
            }
          }
        };

    criteriaQueryPar.multiselect(
        rootPar.get("id").get("que"),
        rootPar.get("id").get("insQue"),
        queCatAssDao.getEntityManager().getCriteriaBuilder().count(rootPar.get("id").get("que")));
    criteriaQueryPar.groupBy(rootPar.get("id").get("que"), rootPar.get("id").get("insQue"));

    criteriaQueryPar.having(
        queCatAssDao
            .getEntityManager()
            .getCriteriaBuilder()
            .gt(
                queCatAssDao
                    .getEntityManager()
                    .getCriteriaBuilder()
                    .count(rootPar.get("id").get("que")),
                0));

    Predicate[] finalPredicateList = new Predicate[predicateList.size()];
    predicateList.toArray(finalPredicateList);

    criteriaQueryPar.where(finalPredicateList);

    List<Object> listTemi15 = queCatAssDao.getTupleListByCriteriaQuery(criteriaQueryPar, null);

    if (null != listTemi15 && listTemi15.size() == 0)
      return new WrappedResponse<List<Temi15UteQue>>()
          .setEntity(new ArrayList<>())
          .setCount(0)
          .setResponse();

    List<Long> queries = new ArrayList<Long>();

    for (Object object : listTemi15) {
      queries.add(Long.valueOf(((Object[]) object)[0].toString()));
    }

    CriteriaQuery<Temi15UteQue> criteriaTemi15 =
        queryDao.getEntityManager().getCriteriaBuilder().createQuery(Temi15UteQue.class);
    Root<Temi15UteQue> rootTemi15 = criteriaTemi15.from(Temi15UteQue.class);

    predicateList =
        new ArrayList<Predicate>() {
          {
            listTemi15.forEach(
                (query) -> {
                  Expression<?> que = rootTemi15.get("que");

                  Predicate predicateQue =
                      queCatAssDao
                          .getEntityManager()
                          .getCriteriaBuilder()
                          .equal(que, (Long) ((Object[]) query)[0]);

                  Expression<?> insQue = rootTemi15.get("insQue");

                  Predicate predicateinsQue =
                      queCatAssDao
                          .getEntityManager()
                          .getCriteriaBuilder()
                          .equal(insQue, (Date) ((Object[]) query)[1]);

                  add(
                      queryDao
                          .getEntityManager()
                          .getCriteriaBuilder()
                          .and(predicateQue, predicateinsQue));
                });
          }
        };

    finalPredicateList = new Predicate[predicateList.size()];
    predicateList.toArray(finalPredicateList);

    criteriaTemi15.where(queryDao.getEntityManager().getCriteriaBuilder().or(finalPredicateList));

    List<Temi15UteQue> listTemi15Out = queryDao.getEntityListByCriteriaQuery(criteriaTemi15, null);

    return new WrappedResponse<List<Temi15UteQue>>()
        .setEntity(listTemi15Out)
        .setCount(listTemi15Out.size())
        .setResponse();
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<List<Object>> getAlreadyAssociatedQuery(
      Long cat, Long insCat, String tipCat) {

    List<Object> listTemi15 = queryDao.getAlreadyAssociatedQuery(cat, insCat, tipCat);

    return new WrappedResponse<List<Object>>()
        .setEntity(listTemi15 == null ? new ArrayList<Object>() : listTemi15)
        .setResponse();
  }
}
