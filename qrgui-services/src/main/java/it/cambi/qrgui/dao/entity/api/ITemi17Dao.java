package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;

public interface ITemi17Dao<T, K> extends IEntityDao<T, K>
{
    T merge(T query);

}
