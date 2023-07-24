/** */
package it.cambi.qrgui.util;

import java.util.Date;

/**
 * Classe di utilità. TODO I metodi per stabilire la differenza possono essere riunificati in un
 * unico modo che prende in input il tipo di dato. Inoltre si da per scontato che i due valori non
 * siano null
 *
 * @author luca
 */
public class Functions {

  /**
   * @param Integer value1
   * @param Integer value2
   * @return boolean
   *     <p>Stabilisce se due integer sono diversi
   */
  public static boolean areDifferentInt(Integer value1, Integer value2) {
    return (value1 == null ? 0 : value1) - (value2 == null ? 0 : value2) != 0;
  }

  /**
   * @param Long value1
   * @param Long value2
   * @return boolean
   *     <p>Stabilisce se due long sono diversi
   */
  public static boolean areDifferentLong(Long value1, Long value2) {
    return (value1 == null ? 0 : value1) - (value2 == null ? 0 : value2) != 0;
  }

  /**
   * @param Date value1
   * @param Date value2
   * @return boolean
   *     <p>Stabilisce se due Date sono diverse
   */
  public static boolean areDifferentDates(Date value1, Date value2) {
    return (value1 == null ? 0 : value1.getTime()) - (value2 == null ? 0 : value2.getTime()) != 0;
  }
}
