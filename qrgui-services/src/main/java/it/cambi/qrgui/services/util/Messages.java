/**
 * 
 */
package it.cambi.qrgui.services.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import it.cambi.qrgui.services.util.messages.UTF8Control;

/**
 * @author luca
 *
 */
public class Messages implements IConstants
{

    private ResourceBundle bundle;

    public Messages(String locale)
    {

        Locale userLocale = new Locale(null == locale ? DEFAULT_LOCALE : locale);
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS_FILE_NAME, userLocale, new UTF8Control());
    }

    public ResourceBundle getBundle()
    {
        return bundle;
    }

    public String getString(String key)
    {
        try
        {
            return getBundle().getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }

    public String getString(String key, Object... params)
    {
        try
        {
            return MessageFormat.format(getBundle().getString(key), params);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}
