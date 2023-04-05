/**
 *
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.api.model.CategoryDto;
import it.cambi.qrgui.api.model.QueCatAssDto;
import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.model.*;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import it.cambi.qrgui.util.Functions;
import it.cambi.qrgui.util.TreeNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi14Service implements ITemi14Service<Temi14UteCat> {

    private final ITemi14Dao<Temi14UteCat, Temi14UteCatId> categoryDao;

    private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    private final ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

    private final ITemi15Service<Temi15UteQue> temi15Service;

    private final ITemi18Service<Temi18RouQue> temi85Service;

    @Override
    @Transactional
    public List<TreeNode<Temi14UteCat, Integer>> saveCategory(
            List<String> functions, Temi14UteCat temi14) {

        /**
         * Se esiste il parent allora la associo come sottocategoria modificando il parent e la data del
         * parent
         */
        if (null != temi14.getPar() || temi14.getCat() > 0) {
            Temi14UteCat temi14Child =
                    categoryDao.getEntityByPrimaryKey(
                            new Temi14UteCatId(temi14.getCat(), temi14.getInsCat()));
            temi14Child.setPar(temi14.getPar());
            temi14Child.setInsPar(temi14.getInsPar());
            categoryDao.merge(temi14);

            return findAll(functions, null);
        }

        temi14.setInsCat(new Date());
        categoryDao.save(temi14);

        return findAll(functions, null);
    }

    @Override
    @Transactional
    public void reAssocAndDeleteOld(CategoryDto cat) {


        List<Object> notReAssoc = new ArrayList<>();

        /** Controllo le query che non sono state riassociate e nel caso le cancello */
        for (Object object :
                queryDao.getAlreadyAssociatedQuery(
                        cat.cat(), cat.insCat())) {
            Object[] associatedQuery = (Object[]) object;
            Temi15UteQueId id = new Temi15UteQueId((long) associatedQuery[0], (Date) associatedQuery[1]);

            if (cat.queCatAsses().stream().noneMatch(temi16 -> !Functions.areDifferentLong(temi16.id().que(), id.getQue())
                    && !Functions.areDifferentDates(temi16.id().insQue(), id.getInsQue()))) {
                notReAssoc.add(object);
            }
        }

        notReAssoc.forEach(object -> {
            Object[] associatedQuery = (Object[]) object;
            Temi15UteQueId id = new Temi15UteQueId((long) associatedQuery[0], (Date) associatedQuery[1]);
            temi85Service.getQueRoutineByQueryId(new Temi15UteQueId(id.getQue(), id.getInsQue())).forEach(temi18 -> temi85Service.deleteQueRoutineAssoc(temi18.getId()));
        });

        notReAssoc.forEach(object -> {
            Object[] associatedQuery = (Object[]) object;
            temi15Service.deleteQuery(new Temi15UteQueId((long) associatedQuery[0], (Date) associatedQuery[1]));
        });

        /**
         * Creo le nuove associazioni se ci sono. Dalla gui un utente può associare query che non
         * avrebbero più associazioni a categorie esistenti
         */
        for (QueCatAssDto temi16QueCatAss : cat.queCatAsses()) {
            Temi16QueCatAss temi16QueCatAss1 = new Temi16QueCatAss();
            temi16QueCatAss1.setId(new Temi16QueCatAssId(
                    temi16QueCatAss.id().que(),
                    temi16QueCatAss.id().cat(),
                    temi16QueCatAss.id().insCat(),
                    temi16QueCatAss.id().insQue())
            );
            queCatAssDao.save(temi16QueCatAss1);
        }
    }

    /**
     * Cancello categoria
     *
     * @return
     */
    @Override
    @Transactional
    public List<TreeNode<Temi14UteCat, Integer>> deleteCategory(
            List<String> functions, CategoryDto cat) {

        log.info("Cancello la categoria " + cat.des());

        deleteCategAndChildrens(cat.cat(), cat.insCat());

        return findAll(functions, null);
    }

    /**
     * @param id
     */
    private void deleteCategAndChildrens(int cat, Date insCat) {
        /** Cancello i parent */
        CriteriaQuery<Temi14UteCat> criteriaQueryPar =
                categoryDao.getEntityManager().getCriteriaBuilder().createQuery(Temi14UteCat.class);
        Root<Temi14UteCat> rootPar = criteriaQueryPar.from(Temi14UteCat.class);

        Predicate predicate14 =
                categoryDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .equal(
                                rootPar.get("par"),
                                categoryDao
                                        .getEntityManager()
                                        .getCriteriaBuilder()
                                        .parameter(Integer.class, "nPar"));

        List<Temi14UteCat> listTemi14 =
                categoryDao
                        .getEntityManager()
                        .createQuery(criteriaQueryPar.where(predicate14))
                        .setParameter("nPar", cat)
                        .getResultList();

        listTemi14.forEach(category -> deleteCategAndChildrens(category.getCat(), category.getInsCat()));

        deleteQueriesCategory(cat, insCat);

        categoryDao.deleteByEntityId(new Temi14UteCatId(cat, insCat));
    }

    /**
     * @param id
     * @return
     */
    private void deleteQueriesCategory(int cat, Date insCat) {
        /** Cancello le associazioni alle query e le sotto categorie */
        CriteriaQuery<Temi16QueCatAss> criteriaQuery =
                queCatAssDao.getEntityManager().getCriteriaBuilder().createQuery(Temi16QueCatAss.class);
        Root<Temi16QueCatAss> root = criteriaQuery.from(Temi16QueCatAss.class);

        Predicate predicate =
                queCatAssDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .equal(
                                root.get("temi14UteCat").get("cat"),
                                queCatAssDao
                                        .getEntityManager()
                                        .getCriteriaBuilder()
                                        .parameter(Integer.class, "cCat"));

        Predicate predicateIns =
                queCatAssDao
                        .getEntityManager()
                        .getCriteriaBuilder()
                        .equal(
                                root.get("temi14UteCat").get("insCat"),
                                queCatAssDao
                                        .getEntityManager()
                                        .getCriteriaBuilder()
                                        .parameter(Date.class, "insCat"));

        List<Temi16QueCatAss> listTemi16 =
                queCatAssDao
                        .getEntityManager()
                        .createQuery(criteriaQuery.where(predicate, predicateIns))
                        .setParameter("cCat", cat)
                        .setParameter("insCat", insCat)
                        .getResultList();

        listTemi16.forEach(assoc -> queCatAssDao.deleteByEntityId(assoc.getId()));
    }

    /**
     * Creo un alberatura delle categorie con l'oggetto generico {@link TreeNode}
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TreeNode<Temi14UteCat, Integer>> findAll(List<String> functions, Temi14UteCatId id) {

        CriteriaQuery<Temi14UteCat> criteriaQuery =
                categoryDao.getEntityManager().getCriteriaBuilder().createQuery(Temi14UteCat.class);

        Root<Temi14UteCat> root = criteriaQuery.from(Temi14UteCat.class);

        List<Predicate> predicateList = new ArrayList<>();

        Expression<?> cTipCat = root.get("temi20AnaTipCat").get("tipCat");

        if (null != id) {
            Predicate predicateCat =
                    categoryDao.getEntityManager().getCriteriaBuilder().equal(root.get("cat"), id.getCat());
            Predicate predicateInsCat =
                    categoryDao
                            .getEntityManager()
                            .getCriteriaBuilder()
                            .equal(root.get("insCat"), id.getInsCat());

            predicateList.add(predicateInsCat);
            predicateList.add(predicateCat);
        }

        criteriaQuery.orderBy(categoryDao.getEntityManager().getCriteriaBuilder().asc(root.get("cat")));
        predicateList.add(cTipCat.in(functions));

        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteriaQuery.where(finalPredicateList);

        List<Temi14UteCat> listTemi14 = categoryDao.getEntityListByCriteriaQuery(criteriaQuery, null);

        List<TreeNode<Temi14UteCat, Integer>> listTreeNode = new ArrayList<>();

        for (Temi14UteCat temi14 : listTemi14) {
            if (!isChild(listTreeNode, temi14)) {

                TreeNode<Temi14UteCat, Integer> treeNode = new TreeNode<>(temi14, temi14.getCat());

                addChilds(listTemi14, temi14, treeNode);

                listTreeNode.add(treeNode);
            }
        }

        return listTreeNode;
    }

    /**
     * Verifico se il nodo è già stato aggiunto al parent
     *
     * @param listTreeNode
     * @param children
     * @return
     */
    private boolean isChild(
            List<TreeNode<Temi14UteCat, Integer>> listTreeNode, Temi14UteCat children) {
        boolean isChild = false;

        for (TreeNode<Temi14UteCat, Integer> treeNode : listTreeNode) {
            for (TreeNode<Temi14UteCat, Integer> child : treeNode.getChildrens()) {
                /**
                 * Se è un child oppure il suo parent è lo stesso del nodo che sto esaminando, è un child e
                 * l'ho già aggiunto all'alberatura del parent
                 */
                if (!Functions.areDifferentInt(child.getData(), children.getCat())
                        || !Functions.areDifferentInt(
                        child.getData(), children.getPar() == null ? 0 : children.getPar())) return true;

                isChild = isChild(child.getChildrens(), children);
            }
        }

        return isChild;
    }

    /**
     * Aggiungo tutto i childs del nodo parent
     *
     * @param listTemi14
     * @param temi14
     * @param treeNode
     */
    private void addChilds(
            List<Temi14UteCat> listTemi14,
            Temi14UteCat temi14,
            TreeNode<Temi14UteCat, Integer> treeNode) {

        for (Temi14UteCat temi14In : listTemi14) {
            if (!Functions.areDifferentInt(
                    temi14.getCat(), temi14In.getPar() == null ? 0 : temi14In.getPar())
                    && !Functions.areDifferentLong(
                    temi14.getInsCat().getTime(),
                    temi14In.getInsPar() == null ? 0 : temi14In.getInsPar().getTime())) {
                TreeNode<Temi14UteCat, Integer> treeNodeSub =
                        treeNode.addChild(temi14In.getCat(), temi14In);

                addChilds(listTemi14, temi14In, treeNodeSub);
            }
        }
    }
}
