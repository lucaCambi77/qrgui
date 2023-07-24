package it.cambi.qrgui.taskExecutor.model;

import it.cambi.qrgui.taskExecutor.QueryExecution;

public interface QueryExecutionCount extends QueryExecution {
  int count();
}
