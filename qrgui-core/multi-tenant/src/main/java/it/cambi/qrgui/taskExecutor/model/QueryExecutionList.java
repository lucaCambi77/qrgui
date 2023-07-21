package it.cambi.qrgui.taskExecutor.model;

import it.cambi.qrgui.taskExecutor.QueryExecution;
import java.util.List;

public interface QueryExecutionList extends QueryExecution {

    List<Object> resultSet();
}
