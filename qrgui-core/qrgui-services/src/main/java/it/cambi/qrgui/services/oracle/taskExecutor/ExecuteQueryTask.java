package it.cambi.qrgui.services.oracle.taskExecutor;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.qrgui.dao.generic.impl.FirstGenericDao;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.QueryService;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Classe che implementa Callable e permette di eseguire un task di esecuzione di una query.
 *
 * @author luca
 */
@Component
@Scope("prototype")
@RequiredArgsConstructor
@Getter
public class ExecuteQueryTask implements Callable<XWrappedResponse<Temi15UteQue, List<Object>>> {

  private final FirstGenericDao firstGenericDao;
  private final QueryService queryService;
  private final ObjectMapper objectMapper;
  private final XWrappedResponse<Temi15UteQue, List<Object>> response;
  private Temi15UteQue query;
  private Integer pageSize;
  private Integer page;

  public void ExecuteQueryTask() {}

  @Override
  public XWrappedResponse<Temi15UteQue, List<Object>> call() throws Exception {

    WrappedResponse<String> queryStringResponse = queryService.getFinalQueryString(query);

    if (!queryStringResponse.isSuccess())
      return response.setSuccess(false).setErrorMessages(queryStringResponse.getErrorMessage());

    String finalQuery = queryService.getFinalQueryString(query).getEntity();

    List<Object> resultSet;

    Long count;

    QueryToJson json = objectMapper.readValue(query.getJson(), QueryToJson.class);

    if (json.getQueryType() == QueryType.COUNT) {

      count = firstGenericDao.executeQueryCount(finalQuery);
      response.setCount(count.intValue());

    } else {
      resultSet = firstGenericDao.getByNativeQuery(finalQuery, page);
      resultSet = queryService.setOneColumnResultSet(json, resultSet);
      response.setEntity(resultSet);
    }

    response.setXentity(query);

    return response;
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
