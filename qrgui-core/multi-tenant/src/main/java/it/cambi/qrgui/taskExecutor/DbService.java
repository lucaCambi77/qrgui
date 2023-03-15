/**
 *
 */
package it.cambi.qrgui.taskExecutor;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.taskExecutor.repository.GenericRepository;
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
public class DbService {

    private final QueryService checkQueryService;

    private final GenericRepository firstGenericDao;

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
