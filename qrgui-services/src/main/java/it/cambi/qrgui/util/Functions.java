/**
 * 
 */
package it.cambi.qrgui.util;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

/**
 * Classe di utilità. TODO I metodi per stabilire la differenza possono essere riunificati in un unico modo che prende in input il tipo di dato.
 * Inoltre si da per scontato che i due valori non siano null
 * 
 * @author luca
 *
 */
public class Functions
{

    /**
     * @return Blob
     * @throws SerialException
     * @throws SQLException
     * 
     *             Ritorna un blob da un array di bytes Non è più utilizzato in quanto i files sono salvato su db come bytes e non più come blob
     */
    @Deprecated
    public static Blob getBlobFromBytes(byte[] bytes) throws SerialException, SQLException
    {
        Blob blob = null; // is our blob object

        blob = new SerialBlob(bytes);
        return blob;
    }

    /**
     * @param Integer
     *            value1
     * @param Integer
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due integer sono diversi
     */
    public static boolean areDifferentInt(Integer value1, Integer value2)
    {
        if ((value1 == null ? 0 : value1)
                - (value2 == null ? 0 : value2) != 0)
            return true;

        return false;
    }

    /**
     * @param Long
     *            value1
     * @param Long
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due long sono diversi
     */
    public static boolean areDifferentLong(Long value1, Long value2)
    {
        if ((value1 == null ? 0 : value1)
                - (value2 == null ? 0 : value2) != 0)
            return true;

        return false;
    }

    /**
     * @param Date
     *            value1
     * @param Date
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due Date sono diverse
     */
    public static boolean areDifferentDates(Date value1, Date value2)
    {
        if ((value1 == null ? 0 : value1.getTime())
                - (value2 == null ? 0 : value2.getTime()) != 0)
            return true;

        return false;
    }

    /**
     * @param bytes
     *            value1
     * @param bytes
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due bytes sono diversi
     */
    public static boolean areDifferentBytes(byte value1, byte value2)
    {
        Byte aByte = new Byte(value1);
        Byte anotherByte = new Byte(value2);

        if (aByte.compareTo(anotherByte) != 0)
        {
            return true;
        }

        return false;
    }

    /**
     * @param String
     *            value1
     * @param String
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due String sono diverse
     */
    public static boolean areDifferentString(String value1, String value2)
    {
        if (!(value1 == null ? "" : value1).equals(value2 == null ? "" : value2))
            return true;

        return false;
    }

    /**
     * @param Boolean
     *            value1
     * @param Boolean
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due boolean sono diversi
     */
    public static boolean areDifferentBoolean(Boolean value1, Boolean value2)
    {
        if (value1 == null || value2 == null)
            return true;

        if (value1 == value2)
            return false;

        return true;
    }

    /**
     * @param Boolean
     *            value1
     * @param Boolean
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due boolean sono diversi
     */
    public static boolean areDifferentDouble(Double value1, Double value2)
    {
        if ((value1 == null ? 0 : value1)
                - (value2 == null ? 0 : value2) != 0)
            return true;

        return false;
    }

    /**
     * @param BigDecimal
     *            value1
     * @param BigDecimal
     *            value2
     * @return boolean
     * 
     *         Stabilisce se due BigDecimal sono diversi
     */
    public static boolean areDifferentDecimal(BigDecimal value1, BigDecimal value2)
    {
        if (value1.compareTo(value2) != 0)
            return true;

        return false;

    }
}
