package it.cambi.qrgui.api.wrappedResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(builderMethodName = "baseBuilder", toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrappedResponse<T> {

  private T entity;
  @Builder.Default private boolean success = true;
  private Integer count;
  private Integer errorCode;
  private List<String> errorMessage;
  private List<String> succeededMessage;
  private String developerMessage;
  private String locale;
  private String queryFilePath;
}
