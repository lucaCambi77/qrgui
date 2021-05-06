package it.cambi.qrgui.dao.generic.impl;

import it.cambi.qrgui.dao.JpaEntityDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Component
public class TemiGenericDao<T, I> extends JpaEntityDao<T, I>
{

    public TemiGenericDao()
    {
    }

    public TemiGenericDao(Class<T> clazz)
    {
        super(clazz);
    }

    @PersistenceContext(name = "emiaTransactionManager")
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

}
