package it.cambi.qrgui.taskExecutor.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class CustomRepositoryImpl implements CustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final int pageSize = 15;

    public List<Object> getByNativeQuery(String nativeQuery, Integer page) {
        return Optional.ofNullable(page)
                .map(
                        p ->
                                entityManager
                                        .createNativeQuery(nativeQuery)
                                        .setMaxResults(pageSize)
                                        .setFirstResult((p - 1) * pageSize)
                                        .getResultList())
                .orElse(entityManager.createNativeQuery(nativeQuery).getResultList());
    }

    public List<Object> getByNativeQuery(String nativeQuery, Integer page, Integer pageSize) {
        return Optional.ofNullable(page)
                .map(
                        p ->
                                entityManager
                                        .createNativeQuery(nativeQuery)
                                        .setMaxResults(pageSize)
                                        .setFirstResult((p - 1) * pageSize)
                                        .getResultList())
                .orElse(entityManager.createNativeQuery(nativeQuery).getResultList());
    }

    public Long executeQueryCount(String sqlQuery) {
        Query nativeQuery =
                entityManager.createNativeQuery("select count(*) from (" + sqlQuery + ") x");
        return ((Number) nativeQuery.getSingleResult()).longValue();
    }

}
