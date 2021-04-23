package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi13Dao;
import it.cambi.qrgui.dao.generic.impl.TemiGenericDao;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;
import it.cambi.qrgui.services.db.model.Temi13DtbInfId;
import it.cambi.qrgui.services.exception.PropertiesFileNotFoundException;
import it.cambi.qrgui.services.util.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Properties;

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

    private Properties applicationProperties;

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

  /*      try
        {
            String applConfDir = System.getProperty(IConstants.APPLICATION_CONF_DIR);

            InputStream input = new FileInputStream(applConfDir + IConstants.CONFIG_PROPERTIES_PATH);
            Reader reader = new InputStreamReader(input, "UTF-8");

            applicationProperties = new Properties();
            applicationProperties.load(reader);

        }
        catch (IOException e)
        {
            log.info("File di properties non esistente");
            throw new PropertiesFileNotFoundException();
        }
  */  }

    public Properties getApplicationProperties()
    {
        return applicationProperties;
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
