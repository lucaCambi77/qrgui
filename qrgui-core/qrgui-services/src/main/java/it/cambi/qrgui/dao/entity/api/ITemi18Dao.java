package it.cambi.qrgui.dao.entity.api;

import java.util.List;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.model.Temi15UteQueId;

public interface ITemi18Dao<T, K> extends IEntityDao<T, K>
{

    List<T> getQueRoutineByQueryId(Temi15UteQueId cque);

}
