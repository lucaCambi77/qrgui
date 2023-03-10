package it.cambi.qrgui.services.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.repository.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        String finalQuery = queryStringResponse.getEntity();

        List<Object> resultSet = new ArrayList<>();

        Long count = 0L;

        QueryToJson json = objectMapper.readValue(query.getJson(), QueryToJson.class);


        if (json.getQueryType() == QueryType.COUNT) {
            count = firstGenericDao.executeQueryCount(finalQuery);
        } else {
            resultSet = firstGenericDao.getByNativeQuery(finalQuery, page, pageSize);
        }

        QueryExecutionResponse queryExecutionResponse = new QueryExecutionResponse(queryService.setResultSet(json, resultSet), count.intValue(), query, json);

        return response.toBuilder().entity(queryExecutionResponse).build();
    }
}
