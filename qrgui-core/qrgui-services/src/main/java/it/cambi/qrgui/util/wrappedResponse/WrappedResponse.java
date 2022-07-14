package it.cambi.qrgui.util.wrappedResponse;

import static it.cambi.qrgui.util.IConstants.ERRORPARSE;
import static it.cambi.qrgui.util.IConstants.HANDLER;
import static it.cambi.qrgui.util.IConstants.HIBERNATELAZYINITIALIZER;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.util.Errors;
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.Messages;
import it.cambi.qrgui.util.objectMapper.ObjectMapperFactory;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "baseBuilder")
@Data
public class WrappedResponse<T> {
  @JsonIgnore private final Logger log = LoggerFactory.getLogger(this.getClass());
  @JsonIgnore private String response;
  @JsonIgnore private Throwable exception;
  @JsonIgnore private ObjectMapper objectMapper;

  @JsonIgnore
  private List<String> ignorableFields =
      Arrays.asList(IConstants.KA, HANDLER, HIBERNATELAZYINITIALIZER);

  /*
   * Proprietà serializzate nel json verso la parte rest
   */
  @Builder.Default
  private boolean success = true;
  private T entity;
  private Integer count;
  private Integer errorCode;
  private List<String> errorMessage;
  private List<String> succededMessage;
  private String developerMessage;
  private String locale;
  private String queryFilePath;

  /**
   * Metodo usato per la ricerca della root cause dell'eccezione (nel caso esista). La parte web
   * mostra il messaggio di errore se success è false
   */
  public WrappedResponse<T> processException() {

    log.error(getException().getMessage(), getException());
    setSuccess(false).setCount(0);

    Errors.setErrorMessageListFromRootCause(this, getException(), null);

    setResponse();

    return this;
  }

  /**
   * Metodo usato per la ricerca della root cause dell'eccezione (nel caso esista). La parte web
   * mostra il messaggio di errore se success è false
   */
  public WrappedResponse<T> processException(String additionalComment) {

    log.error(
        getException().getMessage() + ", ADDITIONAL COMMENT: " + additionalComment, getException());
    setSuccess(false).setCount(0);

    Errors.setErrorMessageListFromRootCause(this, getException(), additionalComment);

    setResponse();

    return this;
  }

  /* Nel caso si abbia bisogno dell'entità in json. Viene usata per gli unit test */
  @JsonIgnore
  public String getSerializedEntity() throws JsonProcessingException {

    return getObjectMapper().writeValueAsString(getEntity());
  }

  public ResponseEntity<String> getResponse(HttpServletRequest sr) {

    if (isSuccess()) {
      logRequestInfo(sr);
      return ResponseEntity.ok(response);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Metodo per serializzare questo stesso oggetto (escluso gli @Ignore) che poi verrà restituito
   * alla parte web come entity della response
   *
   * @return
   */
  public WrappedResponse<T> setResponse() {

    try {

      response = getObjectMapper().writeValueAsString(this);

    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);

      /* Se c'è una eccezione è errore sulla entity e la tolgo dalla serializzazione */
      setIgnorableFields(new String[] {"entity"});

      try {
        List<String> errorMessageList = new ArrayList<String>();
        errorMessageList.add(new Messages("en").getString(ERRORPARSE));
        this.developerMessage = ExceptionUtils.getStackTrace(e);

        setSuccess(false).setErrorMessages(errorMessageList);

        setException(e);

        response = getObjectMapper().writeValueAsString(this);
      } catch (JsonProcessingException e1) {
        /* Non dovrei mai arrivare a questo livello */
        log.error(e1.getMessage(), e1);
      }
    }

    return this;
  }

  public <X extends Principal> WrappedResponse<T> logRequestInfo(HttpServletRequest sr) {

    String builder =
        new StringBuilder()
            .append("Request  ----> " + new ErtaGuiUser(sr).toString())
            .append("\n")
            .append("Response ----> ")
            .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()))
            .append(", ")
            .append("success --> " + isSuccess())
            .append(", ")
            .append("count --> " + getCount())
            .append("\n")
            .toString();

    log.info(builder);

    return this;
  }

  public WrappedResponse<T> setEntity(T entity) {

    this.entity = entity;
    return this;
  }

  public WrappedResponse<T> setCount(Integer count) {
    this.count = count;
    return this;
  }

  public ObjectWriter getObjectMapper() {
    return objectMapper == null
        ? new ObjectMapperFactory()
            .createWriter(getIgnorableFields().stream().toArray(String[]::new))
        : new ObjectMapper().writer();
  }

  public WrappedResponse<T> setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public void setDeveloperMessage(String developerMessage) {
    this.developerMessage = developerMessage;
  }

  public List<String> getErrorMessage() {
    if (null == errorMessage) errorMessage = new ArrayList<String>();

    return errorMessage;
  }

  public WrappedResponse<T> setErrorMessages(List<String> errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public WrappedResponse<T> setException(Throwable exception) {
    this.exception = exception;
    return this;
  }

  public List<String> getIgnorableFields() {
    return ignorableFields == null ? new ArrayList<>() : ignorableFields;
  }

  public WrappedResponse<T> setIgnorableFields(String[] ignorableFieldsList) {
    for (String string : ignorableFieldsList) {
      getIgnorableFields().add(string);
    }

    return this;
  }

  public WrappedResponse<T> setQueryFilePath(String queryFilePath) {
    this.queryFilePath = queryFilePath;

    return this;
  }
}
