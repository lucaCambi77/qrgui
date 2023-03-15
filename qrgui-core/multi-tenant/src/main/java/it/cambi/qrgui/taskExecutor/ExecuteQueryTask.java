package it.cambi.qrgui.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.taskExecutor.repository.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Classe che implementa Callable e permette di eseguire un task di esecuzione di una query.
 *
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class ExecuteQueryTask {

    private final GenericRepository firstGenericDao;
    private final QueryService queryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WrappedResponse<QueryExecutionResponse> response = new WrappedResponse<>();

    public WrappedResponse<QueryExecutionResponse> call(
            UteQueDto query, Integer page, Integer pageSize) throws Exception {

        WrappedResponse<String> queryStringResponse = queryService.getFinalQueryString(query);

        if (!queryStringResponse.isSuccess()) {
            return response.toBuilder().success(false).errorMessage(queryStringResponse.getErrorMessage()).build();
        }

        return response.toBuilder()
                .entity(
                        queryExecutionResponse(
                                query
                                , page
                                , pageSize
                                , queryStringResponse.getEntity(), objectMapper.readValue(query.getJson(), QueryToJson.class)))
                .build();
    }

    private QueryExecutionResponse queryExecutionResponse(UteQueDto query, Integer page, Integer pageSize, String finalQuery, QueryToJson json) {

        return new QueryExecutionResponse(queryService
                .setResultSet(json, json.getQueryType() == QueryType.RESULT_SET ? firstGenericDao.getByNativeQuery(finalQuery, page, pageSize) : Collections.emptyList())
                , json.getQueryType() == QueryType.COUNT ? firstGenericDao.executeQueryCount(finalQuery).intValue() : 0
                , query, json);
    }
}
