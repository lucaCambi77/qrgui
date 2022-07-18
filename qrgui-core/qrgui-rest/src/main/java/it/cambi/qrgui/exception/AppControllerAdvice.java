package it.cambi.qrgui.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

@ControllerAdvice
public class AppControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ResponseEntity<String> runtimeException(
      Exception ex, HttpServletRequest sr) {

    return WrappedResponse.<String>baseBuilder()
        .exception(ex)
        .build()
        .processException()
        .getResponse(sr);
  }
}