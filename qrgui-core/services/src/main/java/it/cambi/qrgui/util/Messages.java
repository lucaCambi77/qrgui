/**
 *
 */
package it.cambi.qrgui.util;

import it.cambi.qrgui.util.messages.UTF8Control;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static it.cambi.qrgui.util.Constants.DEFAULT_LOCALE;
import static it.cambi.qrgui.util.Constants.TRANSLATIONS_FILE_NAME;

/**
 * @author luca
 *
 */
public class Messages {

    private final ResourceBundle bundle;

    public Messages(String locale) {

        Locale userLocale = new Locale(null == locale ? DEFAULT_LOCALE : locale);
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS_FILE_NAME, userLocale, new UTF8Control());
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getString(String key) {
        try {
            return getBundle().getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
