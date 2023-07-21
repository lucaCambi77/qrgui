package it.cambi.qrgui.exception;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {

  private Date timestamp;
  private Integer status;
  private String error;
  private String message;
}
