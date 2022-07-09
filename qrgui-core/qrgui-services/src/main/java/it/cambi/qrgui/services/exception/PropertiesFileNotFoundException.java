/**
 * 
 */
package it.cambi.qrgui.services.exception;

import static it.cambi.qrgui.util.IConstants.ERROR_MISSING_PROPERTIES_FILE;

import it.cambi.qrgui.util.Messages;

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
