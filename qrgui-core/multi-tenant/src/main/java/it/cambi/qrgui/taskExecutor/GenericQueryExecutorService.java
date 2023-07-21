/**
 *
 */
package it.cambi.qrgui.taskExecutor;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.taskExecutor.model.QueryExecutionCountResponse;
import it.cambi.qrgui.taskExecutor.model.QueryExecutionListResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class GenericQueryExecutorService {
    private final QueryExecutor queryExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final XWrappedResponse<UteQueDto, List<Object>> response = new XWrappedResponse<>();

    private final static Executor THREAD_POOL = Executors.newFixedThreadPool(10);

    /**
     * Metodo per l'esecuzione simultanea di tutte le query. Contiene un executor service che lancia N
     * callable {@link QueryExecutor}
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
            throws IOException {

        CompletableFuture<WrappedResponse<QueryExecution>>[] taskList = new CompletableFuture[queries.size() * 2];

        int position = 0;
        int future = 0;
        for (UteQueDto query : queries) {

            QueryToJson json = objectMapper.readValue(query.json(), QueryToJson.class);
            json.setPosition(position);

            taskList[future++] = supplyAsync(() -> {
                try {
                    return queryExecutor.call(query, json, page, pageSize, QueryType.COUNT);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, THREAD_POOL);

            taskList[future++] = supplyAsync(() -> {

                try {
                    return queryExecutor.call(query, json, page, pageSize, QueryType.RESULT_SET);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, THREAD_POOL);

            position++;
        }

        return allOf(taskList).thenApplyAsync(
                fn -> {
                    LinkedList<XWrappedResponse<UteQueDto, List<Object>>> listOut = new LinkedList<>();

                    Map<UteQueDto, Integer> uteQueCountMap = new HashMap<>();

                    for (CompletableFuture<WrappedResponse<QueryExecution>> task : taskList) {
                        WrappedResponse<QueryExecution> response = task.join();

                        if (!response.isSuccess()) {
                            listOut = new LinkedList<>();
                            listOut.add(this.response.toBuilder().errorMessage(response.getErrorMessage()).build());
                            return listOut;
                        }

                        if (response.getEntity().executionType() == QueryType.COUNT) {
                            QueryExecutionCountResponse countResponse = (QueryExecutionCountResponse) response.getEntity();
                            uteQueCountMap.put(countResponse.uteQueDto(), countResponse.count());
                        } else {
                            QueryExecutionListResponse executionListResponse = (QueryExecutionListResponse) response.getEntity();

                            listOut.add(
                                    this.response.toBuilder()
                                            .xentity(executionListResponse.uteQueDto())
                                            .entity(executionListResponse.resultSet())
                                            .build());
                        }

                    }

                    for (XWrappedResponse<UteQueDto, List<Object>> wrappedResponse : listOut) {
                        wrappedResponse.setCount(uteQueCountMap.get(wrappedResponse.getXentity()));
                    }

                    return listOut;
                }, THREAD_POOL
        ).join();
    }
}
