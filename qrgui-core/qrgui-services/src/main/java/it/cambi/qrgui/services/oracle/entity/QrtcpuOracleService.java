/**
 * 
 */
package it.cambi.qrgui.services.oracle.entity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.cambi.qrgui.dao.generic.impl.QrtcpuGenericDao;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author luca
 *
 */
@Component
public class QrtcpuOracleService
{

    @Autowired
    private QrtcpuGenericDao genericDao;

    /**
     * 
     */
    public QrtcpuOracleService()
    {
    }

    public WrappedResponse<QueryToJson> checkQuery(Temi15UteQue que) throws JsonParseException, JsonMappingException, IOException
    {
        return genericDao.checkQuery(que, true);
    }
}
