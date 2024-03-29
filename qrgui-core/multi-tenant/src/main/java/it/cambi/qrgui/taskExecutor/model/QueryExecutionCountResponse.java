package it.cambi.qrgui.taskExecutor.model;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.enums.QueryType;

public record QueryExecutionCountResponse(int count, UteQueDto uteQueDto)
    implements QueryExecutionCount {
  @Override
  public QueryType executionType() {
    return QueryType.COUNT;
  }
}
