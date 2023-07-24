package it.cambi.qrgui.dao.temi.impl;

import it.cambi.qrgui.dao.JpaEntityDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @param <T> Type of the Entity.
 * @param <I> Type of the Primary Key.
 */
@Service
public class TemiGenericDao<T, I> extends JpaEntityDao<T, I> {
  public TemiGenericDao() {}

  public TemiGenericDao(Class<T> clazz) {
    super(clazz);
  }

  @PersistenceContext(unitName = "emiaPU")
  @Qualifier(value = "emiaEntityManagerFactory")
  private EntityManager entityManager;

  public EntityManager getEntityManager() {
    return this.entityManager;
  }
}
