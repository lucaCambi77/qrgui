/**
 *
 */
package it.cambi.qrgui.util;

import java.text.SimpleDateFormat;

/**
 * @author luca
 */
public class DateUtils {
    /**
     * @param SimpleDateFormat formatter es. new SimpleDateFormat("dd-MMM-yyyy");
     * @return String dateInString string to format es. (20-06-2015)
     */
    public static String getStringFromDate(SimpleDateFormat formatter, Long date) {
        return formatter.format(date);
    }
}
