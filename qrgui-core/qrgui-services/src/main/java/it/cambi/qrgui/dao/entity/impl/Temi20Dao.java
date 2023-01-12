package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.dao.temi.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <T> Type of the Entity.
 * @param <I> Type of the Primary Key.
 */
@Component
@Slf4j
public class Temi20Dao extends TemiGenericDao<Temi20AnaTipCat, String>
    implements ITemi20Dao<Temi20AnaTipCat, String> {

  public Temi20Dao() {
    super(Temi20AnaTipCat.class);
  }

  @Override
  public List<Temi20AnaTipCat> findByAllowedCategories(List<String> functions) {

    CriteriaQuery<Temi20AnaTipCat> criteriaTemi20 =
        getEntityManager().getCriteriaBuilder().createQuery(Temi20AnaTipCat.class);

    Root<Temi20AnaTipCat> root = criteriaTemi20.from(Temi20AnaTipCat.class);

    Path<String> anaTipCatPath = root.get("tipCat");

    Predicate predicateAnaTipCat = anaTipCatPath.in(functions);

    if (null != functions && functions.size() > 0) criteriaTemi20.where(predicateAnaTipCat);

    return getEntityListByCriteriaQuery(criteriaTemi20, null);
  }

  public List<String> getFunctionsByRequest(HttpServletRequest request) {

    List<Temi20AnaTipCat> ttps20List = findAll(null);

    if (null == request)
      return ttps20List.stream().map(Temi20AnaTipCat::getTipCat).collect(Collectors.toList());

    List<String> functions =
        ttps20List.stream()
            .map(Temi20AnaTipCat::getTipCat)
            .filter(request::isUserInRole)
            .collect(Collectors.toList());

    String user =
        request.getUserPrincipal() == null ? "LocalHost" : request.getUserPrincipal().getName();

    log.info(
        "L'utente "
            + user
            + " ha visibilità delle seguenti categorie: "
            + Arrays.toString(functions.toArray()));

    return functions;
  }
}
