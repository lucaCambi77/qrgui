package it.cambi.qrgui.dao.generic.impl;

import it.cambi.qrgui.dao.AbstractDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class FirstGenericDao extends AbstractDao
{

    public FirstGenericDao()
    {
    }

    @PersistenceContext(name = "firstTransactionManager")
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

}
