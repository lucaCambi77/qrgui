/**
 * 
 */
package it.cambi.qrgui.services.exception;

import it.cambi.qrgui.services.util.IConstants;
import it.cambi.qrgui.services.util.Messages;

/**
 * @author luca
 *
 */
public class PropertiesFileNotFoundException extends RuntimeException implements IConstants
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
