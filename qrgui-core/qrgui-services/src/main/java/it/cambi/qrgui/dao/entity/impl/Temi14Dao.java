package it.cambi.qrgui.dao.entity.impl;

import it.cambi.qrgui.dao.entity.api.ITemi14Dao;
import it.cambi.qrgui.dao.generic.impl.TemiGenericDao;
import it.cambi.qrgui.services.db.model.Temi14UteCat;
import it.cambi.qrgui.services.db.model.Temi14UteCatId;
import org.springframework.stereotype.Component;

/**
 * @param <T>
 *            Type of the Entity.
 * @param <I>
 *            Type of the Primary Key.
 */
@Component
public class Temi14Dao extends TemiGenericDao<Temi14UteCat, Temi14UteCatId> implements ITemi14Dao<Temi14UteCat, Temi14UteCatId>
{

    public Temi14Dao()
    {
        super(Temi14UteCat.class);
    }

}