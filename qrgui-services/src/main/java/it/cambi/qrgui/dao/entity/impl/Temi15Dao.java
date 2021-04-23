package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.generic.impl.TemiGenericDao;
import it.cambi.qrgui.services.db.model.*;
import it.cambi.qrgui.services.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Component
public class Temi15Dao extends TemiGenericDao<Temi15UteQue, Temi15UteQueId> implements ITemi15Dao<Temi15UteQue, Temi15UteQueId>
{

    @Autowired
    private ITemi14Dao<Temi14UteCat, Temi14UteCatId> categDao;

    @Autowired
    private ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    public Temi15Dao()
    {
        super(Temi15UteQue.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Temi15UteQue merge(Temi15UteQue entity)
    {
        return super.merge(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Temi15UteQue deleteByPk(Temi15UteQueId pk)
    {

        Temi15UteQue query = getEntityByPrimaryKey(pk);

        return super.delete(query);
    }

    @Override
    public List<Temi15UteQue> getByDb(String schema, String type)
    {
        CriteriaQuery<Temi15UteQue> criteriaQuery = getCriteriaBuilder().createQuery(Temi15UteQue.class);
        Root<Temi15UteQue> root = criteriaQuery.from(Temi15UteQue.class);

        ParameterExpression<Temi15UteQue> dbParam = getEntityManager().getCriteriaBuilder().parameter(Temi15UteQue.class, "id");

        Expression<?> dbExpr = root.get("temi13DtbInf").get("id");
        Temi13DtbInfId pk = new Temi13DtbInfId();
        pk.setSch(schema);
        pk.setTyp(type);

        List<Predicate> predicateList = new ArrayList<>();

        Predicate predicate1 = getEntityManager().getCriteriaBuilder().equal(dbExpr, dbParam);

        /* caso AND */
        predicateList.add(predicate1);
        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);
        criteriaQuery.where(finalPredicateList);
        criteriaQuery.orderBy(getCriteriaBuilder().asc(root.get("id").get("que")));

        return getEntityManager().createQuery(criteriaQuery).setParameter("id", pk)
                .getResultList();

    }

    @Override
    public List<Object> getAlreadyAssociatedQuery(Long cat, Long insCat, String tipCat)
    {
        Temi14UteCatId id = new Temi14UteCatId();
        id.setCat(cat);
        id.setInsCat(new Date(insCat));

        List<Temi16QueCatAss> associatedQuery = getQueryCategList(id, new ArrayList<Temi16QueCatAss>());

        List<Tuple> listTemi15 = null;

        if (associatedQuery.size() > 0)
        {

            /**
             * Una volta trovate le query e le categorie , allora cerco le query che hanno il count = 1 raggruppate per categorie. Quelle sono le
             * query che nel caso in cui venga cancellata la categoria possono essere riassociate
             */
            CriteriaQuery<Tuple> criteriaQueryPar = getEntityManager().getCriteriaBuilder()
                    .createQuery(Tuple.class);

            Root<Temi15UteQue> rootPar = criteriaQueryPar.from(Temi15UteQue.class);

            criteriaQueryPar.multiselect(rootPar.get("id"), rootPar.get("nam"),
                    getEntityManager().getCriteriaBuilder().count(rootPar.get("id").get("que")));

            criteriaQueryPar.groupBy(rootPar.get("id"), rootPar.get("nam"));
            criteriaQueryPar.having(getEntityManager().getCriteriaBuilder()
                    .equal(getEntityManager().getCriteriaBuilder().count(rootPar.get("id").get("que")), 1));

            rootPar.join("temi16QueCatAsses");

            @SuppressWarnings("serial")
            List<Predicate> predicateList = new ArrayList<Predicate>()
            {
                {

                    for (Temi16QueCatAss temi15AnaQue : associatedQuery)
                    {

                        Expression<?> parentExpression = rootPar.get("id").get("que");
                        Expression<?> insQueExpression = rootPar.get("id").get("insQue");

                        Predicate predicate = getEntityManager().getCriteriaBuilder().and(
                                getEntityManager().getCriteriaBuilder().equal(parentExpression, temi15AnaQue.getId().getQue()),
                                getEntityManager().getCriteriaBuilder().equal(insQueExpression, temi15AnaQue.getId().getInsQue()));
                        add(predicate);

                    }

                }
            };

            Predicate[] finalPredicateList = new Predicate[predicateList.size()];
            predicateList.toArray(finalPredicateList);

            listTemi15 = getEntityManager().createQuery(criteriaQueryPar.where(getEntityManager().getCriteriaBuilder().or(finalPredicateList)))
                    .getResultList();
        }

        return QueryUtils.getFromTupleListToObjectList(listTemi15 == null ? new ArrayList<Tuple>() : listTemi15);
    }

    /**
     * 
     * Cerco le query associate alla categoria corrente e ai parent
     * 
     * @param ccat
     * @param npar
     * @param queryList
     * @return
     */
    private List<Temi16QueCatAss> getQueryCategList(Temi14UteCatId ccat, List<Temi16QueCatAss> queryList)
    {
        CriteriaQuery<Temi16QueCatAss> criteria = queCatAssDao.getEntityManager().getCriteriaBuilder()
                .createQuery(Temi16QueCatAss.class);
        Root<Temi16QueCatAss> rootTemi16 = criteria.from(Temi16QueCatAss.class);

        Expression<Long> cCatExpression = rootTemi16.get("id").get("cat");

        List<Predicate> predicateList = new ArrayList<Predicate>();

        predicateList.add(queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cCatExpression, ccat.getCat()));

        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteria.where(
                queCatAssDao.getEntityManager().getCriteriaBuilder().or(finalPredicateList));

        List<Temi16QueCatAss> temi16List = queCatAssDao.getEntityListByCriteriaQuery(criteria, null);

        for (Temi16QueCatAss temi16 : temi16List)
        {

            queryList.add(temi16);

        }

        CriteriaQuery<Temi14UteCat> criteria14 = queCatAssDao.getEntityManager().getCriteriaBuilder()
                .createQuery(Temi14UteCat.class);
        Root<Temi14UteCat> rootTemi14 = criteria14.from(Temi14UteCat.class);

        Expression<Integer> cCatExpression14 = rootTemi14.get("par");

        Predicate predicate = queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cCatExpression14, ccat.getCat());

        Expression<Date> insCarExpression14 = rootTemi14.get("insPar");

        Predicate predicateInsCat = queCatAssDao.getEntityManager().getCriteriaBuilder().equal(insCarExpression14, ccat.getInsCat());

        criteria14.where(predicate, predicateInsCat);

        List<Temi14UteCat> temi14List = categDao.getEntityListByCriteriaQuery(criteria14, null);

        for (Temi14UteCat temi14AnaCat : temi14List)
        {
            getQueryCategList(temi14AnaCat.getId(), queryList);

        }

        return queryList;
    }
}
