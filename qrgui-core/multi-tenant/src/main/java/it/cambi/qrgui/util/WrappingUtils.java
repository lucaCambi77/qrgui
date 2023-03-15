/** */
package it.cambi.qrgui.util;

/**
 * @author luca
 */
public class WrappingUtils {

  public static String cleanQueryString(String statement) {
    return statement
        .replaceAll("\\r\\n", " ")
        .replaceAll("\\n", " ")
        .replaceAll("\\t", " ")
        .replaceAll(";", " ");
  }
}
