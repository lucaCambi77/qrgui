package it.cambi.qrgui.services.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.dao.generic.impl.FirstGenericDao;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryExecutionResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.QueryService;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Classe che implementa Callable e permette di eseguire un task di esecuzione di una query.
 *
 * @author luca
 */
@Component
@Scope("prototype")
@RequiredArgsConstructor
@Getter
public class ExecuteQueryTask implements Callable<WrappedResponse<QueryExecutionResponse>> {

  private final FirstGenericDao firstGenericDao;
  private final QueryService queryService;
  private final ObjectMapper objectMapper;

  private final WrappedResponse<QueryExecutionResponse> response;

  private Temi15UteQue query;
  private Integer pageSize;
  private Integer page;

  public void ExecuteQueryTask() {}

  @Override
  public WrappedResponse<QueryExecutionResponse> call() throws Exception {

    WrappedResponse<String> queryStringResponse = queryService.getFinalQueryString(query);

    if (!queryStringResponse.isSuccess()) {
     return response.toBuilder().success(false).errorMessage(queryStringResponse.getErrorMessage()).build();
    }

    String finalQuery = queryStringResponse.getEntity();

    List<Object> resultSet;

    Long count;

    QueryToJson json = objectMapper.readValue(query.getJson(), QueryToJson.class);

    QueryExecutionResponse queryExecutionResponse = new QueryExecutionResponse();
    queryExecutionResponse.setTemi15UteQue(query);
    queryExecutionResponse.setJson(json);

    if (json.getQueryType() == QueryType.COUNT) {
      count = firstGenericDao.executeQueryCount(finalQuery);
      queryExecutionResponse.setCount(count.intValue());
    } else {
      resultSet = firstGenericDao.getByNativeQuery(finalQuery, page, pageSize);
      queryExecutionResponse.setResultSet(queryService.setResultSet(json, resultSet));
    }

    return response.toBuilder().entity(queryExecutionResponse).build();
  }

  public ExecuteQueryTask setQuery(Temi15UteQue query) {
    this.query = query;
    return this;
  }

  public ExecuteQueryTask setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  public ExecuteQueryTask setPage(Integer page) {
    this.page = page;
    return this;
  }
}
