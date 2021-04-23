/**
 * 
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi16Dao;
import it.cambi.qrgui.services.db.model.Temi16QueCatAss;
import it.cambi.qrgui.services.db.model.Temi16QueCatAssId;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;
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
public class Temi16Service implements ITemi16Service<Temi16QueCatAss>
{

    @Autowired
    private ITemi16Dao<Temi16QueCatAss, Temi16QueCatAssId> queCatAssDao;

    /**
     * 
     */
    public Temi16Service()
    {
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WrappedResponse<List<Temi16QueCatAss>> findByCategory(HttpServletRequest request)
    {
        return queCatAssDao.findByCategory(request);

    }

    @Transactional
    @Override
    public WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16)
    {
        return queCatAssDao.addQueriesToCateg(temi16);
    }

}
