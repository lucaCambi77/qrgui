package it.cambi.qrgui.dao.generic.impl;

import it.cambi.qrgui.dao.JpaEntityDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Service
public class TemiGenericDao<T, I> extends JpaEntityDao<T, I>
{

    public TemiGenericDao()
    {
    }

    public TemiGenericDao(Class<T> clazz)
    {
        super(clazz);
    }

    @PersistenceContext(unitName = "emiaPU")
    @Qualifier(value = "emiaEntityManagerFactory")
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

}
