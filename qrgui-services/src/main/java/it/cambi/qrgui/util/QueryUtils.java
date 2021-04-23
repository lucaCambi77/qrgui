/**
 * 
 */
package it.cambi.qrgui.services.util;

import it.cambi.qrgui.enums.OrderType;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luca
 *
 *         Classe per utility su query jpa
 */
public class QueryUtils
{

    /**
     * 
     * 
     * @param Tuple
     *            list
     * @return list Object da una lista di Tuple, da utilizzare nella gui
     */
    public static List<Object> getFromTupleListToObjectList(List<Tuple> list)
    {

        List<Object> resultObjectList = new ArrayList<Object>();

        int i = 0;

        for (Tuple tuple : list)
        {
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
    public static Object getFromTupleToObject(Tuple tuple)
    {
        return tuple.toArray();
    }


    public static void addToOrderList(CriteriaBuilder criteriaBuilder, List<Order> orderList, OrderType orderBy,
            Expression<?> orderAttribute)
    {
        if (null == orderAttribute)
            return;

        if (orderBy == OrderType.ASC)
        {
            orderList.add(criteriaBuilder.asc(orderAttribute));
        }
        else
        {
            orderList.add(criteriaBuilder.desc(orderAttribute));
        }
    }

}
