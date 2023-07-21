package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.temi.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi14UteCatId;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.util.QueryUtils;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @param <T> Type of the Entity.
 * @param <I> Type of the Primary Key.
 */
@Component
public class Temi15Dao extends TemiGenericDao<Temi15UteQue, Temi15UteQueId>
        implements ITemi15Dao<Temi15UteQue, Temi15UteQueId> {

    private final ITemi14Dao<Temi14UteCat, Temi14UteCatId> categDao;

    private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    public Temi15Dao(
            ITemi14Dao<Temi14UteCat, Temi14UteCatId> categDao,
            ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao) {
        super(Temi15UteQue.class);
        this.categDao = categDao;
        this.queCatAssDao = queCatAssDao;
    }

    @Override
    public Temi15UteQue merge(Temi15UteQue entity) {
        return super.merge(entity);
    }

    @Override
    public List<Object> getAlreadyAssociatedQuery(int cat, Date insCat) {

        List<Temi16QueCatAss> associatedQuery =
                getQueryCategList(cat, insCat, new ArrayList<>());

        List<Tuple> listTemi15 = new ArrayList<>();

        if (!associatedQuery.isEmpty()) {

            /**
             * Una volta trovate le query e le categorie , allora cerco le query che hanno il count = 1
             * raggruppate per categorie. Quelle sono le query che nel caso in cui venga cancellata la
             * categoria possono essere riassociate
             */
            CriteriaQuery<Tuple> criteriaQueryPar =
                    getEntityManager().getCriteriaBuilder().createQuery(Tuple.class);

            Root<Temi15UteQue> rootPar = criteriaQueryPar.from(Temi15UteQue.class);

            criteriaQueryPar.multiselect(
                    rootPar.get("que"),
                    rootPar.get("insQue"),
                    rootPar.get("nam"),
                    getEntityManager().getCriteriaBuilder().count(rootPar.get("que")));

            criteriaQueryPar.groupBy(rootPar.get("que"), rootPar.get("insQue"), rootPar.get("nam"));
            criteriaQueryPar.having(
                    getEntityManager()
                            .getCriteriaBuilder()
                            .equal(
                                    getEntityManager().getCriteriaBuilder().count(rootPar.get("que")), 1));

            rootPar.join("temi16QueCatAsses");

            List<Predicate> predicateList =
                    new ArrayList<>() {
                        {
                            for (Temi16QueCatAss temi15AnaQue : associatedQuery) {

                                Expression<?> parentExpression = rootPar.get("que");
                                Expression<?> insQueExpression = rootPar.get("insQue");

                                Predicate predicate =
                                        getEntityManager()
                                                .getCriteriaBuilder()
                                                .and(
                                                        getEntityManager()
                                                                .getCriteriaBuilder()
                                                                .equal(parentExpression, temi15AnaQue.getId().getQue()),
                                                        getEntityManager()
                                                                .getCriteriaBuilder()
                                                                .equal(insQueExpression, temi15AnaQue.getId().getInsQue()));
                                add(predicate);
                            }
                        }
                    };

            Predicate[] finalPredicateList = new Predicate[predicateList.size()];
            predicateList.toArray(finalPredicateList);

            listTemi15 =
                    getEntityManager()
                            .createQuery(
                                    criteriaQueryPar.where(
                                            getEntityManager().getCriteriaBuilder().or(finalPredicateList)))
                            .getResultList();
        }

        return QueryUtils.getFromTupleListToObjectList(listTemi15);
    }

    /**
     * Cerco le query associate alla categoria corrente e ai parent
     *
     * @param ccat
     * @param npar
     * @param queryList
     * @return
     */
    private List<Temi16QueCatAss> getQueryCategList(
            int ccat, Date insCat, List<Temi16QueCatAss> queryList) {
        CriteriaQuery<Temi16QueCatAss> criteria =
                queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Temi16QueCatAss.class);
        Root<Temi16QueCatAss> rootTemi16 = criteria.from(Temi16QueCatAss.class);

        Expression<Long> cCatExpression = rootTemi16.get("id").get("cat");

        List<Predicate> predicateList = new ArrayList<Predicate>();

        predicateList.add(
                queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cCatExpression, ccat));

        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteria.where(queCatAssDao.getEntityManager().getCriteriaBuilder().or(finalPredicateList));

        List<Temi16QueCatAss> temi16List = queCatAssDao.getEntityListByCriteriaQuery(criteria, null);

        queryList.addAll(temi16List);

        CriteriaQuery<Temi14UteCat> criteria14 =
                queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Temi14UteCat.class);
        Root<Temi14UteCat> rootTemi14 = criteria14.from(Temi14UteCat.class);

        Expression<Integer> cCatExpression14 = rootTemi14.get("par");

        Predicate predicate =
                queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cCatExpression14, ccat);

        Expression<Date> insCarExpression14 = rootTemi14.get("insPar");

        Predicate predicateInsCat =
                queCatAssDao.getEntityManager().getCriteriaBuilder().equal(insCarExpression14, insCat);

        criteria14.where(predicate, predicateInsCat);

        List<Temi14UteCat> temi14List = categDao.getEntityListByCriteriaQuery(criteria14, null);

        for (Temi14UteCat temi14AnaCat : temi14List) {
            getQueryCategList(temi14AnaCat.getCat(), insCat, queryList);
        }

        return queryList;
    }
}
