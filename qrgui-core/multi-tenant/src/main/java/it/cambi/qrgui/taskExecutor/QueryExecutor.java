package it.cambi.qrgui.taskExecutor;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.taskExecutor.model.QueryExecutionCountResponse;
import it.cambi.qrgui.taskExecutor.model.QueryExecutionListResponse;
import it.cambi.qrgui.taskExecutor.repository.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Classe che implementa Callable e permette di eseguire un task di esecuzione di una query.
 *
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class QueryExecutor {

  private final GenericRepository firstGenericDao;
  private final QueryService queryService;
  private final WrappedResponse<QueryExecution> response = new WrappedResponse<>();

  public WrappedResponse<QueryExecution> call(
      final UteQueDto query,
      QueryToJson json,
      Integer page,
      Integer pageSize,
      QueryType queryType) {

    WrappedResponse<String> queryStringResponse = queryService.getFinalQueryString(json);

    if (!queryStringResponse.isSuccess()) {
      return response.toBuilder()
          .success(false)
          .errorMessage(queryStringResponse.getErrorMessage())
          .build();
    }

    QueryExecution execution;

    if (queryType == QueryType.COUNT) {
      execution =
          new QueryExecutionCountResponse(
              firstGenericDao.executeQueryCount(queryStringResponse.getEntity()).intValue(), query);
    } else {
      execution =
          new QueryExecutionListResponse(
              queryService.parseResultSet(
                  json,
                  firstGenericDao.getByNativeQuery(
                      queryStringResponse.getEntity(), page, pageSize)),
              query);
    }

    return response.toBuilder().entity(execution).build();
  }
}
