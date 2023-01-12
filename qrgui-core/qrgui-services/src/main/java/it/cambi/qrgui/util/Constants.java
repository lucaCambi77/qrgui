/** Class for application Constants */
package it.cambi.qrgui.util;

/** @author luca */
public class Constants {

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

  /** Sequences */
  public static final String SEMI14_ANA_CAT = "SELECT EMIA.SEMI14_ANA_CAT.nextval from dual";

  public static final String SEMI15_ANA_QUE = "SELECT EMIA.SEMI15_ANA_QUE.nextval from dual";
  public static final String SEMI17_ANA_ROU = "SELECT EMIA.SEMI17_ANA_ROU.nextval from dual";

  public static final String ERROR_NO_QUERY_ASSOCIATION = "ERROR.NO.QUERY.ASSOCIATION";
  public static final String ERROR_MISSING_PROPERTIES_FILE = "ERROR.MISSING.PROPERTIES.FILE";
  /** Fine Errors */

}
