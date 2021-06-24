/**
 * 
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luca
 *
 */
@Component
public class Temi20Service implements ITemi20Service<Temi20AnaTipCat>
{

    @Autowired
    private ITemi20Dao<Temi20AnaTipCat, String> anaTipCatDao;

    /**
     * 
     */
    public Temi20Service()
    {
    }

    @Override
    public List<Temi20AnaTipCat> findAll()
    {

        return anaTipCatDao.findAll(null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WrappedResponse<List<Temi20AnaTipCat>> getByCategory(HttpServletRequest request)
    {

        return anaTipCatDao.findByAllowedCategories(request);
    }

    @Override
    public List<String> getFunctionsByRequest(HttpServletRequest request)
    {

        return anaTipCatDao.getFunctionsByRequest(request);

    }

    @Override
    @Transactional()
    public void merge(Temi20AnaTipCat temi20AnaTipCat) {
        anaTipCatDao.merge(temi20AnaTipCat);
    }

}
