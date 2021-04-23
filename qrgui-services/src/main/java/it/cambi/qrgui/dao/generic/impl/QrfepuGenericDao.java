package it.cambi.qrgui.dao.generic.impl;

import it.cambi.qrgui.dao.AbstractDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class QrfepuGenericDao extends AbstractDao
{

    public QrfepuGenericDao()
    {
    }

    // TODO settare altre PU
    @PersistenceContext()
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

}
