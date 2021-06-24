package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi13Dao;
import it.cambi.qrgui.dao.generic.impl.TemiGenericDao;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi13DtbInfId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Component
public class Temi13Dao extends TemiGenericDao<Temi13DtbInf, Temi13DtbInfId> implements ITemi13Dao<Temi13DtbInf, Temi13DtbInfId>
{
    private static final Logger log = LoggerFactory.getLogger(Temi13Dao.class);

    private String allowedCategories;

    public Temi13Dao()
    {
        super(Temi13DtbInf.class);
    }

    @PostConstruct
    public void postConstruct()
    {

        List<Temi13DtbInf> temi13 = findAll(null);

        temi13.forEach(
                (t) -> log.info("Utenti applicativi censiti --> " + "Tipo --> " + t.getId().getTyp() + " , Schema --> " + t.getId().getSch()));
 }

    /**
     * Il seguente è un modo di spring per utilizzare la transazionalità allo startup
     */
    // @Autowired
    // PlatformTransactionManager transactionManager;
    //

    //
    // @SuppressWarnings("unchecked")
    // Temi13DtbInf temi13 = (Temi13DtbInf) new TransactionTemplate(transactionManager).execute(new TransactionCallback()
    // {
    //
    // @Override
    // public Object doInTransaction(TransactionStatus transactionStatus)
    // {
    // return null;
    // }
    //
    // });
    //
    // }

}
