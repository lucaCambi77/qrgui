/** */
package it.cambi.qrgui.services.emia.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.dao.entity.api.ITemi18Dao;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi14UteCatId;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import it.cambi.qrgui.services.exception.NoCategoriesAllowedException;
import it.cambi.qrgui.util.Functions;
import it.cambi.qrgui.util.TreeNode;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** @author luca */
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi14Service implements ITemi14Service<Temi14UteCat> {

  private final ITemi14Dao<Temi14UteCat, Temi14UteCatId> categoryDao;

  private final ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

  private final ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

  private final ITemi18Dao<Temi18RouQue, Temi18RouQueId> queryRouDao;

  private final ITemi20Service<Temi20AnaTipCat> temi20Service;

  /**
   * Creo una nuova categoria
   *
   * @param cTipCateg
   * @param request
   * @param ccat2
   * @return
   */
  @Override
  @Transactional
  public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Integer>>> saveCategory(
      HttpServletRequest sr, Temi14UteCat temi14) throws NoCategoriesAllowedException {

    /**
     * Se esiste il parent allora la associo come sottocategoria modificando il parent e la data del
     * parent
     */
    if (null != temi14.getPar() && temi14.getCat() > 0) {
      Temi14UteCat temi14Child =
          categoryDao.getEntityByPrimaryKey(
              new Temi14UteCatId(temi14.getCat(), temi14.getInsCat()));
      temi14Child.setPar(temi14.getPar());
      temi14Child.setInsPar(temi14.getInsPar());

      return findAll(sr, null).setXentity(temi14).setResponse();
    }

    temi14.setInsCat(new Date());
    Temi14UteCat newCat = categoryDao.merge(temi14);

    return findAll(sr, null).setXentity(newCat).setResponse();
  }

  /**
   * Cancello categoria
   *
   * @return
   */
  @Override
  @Transactional
  public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Integer>>> deleteCategory(
      HttpServletRequest sr, Temi14UteCat cat) throws NoCategoriesAllowedException {

    log.info("Cancello la categoria " + cat.getCat());

    /** Controllo le query che non sono state riassociate e nel caso le cancello */
    boolean reAssociated;

    for (Object object :
        queryDao.getAlreadyAssociatedQuery(
            cat.getCat(), cat.getInsCat().getTime(), cat.getTemi20AnaTipCat().getTipCat())) {
      Object[] temi16 = (Object[]) object;
      Temi15UteQueId id = (Temi15UteQueId) temi16[0];
      reAssociated = false;
      for (Temi16QueCatAss temi16QueCatAss : cat.getTemi16QueCatAsses()) {
        if (!Functions.areDifferentLong(id.getQue(), temi16QueCatAss.getId().getQue())
            && !Functions.areDifferentLong(
                id.getInsQue().getTime(), temi16QueCatAss.getId().getInsQue().getTime())) {
          reAssociated = true;
          break;
        }
      }

      if (!reAssociated) {
        Temi15UteQue temi15 = queryDao.getEntityByPrimaryKey(id);

        queCatAssDao.delete(
            queCatAssDao.getEntityByPrimaryKey(
                new Temi16QueCatAssId(id.getQue(), cat.getCat(), cat.getInsCat(), id.getInsQue())));

        queryRouDao
            .getQueRoutineByQueryId(new Temi15UteQueId(id.getQue(), id.getInsQue()))
            .forEach(queryRouDao::delete);

        queryDao.delete(temi15);
      }
    }

    deleteCategAndChildrens(cat);

    /**
     * Creo le nuove associazioni se ci sono. Dalla gui un utente può associare query che non
     * avrebbero più associazioni a categorie esistenti
     */
    if (null != cat.getTemi16QueCatAsses())
      for (Temi16QueCatAss temi16QueCatAss : cat.getTemi16QueCatAsses()) {
        queCatAssDao.merge(temi16QueCatAss);
      }

    return findAll(sr, null).setResponse();
  }

  /** @param id */
  private void deleteCategAndChildrens(Temi14UteCat id) {
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
            .setParameter("nPar", id.getCat())
            .getResultList();

    listTemi14.forEach(this::deleteCategAndChildrens);

    deleteQueriesCategory(id);

    categoryDao.delete(
        categoryDao.getEntityByPrimaryKey(new Temi14UteCatId(id.getCat(), id.getInsCat())));
  }

  /**
   * @param id
   * @return
   */
  private void deleteQueriesCategory(Temi14UteCat id) {
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
            .setParameter("cCat", id.getCat())
            .setParameter("insCat", id.getInsCat())
            .getResultList();

    listTemi16.forEach(queCatAssDao::delete);
  }

  /**
   * Creo un alberatura delle categorie con l'oggetto generico {@link #TreeNode}
   *
   * @param request
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Integer>>> findAll(
      HttpServletRequest sr, Temi14UteCatId id) throws NoCategoriesAllowedException {

    /** Controllo ed aggiungo i tipi categorie per cui l'utente è abilitato */
    List<String> functions = temi20Service.getFunctionsByRequest(sr);

    if (functions == null || functions.size() == 0) throw new NoCategoriesAllowedException();

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

        TreeNode<Temi14UteCat, Integer> treeNode =
            new TreeNode<>(temi14, temi14.getCat());

        addChilds(listTemi14, temi14, treeNode);

        listTreeNode.add(treeNode);
      }
    }

    return XWrappedResponse.<Temi14UteCat, List<TreeNode<Temi14UteCat, Integer>>>builder()
        .entity(listTreeNode)
        .build()
        .setResponse();
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
