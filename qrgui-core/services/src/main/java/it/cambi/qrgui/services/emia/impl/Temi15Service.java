/**
 *
 */
package it.cambi.qrgui.services.emia.impl;

import static it.cambi.qrgui.util.Constants.ERROR_NO_QUERY_ASSOCIATION;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import it.cambi.qrgui.util.Messages;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class Temi15Service implements ITemi15Service<Temi15UteQue> {

    private final ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

    private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> queRouAssDao;

    @Transactional
    @Override
    public Temi15UteQue postQuery(UteQueDto uteQueDto, String locale) {

        if (null == uteQueDto.temi16QueCatAsses()) {
            throw new IllegalArgumentException(
                    new Messages(locale).getString(ERROR_NO_QUERY_ASSOCIATION));
        }

        Temi15UteQue temi15UteQue = new Temi15UteQue();
        temi15UteQue.setTenant(uteQueDto.tenant());
        temi15UteQue.setInsQue(uteQueDto.insQue() == null ? new Date() : uteQueDto.insQue());
        temi15UteQue.setQue(uteQueDto.que());
        temi15UteQue.setNam(uteQueDto.nam());
        temi15UteQue.setJson(uteQueDto.json());

        Temi15UteQue newQuery = queryDao.merge(temi15UteQue);

        if (uteQueDto.que() == 0) {
            uteQueDto.temi16QueCatAsses().forEach(
                    temi16 -> {

                        Temi16QueCatAss temi16QueCatAss = new Temi16QueCatAss();
                        Temi16QueCatAssId temi16QueCatAssId = new Temi16QueCatAssId();
                        temi16QueCatAssId.setQue(newQuery.getQue());
                        temi16QueCatAssId.setInsQue(newQuery.getInsQue());
                        temi16QueCatAssId.setCat(temi16.id().cat());
                        temi16QueCatAssId.setInsCat(temi16.id().insCat());

                        temi16QueCatAss.setId(temi16QueCatAssId);

                        queCatAssDao.merge(temi16QueCatAss);
                    });
        }

        return newQuery;
    }

    /**
     * Cancello tutte le associazioni e la query
     *
     * @param cque
     * @return
     */
    @Transactional
    @Override
    public Temi15UteQue deleteQuery(Temi15UteQueId cque) {
        List<Temi16QueCatAss> assocList = getQueCatByQueryId(cque);

        assocList.forEach(queCatAssDao::delete);

        /* Cancello le associazioni con le routine */
        queRouAssDao.getQueRoutineByQueryId(cque).forEach(queRouAssDao::delete);

        return queryDao.getEntityByPrimaryKey(cque);
    }

    /**
     * @param cque
     * @return
     */
    public List<Temi16QueCatAss> getQueCatByQueryId(Temi15UteQueId cque) {
        CriteriaQuery<Temi16QueCatAss> criteriaQuery =
                queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Temi16QueCatAss.class);
        Root<Temi16QueCatAss> root = criteriaQuery.from(Temi16QueCatAss.class);

        ParameterExpression<Long> cQueParam =
                queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "cQue");

        ParameterExpression<Date> cInsParam =
                queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insQue");

        Expression<?> cqueExpr = root.get("id").get("que");
        Expression<?> cinsExpr = root.get("id").get("insQue");

        Predicate predicateCQue =
                queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cqueExpr, cQueParam);
        Predicate predicateIns =
                queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cinsExpr, cInsParam);

        return queCatAssDao
                .getEntityManager()
                .createQuery(criteriaQuery.where(predicateCQue, predicateIns))
                .setParameter("cQue", cque.getQue())
                .setParameter("insQue", cque.getInsQue())
                .getResultList();
    }

    @Override
    @SuppressWarnings("serial")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Temi15UteQue> getByTipCateg(
            List<String> listAllowedCat, List<Temi15UteQue> queriesIn, List<String> functions) {

        CriteriaQuery<Tuple> criteriaQueryPar =
                queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Tuple.class);

        Root<Temi16QueCatAss> rootPar = criteriaQueryPar.from(Temi16QueCatAss.class);

        Join<Temi14UteCat, Temi16QueCatAss> join14 = rootPar.join("temi14UteCat");

        /**
         * Cerco le query per quel tipo di categoria, escluso quelle che sono già associate alla
         * categoria corrente
         */
        List<Predicate> predicateList =
                new ArrayList<>() {
                    {
                        if (null != queriesIn && queriesIn.size() > 0) {

                            queriesIn.forEach(
                                    (temi15) -> {
                                        Expression<?> que = rootPar.get("que");

                                        Predicate predicateQue =
                                                queCatAssDao
                                                        .getEntityManager()
                                                        .getCriteriaBuilder()
                                                        .equal(que, temi15.getQue());

                                        Expression<?> insQue = rootPar.get("insQue");

                                        Predicate predicateinsQue =
                                                queCatAssDao
                                                        .getEntityManager()
                                                        .getCriteriaBuilder()
                                                        .equal(insQue, temi15.getInsQue());

                                        add(
                                                queCatAssDao
                                                        .getEntityManager()
                                                        .getCriteriaBuilder()
                                                        .not(
                                                                queCatAssDao
                                                                        .getEntityManager()
                                                                        .getCriteriaBuilder()
                                                                        .and(predicateQue, predicateinsQue)));
                                    });
                        }

                        Expression<String> nPar = join14.get("temi20AnaTipCat").get("tipCat");

                        /** Se non cerco una categoria in particolare, metto tutte quelle visibili */
                        if (null == listAllowedCat || listAllowedCat.size() == 0) {
                            add(nPar.in(functions));

                        } else {

                            add(nPar.in(listAllowedCat));
                        }
                    }
                };

        criteriaQueryPar.multiselect(
                rootPar.get("id").get("que"),
                rootPar.get("id").get("insQue"),
                queCatAssDao.getEntityManager().getCriteriaBuilder().count(rootPar.get("id").get("que")));
        criteriaQueryPar.groupBy(rootPar.get("id").get("que"), rootPar.get("id").get("insQue"));

        criteriaQueryPar.having(
                queCatAssDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .gt(
                                queCatAssDao
                                        .getEntityManager()
                                        .getCriteriaBuilder()
                                        .count(rootPar.get("id").get("que")),
                                0));

        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteriaQueryPar.where(finalPredicateList);

        List<Object> listTemi15 = queCatAssDao.getTupleListByCriteriaQuery(criteriaQueryPar, null);

        if (null != listTemi15 && listTemi15.size() == 0) new ArrayList<>();

        CriteriaQuery<Temi15UteQue> criteriaTemi15 =
                queryDao.getEntityManager().getCriteriaBuilder().createQuery(Temi15UteQue.class);
        Root<Temi15UteQue> rootTemi15 = criteriaTemi15.from(Temi15UteQue.class);

        predicateList =
                new ArrayList<>() {
                    {
                        listTemi15.forEach(
                                (query) -> {
                                    Expression<?> que = rootTemi15.get("que");

                                    Predicate predicateQue =
                                            queCatAssDao
                                                    .getEntityManager()
                                                    .getCriteriaBuilder()
                                                    .equal(que, (Long) ((Object[]) query)[0]);

                                    Expression<?> insQue = rootTemi15.get("insQue");

                                    Predicate predicateinsQue =
                                            queCatAssDao
                                                    .getEntityManager()
                                                    .getCriteriaBuilder()
                                                    .equal(insQue, (Date) ((Object[]) query)[1]);

                                    add(
                                            queryDao
                                                    .getEntityManager()
                                                    .getCriteriaBuilder()
                                                    .and(predicateQue, predicateinsQue));
                                });
                    }
                };

        finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteriaTemi15.where(queryDao.getEntityManager().getCriteriaBuilder().or(finalPredicateList));

        return queryDao.getEntityListByCriteriaQuery(criteriaTemi15, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Object> getAlreadyAssociatedQuery(int cat, String insCat) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        return queryDao.getAlreadyAssociatedQuery(cat, simpleDateFormat.parse(insCat.replace(" ", "+")));
    }
}
