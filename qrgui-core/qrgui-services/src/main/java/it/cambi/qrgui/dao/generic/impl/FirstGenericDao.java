package it.cambi.qrgui.dao.generic.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.cambi.qrgui.dao.AbstractDao;

@Service
public class FirstGenericDao extends AbstractDao
{

    public FirstGenericDao()
    {
    }

    @PersistenceContext(unitName = "firstPU")
    @Qualifier(value = "firstEntityManagerFactory")
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

}
