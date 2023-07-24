/** */
package it.cambi.qrgui.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.taskExecutor.repository.GenericRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class DbService {

  private final QueryService checkQueryService;

  private final GenericRepository genericRepository;

  private final ObjectMapper objectMapper;

  /**
   * @param que
   * @return
   * @throws IOException
   */
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<QueryToJson> checkQuery(UteQueDto que) throws IOException {

    QueryToJson json = objectMapper.readValue(que.json(), QueryToJson.class);
    return checkQueryService.checkQuery(json, Optional.of(genericRepository::getByNativeQuery));
  }
}
