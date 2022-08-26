/**
 * 
 */
package it.cambi.qrgui.util;

import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.NoResultException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLSyntaxErrorException;

import static it.cambi.qrgui.util.Constants.NO_RESULT_EXCEPTION;
import static it.cambi.qrgui.util.Constants.NULL_POINTER_EXCEPTION;

/**
 * @author luca
 *
 */
public class Errors
{

    /**
     * 
     * @param wrappedResponse
     * @param exception
     * @param additionalComment
     */
    public static void setErrorMessageListFromRootCause(WrappedResponse<?> wrappedResponse, Throwable exception, String additionalComment)
    {

        Throwable rootCause = ExceptionUtils.getRootCause(exception);

        setErrorMessage(wrappedResponse, exception, additionalComment, rootCause);

    }

    /**
     * 
     * @param wrappedResponse
     * @param exception
     * @param additionalComment
     */
    public static void setErrorMessageListFromException(WrappedResponse<?> wrappedResponse, Throwable exception, String additionalComment)
    {

        setErrorMessage(wrappedResponse, exception, additionalComment, exception);

    }

    /**
     * @param errorMessageList
     * @param exception
     * @param additionalComment
     * @param rootCause
     */
    private static void setErrorMessage(WrappedResponse<?> wrappedResponse, Throwable exception, String additionalComment, Throwable rootCause)
    {
        StringBuilder finalMaessage = new StringBuilder();

        String comment = (null != additionalComment && !additionalComment.isEmpty()) ? additionalComment : " ";

        Throwable inException = null == rootCause ? exception : rootCause;

        if (null != inException)
        {

            if (SQLSyntaxErrorException.class.isAssignableFrom(inException.getClass()))
            {
                finalMaessage.append(Constants.SYNTAXERROR + ". " + comment);
            }
            else if (NullPointerException.class.isAssignableFrom(inException.getClass()))
            {
                finalMaessage.append(NULL_POINTER_EXCEPTION + ". " + comment);
            }
            else if (NoResultException.class.isAssignableFrom(inException.getClass()))
            {
                finalMaessage.append(NO_RESULT_EXCEPTION + ". " + comment);
            }

            finalMaessage.append("\n").append(inException.getMessage() == null ? "" : inException.getMessage());

            if (!wrappedResponse.getErrorMessage()
                    .contains(finalMaessage.toString()))
                wrappedResponse.getErrorMessage()
                        .add(finalMaessage.toString());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            inException.printStackTrace(pw);
            String sStackTrace = sw.getBuffer().toString();

            wrappedResponse.setDeveloperMessage(sStackTrace);
        }

    }
}
