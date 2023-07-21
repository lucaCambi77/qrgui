/**
 *
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi17Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi17Service;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class Temi17Service implements ITemi17Service<Temi17UteRou> {

    private final ITemi17Dao<Temi17UteRou, Temi17UteRouId> routineDao;

    private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> queRoutineDao;

    /**
     * @param temi17
     * @return
     */
    @Transactional
    @Override
    public Temi17UteRou merge(Temi17UteRou temi17) {
        if (null == temi17.getInsRou()) temi17.setInsRou(new Date());

        return routineDao.merge(temi17);
    }

    /**
     * @param crou
     * @return
     */
    @Transactional
    @Override
    public void delete(Temi17UteRouId crou) {
        CriteriaQuery<Temi17UteRou> criteriaQueryPk =
                routineDao.getEntityManager().getCriteriaBuilder().createQuery(Temi17UteRou.class);
        Root<Temi17UteRou> rootPk = criteriaQueryPk.from(Temi17UteRou.class);

        Predicate predicateRouPk =
                routineDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .equal(
                                rootPk.get("rou"),
                                routineDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "crou"));

        Predicate predicateInsPk =
                routineDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .equal(
                                rootPk.get("insRou"),
                                routineDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insRou"));

        Temi17UteRou temi17 =
                routineDao
                        .getEntityManager()
                        .createQuery(criteriaQueryPk.where(predicateRouPk, predicateInsPk))
                        .setParameter("crou", crou.getRou())
                        .setParameter("insRou", crou.getInsRou())
                        .getSingleResult();

        /** Cancello le associazioni alle query */
        CriteriaQuery<Temi18RouQue> criteriaQuery =
                queRoutineDao.getEntityManager().getCriteriaBuilder().createQuery(Temi18RouQue.class);
        Root<Temi18RouQue> root = criteriaQuery.from(Temi18RouQue.class);

        ParameterExpression<Long> cRouParam =
                queRoutineDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "crou");

        Expression<?> cRouExpr = root.get("id").get("rou");

        Predicate predicate =
                queRoutineDao.getEntityManager().getCriteriaBuilder().equal(cRouExpr, cRouParam);

        ParameterExpression<Date> cRouInsParam =
                queRoutineDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insRou");

        Expression<?> cInsExpr = root.get("id").get("insRou");

        Predicate predicateIns =
                queRoutineDao.getEntityManager().getCriteriaBuilder().equal(cInsExpr, cRouInsParam);

        List<Temi18RouQue> listTemi18 =
                queRoutineDao
                        .getEntityManager()
                        .createQuery(criteriaQuery.where(predicate, predicateIns))
                        .setParameter("crou", crou.getRou())
                        .setParameter("insRou", crou.getInsRou())
                        .getResultList();

        listTemi18.forEach(queRoutineDao::delete);

        routineDao.delete(temi17);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Temi17UteRou> findAll(List<String> functions) {

        return Optional.of(functions)
                .map(
                        obj -> {
                            CriteriaQuery<Temi17UteRou> criteriaQuery =
                                    routineDao
                                            .getEntityManager()
                                            .getCriteriaBuilder()
                                            .createQuery(Temi17UteRou.class);
                            Root<Temi17UteRou> root = criteriaQuery.from(Temi17UteRou.class);

                            criteriaQuery.orderBy(
                                    routineDao.getEntityManager().getCriteriaBuilder().asc(root.get("rou")));

                            return routineDao.getEntityListByCriteriaQuery(criteriaQuery, null);
                        })
                .orElseGet(List::of);
    }
}
