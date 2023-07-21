/**
 *
 */
package it.cambi.qrgui.util;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luca
 *
 *         Classe per utility su query jpa
 */
public class QueryUtils {

    /**
     *
     *
     * @param Tuple
     *            list
     * @return list Object da una lista di Tuple, da utilizzare nella gui
     */
    public static List<Object> getFromTupleListToObjectList(List<Tuple> list) {

        List<Object> resultObjectList = new ArrayList<>();

        int i = 0;

        for (Tuple tuple : list) {
            resultObjectList.add(i, tuple.toArray());
            i++;
        }

        return resultObjectList;
    }

    /**
     *
     *
     * @param Tuple
     * @return Object da una Tuple, da utilizzare nella gui
     */
    public static Object getFromTupleToObject(Tuple tuple) {
        return tuple.toArray();
    }
}
