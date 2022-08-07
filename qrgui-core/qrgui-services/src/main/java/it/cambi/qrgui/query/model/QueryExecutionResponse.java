package it.cambi.qrgui.query.model;

import java.util.List;

import it.cambi.qrgui.model.Temi15UteQue;
import lombok.Data;

@Data
public class QueryExecutionResponse {
    List<Object> resultSet;
    int count;
    Temi15UteQue temi15UteQue;
    QueryToJson json;
}
