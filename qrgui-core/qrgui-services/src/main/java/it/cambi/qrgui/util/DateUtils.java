/**
 * 
 */
package it.cambi.qrgui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luca
 *
 */
public class DateUtils
{

    /**
     * @param SimpleDateFormat
     *            formatter es. new SimpleDateFormat("dd-MMM-yyyy");
     * @return String dateInString string to format es. (20-06-2015)
     * @throws ParseException
     */
    public static Date getDateFromString(SimpleDateFormat formatter, String dateInString) throws ParseException
    {

        return formatter.parse(dateInString);
    }

    /**
     * @param SimpleDateFormat
     *            formatter es. new SimpleDateFormat("dd-MMM-yyyy");
     * @return String dateInString string to format es. (20-06-2015)
     */
    public static String getStringFromDate(SimpleDateFormat formatter, Long date)
    {

        String dateInString = null;
        dateInString = formatter.format(date);

        return dateInString;
    }
}
