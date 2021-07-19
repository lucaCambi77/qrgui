/** */
package it.cambi.qrgui.services.oracle.taskExecutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/** @author luca */
@Component
public class GenericQueryTaskExecutorService {
  @Autowired private IQueryExecutorFactory queryExecutorFactory;

  @Autowired private ObjectMapper objectMapper;

  /** */
  public GenericQueryTaskExecutorService() {}

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

    List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut = new ArrayList<>();
    ExecutorService executor = Executors.newFixedThreadPool(queryAttributes.size() * 2);

    List<Future<XWrappedResponse<Temi15UteQue, List<Object>>>> taskList =
        new ArrayList<Future<XWrappedResponse<Temi15UteQue, List<Object>>>>();

    int position = 0;

    /** Creo una query per il result set ed una per la count, e lancio un thread per ognuna */
    for (Temi15UteQue query : queryAttributes) {

      String aQuery = objectMapper.writeValueAsString(query);

      /** Faccio due copie del Json e ne utilizzo una per il result set ed una per la count */
      Temi15UteQue aCopy = new ObjectMapper().readValue(aQuery, Temi15UteQue.class);
      Temi15UteQue anotherCopy = new ObjectMapper().readValue(aQuery, Temi15UteQue.class);

      /** Count */
      QueryToJson json = objectMapper.readValue(anotherCopy.getJson(), QueryToJson.class);

      json.setPosition(position);
      json.setQueryType(QueryType.COUNT);

      aCopy.setJson(objectMapper.writeValueAsString(json));

      FutureTask<XWrappedResponse<Temi15UteQue, List<Object>>> myTask =
          new FutureTask<XWrappedResponse<Temi15UteQue, List<Object>>>(
              queryExecutorFactory
                  .getExecuteQueryTask()
                  .setPage(page)
                  .setPageSize(pageSize)
                  .setQuery(aCopy));

      /** Result Set */
      QueryToJson anotherJson = objectMapper.readValue(anotherCopy.getJson(), QueryToJson.class);

      anotherJson.setQueryType(QueryType.RESULT_SET);
      anotherJson.setPosition(position);

      anotherCopy.setJson(objectMapper.writeValueAsString(anotherJson));

      /** Task Executor */
      FutureTask<XWrappedResponse<Temi15UteQue, List<Object>>> myTaskCount =
          new FutureTask<XWrappedResponse<Temi15UteQue, List<Object>>>(
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

    for (Future<XWrappedResponse<Temi15UteQue, List<Object>>> task : taskList) {
      XWrappedResponse<Temi15UteQue, List<Object>> response = task.get();

      if (!response.isSuccess())
        return new ArrayList<XWrappedResponse<Temi15UteQue, List<Object>>>() {
          {
            add(response);
          }
        };

      listOut.add(response);
    }

    List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut1 = new ArrayList<>();

    position = 0;

    /** Una volta raccolto tutti i risultati, assegno alla query col result set la sua count */
    for (XWrappedResponse<Temi15UteQue, List<Object>> response : listOut) {
      QueryToJson json = objectMapper.readValue(response.getXentity().getJson(), QueryToJson.class);

      if (json.getPosition() - position == 0 && json.getQueryType() == QueryType.COUNT) {

        for (XWrappedResponse<Temi15UteQue, List<Object>> response1 : listOut) {
          QueryToJson json1 =
              objectMapper.readValue(response1.getXentity().getJson(), QueryToJson.class);
          if (json1.getPosition() - position == 0 && json1.getQueryType() == QueryType.RESULT_SET) {
            response1.setCount(response.getCount());
            listOut1.add(response1);

            position++;
            break;
          }
        }
      }
    }

    return listOut1;
  }
}
