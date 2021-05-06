package it.cambi.qrgui.services.oracle.taskExecutor;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public abstract class QueryExecutorFactoryImpl implements IQueryExecutorFactory
{

    @Lookup
    @Override
    public abstract ExecuteQueryTask getExecuteQueryTask();

}