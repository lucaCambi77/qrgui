/**
 * 
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi13Dao;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;
import it.cambi.qrgui.services.db.model.Temi13DtbInfId;
import it.cambi.qrgui.services.emia.api.ITemi13Service;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luca
 *
 */
@Component
public class Temi13Service implements ITemi13Service<Temi13DtbInf>
{

    @Autowired
    private ITemi13Dao<Temi13DtbInf, Temi13DtbInfId> databaseInfoDao;

    /**
     * 
     */
    public Temi13Service()
    {
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WrappedResponse<List<Temi13DtbInf>> findAll()
    {
        return new WrappedResponse<List<Temi13DtbInf>>().setEntity(databaseInfoDao.findAll(null)).setResponse();
    }

}
