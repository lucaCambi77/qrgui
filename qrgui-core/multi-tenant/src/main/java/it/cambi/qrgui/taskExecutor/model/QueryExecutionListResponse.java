package it.cambi.qrgui.taskExecutor.model;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.enums.QueryType;
import java.util.List;

public record QueryExecutionListResponse(List<Object> resultSet, UteQueDto uteQueDto) implements QueryExecutionList {
    @Override
    public QueryType executionType() {
        return QueryType.RESULT_SET;
    }
}
