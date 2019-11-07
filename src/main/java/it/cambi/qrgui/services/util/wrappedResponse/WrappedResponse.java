package it.cambi.qrgui.services.util.wrappedResponse;

import java.security.Principal;
import java.text.SimpleDateFormat;
/*
 * Classe utlizzata per serializzare gli oggetti che transitano da server verso la parte rest.
 * Contiene le informazioni delle entity estratte da db e info associate.
 * In particolare il metodo setResponse() serializza questa stessa classe, oggetto finale verso i servizi rest.
 * 
 * */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.services.util.Errors;
import it.cambi.qrgui.services.util.IConstants;
import it.cambi.qrgui.services.util.Messages;
import it.cambi.qrgui.services.util.objectMapper.ObjectMapperFactory;

public class WrappedResponse<T> implements IConstants
{

    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @JsonIgnore
    private String response;
    /*
     * @JsonIgnore private UriInfo uriInfo;
     */

    @JsonIgnore
    private Throwable exception;
    @JsonIgnore
    private ObjectMapper objectMapper;

    @SuppressWarnings("serial")
    @JsonIgnore
    private List<String> ignorableFields = new ArrayList<String>()
    {
        {
            /**
             * questi campi vengono aggiunti sempre da hibernate nella serializzazione, li metto come ignorabili di default. Eclipse link non sembra
             * aggiunga niente all'oggetto da serializzare
             */
            add(KA);
            add(HANDLER);
            add(HIBERNATELAZYINITIALIZER);
        }
    };

    /*
     * Proprietà serializzate nel json verso la parte rest
     */
    private boolean success = true;
    private T entity;
    private Integer count;
    // private Status statusType;
    private Integer errorCode;
    private List<String> errorMessage;
    private List<String> succededMessage;
    private String developerMessage;
    private String locale;
    private String queryFilePath;

    /**
     * 
     */
    public WrappedResponse()
    {
    }

    /**
     * 
     */
    public WrappedResponse(T entity)
    {
        this.entity = entity;
    }

    public WrappedResponse(Throwable exception)
    {
        this.exception = exception;
    }

    /**
     * Metodo usato per la ricerca della root cause dell'eccezione (nel caso esista). La parte web mostra il messaggio di errore se success è false
     * 
     */
    public WrappedResponse<T> processException()
    {

        log.error(getException().getMessage(), getException());
        setSuccess(false).setCount(0);

        Errors.setErrorMessageListFromRootCause(this, getException(), null);

        setResponse();

        return this;
    }

    /**
     * Metodo usato per la ricerca della root cause dell'eccezione (nel caso esista). La parte web mostra il messaggio di errore se success è false
     * 
     */
    public WrappedResponse<T> processException(String additionalComment)
    {

        log.error(getException().getMessage() + ", ADDITIONAL COMMENT: " + additionalComment, getException());
        setSuccess(false).setCount(0);

        Errors.setErrorMessageListFromRootCause(this, getException(), additionalComment);

        setResponse();

        return this;
    }

    /* Nel caso si abbia bisogno dell'entità in json. Viene usata per gli unit test */
    @JsonIgnore
    public String getSerializedEntity() throws JsonProcessingException
    {

        return getObjectMapper().writeValueAsString(getEntity());
    }

    /*
     * public Response getResponse(HttpServletRequest sr) {
     * 
     * if (isSuccess()) { logRequestInfo(sr); return Response.ok(response).type(MediaType.APPLICATION_JSON).build(); }
     * 
     * setStatusType(Status.INTERNAL_SERVER_ERROR).logRequestInfo(sr);
     * 
     * 
     * Response with errors
     * 
     * return Response.serverError() .entity(response) .type(MediaType.APPLICATION_JSON).build();
     * 
     * }
     */

    /**
     * Metodo per serializzare questo stesso oggetto (escluso gli @Ignore) che poi verrà restituito alla parte web come entity della response
     * 
     * @return
     */
    public WrappedResponse<T> setResponse()
    {

        try
        {

            response = getObjectMapper().writeValueAsString(this);
        }
        catch (JsonProcessingException e)
        {
            log.error(e.getMessage(), e);

            /* Se c'è una eccezione è errore sulla entity e la tolgo dalla serializzazione */
            setIgnorableFields(new String[] { "entity" });

            try
            {
                List<String> errorMessageList = new ArrayList<String>();
                errorMessageList.add(new Messages("en").getString(ERRORPARSE));
                this.developerMessage = ExceptionUtils.getStackTrace(e);

                setSuccess(false).setErrorMessages(errorMessageList);

                setException(e);

                response = getObjectMapper().writeValueAsString(this);
            }
            catch (JsonProcessingException e1)
            {
                /* Non dovrei mai arrivare a questo livello */
                log.error(e1.getMessage(), e1);
            }

        }

        return this;
    }

    public <X extends Principal> WrappedResponse<T> logRequestInfo(HttpServletRequest sr)
    {

        String builder = new StringBuilder()
                .append("Request  ----> " + new ErtaGuiUser(sr).toString()).append("\n")
                .append("Response ----> ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())).append(", ")
                .append("success --> " + isSuccess()).append(", ")
                .append("count --> " + getCount()).append("\n").toString();

        log.info(builder);

        return this;
    }

    /**
     * 
     * @param uriInfo
     * @return
     */
    /*
     * @Deprecated public String toString(UriInfo uriInfo) {
     * 
     * StringBuilder queryParamBuilder = uriInfo.getQueryParameters().entrySet().size() > 0 ? new StringBuilder() : null;
     * 
     * for (Entry<String, List<String>> string : uriInfo.getQueryParameters().entrySet()) { queryParamBuilder.append(string.getKey() + "--> " +
     * Arrays.toString(string.getValue().toArray())).append(", "); }
     * 
     * String queryParameters = setParameterString(queryParamBuilder);
     * 
     * StringBuilder pathParamBuilder = uriInfo.getPathParameters().entrySet().size() > 0 ? new StringBuilder() : null;
     * 
     * for (Entry<String, List<String>> string : uriInfo.getPathParameters().entrySet()) {
     * 
     * pathParamBuilder.append(string.getKey() + "--> " + Arrays.toString(string.getValue().toArray())).append(", "); }
     * 
     * String pathParameters = setParameterString(pathParamBuilder);
     * 
     * return new StringBuilder() .append("Response uri Info --> " + (null == uriInfo ? "N.A." : uriInfo.getAbsolutePath())).append("\n") .append(new
     * Date().toString()).append("\n") .append("query Parameters --> ").append(queryParameters.isEmpty() ? "\n" : queryParameters)
     * .append("path Parameters --> ").append(pathParameters.isEmpty() ? "\n" : pathParameters) .append("success --> " + isSuccess()).append("\n")
     * .append("count --> " + getCount()).append("\n").toString(); }
     */

    /**
     * @param paramBuilder
     * @return
     */
    private String setParameterString(StringBuilder paramBuilder)
    {
        if (paramBuilder == null)
            return "";

        return paramBuilder.toString().substring(0, (paramBuilder.toString().length() - 2)) + "\n";
    }

    public ObjectWriter getObjectMapper()
    {
        return objectMapper == null ? new ObjectMapperFactory().createWriter(getIgnorableFields().stream().toArray(String[]::new))
                : new ObjectMapper().writer();
    }

    public WrappedResponse<T> setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
        return this;
    }

    public T getEntity()
    {
        return entity;
    }

    public WrappedResponse<T> setEntity(T entity)
    {

        this.entity = entity;
        return this;
    }

    /*
     * public Status getStatusType() { return statusType; }
     * 
     * public WrappedResponse<T> setStatusType(Status errorType) { this.statusType = errorType; return this; }
     */

    public Integer getCount()
    {
        return count;
    }

    public WrappedResponse<T> setCount(Integer count)
    {
        this.count = count;
        return this;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public WrappedResponse<T> setSuccess(boolean success)
    {
        this.success = success;
        return this;
    }

    public String getDeveloperMessage()
    {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage)
    {
        this.developerMessage = developerMessage;
    }

    public List<String> getErrorMessage()
    {
        if (null == errorMessage)
            errorMessage = new ArrayList<String>();

        return errorMessage;
    }

    public WrappedResponse<T> setErrorMessages(List<String> errorMessage)
    {
        this.errorMessage = errorMessage;
        return this;
    }

    public List<String> getSuccededMessage()
    {
        return succededMessage;
    }

    public void setSuccededMessage(List<String> succededMessage)
    {
        this.succededMessage = succededMessage;
    }

    public Throwable getException()
    {
        return exception;
    }

    public Logger getLog()
    {
        return log;
    }

    public WrappedResponse<T> setException(Throwable exception)
    {
        this.exception = exception;
        return this;
    }

    public String getLocale()
    {
        return locale;
    }

    public WrappedResponse<T> setLocale(String locale)
    {
        this.locale = locale;
        return this;
    }

    public Integer getErrorCode()
    {
        return errorCode;
    }

    public WrappedResponse<T> setErrorCode(Integer errorCode)
    {
        this.errorCode = errorCode;
        return this;
    }

    public List<String> getIgnorableFields()
    {
        return ignorableFields == null ? new ArrayList<>() : ignorableFields;
    }

    public WrappedResponse<T> setIgnorableFields(String[] ignorableFieldsList)
    {
        for (String string : ignorableFieldsList)
        {
            getIgnorableFields().add(string);
        }

        return this;
    }

    public String getQueryFilePath()
    {
        return queryFilePath;
    }

    public WrappedResponse<T> setQueryFilePath(String queryFilePath)
    {
        this.queryFilePath = queryFilePath;

        return this;
    }

}
