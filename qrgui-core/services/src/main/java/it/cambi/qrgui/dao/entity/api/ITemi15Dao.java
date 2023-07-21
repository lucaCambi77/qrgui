package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;
import java.util.Date;
import java.util.List;

public interface ITemi15Dao<T, K> extends IEntityDao<T, K> {

    /**
     * @param ccat
     * @param insCat
     * @return
     */
    List<Object> getAlreadyAssociatedQuery(int ccat, Date insCat);

    /**
     *
     */
    T merge(T query);

}
