/** */
package it.cambi.qrgui.services.oracle.entity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.cambi.qrgui.dao.generic.impl.FirstGenericDao;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.QueryService;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/** @author luca */
@Component
@RequiredArgsConstructor
public class FirstOracleService {

  private final QueryService checkQueryService;

  private final FirstGenericDao firstGenericDao;
  /**
   * @param que
   * @return
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public WrappedResponse<QueryToJson> checkQuery(Temi15UteQue que)
      throws JsonMappingException, IOException {
    return checkQueryService.checkQuery(que, true, firstGenericDao);
  }
}
