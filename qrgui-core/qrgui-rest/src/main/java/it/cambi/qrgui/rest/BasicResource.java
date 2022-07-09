package it.cambi.qrgui.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;

/**
 * @author luca Abstract Class for Basic Resource attributes
 *     <p>All class extending BasicResource implements a @Path Interface for rest easy services that
 *     is used as an @Inject
 */
@CrossOrigin(origins = "*")
public abstract class BasicResource {

  /* In realtà viene usato l'object mapper dentro la wrapped Response, questo è in caso se ne voglia utilizzare uno custom */
  protected ObjectWriter getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    return mapper.writer();
  }

  protected <T> ResponseEntity<String> getObjectMapperXResponseList(
      List<XWrappedResponse<Temi15UteQue, List<Object>>> wrappedResponses, HttpServletRequest sr)
      throws JsonProcessingException {

    for (XWrappedResponse<Temi15UteQue, List<Object>> wrappedResponse : wrappedResponses) {
      if (wrappedResponse.isSuccess()) continue;

      /*
       * Response with errors
       */
      return WrappedResponse.<XWrappedResponse<Temi15UteQue, List<Object>>>baseBuilder()
          .entity(wrappedResponse)
          .build()
          .setResponse()
          .getResponse(sr);
    }

    return WrappedResponse.<List<XWrappedResponse<Temi15UteQue, List<Object>>>>baseBuilder()
        .entity(wrappedResponses)
        .build()
        .setResponse()
        .getResponse(sr);
  }
}
