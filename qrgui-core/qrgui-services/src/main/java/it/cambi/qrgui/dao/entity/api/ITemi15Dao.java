package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;

import java.util.List;

public interface ITemi15Dao<T, K> extends IEntityDao<T, K>
{

    /**
     * 
     * @param schema
     * @param type
     * @return
     */
    List<T> getByDb(String schema, String type);

    /**
     * 
     * @param ccat
     * @param insCat
     * @param tipCat
     * @return
     */
    List<Object> getAlreadyAssociatedQuery(int ccat, Long insCat, String tipCat);

    /**
     * 
     * @param query
     */
    T deleteByPk(K query);

    /**
     * 
     */
    T merge(T query);

}
