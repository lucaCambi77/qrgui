package it.cambi.qrgui.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.util.QueryUtils;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
public class JpaEntityDao<T, K> extends AbstractDao implements IEntityDao<T, K>
{

    private Class<T> entityClass;

    public JpaEntityDao()
    {
        // TODO Auto-generated constructor stub
    }

    public Class<T> getEntityClass()
    {
        return entityClass;
    }

    public JpaEntityDao(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    public List<T> findAll(List<Order> orderList)
    {
        final CriteriaQuery<T> criteriaQuery = getCriteriaBuilder().createQuery(getEntityClass());

        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(root);

        if (null != orderList)
            criteriaQuery.orderBy(orderList);

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public T merge(T entity)
    {
        return this.getEntityManager().merge(entity);
    }

    public T delete(T entity)
    {
        this.getEntityManager().remove(entity);

        return entity;
    }

    @Override
    public EntityManager getEntityManager()
    {
        return null;
    }

    public List<T> getEntityListByCriteriaQuery(
            CriteriaQuery<T> criteria, Integer pageNumber)
    {

        if (null == pageNumber)
            return getEntityManager().createQuery(criteria)
                    .getResultList();

        return getEntityManager().createQuery(criteria)
                .setMaxResults(getPageSize())
                .setFirstResult((pageNumber - 1) * getPageSize())
                .getResultList();
    }

    public T getEntityByCriteriaQuery(
            CriteriaQuery<T> criteria)
    {

        try
        {
            return getEntityManager().createQuery(criteria)
                    .getSingleResult();
        }
        catch (NoResultException e)
        {

            return null;
        }

    }

    public Integer deleteEntityByCriteria(
            CriteriaDelete<T> criteria)
    {
        return getEntityManager().createQuery(criteria)
                .executeUpdate();
    }

    public Object getTupleByCriteriaQuery(
            CriteriaQuery<Tuple> criteria, Integer pageNumber)
    {

        try
        {
            if (null == pageNumber)
                return QueryUtils.getFromTupleToObject(getEntityManager().createQuery(criteria)
                        .getSingleResult());

            return QueryUtils.getFromTupleToObject(getEntityManager().createQuery(criteria)
                    .setMaxResults(getPageSize())
                    .setFirstResult((pageNumber - 1) * getPageSize()).getSingleResult());

        }
        catch (NoResultException e)
        {

            return null;
        }

    }

    public List<Object> getTupleListByCriteriaQuery(
            CriteriaQuery<Tuple> criteria, Integer pageNumber)
    {

        if (null == pageNumber)
            return QueryUtils
                    .getFromTupleListToObjectList(getEntityManager().createQuery(criteria)
                            .getResultList());

        return QueryUtils
                .getFromTupleListToObjectList(getEntityManager().createQuery(criteria)
                        .setMaxResults(getPageSize())
                        .setFirstResult((pageNumber - 1) * getPageSize())
                        .getResultList());

    }

    public T getEntityByPrimaryKey(K primaryKey)
    {

        return getEntityManager().find(getEntityClass(), primaryKey);
    }
}
