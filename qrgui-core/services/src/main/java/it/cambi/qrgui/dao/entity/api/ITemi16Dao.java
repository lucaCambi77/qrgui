package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import java.util.List;

public interface ITemi16Dao<T, K> extends IEntityDao<T, K> {

  List<T> findByCategory(List<String> functions);

  Integer addQueriesToCateg(List<Temi16QueCatAss> temi16);
}
