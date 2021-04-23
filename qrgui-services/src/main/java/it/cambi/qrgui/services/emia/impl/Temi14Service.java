/**
 * 
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.services.db.model.*;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import it.cambi.qrgui.services.exception.NoCategoriesAllowedException;
import it.cambi.qrgui.services.util.Functions;
import it.cambi.qrgui.services.util.TreeNode;
import it.cambi.qrgui.services.util.wrappedResponse.XWrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luca
 *
 */
@Component
public class Temi14Service implements ITemi14Service<Temi14UteCat>
{
    private static final Logger log = LoggerFactory.getLogger(Temi14Service.class);

    @Autowired
    private ITemi14Dao<Temi14UteCat, Temi14UteCatId> categoryDao;

    @Autowired
    private ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    @Autowired
    private ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

    @Autowired
    private ITemi18Dao<Temi18RouQue, Temi18RouQueId> queryRouDao;

    @Autowired
    private ITemi20Service<Temi20AnaTipCat> temi20Service;

    /**
     * 
     */
    public Temi14Service()
    {
    }

    /**
     * Creo una nuova categoria
     * 
     * @param cTipCateg
     * 
     * @param request
     * 
     * @param ccat2
     * 
     * @return
     */
    @Override
    @Transactional
    public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Long>>> saveCategory(HttpServletRequest sr, Temi14UteCat temi14)
            throws NoCategoriesAllowedException
    {

        /**
         * Se esiste il parent allora la associo come sottocategoria modificando il parent e la data del parent
         */
        if (null != temi14.getPar() && null != temi14.getId() && temi14.getId().getCat() > 0)
        {
            Temi14UteCat temi14Child = categoryDao.getEntityByPrimaryKey(temi14.getId());
            temi14Child.setPar(temi14.getPar());
            temi14Child.setInsPar(temi14.getInsPar());

            return findAll(sr, null).setXentity(temi14).setResponse();

        }

        if (null == temi14.getId().getInsCat())
            temi14.getId().setInsCat(new Date());

        categoryDao.merge(temi14);

        return findAll(sr, null).setXentity(temi14).setResponse();

    }

    /**
     * Cancello categoria
     * 
     * @return
     */
    @Override
    @Transactional
    public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Long>>> deleteCategory(HttpServletRequest sr, Temi14UteCat cat)
            throws NoCategoriesAllowedException
    {

        log.info("Cancello la categoria " + cat.getId().getCat());

        /**
         * Controllo le query che non sono state riassociate e nel caso le cancello
         */
        boolean reAssociated;

        for (Object object : queryDao.getAlreadyAssociatedQuery(cat.getId().getCat(), cat.getId().getInsCat().getTime(),
                cat.getTemi20AnaTipCat().getTipCat()))
        {
            Object[] temi16 = (Object[]) object;
            Temi15UteQueId id = (Temi15UteQueId) temi16[0];
            reAssociated = false;
            for (Temi16QueCatAss temi16QueCatAss : cat.getTemi16QueCatAsses())
            {
                if (!Functions.areDifferentLong(id.getQue(), temi16QueCatAss.getId().getQue())
                        && !Functions.areDifferentLong(id.getInsQue().getTime(),
                                temi16QueCatAss.getId().getInsQue().getTime()))
                    reAssociated = true;

            }

            if (!reAssociated)
            {
                Temi15UteQue temi15 = queryDao.getEntityByPrimaryKey(id);

                queCatAssDao.delete(queCatAssDao.getEntityByPrimaryKey(
                        new Temi16QueCatAssId(id.getQue(), cat.getId().getCat(), cat.getId().getInsCat(), id.getInsQue())));

                queryRouDao.getQueRoutineByQueryId(new Temi15UteQueId(id.getQue(), id.getInsQue())).forEach((temi18) -> queryRouDao.delete(temi18));

                queryDao.delete(temi15);

            }
        }

        deleteCategAndChildrens(cat);

        /**
         * Creo le nuove associazioni se ci sono. Dalla gui un utente può associare query che non avrebbero più associazioni a categorie esistenti
         */
        if (null != cat.getTemi16QueCatAsses())
            for (Temi16QueCatAss temi16QueCatAss : cat.getTemi16QueCatAsses())
            {
                queCatAssDao.merge(temi16QueCatAss);
            }

        return findAll(sr, null).setResponse();
    }

    /**
     * 
     * @param id
     */
    private void deleteCategAndChildrens(Temi14UteCat id)
    {
        /**
         * Cancello i parent
         */
        CriteriaQuery<Temi14UteCat> criteriaQueryPar = categoryDao.getEntityManager().getCriteriaBuilder()
                .createQuery(Temi14UteCat.class);
        Root<Temi14UteCat> rootPar = criteriaQueryPar.from(Temi14UteCat.class);

        ParameterExpression<Integer> nParParam = categoryDao.getEntityManager().getCriteriaBuilder().parameter(Integer.class, "nPar");

        Expression<?> nPar = rootPar.get("par");

        Predicate predicate14 = categoryDao.getEntityManager().getCriteriaBuilder().equal(nPar, nParParam);

        List<Temi14UteCat> listTemi14 = categoryDao.getEntityManager().createQuery(criteriaQueryPar.where(predicate14))
                .setParameter("nPar", (int) id.getId().getCat()).getResultList();

        listTemi14.forEach((temi14) -> deleteCategAndChildrens(temi14));

        deleteQueriesCategory(id);

        categoryDao.delete(categoryDao.getEntityByPrimaryKey(id.getId()));

    }

    /**
     * 
     * @param id
     * @return
     */
    private List<Temi16QueCatAss> deleteQueriesCategory(Temi14UteCat id)
    {
        /**
         * Cancello le associazioni alle query e le sotto categorie
         */
        CriteriaQuery<Temi16QueCatAss> criteriaQuery = queCatAssDao.getEntityManager().getCriteriaBuilder()
                .createQuery(Temi16QueCatAss.class);
        Root<Temi16QueCatAss> root = criteriaQuery.from(Temi16QueCatAss.class);

        ParameterExpression<Long> cCatParam = queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Long.class, "cCat");

        Expression<?> cCat = root.get("id").get("cat");

        Predicate predicate = queCatAssDao.getEntityManager().getCriteriaBuilder().equal(cCat, cCatParam);

        ParameterExpression<Date> insCatParam = queCatAssDao.getEntityManager().getCriteriaBuilder().parameter(Date.class, "insCat");

        Expression<?> insCat = root.get("id").get("insCat");

        Predicate predicateIns = queCatAssDao.getEntityManager().getCriteriaBuilder().equal(insCat, insCatParam);

        List<Temi16QueCatAss> listTemi16 = queCatAssDao.getEntityManager().createQuery(criteriaQuery.where(predicate, predicateIns))
                .setParameter("cCat", id.getId().getCat()).setParameter("insCat", id.getId().getInsCat()).getResultList();

        listTemi16.forEach((temi16) -> queCatAssDao.delete(temi16));

        return listTemi16;
    }

    /**
     * Creo un alberatura delle categorie con l'oggetto generico {@link #TreeNode}
     * 
     * @param request
     * 
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Long>>> findAll(HttpServletRequest sr, Temi14UteCatId id)
            throws NoCategoriesAllowedException
    {

        /**
         * Controllo ed aggiungo i tipi categorie per cui l'utente è abilitato
         */
        List<String> functions = temi20Service.getFunctionsByRequest(sr);

        if (functions == null || (null != functions && functions.size() == 0))
            throw new NoCategoriesAllowedException();

        CriteriaQuery<Temi14UteCat> criteriaQuery = categoryDao.getEntityManager().getCriteriaBuilder()
                .createQuery(Temi14UteCat.class);

        Root<Temi14UteCat> root = criteriaQuery.from(Temi14UteCat.class);

        List<Predicate> predicateList = new ArrayList<>();

        Expression<?> cTipCat = root.get("temi20AnaTipCat").get("tipCat");

        if (null != id)
        {
            Expression<?> cat = root.get("id").get("cat");

            Predicate predicateCat = categoryDao.getEntityManager().getCriteriaBuilder().equal(cat, id.getCat());
            Expression<?> insCat = root.get("id").get("insCat");
            Predicate predicateInsCat = categoryDao.getEntityManager().getCriteriaBuilder().equal(insCat, id.getInsCat());

            predicateList.add(predicateInsCat);
            predicateList.add(predicateCat);

        }

        criteriaQuery.orderBy(categoryDao.getEntityManager().getCriteriaBuilder().asc(root.get("id").get("cat")));
        predicateList.add(cTipCat.in(functions));

        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);

        criteriaQuery.where(finalPredicateList);

        List<Temi14UteCat> listTemi14 = categoryDao.getEntityListByCriteriaQuery(criteriaQuery, null);

        List<TreeNode<Temi14UteCat, Long>> listTreeNode = new ArrayList<TreeNode<Temi14UteCat, Long>>();

        for (Temi14UteCat temi14 : listTemi14)
        {
            if (isChild(listTreeNode, temi14))
            {
                continue;
            }
            else
            {

                TreeNode<Temi14UteCat, Long> treeNode = new TreeNode<Temi14UteCat, Long>(temi14, temi14.getId().getCat());

                addChilds(listTemi14, temi14, treeNode);

                listTreeNode.add(treeNode);
            }
        }

        return new XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Long>>>().setEntity(listTreeNode).setResponse();
    }

    /**
     * Verifico se il nodo è già stato aggiunto al parent
     * 
     * @param listTreeNode
     * @param children
     * @return
     */
    private boolean isChild(List<TreeNode<Temi14UteCat, Long>> listTreeNode, Temi14UteCat children)
    {
        boolean isChild = false;

        for (TreeNode<Temi14UteCat, Long> treeNode : listTreeNode)
        {
            for (TreeNode<Temi14UteCat, Long> child : treeNode.getChildrens())
            {
                /**
                 * Se è un child oppure il suo parent è lo stesso del nodo che sto esaminando, è un child e l'ho già aggiunto all'alberatura del
                 * parent
                 */
                if (!Functions.areDifferentLong(child.getData(), children.getId().getCat())
                        || !Functions.areDifferentLong(child.getData(), children.getPar() == null ? 0 : children.getPar().longValue()))
                    return true;

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
    private void addChilds(List<Temi14UteCat> listTemi14, Temi14UteCat temi14, TreeNode<Temi14UteCat, Long> treeNode)
    {

        for (Temi14UteCat temi14In : listTemi14)
        {
            if (!Functions.areDifferentLong(temi14.getId().getCat(), temi14In.getPar() == null ? 0 : temi14In.getPar().longValue())
                    && !Functions.areDifferentLong(temi14.getId().getInsCat().getTime(),
                            temi14In.getInsPar() == null ? 0 : temi14In.getInsPar().getTime()))
            {
                TreeNode<Temi14UteCat, Long> treeNodeSub = treeNode.addChild(temi14In.getId().getCat(), temi14In);

                addChilds(listTemi14, temi14In, treeNodeSub);
            }

        }
    }

}
