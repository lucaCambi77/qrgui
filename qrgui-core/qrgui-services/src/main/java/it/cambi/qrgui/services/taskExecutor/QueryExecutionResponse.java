package it.cambi.qrgui.services.taskExecutor;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.query.model.QueryToJson;

import java.util.List;

public record QueryExecutionResponse(List<Object> resultSet, int count, UteQueDto temi15UteQue, QueryToJson json) {
}
