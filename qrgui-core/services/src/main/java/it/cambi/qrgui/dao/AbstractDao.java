package it.cambi.qrgui.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** */
public abstract class AbstractDao {
  private static final Logger log = LoggerFactory.getLogger(AbstractDao.class);

  private int pageSize = 15;

  public abstract EntityManager getEntityManager();

  public CriteriaBuilder getCriteriaBuilder() {
    return getEntityManager().getCriteriaBuilder();
  }

  public int getPageSize() {
    return pageSize;
  }

  public List<Object> getByNativeQuery(String nativeQuery, Integer page) {
    return Optional.ofNullable(page)
        .map(
            p ->
                getEntityManager()
                    .createNativeQuery(nativeQuery)
                    .setMaxResults(getPageSize())
                    .setFirstResult((p - 1) * getPageSize())
                    .getResultList())
        .orElse(getEntityManager().createNativeQuery(nativeQuery).getResultList());
  }

  public List<Object> getByNativeQuery(String nativeQuery, Integer page, Integer pageSize) {
    return Optional.ofNullable(page)
        .map(
            p ->
                getEntityManager()
                    .createNativeQuery(nativeQuery)
                    .setMaxResults(pageSize)
                    .setFirstResult((p - 1) * pageSize)
                    .getResultList())
        .orElse(getEntityManager().createNativeQuery(nativeQuery).getResultList());
  }

  public Long executeQueryCount(String sqlQuery) {
    Query nativeQuery =
        getEntityManager().createNativeQuery("select count(*) from (" + sqlQuery + ") x");
    return ((Number) nativeQuery.getSingleResult()).longValue();
  }

  public Long getSequence(String sequenceName) {

    log.info("Creo la sequence " + sequenceName);
    Query q = getEntityManager().createNativeQuery(sequenceName);

    return ((BigDecimal) q.getSingleResult()).longValue();
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
