package it.cambi.qrgui.dao.api;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import java.util.List;

public interface IEntityDao<T, K>
{

    List<T> findAll(List<Order> orderList);

    Long getSequence(String sequenceName);

    EntityManager getEntityManager();

    T merge(T entity);

    T delete(T entity);

    List<T> getEntityListByCriteriaQuery(CriteriaQuery<T> criteriaQuery, Integer pageNumber);

    Object getTupleByCriteriaQuery(
            CriteriaQuery<Tuple> criteria, Integer pageNumber);

    List<Object> getTupleListByCriteriaQuery(
            CriteriaQuery<Tuple> criteria, Integer pageNumber);

    T getEntityByCriteriaQuery(
            CriteriaQuery<T> criteria);

    T getEntityByPrimaryKey(K primaryKey);

    Integer deleteEntityByCriteria(CriteriaDelete<T> criteria);
}
