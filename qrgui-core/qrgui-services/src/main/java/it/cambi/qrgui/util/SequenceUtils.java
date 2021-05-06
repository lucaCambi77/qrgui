package it.cambi.qrgui.util;

import java.math.BigDecimal;

public class SequenceUtils
{

    /**
     * Viene utilizzato perchè la creazione della sequence nell'AbstractDao non funziona per gli unit Test H2 (non ho capito per qualche motivo crea
     * la sequence correttamente ma il metodo restutisce null, inoltre diversamente dal driver Oracle, il driver h2 crea una sequence Long e non
     * BigDecimal), quindi creo la sequence nel service e ottengo la sequence con questo metodo
     * 
     * @param sequence
     * @return
     */
    public static Long getSequence(Object sequence)
    {

        if (sequence instanceof BigDecimal)
            return ((BigDecimal) sequence).longValue();

        /**
         * Caso H2
         */
        if (sequence instanceof Long)
            return ((Long) sequence);

        return null;
    }
}
