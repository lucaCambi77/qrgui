package it.cambi.qrgui.exception;

import it.cambi.qrgui.rest.BasicResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppControllerAdvice extends BasicResource {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ResponseEntity<String> runtimeException(
      Exception ex, HttpServletRequest sr) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public @ResponseBody ResponseEntity<String> accessDeniedException(
      AccessDeniedException ex, HttpServletRequest sr) {

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }
}
