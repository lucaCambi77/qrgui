/**
 * 
 */
package it.cambi.qrgui.services.exception;

import it.cambi.qrgui.util.Messages;

import static it.cambi.qrgui.util.Constants.ERROR_MISSING_PROPERTIES_FILE;

/**
 * @author luca
 *
 */
public class PropertiesFileNotFoundException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public PropertiesFileNotFoundException()
    {

        super(new Messages("en").getString(ERROR_MISSING_PROPERTIES_FILE));

    }
}
