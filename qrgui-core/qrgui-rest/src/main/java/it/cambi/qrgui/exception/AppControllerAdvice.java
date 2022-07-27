package it.cambi.qrgui.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class AppControllerAdvice {

  private final WrappedResponse<String> response;

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ResponseEntity<String> runtimeException(
      Exception ex, HttpServletRequest sr) {

    return response.toBuilder()
        .exception(ex)
        .build()
        .processException()
        .getResponse(sr, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public @ResponseBody ResponseEntity<String> accessDeniedException(
      AccessDeniedException ex, HttpServletRequest sr) {

    return response.toBuilder()
        .exception(ex)
        .build()
        .processException()
        .getResponse(sr, HttpStatus.FORBIDDEN);
  }
}
