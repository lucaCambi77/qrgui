package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.dao.generic.impl.TemiGenericDao;
import it.cambi.qrgui.services.db.model.Temi20AnaTipCat;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Component
public class Temi20Dao extends TemiGenericDao<Temi20AnaTipCat, String> implements ITemi20Dao<Temi20AnaTipCat, String>
{
    private static final Logger log = LoggerFactory.getLogger(Temi20Dao.class);

    @Autowired
    private Temi13Dao temi13dao;

    public Temi20Dao()
    {
        super(Temi20AnaTipCat.class);
    }

    @Override
    public WrappedResponse<List<Temi20AnaTipCat>> findByAllowedCategories(HttpServletRequest request)
    {

        List<String> functions = getFunctionsByRequest(request);

        CriteriaQuery<Temi20AnaTipCat> criteriaTemi20 = getEntityManager().getCriteriaBuilder().createQuery(Temi20AnaTipCat.class);

        Root<Temi20AnaTipCat> root = criteriaTemi20.from(Temi20AnaTipCat.class);

        Path<String> anaTipCatPath = root.get("tipCat");

        Predicate predicateAnaTipCat = anaTipCatPath.in(functions);

        if (null != functions && functions.size() > 0)
            criteriaTemi20.where(predicateAnaTipCat);

        List<Temi20AnaTipCat> listTemi20 = getEntityListByCriteriaQuery(criteriaTemi20, null);

        return new WrappedResponse<List<Temi20AnaTipCat>>().setEntity(listTemi20).setResponse();
    }

    public List<String> getFunctionsByRequest(HttpServletRequest request)
    {

        List<Temi20AnaTipCat> ttps20List = findAll(null);

        if(null == request)
            return ttps20List.stream().map(Temi20AnaTipCat::getTipCat).collect(Collectors.toList());

        List<String> functions = new ArrayList<String>();

        for (Temi20AnaTipCat atc : ttps20List)
        {
            String cTipCat = atc.getTipCat();
            if (request.isUserInRole(cTipCat))
                functions.add(atc.getTipCat());

        }

        String user = request.getUserPrincipal() == null ? "LocalHost"
                : request.getUserPrincipal().getName();

        log.info("L'utente " + user + " ha visibilità delle seguenti categorie: "
                + Arrays.toString(functions.toArray()));

        return functions;
    }
}
