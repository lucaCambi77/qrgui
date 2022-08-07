/** */
package it.cambi.qrgui.services.taskExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryExecutionResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.util.objectMapper.ObjectMapperFactory;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import lombok.RequiredArgsConstructor;

/** @author luca */
@Component
@RequiredArgsConstructor
public class GenericQueryTaskExecutorService {
  private final IQueryExecutorFactory queryExecutorFactory;

  private final ObjectMapperFactory objectMapper;

  private final XWrappedResponse<Temi15UteQue, List<Object>> response;

  /**
   * Metodo per l'esecuzione simultanea di tutte le query. Contiene un executor service che lancia N
   * callable. Per l' {@link ExecuteQueryTask} , vedi applicationContext.xml, in cui viene definita
   * la factory {@link QueryExecutorFactoryImpl} che col metodo getExecuteQueryTask restitiusce
   * sempre una nuova instanza
   *
   * @param queryAttributes
   * @param page
   * @param pageSize
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  @SuppressWarnings("serial")
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public List<XWrappedResponse<Temi15UteQue, List<Object>>> executeQuery(
      List<Temi15UteQue> queryAttributes, Integer page, Integer pageSize)
      throws InterruptedException, ExecutionException, JsonMappingException, IOException {

    ExecutorService executor = Executors.newFixedThreadPool(queryAttributes.size() * 2);

    List<Future<WrappedResponse<QueryExecutionResponse>>> taskList =
        new ArrayList<>();

    int position = 0;

    /** Creo una query per il result set ed una per la count, e lancio un thread per ognuna */
    for (Temi15UteQue query : queryAttributes) {

      String aQuery = objectMapper.getObjectMapper().writeValueAsString(query);

      /** Faccio due copie del Json e ne utilizzo una per il result set ed una per la count */
      Temi15UteQue aCopy = objectMapper.getObjectMapper().readValue(aQuery, Temi15UteQue.class);
      Temi15UteQue anotherCopy = objectMapper.getObjectMapper().readValue(aQuery, Temi15UteQue.class);

      /** Count */
      QueryToJson json = objectMapper.getObjectMapper().readValue(anotherCopy.getJson(), QueryToJson.class);

      json.setPosition(position);
      json.setQueryType(QueryType.COUNT);

      aCopy.setJson(objectMapper.getObjectMapper().writeValueAsString(json));

      FutureTask<WrappedResponse<QueryExecutionResponse>> myTask =
          new FutureTask<>(
              queryExecutorFactory
                  .getExecuteQueryTask()
                  .setPage(page)
                  .setPageSize(pageSize)
                  .setQuery(aCopy));

      /** Result Set */
      QueryToJson anotherJson = objectMapper.getObjectMapper().readValue(anotherCopy.getJson(), QueryToJson.class);

      anotherJson.setQueryType(QueryType.RESULT_SET);
      anotherJson.setPosition(position);

      anotherCopy.setJson(objectMapper.getObjectMapper().writeValueAsString(anotherJson));

      /** Task Executor */
      FutureTask<WrappedResponse<QueryExecutionResponse>> myTaskCount =
          new FutureTask<>(
              queryExecutorFactory
                  .getExecuteQueryTask()
                  .setPage(page)
                  .setPageSize(pageSize)
                  .setQuery(anotherCopy));

      executor.execute(myTask);
      executor.execute(myTaskCount);

      taskList.add(myTask);
      taskList.add(myTaskCount);

      position++;
    }

    /** Best practice per l'executor, faccio lo shutdown ed aspetto la fine di tutti i risultati */
    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

    List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut = new ArrayList<>();

    Map<Temi15UteQue, Integer> uteQueCountMap = new HashMap<>();

    for (Future<WrappedResponse<QueryExecutionResponse>> task : taskList) {
      WrappedResponse<QueryExecutionResponse> response = task.get();

      if (!response.isSuccess())
        return List.of(this.response.toBuilder().errorMessage(response.getErrorMessage()).build());

      if(response.getEntity().getJson().getQueryType() == QueryType.COUNT) {
        uteQueCountMap.put(response.getEntity().getTemi15UteQue(), response.getEntity().getCount());
        continue;
      }

      listOut.add(this.response.toBuilder()
        .xentity(response.getEntity().getTemi15UteQue())
        .entity(response.getEntity().getResultSet())
        .build());
    }

    for (XWrappedResponse<Temi15UteQue, List<Object>> wrappedResponse : listOut) {
      wrappedResponse.setCount(uteQueCountMap.get(wrappedResponse.getXentity()));
    }

    return listOut.stream().sorted(Comparator.comparing(a -> a.getXentity()
      .getNam())).collect(Collectors.toList());
  }
}
