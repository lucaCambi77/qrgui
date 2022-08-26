package it.cambi.qrgui.query.model;

import it.cambi.qrgui.model.Temi15UteQue;
import lombok.Data;

import java.util.List;

@Data
public class QueryExecutionResponse {
  List<Object> resultSet;
  int count;
  Temi15UteQue temi15UteQue;
  QueryToJson json;
}
