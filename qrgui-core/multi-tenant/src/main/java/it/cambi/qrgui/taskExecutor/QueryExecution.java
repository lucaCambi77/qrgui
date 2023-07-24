package it.cambi.qrgui.taskExecutor;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.enums.QueryType;

public interface QueryExecution {

  QueryType executionType();

  UteQueDto uteQueDto();
}
