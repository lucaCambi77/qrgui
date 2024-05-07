package it.cambi.qrgui.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

/** */
public abstract class AbstractDao {
  public abstract EntityManager getEntityManager();

  public CriteriaBuilder getCriteriaBuilder() {
    return getEntityManager().getCriteriaBuilder();
  }

  public int getPageSize() {
    return 15;
  }
}
