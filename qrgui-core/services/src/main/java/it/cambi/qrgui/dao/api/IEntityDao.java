package it.cambi.qrgui.dao.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import java.util.List;

public interface IEntityDao<T, K> {

  void save(T entity);

  List<T> findAll(List<Order> orderList);

  Long getSequence(String sequenceName);

  EntityManager getEntityManager();

  T merge(T entity);

  T delete(T entity);

  List<T> getEntityListByCriteriaQuery(CriteriaQuery<T> criteriaQuery, Integer pageNumber);

  Object getTupleByCriteriaQuery(CriteriaQuery<Tuple> criteria, Integer pageNumber);

  List<Object> getTupleListByCriteriaQuery(CriteriaQuery<Tuple> criteria, Integer pageNumber);

  T getEntityByCriteriaQuery(CriteriaQuery<T> criteria);

  T getEntityByPrimaryKey(K primaryKey);

  void deleteByEntityId(K id);
}
