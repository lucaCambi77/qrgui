/**
 * Class for application Constants
 */
package it.cambi.qrgui.services.util;

/**
 * @author luca
 *
 */
public class IConstants
{

    public static final String GMT = "GMT";
    public static final String APPLICATION_CONF_DIR = "application.conf.dir";
    public static final String CONFIG_PROPERTIES_PATH = "/erta-qr-gui/erta.qr.gui.properties";

    // Locales
    public static final String DEFAULT_LOCALE = "en";
    public static final String TRANSLATIONS_FILE_NAME = "translations";

    // Numbers
    public static String ONE = "1";
    public static final String TEN = "10";
    public static String FIFTEEN = "15";
    public static String THIRTY = "30";
    public static String HUNDRED = "100";

    public int DEFAULT_PAGE_SIZE = 15;
    public Long NULL_LONG = -999999L;

    // Date format
    public static final String YYYY_MM_DD_HH_MI_SS = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy/MM/dd";

    // Fake Date
    public static final String FAKE_DATE = "3000/01/01 00:00:00";
    public static final String FAKE_DATE_TRUNC = "3000/01/01";

    //
    public final String NULL = "null";
    public final String PERCENTAGE = "%";

    /**
     * Errors
     */
    public static final String ERRORPARSE = "PARSEJSONERROR";
    public static final String ERRORBLOB = "BLOBERROR";
    public static final String SYNTAXERROR = "SYNTAXERROR";

    public static final String UNKOWN_EXCEPTION = "UNKNOWK EXCEPTION";
    public static final String NULL_POINTER_EXCEPTION = "NULL POINTER EXCEPTION";
    public static final String NO_RESULT_EXCEPTION = "NO RESULT EXCEPTION";
    public static final String ERROR_NO_QUERY_ASSOCIATION = "ERROR.NO.QUERY.ASSOCIATION";
    public static final String ERROR_MISSING_PROPERTIES_FILE = "ERROR.MISSING.PROPERTIES.FILE";
    /**
     * Fine Errors
     */

    // Ignorable fields, questi campi sono da ignorare in hibernate
    public static final String KA = "ka";
    public static final String HANDLER = "handler";
    public static final String HIBERNATELAZYINITIALIZER = "hibernateLazyInitializer";

    /**
     * Sequences
     * 
     */

    public final static String SEMI14_ANA_CAT = "SELECT EMIA.SEMI14_ANA_CAT.nextval from dual";
    public final static String SEMI15_ANA_QUE = "SELECT EMIA.SEMI15_ANA_QUE.nextval from dual";
    public final static String SEMI17_ANA_ROU = "SELECT EMIA.SEMI17_ANA_ROU.nextval from dual";

    // Roles Functions
    public static final String R_FEPQRA = "FEP.QRA";
    public static final String R_FEPQR1 = "FEP.QR1";
    public static final String R_FEPQR2 = "FEP.QR2";

    public static final String F_QRCG00 = "F.FEP.QRCG00";
    public static final String F_QRCG01 = "F.FEP.QRCG01";
    public static final String F_QRCINS = "F.FEP.QRCINS";
    public static final String F_QRCMOD = "F.FEP.QRCMOD";
    public static final String F_QRRE00 = "F.FEP.QRRE00";
    public static final String F_QRRINS = "F.FEP.QRRINS";
    public static final String F_QRRMOD = "F.FEP.QRRMOD";
    public static final String F_QRQMOD = "F.FEP.QRQMOD";
    public static final String F_QRQE00 = "F.FEP.QRQE00";
    public static final String F_QRQINS = "F.FEP.QRQINS";
}
