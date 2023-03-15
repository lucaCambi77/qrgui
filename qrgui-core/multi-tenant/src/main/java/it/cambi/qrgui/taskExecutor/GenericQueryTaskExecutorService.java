/**
 *
 */
package it.cambi.qrgui.taskExecutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.query.model.QueryToJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class GenericQueryTaskExecutorService {
    private final ExecuteQueryTask queryExecutorFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final XWrappedResponse<UteQueDto, List<Object>> response = new XWrappedResponse<>();

    /**
     * Cached thread pool: not bound to a size, but can reuse existing threads.
     */
    private final static Executor CACHED_THREAD_POOL = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    /**
     * Metodo per l'esecuzione simultanea di tutte le query. Contiene un executor service che lancia N
     * callable {@link ExecuteQueryTask}
     *
     * @param queries
     * @param page
     * @param pageSize
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<XWrappedResponse<UteQueDto, List<Object>>> executeQuery(
            List<UteQueDto> queries, Integer page, Integer pageSize)
            throws JsonMappingException, IOException {

        CompletableFuture<WrappedResponse<QueryExecutionResponse>>[] taskList = new CompletableFuture[queries.size() * 2];

        int position = 0;
        int future = 0;
        for (UteQueDto query : queries) {

            String aQuery = objectMapper.writeValueAsString(query);

            /** Faccio due copie del Json e ne utilizzo una per il result set ed una per la count */
            UteQueDto aCopy = objectMapper.readValue(aQuery, UteQueDto.class);
            UteQueDto anotherCopy = objectMapper.readValue(aQuery, UteQueDto.class);

            /** Count */
            QueryToJson json = objectMapper.readValue(anotherCopy.getJson(), QueryToJson.class);

            json.setPosition(position);
            json.setQueryType(QueryType.COUNT);

            aCopy.setJson(objectMapper.writeValueAsString(json));

            /** Result Set */
            QueryToJson anotherJson = objectMapper.readValue(anotherCopy.getJson(), QueryToJson.class);

            anotherJson.setQueryType(QueryType.RESULT_SET);
            anotherJson.setPosition(position);

            anotherCopy.setJson(objectMapper.writeValueAsString(anotherJson));

            taskList[future++] = supplyAsync(() -> {
                try {
                    return queryExecutorFactory.call(aCopy, page, pageSize);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, CACHED_THREAD_POOL);

            taskList[future++] = supplyAsync(() -> {
                try {
                    return queryExecutorFactory.call(anotherCopy, page, pageSize);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, CACHED_THREAD_POOL);

            position++;
        }

        return allOf(taskList).thenApplyAsync(
                fn -> {
                    LinkedList<XWrappedResponse<UteQueDto, List<Object>>> listOut = new LinkedList<>();

                    Map<UteQueDto, Integer> uteQueCountMap = new HashMap<>();

                    for (CompletableFuture<WrappedResponse<QueryExecutionResponse>> task : taskList) {
                        WrappedResponse<QueryExecutionResponse> response = task.join();

                        if (!response.isSuccess()) {
                            listOut = new LinkedList<>();
                            listOut.add(this.response.toBuilder().errorMessage(response.getErrorMessage()).build());
                            return listOut;
                        }

                        if (response.getEntity().json().getQueryType() == QueryType.COUNT) {
                            uteQueCountMap.put(response.getEntity().temi15UteQue(), response.getEntity().count());
                            continue;
                        }

                        listOut.add(
                                this.response.toBuilder()
                                        .xentity(response.getEntity().temi15UteQue())
                                        .entity(response.getEntity().resultSet())
                                        .build());
                    }

                    for (XWrappedResponse<UteQueDto, List<Object>> wrappedResponse : listOut) {
                        wrappedResponse.setCount(uteQueCountMap.get(wrappedResponse.getXentity()));
                    }

                    return listOut;
                }, CACHED_THREAD_POOL
        ).join();
    }
}
