/**
 *
 */
package it.cambi.qrgui.services.database;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.dao.generic.impl.FirstGenericDao;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.taskExecutor.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class FirstDbService {

    private final QueryService checkQueryService;

    private final FirstGenericDao firstGenericDao;

    /**
     * @param que
     * @return
     * @throws IOException
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WrappedResponse<QueryToJson> checkQuery(UteQueDto que) throws IOException {
        return checkQueryService.checkQuery(que, Optional.of(firstGenericDao::getByNativeQuery));
    }
}
